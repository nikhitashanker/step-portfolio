package com.google.sps.utilities;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonUtils {
  public static String convertToJson(List<Comment> comments) {
    return new Gson().toJson(comments);
  }

  public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null || value.isEmpty()) ? defaultValue : value;
  }
}