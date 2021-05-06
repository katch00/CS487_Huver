package myapp;

import java.io.IOException;
import javax.servlet.http.*;
import java.util.Random;

import com.google.appengine.repackaged.com.google.protobuf.Timestamp;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class AccountChangeServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String firstName = getParameter(req, "First Name", "");
    String lastName = getParameter(req, "Last Name", "");
    String phoneNumber = getParameter(req, "Phone Number", "");
    String address = getParameter(req, "Address", "");
    String city = getParameter(req, "City", "");
    String zipCode = getParameter(req, "Zip Code", "");
    String state = getParameter(req, "State", "");
    String username = getParameter(req, "Username", "");
    String password = getParameter(req, "Password", "");
    
    HttpSession session=req.getSession(false);
    String userType = (String) session.getAttribute("userType");
    String currUsername = (String) session.getAttribute("username");
    
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter correctUsername = new FilterPredicate("username", FilterOperator.EQUAL, currUsername);
        Query query = new Query(userType).setFilter(correctUsername);
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            if(!firstName.equals("")){
                entity.setProperty("firstName", firstName);
                session.setAttribute("name", firstName);
            } 
            if(!lastName.equals("")){
                entity.setProperty("lastName", lastName);
            }
            if(!phoneNumber.equals("")){
                entity.setProperty("phoneNumber", phoneNumber);
            }
            if(!address.equals("")){
                entity.setProperty("address", address);
            }  
            if(!city.equals("")){
                entity.setProperty("city", city);
            }
            if(!zipCode.equals("")){
                entity.setProperty("zipCode", zipCode);
            }
            if(!state.equals("")){
                entity.setProperty("state", state);
            }
            if(!username.equals("")){
                entity.setProperty("username", username);
                session.setAttribute("username", username);
            }
            if(!password.equals("")){
                entity.setProperty("password", password);
            }
            
            datastore.put(entity);
        }

    System.out.println("Account information changed.");

    resp.sendRedirect("/successPage.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    //System.out.print(name + value);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}