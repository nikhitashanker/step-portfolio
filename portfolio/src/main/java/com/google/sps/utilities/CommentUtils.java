package com.google.sps.utilities;

import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.Comment;

public class CommentUtils {
  private static final String BLOB_KEY_STRING = "blobKeyString";
  private static final String ITEM_TYPE = "Comment";
  private static final String EMAIL = "commenterEmail";
  private static final String NAME = "commenterName";
  private static final String TEXT = "text";
  private static final String TIMESTAMP = "timestamp";

  public static Comment entityToComment(Entity entity) {
    String blobKeyString = (String) entity.getProperty(BLOB_KEY_STRING);
    String commenterEmail = (String) entity.getProperty(EMAIL);
    String commenterName = (String) entity.getProperty(NAME);
    long id = entity.getKey().getId();
    String text = (String) entity.getProperty(TEXT);
    long timestamp = (long) entity.getProperty(TIMESTAMP);
    return new Comment(blobKeyString, commenterEmail, commenterName, id, text, timestamp);
  }

  public static Entity buildCommentEntity(String blobKeyString, String commenterEmail,
      String commenterName, String text, long timestamp) {
    Entity commentEntity = new Entity(ITEM_TYPE);
    commentEntity.setProperty(BLOB_KEY_STRING, blobKeyString);
    commentEntity.setProperty(EMAIL, commenterEmail);
    commentEntity.setProperty(NAME, commenterName);
    commentEntity.setProperty(TEXT, text);
    commentEntity.setProperty(TIMESTAMP, timestamp);
    return commentEntity;
  }
}
