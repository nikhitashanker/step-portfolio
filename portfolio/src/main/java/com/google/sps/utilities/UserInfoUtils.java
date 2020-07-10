package com.google.sps.utilities;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.UserInfo;

public class UserInfoUtils {
  private static final String ID = "id";
  private static final String EMAIL = "email";
  private static final String SHOW_EMAIL = "showEmail";
  private static final String USERNAME = "username";
  private static final String USER_INFO = "UserInfo";

  /**
   * Returns the user info of the user with id, or null if the user is not found.
   */
  public static UserInfo getUserInfo(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(USER_INFO).setFilter(
        new Query.FilterPredicate(ID, Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    return entityToUserInfo(entity);
  }

  public static Entity buildUserInfoEntity(
      String id, String email, boolean showEmail, String username) {
    Entity entity = new Entity(USER_INFO, id);
    entity.setProperty(ID, id);
    entity.setProperty(EMAIL, email);
    entity.setProperty(SHOW_EMAIL, showEmail);
    entity.setProperty(USERNAME, username);
    return entity;
  }

  private static UserInfo entityToUserInfo(Entity entity) {
    String email = (String) entity.getProperty(EMAIL);
    boolean showEmail = (boolean) entity.getProperty(SHOW_EMAIL);
    String username = (String) entity.getProperty(USERNAME);
    return new UserInfo(email, showEmail, username);
  }
}