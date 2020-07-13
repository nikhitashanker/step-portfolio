package com.google.sps.data;

/** Object that stores comment data. */
public class Comment {
  private final String blobKeyString;
  private final String commenterEmail;
  private final String commenterName;
  private final long id;
  private final String text;
  private final long timestamp;

  public Comment(String blobKeyString, String commenterEmail, String commenterName, long id,
      String text, long timestamp) {
    this.blobKeyString = blobKeyString;
    this.commenterEmail = commenterEmail;
    this.commenterName = commenterName;
    this.id = id;
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

  public String getBlobKeyString() {
    return blobKeyString;
  }

  public String getText() {
    return text;
  }
}