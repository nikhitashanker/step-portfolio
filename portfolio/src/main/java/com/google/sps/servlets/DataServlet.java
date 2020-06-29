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

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> comments;

  @Override
  public void init() {
    comments = new ArrayList<String>();
    comments.add("I also like Despicable Me 2!");
    comments.add("I do not like running.");
    comments.add("Your projects look interesting.");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");

    // Convert the comments into JSON.
    String json = convertToJson(comments);

    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /*
   * Converts List instance into a JSON using the gson library.
   */
  private String convertToJson(List<String> messages){
    Gson gson = new Gson();
    return gson.toJson(messages);
  }
}
