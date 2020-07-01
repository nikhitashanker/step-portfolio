package com.google.sps.data;

/** Object that stores comment data. */
public class Comment {
  private String commenterEmail;
  private String commenterName;
  private long id;
  private String text;

  public Comment(
      String commenterEmail, String commenterName, long id, String text) {
    this.commenterEmail = commenterEmail;
    this.commenterName = commenterName;
    this.id = id;
    this.text = text;
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

  public String getText() {
    return text;
  }
}