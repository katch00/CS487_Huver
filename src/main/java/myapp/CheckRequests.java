package main.java.myapp;

import javax.servlet.http.*;

import java.io.FilterOutputStream;
import java.io.IOException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

public class CheckRequests extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = null;
        try{
            HttpSession session=req.getSession(false);
                
            name = (String) session.getAttribute("name");
        }catch(Exception exp){
            System.out.println(exp);
            resp.sendRedirect("/index.html");
        }

        Filter driverFilter = new FilterPredicate("driver", FilterOperator.EQUAL, name);
        Filter statusFilter = new FilterPredicate("isAccepted", FilterOperator.NOT_EQUAL, "rejected");
        Filter status2Filter = new FilterPredicate("isAccepted", FilterOperator.NOT_EQUAL, "true");

        Filter validFilter = CompositeFilterOperator.and(driverFilter, statusFilter, status2Filter);

        Query query = new Query("Requests").setFilter(validFilter);
   
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        String isRequested = "false";
        for (Entity entity : results.asIterable()) {
            isRequested = (String) entity.getProperty("isRequested");
            System.out.println(isRequested);
            if(isRequested.equals("true"))
            {
                String cost = (String) entity.getProperty("cost");
                String time = (String) entity.getProperty("time");
                String cust = (String) entity.getProperty("cust");
                resp.sendRedirect("/requestResponse.html?cust="+cust+"&cost="+cost+"&time="+time);
            }
        }

    }
}