package com.google.sps.data;

import com.google.sps.data.ConvertibleToJSON;

/** Object that stores comment data. */
public class Comment {
  private String commenterEmail;
  private String commenterName;
  private long id;
  private String imageUrl;
  private String text;
  private long timestamp;

  public Comment(String commenterEmail, String commenterName, long id, String imageUrl, String text,
      long timestamp) {
    this.commenterEmail = commenterEmail;
    this.commenterName = commenterName;
    this.id = id;
    this.imageUrl = imageUrl;
    this.text = text;
    this.timestamp = timestamp;
  }

  public String getCommenterEmail() {
    return commenterEmail;
  }

  public String getCommenterName() {
    return commenterName;
  }

  public long getId() {
    return id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getText() {
    return text;
  }
}