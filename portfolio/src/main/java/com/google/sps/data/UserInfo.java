package com.google.sps.data;

public class UserInfo {
  private String email;
  private boolean showEmail;
  private String username;

  public UserInfo(String email, boolean showEmail, String username) {
    this.email = email;
    this.showEmail = showEmail;
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public boolean getShowEmail() {
    return showEmail;
  }

  public String getUsername() {
    return username;
  }
}