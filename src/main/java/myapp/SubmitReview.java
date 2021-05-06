package main.java.myapp;

import java.io.IOException;
import javax.servlet.http.*;
import java.time.LocalDate;
import java.util.Random;

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

public class SubmitReview extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        double tip = Double.parseDouble(getParameter(req, "tip", "0"));
        System.out.println("tip amount " + tip);
        int rating = Integer.parseInt(getParameter(req, "rate", "0"));
        System.out.println("ratin: " + rating);
        String desc = getParameter(req, "desc", "");
        System.out.println("desc" + desc);
        String driverName = getParameter(req, "drivName", "");
        System.out.println("driverName" + driverName);

        //remove user requests, update driver money/rating, reviews table

        String custName = "";
        try{
            HttpSession session=req.getSession(false);
                
            custName = (String) session.getAttribute("username");
        }catch(Exception exp){
            System.out.println(exp);
            resp.sendRedirect("/index.html");
        }

        Filter driverFilter = new FilterPredicate("driver", FilterOperator.EQUAL, driverName);
        Filter custFilter = new FilterPredicate("cust", FilterOperator.EQUAL, custName);

        Filter validFilter = CompositeFilterOperator.and(driverFilter, custFilter);

        Query query = new Query("Requests").setFilter(validFilter);
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            String isAccepted = (String) entity.getProperty("isAccepted");
            entity.setProperty("isAccepted", "complete");
            datastore.put(entity);
        }

        if(tip >0 || rating > 0)
        {    
            Query driverQuery = new Query("Driver").setFilter(driverFilter);
            PreparedQuery driverResults = datastore.prepare(query);
            for (Entity entity : driverResults.asIterable()) {
                double money = (double) entity.getProperty("money");
                double newWallet = money + tip;
                System.out.println("Old wallet" + money + " new wallet: " + newWallet);
                entity.setProperty("money", newWallet);
                long currRating = (long) entity.getProperty("overallRating");
                long newRating = currRating + rating;
                entity.setProperty("overallRating", newRating);
                System.out.println("Old rating" + currRating + " new wallet: " + newRating);
                datastore.put(entity);
            }
        }

        resp.sendRedirect("/FeedbackSubmit.html");

        
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}