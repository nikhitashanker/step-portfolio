package com.google.sps.data;

import com.google.gson.Gson;

public abstract class ConvertibleToJSON {
  /*
   * Converts object to JSON.
   */
  public String convertToJson() {
    return new Gson().toJson(this);
  }
}