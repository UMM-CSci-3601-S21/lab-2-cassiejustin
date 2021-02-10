import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import todo.Todo;
import todo.TodoDatabase;

/**
 * Tests umm3601.user.Database listTodos with _age_ and _company_ query
 * parameters
 */
public class FilterTodosByStatus {

  @Test
  public void listTodosByStatusComplete() throws IOException {
    TodoDatabase db = new TodoDatabase("/todos.json");
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("status", Arrays.asList(new String[] { "complete" }));
    Todo[] completeTodos = db.listTodos(queryParams);
    assertEquals(143, completeTodos.length, "Incorrect number of Todos with complete Status");
  }

  @Test
  public void listTodosByStatusIncomplete() throws IOException {
    TodoDatabase db = new TodoDatabase("/todos.json");
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("status", Arrays.asList(new String[] { "incomplete" }));
    Todo[] incompleteTodos = db.listTodos(queryParams);
    assertEquals(157, incompleteTodos.length, "Incorrect number of Todos with complete Status");
  }

}
