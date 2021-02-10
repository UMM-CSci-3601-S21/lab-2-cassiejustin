import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.http.Context;
import todo.Todo;
import todo.TodoDatabase;

/**
 * Tests umm3601.user.Database listTodos with _age_ and _company_ query
 * parameters
 */
public class FilterTodosByStatus {


  private static TodoDatabase db;
  private Context ctx = mock(Context.class);
  Map<String, List<String>> queryParams;

  @BeforeEach
  public void setUp() throws IOException {
    ctx.clearCookieStore();
    db = new TodoDatabase("/todos.json");
    queryParams = new HashMap<>();

  }

  @Test
  public void listTodosByStatusComplete() throws IOException {
    queryParams.put("status", Arrays.asList(new String[] { "complete" }));
    Todo[] completeTodos = db.listTodos(queryParams);
    assertEquals(143, completeTodos.length, "Incorrect number of Todos with complete Status");
  }

  @Test
  public void listTodosByStatusIncomplete() throws IOException {
    queryParams.put("status", Arrays.asList(new String[] { "incomplete" }));
    Todo[] incompleteTodos = db.listTodos(queryParams);
    assertEquals(157, incompleteTodos.length, "Incorrect number of Todos with complete Status");
  }
  @Test
  public void firstTodoWithIncomplete() throws IOException {
    queryParams.put("status", Arrays.asList(new String[] { "incomplete" }));
    Todo[] incompleteTodos = db.listTodos(queryParams);
    Todo firstTodo = incompleteTodos[0];
    assertEquals(firstTodo.status, false, "Incorrect status for first Todo");
    assertEquals(firstTodo.owner, "Blanche", "Incorrect Owner for first incomplete todo");
    assertEquals(firstTodo._id, "58895985a22c04e761776d54", "incorrect id for first incomplete Todo");
  }

}
