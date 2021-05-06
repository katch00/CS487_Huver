package myapp;

import java.io.IOException;
import javax.servlet.http.*;

public class SignoutServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession session = req.getSession();
    session.removeAttribute("username");
    session.removeAttribute("userType");
    System.out.println("Signed out.");
    resp.sendRedirect("/index.html");   
  }
}