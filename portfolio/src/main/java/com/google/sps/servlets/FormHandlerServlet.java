package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.UserInfo;
import com.google.sps.utilities.CommentUtils;
import com.google.sps.utilities.CommonUtils;
import com.google.sps.utilities.SentimentUtils;
import com.google.sps.utilities.UserInfoUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When the user submits the form, Blobstore processes the file upload and then forwards the request
 * to this servlet. This servlet can then process the request using the file URL we get from
 * Blobstore.
 */
@WebServlet("/my-form-handler")
public class FormHandlerServlet extends HttpServlet {
  private static final UserService userService = UserServiceFactory.getUserService();
  private static final String TEXT = "comment-text";
  private static final String IMAGE = "image";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the URL of the image that the user uploaded to Blobstore.
    String blobKeyString;
    try {
      blobKeyString = getUploadedBlobKeyString(request, IMAGE);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      response.setContentType("text/html");
      response.getWriter().println(String.format("The error is %s", e.toString()));
      return;
    }

    // Set the commenter email.
    UserInfo userInfo = UserInfoUtils.getUserInfo(userService.getCurrentUser().getUserId());
    String commenterEmail = getCommenterEmail(userInfo);

    // Set commenter name to username if user provided one and Anonymous otherwise.
    String commenterName = userInfo == null ? "Anonymous" : userInfo.getUsername();

    // Get input from the form for comment text.
    String text = CommonUtils.getParameter(request, TEXT, /* DefaultValue= */ "");

    // Get text with the sentiment tag added.
    String textWithSentiment = null;
    try {
      textWithSentiment = String.format("(%s) : %s", SentimentUtils.getSentimentTag(text), text);
    } catch (IOException e) {
      System.err.println("IOException occurred.");
      response.setContentType("text/html");
    }

    // Add the comment to datastore.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(CommentUtils.buildCommentEntity(blobKeyString, commenterEmail, commenterName,
        textWithSentiment, System.currentTimeMillis()));

    // Redirect to same HTML page.
    response.sendRedirect("/index.html");
  }

  /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
  private static String getUploadedBlobKeyString(
      HttpServletRequest request, String formInputElementName) throws Exception {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(IMAGE);

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // User submitted more than 1 file, so we throw an exception.
    if (blobKeys.size() > 1) {
      throw new Exception(String.format("Number of files %d uploaded exceeds 1.", blobKeys.size()));
    }

    // Get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    return blobKey.getKeyString();
  }

  // Return email from userInfo if user opted to show their email
  // and Unknown otherwise.
  private static String getCommenterEmail(UserInfo userInfo) {
    if (userInfo != null && userInfo.getShowEmail()) {
      return userInfo.getEmail();
    }
    return "Unknown";
  }
}