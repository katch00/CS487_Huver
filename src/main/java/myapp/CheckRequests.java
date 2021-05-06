package main.java.myapp;

public class CheckRequests extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String driverName = getParameter(req, "driverName", "");
        String custName = getParameter(req, "custName", "");

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
            resp.sendRedirect("/index.html");
        }

    }
}