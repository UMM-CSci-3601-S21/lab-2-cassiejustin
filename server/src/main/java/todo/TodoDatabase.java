package todo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import io.javalin.http.BadRequestResponse;

public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {

    Gson gson = new Gson();
    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(todoDataFile));
    allTodos = gson.fromJson(reader, Todo[].class);
  }

  /**
   * Get an array of all the users satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the users matching the given criteria
   */
  public Todo[] listTodos(Map<String, List<String>> queryParams) {
    Todo[] filteredTodos = allTodos;

    // Filter by status if defined
    if (queryParams.containsKey("status")) {
      String statusParam = queryParams.get("status").get(0);
      boolean targetStatus;
        if ("incomplete".equals(statusParam)){
          targetStatus = false;
        }
        else {
          targetStatus = true;
        }
      filteredTodos = filterTodosByStatus(filteredTodos, targetStatus);
    }
    //filter by owner if requested
    if(queryParams.containsKey("owner")){
      String ownerParam = queryParams.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, ownerParam);
    }
    // filter by body if defined
    if (queryParams.containsKey("contains")){
      String bodyParam = queryParams.get("contains").get(0);
      filteredTodos = filterTodosByContains(filteredTodos, bodyParam);
    }
    if (queryParams.containsKey("category")){
      String categoryParam = queryParams.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos, categoryParam);
    }
    // filter by limit if defined
    if (queryParams.containsKey("limit")){
      String limitParam = queryParams.get("limit").get(0);
      try{
        int targetLimit = Integer.parseInt(limitParam);
        filteredTodos = filterTodosByLimit(filteredTodos, targetLimit);
      } catch (NumberFormatException e){
        throw new BadRequestResponse("Specified status '" + limitParam + "' can't be parsed to an integer");
      }
    }
    if(queryParams.containsKey("orderBy")){
      String sortParam = queryParams.get("orderBy").get(0);
      if(sortParam.equals("body")){
        filteredTodos = sortTodosByBody(filteredTodos);
      }
      else if(sortParam.equals("owner")){
        filteredTodos = sortTodosByOwner(filteredTodos);
      }
      else if(sortParam.equals("category")){
        filteredTodos = sortTodosByCategory(filteredTodos);
      }
      else if(sortParam.equals("status")){
        filteredTodos = sortTodosByStatus(filteredTodos);
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

  public Todo[] filterTodosByStatus(Todo[] todos, boolean status){
    return Arrays.stream(todos).filter(x -> x.status == (status)).toArray(Todo[]::new);

  }
  public Todo[] filterTodosByLimit(Todo[] todos, int limit){
    return Arrays.copyOfRange(todos, 0, limit);
  }

  public Todo[] filterTodosByContains(Todo[] todos, String string){
    return Arrays.stream(todos).filter(x-> x.body.contains(string)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByOwner(Todo[] todos, String owner){
    return Arrays.stream(todos).filter(x-> x.owner.equals(owner)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByCategory(Todo[] todos, String category ){
    return Arrays.stream(todos).filter(x-> x.category.equals(category)).toArray(Todo[]::new);
  }

  public Todo[] sortTodosByBody(Todo[] todos) throws NullPointerException{
    Arrays.sort(todos, new SortTodosByBody());
    return todos;
  }
  public Todo[] sortTodosByOwner(Todo[] todos){
    Arrays.sort(todos, new SortTodosByOwner());
    return todos;
  }

  public Todo[] sortTodosByCategory(Todo[] todos){
   Arrays.sort(todos, new SortTodosByCategory());
    return todos;
  }

  public Todo[] sortTodosByStatus(Todo[] todos){
    Arrays.sort(todos, new SortTodosByStatus());
    return todos;
  }

  class SortTodosByCategory implements Comparator<Todo>{
    public int compare(Todo a, Todo b){
      return a.category.compareTo(b.category);
    }
  }
  class SortTodosByBody implements Comparator<Todo>{
    public int compare(Todo a, Todo b){
      return a.body.compareTo(b.body);
    }
  }
  class SortTodosByStatus implements Comparator<Todo>{
    public int compare(Todo a, Todo b){
      int s1 = a.status ? 0 : 1;
      int s2 = b.status ? 0 : 1;
      return s2 - s1;
    }

  }
  class SortTodosByOwner implements Comparator<Todo>{
    public int compare(Todo a, Todo b){
      return a.owner.compareTo(b.owner);
    }

  }




}
