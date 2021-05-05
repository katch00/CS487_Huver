package myapp;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class CreateUser extends HttpServlet {
  
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        String firstName = getParameter(request, "firstName", "");
        String lastName = getParameter(request, "lastName", "");
        String userName = getParameter(request, "userName", "");
        int birthyear = Integer.parseInt(request.getParameter("year"));
        int birthmonth = Integer.parseInt(request.getParameter("month"));
        int birthday = Integer.parseInt(request.getParameter("day"));
        String number = getParameter(request, "phone-number", "");
        String address = getParameter(request, "address", "");
        String type = getParameter(request, "user-type", "");
        String password = getParameter(request, "password", "");

        // we will hash the password here for security reasons

        
        //check birthday code
        //LocalDate birthDate = LocalDate.of(birthyear, birthmonth, birthday);
        //LocalDate now = new LocalDate();
        int userAge = 19;//calculateAge(birthDate, now);
        if(userAge < 18)
        {
            // handle inappropriate age
            response.sendRedirect("/errorPage.html");
        }

        if(type == "customer")
        {
            String creditCardNum = getParameter(request, "cardNum", "");
            Entity custEntity = new Entity("customer");
            custEntity.setProperty("firstName", firstName);
            custEntity.setProperty("lastName", lastName);
            custEntity.setProperty("userName", userName);
            custEntity.setProperty("phoneNumber", number);
            custEntity.setProperty("address", address);
            custEntity.setProperty("password", password);
            datastore.put(custEntity);
        }
        else if(type == "driver")
        {
            String license = getParameter(request, "licNum", "");
            String plateNum = getParameter(request, "plateNum", "");
            int numPass = Integer.parseInt(request.getParameter("numPass"));
            Entity drivEntity = new Entity("driver");
            drivEntity.setProperty("firstName", firstName);
            drivEntity.setProperty("lastName", lastName);
            drivEntity.setProperty("userName", userName);
            drivEntity.setProperty("phoneNumber", number);
            drivEntity.setProperty("address", address);
            drivEntity.setProperty("password", password);
            drivEntity.setProperty("licenseNum", license);
            drivEntity.setProperty("plateNum", plateNum);
            drivEntity.setProperty("numPass", numPass);
            datastore.put(drivEntity);
        }

        // will redirect to correct page once implemented
        response.sendRedirect("/index.html");
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

    /*public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }*/
}
