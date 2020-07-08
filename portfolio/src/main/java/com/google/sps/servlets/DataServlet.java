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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.utilities.CommonUtils;
import com.google.sps.utilities.DatastoreHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  public static final String NUMBER_OF_COMMENTS = "number-of-comments";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get input from the form.
    int numberOfComments;
    try {
      numberOfComments = getNumberOfComments(request);
      response.setContentType("application/json;");
      response.getWriter().println(convertToJson(getCommentsFromDataStore(numberOfComments)));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      response.setContentType("text/html");
      response.getWriter().println("Please enter an integer value greater than 1");
      return;
    }
  }

  /*
   * Converts List of comments into a JSON using the gson library.
   */
  private static String convertToJson(List<Comment> comments) {
    return new Gson().toJson(comments);
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null || value.isEmpty()) ? defaultValue : value;
  }

  /** Returns number of comments entered by the user or -1 if choice is invalid */
  private static int getNumberOfComments(HttpServletRequest request) throws Exception {
    // Get input from the form.
    String numberOfCommentsString = request.getParameter(NUMBER_OF_COMMENTS);

    // Convert input to an int.
    int numberOfComments;
    try {
      numberOfComments = Integer.parseInt(numberOfCommentsString);
    } catch (NumberFormatException e) {
      throw(new NumberFormatException(
          String.format("Cannot convert to int: %s", numberOfCommentsString)));
    }

    // Check that the input is greater than 0.
    if (numberOfComments < 0) {
      throw(new Exception(String.format(
          "Number of comments specified is out of range: %s", numberOfCommentsString)));
    }
    return numberOfComments;
  }

  /** Returns comments limiting the number to numberOfComments */
  private static List<Comment> getCommentsFromDataStore(int numberOfComments) {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<Entity> resultsList = results.asList(FetchOptions.Builder.withLimit(numberOfComments));

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : resultsList) {
      comments.add(DatastoreHelper.entityToComment(entity));
    }
    return comments;
  }
}
