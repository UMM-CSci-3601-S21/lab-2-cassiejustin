package umm3601.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import todo.Todo;
import todo.TodoDatabase;

/**
 * Tests umm3601.user.Database listUser functionality
 */
public class FullUserListFromDB {

  @Test
  public void totalUserCount() throws IOException {
    userDatabase db = new userDatabase("/users.json");
    User[] allUsers = db.listUsers(new HashMap<>());
    assertEquals(10, allUsers.length, "Incorrect total number of users");
  }

  @Test
  public void firstUserInFullList() throws IOException {
    userDatabase db = new userDatabase("/users.json");
    User[] allUsers = db.listUsers(new HashMap<>());
    User firstUser = allUsers[0];
    assertEquals("Connie Stewart", firstUser.name, "Incorrect name");
    assertEquals(25, firstUser.age, "Incorrect age");
    assertEquals("OHMNET", firstUser.company, "Incorrect company");
    assertEquals("conniestewart@ohmnet.com", firstUser.email, "Incorrect e-mail");
  }

    @Test
    public void totalTodoCount() throws IOException {
      TodoDatabase db = new TodoDatabase("/todos.json");
      Todo[] allTodos = db.listTodos(new HashMap<>());
      Todo firstTodo = allTodos[0];
      assertEquals("Blanche", firstTodo.owner, "Incorrect owner");
      assertEquals(false, firstTodo.status, "Incorrect status");
      assertEquals("software design", firstTodo.category, "Incorrect category");
      assertEquals("In sunt ex non tempor cillum commodo amet incididunt anim qui commodo quis. Cillum non labore ex sint esse.", firstTodo.body, "Incorrect e-mail");
    }


}

