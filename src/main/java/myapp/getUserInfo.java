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

public class getUserInfo extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        String username_sesh = null;
        String type = null;
        try{
                HttpSession session=req.getSession(false);
                type = (String) session.getAttribute("userType");
                System.out.println("type: " + type);
                username_sesh = (String) session.getAttribute("username");
                System.out.println("Name: " + username_sesh);
            }catch(Exception exp){
                System.out.println(exp);
                resp.sendRedirect("/index.html");
            }
            
            if(type.equals("Customer")){
                System.out.println("hrllllp");
                Filter userFilter = new FilterPredicate("username", FilterOperator.EQUAL, username_sesh);

                Query query = new Query("Customer").setFilter(userFilter);
                    
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                PreparedQuery results = datastore.prepare(query);
                    
                List<Customer> cust = new ArrayList<>();
                System.out.println(results.countEntities());
                for (Entity entity : results.asIterable()) {
                    String username = (String) entity.getProperty("username");
                    String firstName = (String) entity.getProperty("firstName");
                    String address = (String) entity.getProperty("address");
                    String city = (String) entity.getProperty("city");
                    String zipCode = (String) entity.getProperty("zipCode");
                    String state = (String) entity.getProperty("state");
                    String lastName = (String) entity.getProperty("lastName");
                    int birthyear = 0;
                    int birthmonth = 0;
                    int birthday = 0;
                    String number = (String) entity.getProperty("phoneNumber");
                    String password = (String) entity.getProperty("password");
                    String cardNo = (String) entity.getProperty("cardNumber");
                    String securityNo = (String) entity.getProperty("securityNumber");
                    long overallRating = (long) entity.getProperty("overallRating");
                    long wallet = 0;
                    


                    Customer c = new Customer(firstName, lastName, username, birthday, birthmonth, birthyear, number, address, city, zipCode, state, password, cardNo, securityNo, overallRating, wallet);
                    cust.add(c);
                                            
                }
                System.out.println("hrrrlp");
                String json = convertToJson(cust);
                System.out.println(json);
                resp.setContentType("application/json;");
                resp.getWriter().println(json); 
            }else if(type.equals("Driver")){
                Filter userFilter = new FilterPredicate("username", FilterOperator.EQUAL, username_sesh);

                Query query = new Query("Driver").setFilter(userFilter);
                    
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                PreparedQuery results = datastore.prepare(query);
                    
                List<Driver> driv = new ArrayList<>();
                
                for (Entity entity : results.asIterable()) {
                    String username = (String) entity.getProperty("username");
                    String firstName = (String) entity.getProperty("firstName");
                    String address = (String) entity.getProperty("staddr");
                    String city = (String) entity.getProperty("city");
                    String zipCode = (String) entity.getProperty("zipCode");
                    String state = (String) entity.getProperty("state");
                    String lastName = (String) entity.getProperty("lastName");
                    String birthdate = (String) entity.getProperty("birthdate");
                    String number = (String) entity.getProperty("phoneNumber");
                    String password = (String) entity.getProperty("password");
                    String license = (String) entity.getProperty("licenseNumber");
                    String plateNum = (String) entity.getProperty("plateNumber");
                    long overallRating = (long) entity.getProperty("overallRating");
                    String isOnline = (String) entity.getProperty("isOnline");
                    String isAvailable = (String) entity.getProperty("isAvailable");
                    String numPassengers = (String) entity.getProperty("numPassengers");
                
                    Driver d = new Driver(firstName, lastName, username, birthdate, number,
                        address, city, zipCode, state, password, license, plateNum, numPassengers,
                        overallRating, isOnline, isAvailable);
                    driv.add(d);
                                            
                }
                String json = convertToJsonD(driv);

                resp.setContentType("application/json;");
                resp.getWriter().println(json);
            }
            
    }

    public String convertToJson(List<Customer> arr) {
        System.out.println("herlp");
        Gson gson = new Gson();
        System.out.println("heerlp");
        String json = gson.toJson(arr);
        System.out.println("heeerlp");
        return json;
    }

    public String convertToJsonD(List<Driver> arr) {
        Gson gson = new Gson();
        String json = gson.toJson(arr);
        return json;
    }




}