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
    Todo[] filteredUsers = allTodos;

    // Filter age if defined
    if (queryParams.containsKey("age")) {
      String ageParam = queryParams.get("age").get(0);
      try {
        // int targetAge = Integer.parseInt(ageParam);
        // filteredUsers = filterUsersByAge(filteredUsers, targetAge);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified age '" + ageParam + "' can't be parsed to an integer");
      }
    }
    // Filter company if defined
    if (queryParams.containsKey("company")) {
      // String targetCompany = queryParams.get("company").get(0);
      // filteredUsers = filterUsersByCompany(filteredUsers, targetCompany);
    }
    // Process other query parameters here...

    return filteredUsers;
  }


    /**
   * Get the single user specified by the given ID. Return `null` if there is no
   * user with that ID.
   *
   * @param id the ID of the desired user
   * @return the user with the given ID, or null if there is no user with that ID
   */
  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }



}
