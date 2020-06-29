package com.google.sps.servlets;

/** Object that stores comment data. */
public class Comment {
  String text;
  String commenterName;
  String commenterEmail;

  public Comment(String text, String commenterName, String commenterEmail){
    this.text = text;
    this.commenterName = commenterName;
    this.commenterEmail = commenterEmail;
  }
}