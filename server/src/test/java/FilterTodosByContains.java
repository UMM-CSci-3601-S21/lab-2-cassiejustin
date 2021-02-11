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

public class FilterTodosByContains {

  private static TodoDatabase db;
  private Context ctx = mock(Context.class);
  Map<String, List<String>> queryParams;

  @BeforeEach
  public void setUp() throws IOException {
    ctx.clearCookieStore();
    db = new TodoDatabase("/todos.json");
    queryParams = new HashMap<>();

  }



}
