package todo;

import org.eclipse.jetty.server.session.DatabaseAdaptor;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class TodoController {

    private TodoDatabase database;

    public TodoController(TodoDatabase database) {
      this.database = database;
    }


     /**
   * Get a JSON response with a list of all the users in the "database".
   *
   * @param ctx a Javalin HTTP context
   */
  public void getTodos(Context ctx) {
    Todo[] todos = database.listTodos(ctx.queryParamMap());
    ctx.json(todos);
  }




}
