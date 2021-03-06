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
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.UserInfo;
import com.google.sps.utilities.CommonUtils;
import com.google.sps.utilities.UserInfoUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user-info")
public class UserInfoServlet extends HttpServlet {
  private static final String USERNAME = "username";
  private static final String SHOW_EMAIL = "show-email";
  private static final UserService userService = UserServiceFactory.getUserService();
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!userService.isUserLoggedIn()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in.");
    } else {
      // Send user info as JSON response.
      response.setContentType("application/json;");
      UserInfo userInfo = UserInfoUtils.getUserInfo(userService.getCurrentUser().getUserId());
      response.getWriter().println(CommonUtils.convertToJson(userInfo));
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/login");
      return;
    }

    // Get user information from userService and request parameters.
    // For showEmail, when SHOW_EMAIL is null meaning the checkbox is unchecked
    // showEmail is set to false otherwise it is set to true.
    User currentUser = userService.getCurrentUser();
    String email = currentUser.getEmail();
    String id = currentUser.getUserId();
    boolean showEmail = request.getParameter(SHOW_EMAIL) != null;
    String username = request.getParameter(USERNAME);
    datastore.put(UserInfoUtils.buildUserInfoEntity(id, email, showEmail, username));

    // Redirect to the same HTML page.
    response.sendRedirect("/comments.html");
  }
}
