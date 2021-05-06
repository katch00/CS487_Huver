/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public class SessionLoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
        try{
            HttpSession session=req.getSession(false);
            String username = (String) session.getAttribute("username");
            System.out.println("Name: " + username);

            Gson gson = new Gson();
            String json = gson.toJson(username);

            resp.setContentType("application/json;");
            System.out.println(json + "kalp2");
            resp.getWriter().println(json);
            /*
            if(username == null || username.equals("")){
                String text = "false";
                System.out.println("Goal: " + text);
                resp.setContentType("text/plain"); 
                resp.setCharacterEncoding("UTF-8"); 
                resp.getWriter().println(text);
            }
            else{
                System.out.println("Goal: true");
                resp.setContentType("text/plain");
                resp.getWriter().print(true);
            }*/
        }catch(Exception exp){
            System.out.println(exp);
        }
        //resp.sendRedirect("/index.html");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = getParameter(req, "username", "");
        String password = getParameter(req, "pwd", "");
        String role = getParameter(req, "role", "");
    
        String kind;
        if(role.equals("Customer")){
            kind = "Customer";
        }
        else{
            kind = "Driver";
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter correctUsername = new FilterPredicate("username", FilterOperator.EQUAL, username);
        Query query = new Query(kind).setFilter(correctUsername);
        PreparedQuery results = datastore.prepare(query);
        boolean valid = false;
        for (Entity entity : results.asIterable()) {
            if(entity.getProperty("password").equals(password)){
                System.out.println("Valid account");
                valid = true;
                if(kind.equals("Driver"))
                {
                    entity.setProperty("isAvailable", "true");
                    entity.setProperty("isOnline", "true");
                }
            }      
        }
        if(!valid){
            System.out.println("Invalid account");
            PrintWriter pwriter = resp.getWriter();  
            resp.setContentType("text/html");  
            pwriter.print("<script type=\"text/javascript\">");  
            pwriter.print("alert('Invalid username or password');");  
            pwriter.print("</script>");
            //pwriter.print("<a href='/index.html'>Return to Login</a>");
            pwriter.print("<meta http-equiv =\"refresh\" content = \"0; url=/login.html\">");
            pwriter.close();
        }
        else{
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("userType", kind);
            System.out.println("Username: " + username);    
            resp.sendRedirect("/index.html");
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

