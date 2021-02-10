package todo;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.javalin.http.BadRequestResponse;

public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {

    Gson gson = new Gson();
    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(todoDataFile));
    allTodos = gson.fromJson(reader, Todo[].class);
  }

  public int size() {
    return allTodos.length;
  }

  /**
   * Get an array of all the users satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the users matching the given criteria
   */
  public Todo[] listTodos(Map<String, List<String>> queryParams) {
    Todo[] filteredTodos = allTodos;

    // Filter age if defined
    if (queryParams.containsKey("status")) {
      String statusParam = queryParams.get("status").get(0);

      try {
        boolean targetStatus=true;
        if (statusParam == "complete"){
            targetStatus = true;
        }
        else{
            targetStatus = false;
        }

        filteredTodos = filterTodosByStatus(filteredTodos, targetStatus);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified status '" + statusParam + "' can't be parsed to an integer");
      }
    }
    return filteredTodos;
  }


    /**
   * Get the single user specified by the given ID. Return `null` if there is no
   * user with that ID.
   *
   * @param id the ID of the desired user
   * @return the user with the given ID, or null if there is no user with that ID
   */
  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id == (id)).findFirst().orElse(null);
  }

  public Todo[] filterTodosByStatus(Todo[] todos, boolean status){
    return Arrays.stream(todos).filter(x -> x.status == status).toArray(Todo[]::new);

  }



}