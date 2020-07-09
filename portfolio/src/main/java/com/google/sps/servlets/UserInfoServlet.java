// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.UserInfo;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user-info")
public class UserInfoServlet extends HttpServlet {
  UserService userService = UserServiceFactory.getUserService();
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (userService.isUserLoggedIn) {
        UserInfo userInfo = getUserInfo(userService.getCurrentUser().getUserId());
    
        // Send the JSON as the response
        response.setContentType("application/json;");
        response.getWriter().println(CommonUtils.convertToJson(userInfo));
    } else {

    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/login");
      return;
    }

    String name = request.getParameter("username");
    boolean showEmail = request.getParameter("showEmail"); 
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity("UserInfo", id);
    entity.setProperty("id", id);
    entity.setProperty("name", name);
    entity.setProperty("showEmail", showEmail);
    // The put() function automatically inserts new data or updates existing data based on ID
    datastore.put(entity);

    // Redirect to the same HTML page.
    response.sendRedirect("/index.html");
  }

  /**
   * Returns the username of the user with id, or empty String if the user has not set a username.
   */
  private String getUserInfo(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("UserInfo")
                      .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return "";
    }
    String userName = (String) entity.getProperty("username");
    boolean showEmail = (boolean) entity.getProperty("showEmail");
    return new UserInfo(userName, showEmail);
  }
}
