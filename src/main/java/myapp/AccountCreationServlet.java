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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

import com.google.appengine.repackaged.com.google.protobuf.Timestamp;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class AccountCreationServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String firstName = getParameter(req, "First Name", "");
    String lastName = getParameter(req, "Last Name", "");
    String phoneNumber = getParameter(req, "Phone Number", "");
    String address = getParameter(req, "Address", "");
    String city = getParameter(req, "City", "");
    String zipCode = getParameter(req, "Zip Code", "");
    String state = getParameter(req, "State", "");
    String username = getParameter(req, "Username", "");
    String password = getParameter(req, "Password", "");
    String date = req.getParameter("datemin");
    String user = getParameter(req, "user", "Customer");
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Random rand = new Random();
    double money = Math.floor(rand.nextDouble() * 10000)/100;
    System.out.println("Money: $" + money);

    if(user.equals("Customer")){
        Entity custEntity = new Entity("Customer");

        String cardNumber = getParameter(req, "Card Number", "");
        String securityNumber = getParameter(req, "Security Number", "");

        custEntity.setProperty("firstName", firstName);
        custEntity.setProperty("lastName", lastName);
        custEntity.setProperty("phoneNumber", phoneNumber);
        custEntity.setProperty("address", address);
        custEntity.setProperty("city", city);
        custEntity.setProperty("zipCode", zipCode);
        custEntity.setProperty("state", state);
        custEntity.setProperty("username", username);
        custEntity.setProperty("password", password);
        custEntity.setProperty("date", date);
        custEntity.setProperty("cardNumber", cardNumber);
        custEntity.setProperty("securityNumber", securityNumber);
        custEntity.setProperty("overallRating",0);
        custEntity.setProperty("money", money);
        datastore.put(custEntity);
    }
    else{
        Entity driverEntity = new Entity("Driver");

        String licenseNumber = getParameter(req, "Drivers License Number", "");
        String plateNumber = getParameter(req, "License Plate Number", "");
        String maxNumPassengers = getParameter(req, "mnp_entry", "");
        
        driverEntity.setProperty("firstName", firstName);
        driverEntity.setProperty("lastName", lastName);
        driverEntity.setProperty("phoneNumber", phoneNumber);
        driverEntity.setProperty("address", address);
        driverEntity.setProperty("city", city);
        driverEntity.setProperty("zipCode", zipCode);
        driverEntity.setProperty("state", state);
        driverEntity.setProperty("username", username);
        driverEntity.setProperty("password", password);
        driverEntity.setProperty("date", date);
        driverEntity.setProperty("licenseNumber", licenseNumber);
        driverEntity.setProperty("plateNumber", plateNumber);
        driverEntity.setProperty("maxNumPassengers", maxNumPassengers);
        driverEntity.setProperty("overallRating",0);
        driverEntity.setProperty("isOnline","false");
        driverEntity.setProperty("isAvailable","false");
        driverEntity.setProperty("money", money);
        datastore.put(driverEntity);
    }

    System.out.println("Account created.");

    resp.sendRedirect("/index.html");
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
