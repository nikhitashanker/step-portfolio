package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns the login status of the user. */
@WebServlet("/login-status")
public class LoginStatusServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    LoginStatus status;
    if (userService.isUserLoggedIn()) {
      status = new LoginStatus(true);
      System.out.println(true);
    } else {
      status = new LoginStatus(false);
      System.out.println(false);
    }

    System.out.println(convertToJson(status));
    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(convertToJson(status));
  }

  private String convertToJson(LoginStatus status) {
    return new Gson().toJson(status);
  }

  public class LoginStatus {
    private boolean isLoggedIn;
    public LoginStatus(boolean isLoggedIn) {
      this.isLoggedIn = isLoggedIn;
    }
  }
}