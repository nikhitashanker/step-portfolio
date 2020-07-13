package com.google.sps.utilities;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.sps.data.Comment;
import java.io.IOException;

public class SentimentUtils {
  private static final String POSITIVE_COMMENT_TAG = "POSITIVE COMMENT";
  private static final String NEGATIVE_COMMENT_TAG = "NEGATIVE COMMENT";

  public static String getSentimentTag(String text) throws IOException {
    if (decideIsPositive(text)) {
      return POSITIVE_COMMENT_TAG;
    } else {
      return NEGATIVE_COMMENT_TAG;
    }
  }

  private static boolean decideIsPositive(String comment) throws IOException {
    Document doc =
        Document.newBuilder().setContent(comment).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();
    return score > 0;
  }
}