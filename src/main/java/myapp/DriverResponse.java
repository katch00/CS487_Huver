package main.java.myapp;

import java.io.IOException;
import javax.servlet.http.*;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class DriverResponse extends HttpServlet
{
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String custName = getParameter(req, "cust", "");
        double cost = Double.parseDouble(getParameter(req, "cost", ""));
        String driverResponse = getParameter(req, "driverResp", "");

        String driverName = "";
        try{
            HttpSession session=req.getSession(false);
                
            driverName = (String) session.getAttribute("name");
        }catch(Exception exp){
            System.out.println(exp);
            resp.sendRedirect("/index.html");
        }

        Filter driverFilter = new FilterPredicate("driver", FilterOperator.EQUAL, driverName);
        Filter custFilter = new FilterPredicate("cust", FilterOperator.EQUAL, custName);

        Filter validFilter = CompositeFilterOperator.and(driverFilter, custFilter);

        Query query = new Query("Requests").setFilter(validFilter);
   
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        String true1 = "true";
        if(driverResponse.equals("accepted"))
        {
            Filter custNameFilter = new FilterPredicate("username", FilterOperator.EQUAL, custName);
            Query newquery = new Query("Customer").setFilter(custNameFilter);
            PreparedQuery newresults = datastore.prepare(newquery);
            for (Entity newentity : newresults.asIterable()) {
                double money = (double) newentity.getProperty("money");
                double newWallet = money - cost;
                newentity.setProperty("money", newWallet);
                String message = "Thank you for your purchase. Your total today was: " + cost;
                String email = (String) newentity.getProperty("phoneNumber");
                try
                {
                    sendEmail(message, email);
                }
                catch(Exception e)
                {
                    System.err.println("Email could not be sent");
                }
                datastore.put(newentity);
            }
            Filter newdriverFilter = new FilterPredicate("firstName", FilterOperator.EQUAL, driverName);
            Query newerquery = new Query("Driver").setFilter(newdriverFilter);
            PreparedQuery newerresults = datastore.prepare(newerquery);
            for (Entity newentity : newerresults.asIterable()) {
                double money = (double) newentity.getProperty("money");
                double newWallet = money + cost;
                newentity.setProperty("money", newWallet);
                datastore.put(newentity);
            }
            
            for (Entity entity : results.asIterable()) {
                entity.setProperty("isAccepted", true1);
                datastore.put(entity);
                resp.sendRedirect("/DriveInProgress.html?user=driver");
            }

        }
        else
        {
            for (Entity entity : results.asIterable()) {
                entity.setProperty("isAccepted", "rejected");
                datastore.put(entity);
            }
            resp.sendRedirect("/driverPageStart.html");
        }

    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static void sendEmail(String message, String userEmail) throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(Key.API_KEY, Key.API_SECRET, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
        .property(Emailv31.MESSAGES, new JSONArray()
        .put(new JSONObject()
        .put(Emailv31.Message.FROM, new JSONObject()
        .put("Email", "nstroupe@hawk.iit.edu")
        .put("Name", "Huver"))
        .put(Emailv31.Message.TO, new JSONArray()
        .put(new JSONObject()
        .put("Email", userEmail)
        .put("Name", "Student"))) 
        .put(Emailv31.Message.SUBJECT, "Thank you for your purchase!")
        .put(Emailv31.Message.TEXTPART, "")
        .put(Emailv31.Message.HTMLPART, message)
        .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
        
    }
}