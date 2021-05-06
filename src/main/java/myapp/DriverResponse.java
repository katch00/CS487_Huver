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
        System.out.println(custName);
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
        System.out.println(driverResponse);
        String true1 = "true";
        if(driverResponse.equals("accepted"))
        {
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