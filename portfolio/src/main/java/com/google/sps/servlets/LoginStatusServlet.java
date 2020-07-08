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
import com.google.sps.data.ConvertibleToJSON;
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
    // Get the current login status.
    LoginStatus status;
    if (UserServiceFactory.getUserService().isUserLoggedIn()) {
      status = LoginStatus.getLoggedInInstance();
    } else {
      status = LoginStatus.getNotLoggedInInstance();
    }

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(status.convertToJson());
  }

  private static class LoginStatus extends ConvertibleToJSON {
    private static final LoginStatus STATUS_LOGGED_IN = new LoginStatus(true);
    private static final LoginStatus STATUS_NOT_LOGGED_IN = new LoginStatus(false);
    private boolean isLoggedIn;

    private LoginStatus(boolean isLoggedIn) {
      this.isLoggedIn = isLoggedIn;
    }
    public static LoginStatus getLoggedInInstance() {
      return STATUS_LOGGED_IN;
    }
    public static LoginStatus getNotLoggedInInstance() {
      return STATUS_NOT_LOGGED_IN;
    }
  }
}