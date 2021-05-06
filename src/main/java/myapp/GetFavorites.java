package myapp;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.http.*;

public class GetFavorites extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        String username_sesh = null;
        try{
                HttpSession session=req.getSession(false);
                
                username_sesh = (String) session.getAttribute("username");
                System.out.println("Name: " + username_sesh);
            }catch(Exception exp){
                System.out.println(exp);
                resp.sendRedirect("/index.html");
            }

            
            if(username_sesh != null){
                Filter userFilter = new FilterPredicate("user", FilterOperator.EQUAL, username_sesh);

                Query query = new Query("Favorites").setFilter(userFilter);
                
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                PreparedQuery results = datastore.prepare(query);
                
                List<Favorites> favs = new ArrayList<>();
               
                for (Entity entity : results.asIterable()) {
                    String username = (String) entity.getProperty("user");
                    String address = (String) entity.getProperty("staddr");
                    String city = (String) entity.getProperty("city");
                    String zipCode = (String) entity.getProperty("zip");
                    String state = (String) entity.getProperty("state");
            
                    Favorites fav = new Favorites(username, address, city, zipCode, state);
                    favs.add(fav);
                                        
                }
                String json = convertToJson(favs);

                resp.setContentType("application/json;");
                resp.getWriter().println(json); 
            }
            
    }

    public String convertToJson(List<Favorites> arr) {
        Gson gson = new Gson();
        String json = gson.toJson(arr);
        return json;
    }

    private String getParameter(HttpServletRequest request, String username, String defaultValue) {
        String value = request.getParameter(username);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }




}