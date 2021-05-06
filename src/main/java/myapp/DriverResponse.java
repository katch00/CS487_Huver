package main.java.myapp;

import java.io.IOException;
import javax.servlet.http.*;

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
    //System.out.print(name + value);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}