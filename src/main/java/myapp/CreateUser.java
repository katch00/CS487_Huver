package myapp;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUser extends HttpServlet {
  
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        UserService userService = UserServiceFactory.getUserService();

        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/login");
            return;
        }

        String firstName = getParameter(request, "firstName", "");
        String lastName = getParameter(request, "lastName", "");
        String username = getParameter(request, "userName", "");
        int birthyear = getParameter(request, "year", "");
        int birthmonth = getParamter(request, "month", "");
        int birthday = getParameter(request, "day", "");
        String number = getParameter(request, "phone-number", "");
        String address = getParameter(request, "address", "");
        String type = getParameter(request, "user-type", "");
        String password = getParameter(request, "password", "");

        // we will hash the password here for security reasons

        
        //check birthday code
        LocalDate birthDate = LocalDate.of(birthyear, birthmonth, birthday);
        LocalDate now = new LocalDate();
        int userAge = calulateAge(birthDate, now);
        if(userAge < 18)
        {
            // handle inappropriate age
            response.redirect("/errorPage.html");
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
            int numPass = getParameter(request, "numPass", "");
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


    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}
