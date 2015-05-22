package com.clouway.bank.db;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface SessionRepository {
  void registerSession(String username, String session, long time);

  String generateSid(String username);

  long generateTime();

  Session getSession(String sid);

  void deleteSession(String username);

  void refreshSession(String username, long time);
}
