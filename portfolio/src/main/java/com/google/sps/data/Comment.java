package com.google.sps.data;

/** Object that stores comment data. */
public class Comment {
  private final String commenterEmail;
  private final String commenterName;
  private final long id;
  private final String imageUrl;
  private final String text;
  private final long timestamp;

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