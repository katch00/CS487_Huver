package myapp;

import java.io.IOException;
import javax.servlet.http.*;

public class SignoutServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession session = req.getSession();
    session.removeAttribute("username");
    session.removeAttribute("userType");
    resp.sendRedirect("/index.html");   
  }
}