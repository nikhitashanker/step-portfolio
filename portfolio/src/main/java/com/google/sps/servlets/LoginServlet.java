import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login-url")
public class LoginServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    String loginUrl = UserServiceFactory.getUserService().createLoginURL(
        /* urlToRedirectToAfterLogin= */ "/comments.html");
    response.getWriter().println(loginUrl);
  }
}
