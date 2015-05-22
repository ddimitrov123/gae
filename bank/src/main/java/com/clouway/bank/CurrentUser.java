package com.clouway.bank;

import com.clouway.bank.db.SessionRepository;
import com.clouway.bank.db.UserRepository;
import com.google.inject.Inject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class CurrentUser {
  private HttpServletRequest request;
  private UserRepository userRepository;
  private SessionRepository sessionRepository;

  @Inject
  public CurrentUser(HttpServletRequest request, UserRepository userRepository, SessionRepository sessionRepository) {
    this.request = request;
    this.userRepository = userRepository;
    this.sessionRepository = sessionRepository;
  }

  public boolean checkForExistingUser(String username, String password){
    return userRepository.findUser(username, password);
  }

  public String getSid(){
    String sid = null;
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    for (Cookie each : cookies) {
      if (each.getName().equalsIgnoreCase("sid")) {
        sid = each.getValue();
      }
    }
    return sid;
  }

  public void registerSession(String username, String sid, long time) {
    sessionRepository.registerSession(username, sid, time);
  }

  public String generateSid(String username) {
    return sessionRepository.generateSid(username);
  }

  public long generateTime() {
    return sessionRepository.generateTime();
  }
}
