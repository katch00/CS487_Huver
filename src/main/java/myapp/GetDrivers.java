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
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetDrivers extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String filterCity = getParameter(request, "city", "Chicago");
        String numPass = getParameter(request, "numPass", "1");

        Filter cityFilter = new FilterPredicate("city", FilterOperator.EQUAL, filterCity);
        Filter numPassFilter = new FilterPredicate("maxNumPassengers", FilterOperator.GREATER_THAN_OR_EQUAL, numPass);

        Filter validFilter = CompositeFilterOperator.and(cityFilter, numPassFilter);

        Query query = new Query("Driver").setFilter(validFilter);
   
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<Driver> drivers = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            String firstName = (String) entity.getProperty("firstName");
            String lastName = (String) entity.getProperty("lastName");
            String username = (String) entity.getProperty("username");
            String number = (String) entity.getProperty("phoneNumber");
            String date = (String) entity.getProperty("date");
            String address = (String) entity.getProperty("address");
            String city = (String) entity.getProperty("city");
            String zipCode = (String) entity.getProperty("zipCode");
            String state = (String) entity.getProperty("state");
            String password = (String) entity.getProperty("password");
            String license = (String) entity.getProperty("licenseNumber");
            String plateNum = (String) entity.getProperty("plateNumber");
            String numPassengers = (String) entity.getProperty("maxNumPassengers");
            long overallRating = (long) entity.getProperty("overallRating");
            String isOnline = (String) entity.getProperty("isOnline");
            String isAvailable = (String) entity.getProperty("isAvailable");
      
            Driver driver = new Driver(firstName, lastName, username, date, number, address, city, zipCode, state, password, license, plateNum, numPassengers, overallRating, isOnline, isAvailable);
            drivers.add(driver);
        }

        String json = convertToJson(drivers);

        response.setContentType("application/json;");
        response.getWriter().println(json); 
    }

    public String convertToJson(List<Driver> arr) {
        Gson gson = new Gson();
        String json = gson.toJson(arr);
        return json;
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }


}