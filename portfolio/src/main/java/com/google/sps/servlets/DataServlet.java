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
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
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
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      comments.add(entityToComment(entity));
    }

    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(convertToJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get input from the form.
    String text = getParameter(request, "comment-text", "");
    String commenterName = getParameter(request, "commenter-name", "Anonymous");
    String commenterEmail = getParameter(request, "commenter-email", "Unknown");
    long timestamp = System.currentTimeMillis();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(buildCommentEntity(commenterEmail, commenterName, text, timestamp));

    // Redirect to same HTML page.
    response.sendRedirect("/index.html");
  }

  /*
   * Converts List of comments into a JSON using the gson library.
   */
  private String convertToJson(List<Comment> comments) {
    return new Gson().toJson(comments);
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null || value.isEmpty()) ? defaultValue : value;
  }

  private Comment entityToComment(Entity entity) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty("text");
      String commenterName = (String) entity.getProperty("commenterName");
      String commenterEmail = (String) entity.getProperty("commenterEmail");
      long timestamp = (long) entity.getProperty("timestamp");
      return new Comment(commenterEmail, commenterName, id, text, timestamp);
  }

  private Entity buildCommentEntity(String commenterEmail, String commenterName, String text, long timestamp) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("commenterEmail", commenterEmail);
    commentEntity.setProperty("commenterName", commenterName);
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("timestamp", timestamp);
    return commentEntity;
  }
}
