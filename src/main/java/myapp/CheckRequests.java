package main.java.myapp;

import javax.servlet.http.*;
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

        Query query = new Query("Requests").setFilter(driverFilter);
   
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