package com.google.sps.data;

public class UserInfo {
  private String username;
  private boolean showEmail;

  public UserInfo(String username, boolean showEmail) {
    this.username = username;
    this.showEmail = showEmail;
  }

  public String getUsername() {
    return username;
  }

  public boolean getShowEmail() {
    return showEmail;
  }
}