package main.java.myapp;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import javax.servlet.http.*;

public class RequestDriver extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String driverName = getParameter(req, "driverName", "");
        double cost = Double.parseDouble(getParameter(req, "cost", ""));
        System.out.println("Cost=" + cost);
        String time = getParameter(req, "time", "");
        
        String username = null;
        try{
            HttpSession session=req.getSession(false);
                
            username = (String) session.getAttribute("username");
            System.out.println("Name: " + username);
        }catch(Exception exp){
            System.out.println(exp);
            resp.sendRedirect("/index.html");
        }

        // insert check if user can pay

        Filter custFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);

        Query query = new Query("Customer").setFilter(custFilter);
   
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        double wallet = 0;
        for (Entity entity : results.asIterable()) {
            wallet = (double) entity.getProperty("money");
        }

        if(wallet >= cost)
        {
            Entity reqEntity = new Entity("Requests");
            if(username != null)
            {
                reqEntity.setProperty("cust", username);
                reqEntity.setProperty("driver", driverName);
                reqEntity.setProperty("cost", cost);
                reqEntity.setProperty("time", time);
                reqEntity.setProperty("isRequested", "true");
                reqEntity.setProperty("isAccepted", "false");

                datastore.put(reqEntity);
            }
        
            resp.sendRedirect("/custWaitingPage.html?driverName="+driverName+"&custName="+username+"&cost="+cost);
        }
        else
        {
            resp.sendRedirect("/login.html");
        }
        
        //resp.sendRedirect("/custWaitingPage.html?driverName="+driverName+"&custName="+username+"&cost="+cost);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String driverName = getParameter(req, "driverName", "");
        String custName = getParameter(req, "custName", "");
        String cost = getParameter(req, "cost", "");
        System.out.println(cost);

        Filter driverFilter = new FilterPredicate("driver", FilterOperator.EQUAL, driverName);
        Filter custFilter = new FilterPredicate("cust", FilterOperator.EQUAL, custName);

        Filter validFilter = CompositeFilterOperator.and(driverFilter, custFilter);

        Query query = new Query("Requests").setFilter(validFilter);
   
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        String isAccepted = "false";
        for (Entity entity : results.asIterable()) {
            isAccepted = (String) entity.getProperty("isAccepted");
        }

        if(isAccepted.equals("true"))
        {
            resp.sendRedirect("/DriveInProgress.html?user=cust&cost="+cost+"&driver="+driverName);
        }
        else if(isAccepted.equals("rejected"))
        {
            resp.sendRedirect("/custPageStart.html");
        }

    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

}