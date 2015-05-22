package com.clouway.bank.db;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistenceSessionRepository implements SessionRepository {
  @Override
  public void registerSession(String username, String session, long time) {
    ObjectDatastore datastore = new AnnotationObjectDatastore();
    datastore.store(new Session(username, session, time));
  }

  @Override
  public String generateSid(String username) {
    UUID uuid = new UUID(10, 5);
    String randomValue = username + uuid.randomUUID().toString() + "qwerty";
    return sha1(randomValue);
  }

  @Override
  public long generateTime() {
    return new Date().getTime();
  }

  @Override
  public Session getSession(String sid) {
    ObjectDatastore datastore = new AnnotationObjectDatastore();
    return datastore.find().type(Session.class).addFilter("value", FilterOperator.EQUAL, sid).returnResultsNow().next();
  }

  @Override
  public void deleteSession(String username) {
    ObjectDatastore datastore = new AnnotationObjectDatastore();
    Entity entity = new Entity("com_clouway_bank_db_Session", username);
    Session session = datastore.load(entity.getKey());
    datastore.delete(session);
  }

  @Override
  public void refreshSession(String username, long time) {
    ObjectDatastore datastore = new AnnotationObjectDatastore();
    Entity entity = new Entity("com_clouway_bank_db_Session", username);
    Session session = datastore.load(entity.getKey());
    session.expDate = time;
    datastore.update(session);
  }

  static String sha1(String input) {
    MessageDigest mDigest = null;
    try {
      mDigest = MessageDigest.getInstance("SHA1");
    } catch (NoSuchAlgorithmException e) {
      return "";
    }

    byte[] result = mDigest.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
  }
}
