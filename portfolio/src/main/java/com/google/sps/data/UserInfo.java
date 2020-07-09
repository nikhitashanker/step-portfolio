package com.google.sps.data;

public class UserInfo {
  private String name;
  private boolean showEmail;

  public UserInfo(String name, boolean showEmail) {
    this.name = name;
    this.showEmail = showEmail;
  }

  public String getName() {
    return name;
  }

  public String getShowEmail() {
    return showEmail;
  }
}