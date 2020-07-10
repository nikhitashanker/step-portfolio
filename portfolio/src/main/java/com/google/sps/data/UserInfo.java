package com.google.sps.data;

public class UserInfo {
  private String email;
  private boolean showEmail;
  private String username;

  public UserInfo(String email, boolean showEmail, String username) {
    this.email = email;
    this.username = username;
    this.showEmail = showEmail;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public boolean getShowEmail() {
    return showEmail;
  }
}