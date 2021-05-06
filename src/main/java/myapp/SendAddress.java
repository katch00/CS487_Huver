package myapp;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.http.*;

public class SendAddress extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String staddr = getParameter(req, "staddr", "");
        String city = getParameter(req, "city", "");
        String zip = getParameter(req, "zip", "");
        String state = getParameter(req, "state", "");
        String numPass = getParameter(req, "numPass", "");
        String isFavorite = getParameter(req, "isFav", "no");

        if(isFavorite.equals("yes"))
        {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Entity favEntity = new Entity("Favorites");
            String username = null;
            try{
                HttpSession session=req.getSession(false);
                
                username = (String) session.getAttribute("username");
                System.out.println("Name: " + username);
            }catch(Exception exp){
                System.out.println(exp);
                resp.sendRedirect("/index.html");
            }

            if(username != null)
            {
                favEntity.setProperty("user", username);
                favEntity.setProperty("staddr", staddr);
                favEntity.setProperty("city", city);
                favEntity.setProperty("zip", zip);
                favEntity.setProperty("state",state);
                datastore.put(favEntity);
            }

        }
        
        resp.sendRedirect("/showDrivers.html?city=" + city + "&numPass=" + numPass);
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

}