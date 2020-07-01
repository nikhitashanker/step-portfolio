package com.google.sps.data;

/** Object that stores comment data. */
public class Comment {
  private String commenterEmail;
  private String commenterName;
  private String text;

  public Comment(String commenterEmail, String commenterName, String text) {
    this.commenterEmail = commenterEmail;
    this.commenterName = commenterName;
    this.text = text;
  }

  public String getCommenterEmail() {
    return commenterEmail;
  }

  public String getCommenterName() {
    return commenterName;
  }

  public String getText() {
    return text;
  }
}