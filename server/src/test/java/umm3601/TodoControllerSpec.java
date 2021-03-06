package umm3601;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.javalin.core.validation.Validator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import todo.Todo;
import todo.TodoController;
import todo.TodoDatabase;
import umm3601.Server;



public class TodoControllerSpec {

  private Context ctx = mock(Context.class);

  private TodoController todoController;
  Map<String, List<String>> queryParams;
  private static TodoDatabase db;

  @BeforeEach
  public void setUp() throws IOException {
    ctx.clearCookieStore();

    db = new TodoDatabase(Server.TODO_DATA_FILE);
    todoController = new TodoController(db);
  }

  @Test
  public void GET_to_request_todos_with_limit() throws IOException{
    db = new TodoDatabase("/todos.json");
    queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] {"3"}));
    Todo[] limitedTodos = db.listTodos(queryParams);
    assertEquals(3, limitedTodos.length, "Incorrect number of Todos");
  }



  @Test
  public void GET_to_request_todos_with_incomplete_status(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] { "incomplete" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` have false status
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertTrue(argument.getValue().length > 20);
    for (Todo todo : argument.getValue()) {
      assertEquals(false, todo.status);
    }
  }

  @Test
  public void GET_to_request_todos_with_complete_status(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] { "complete" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` have false status
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertTrue(argument.getValue().length > 20);
    for (Todo todo : argument.getValue()) {
      assertEquals(true, todo.status);
    }
  }
  @Test
  public void GET_to_request_todos_with_illegal_limit() {
    // We'll set the requested "age" to be a string ("abc")
    // that can't be parsed to a number.
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] { "sheep" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    // This should now throw a `BadRequestResponse` exception because
    // our request has an age that can't be parsed to a number.
    Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getTodos(ctx);
    });
  }

  @Test
  public void GET_to_request_todos_with_contains_body(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("contains", Arrays.asList(new String[] { "nisi" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` have false status
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertTrue(argument.getValue().length > 5);
    for (Todo todo : argument.getValue()) {
      assertEquals(true, todo.body.contains("nisi"));
    }
  }

  @Test
  public void GET_to_request_todos_filter_owner(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("owner", Arrays.asList(new String[] { "Fry" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` have false status
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertTrue(argument.getValue().length > 5);
    for (Todo todo : argument.getValue()) {
      assertEquals(true, todo.owner.equals("Fry"));
    }

  }

  @Test
  public void GET_to_request_todos_filter_category(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("category", Arrays.asList(new String[] { "software design" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` have false status
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    assertTrue(argument.getValue().length > 5);
    for (Todo todo : argument.getValue()) {
      assertEquals(true, todo.category.equals("software design"));
    }

  }

  @Test
  public void GET_to_request_todos_orderBy_body(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "body" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` are in alphabetical
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    int length = argument.getValue().length;

    for(int i=0; i<length; i++){
      for(int j=i+1; j<length-1; j++){
        assertTrue(argument.getValue()[i].body.compareTo(argument.getValue()[j].body) <= 0 );
      }
    }


  }

  @Test
  public void GET_to_request_todos_orderBy_owner(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "owner" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` are in alphabetical
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    int length = argument.getValue().length;

    for(int i=0; i<length; i++){
      for(int j=i+1; j<length-1; j++){
        assertTrue(argument.getValue()[i].owner.compareTo(argument.getValue()[j].owner) <= 0 );
      }
    }


  }

  @Test
  public void GET_to_request_todos_orderBy_category(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "category" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` are in alphabetical
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    int length = argument.getValue().length;

    for(int i=0; i<length; i++){
      for(int j=i+1; j<length-1; j++){
        assertTrue(argument.getValue()[i].category.compareTo(argument.getValue()[j].category) <= 0 );
      }
    }


  }
  public int compareStatus(Todo todo1, Todo todo2){
    int s1 = todo1.status ? 0 : 1;
    int s2 = todo2.status ? 0 : 1;

    return s2 - s1;
  }


  @Test
  public void GET_to_request_todos_orderBy_status(){
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("orderBy", Arrays.asList(new String[] { "status" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    todoController.getTodos(ctx);

    //Confirm that all the todos passed to `json` are in alphabetical
    ArgumentCaptor<Todo[]> argument = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(argument.capture());
    int length = argument.getValue().length;

    for(int i=0; i<length; i++){
      for(int j=i+1; j<length-1; j++){
        assertTrue((compareStatus(argument.getValue()[i],argument.getValue()[j]) <= 0));
      }
    }




  }

}
