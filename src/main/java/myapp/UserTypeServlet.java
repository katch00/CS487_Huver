package myapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

import com.google.appengine.repackaged.com.google.protobuf.Timestamp;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;

public class UserTypeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
        try{
            HttpSession session=req.getSession(false);
            String userType = (String) session.getAttribute("userType");
            System.out.println("Type: " + userType);

            Gson gson = new Gson();
            String json = gson.toJson(userType);

            resp.setContentType("application/json;");
            System.out.println(json + "kalp2");
            resp.getWriter().println(json);
            
        }catch(Exception exp){
            System.out.println(exp);
        }        
    }
}