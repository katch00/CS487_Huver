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

import com.google.appengine.repackaged.com.google.protobuf.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

public class AccountCreationServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
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
    
    String kind;
    if(user.equals("Customer")){
        kind = "Customer_Account";
        String cardNumber = getParameter(req, "Card Number", "");
        String securityNumber = getParameter(req, "Security Number", "");
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind(kind);
        FullEntity user_Account = Entity.newBuilder(keyFactory.newKey())
            .set("firstName", firstName)
            .set("lastName", lastName)
            .set("phoneNumber", phoneNumber)
            .set("address", address)
            .set("city", city)
            .set("zipCode", zipCode)
            .set("state", state)
            .set("username", username)
            .set("password", password)
            .set("date", date)
            .set("cardNumber", cardNumber)
            .set("securityNumber", securityNumber)
            .build();
            datastore.put(user_Account);
    }
    else{
        kind = "Driver_Account";
        String licenseNumber = getParameter(req, "Drivers License Number", "");
        String plateNumber = getParameter(req, "License Plate Number", "");
        String maxNumPassengers = getParameter(req, "mnp_entry", "");
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind(kind);
        FullEntity user_Account = Entity.newBuilder(keyFactory.newKey())
            .set("firstName", firstName)
            .set("lastName", lastName)
            .set("phoneNumber", phoneNumber)
            .set("address", address)
            .set("city", city)
            .set("zipCode", zipCode)
            .set("state", state)
            .set("username", username)
            .set("password", password)
            .set("date", date)
            .set("licenseNumber", licenseNumber)
            .set("plateNumber", plateNumber)
            .set("maxNumPassengers", maxNumPassengers)
            .build();
            datastore.put(user_Account);
    }

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
