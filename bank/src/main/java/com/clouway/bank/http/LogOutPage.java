package com.clouway.bank.http;

import com.clouway.bank.db.SessionRepository;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/logout")
@Show("index.html")
public class LogOutPage {
  private SessionRepository sessionRepository;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @Inject
  public LogOutPage(SessionRepository sessionRepository, HttpServletRequest request, HttpServletResponse response) {
    this.sessionRepository = sessionRepository;
    this.request = request;
    this.response = response;
  }

  @Get
  public void get() {
    String sid = getSid();
    if(sid != null){
      Cookie cookie = request.getCookies()[0];
      cookie.setMaxAge(0);
      sessionRepository.deleteSession(sessionRepository.getSession(sid).username);
      response.addCookie(cookie);
    }
  }

  private String getSid() {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }

    for (Cookie each : cookies) {
      if (each.getName().equalsIgnoreCase("sid")) {
        return each.getValue();
      }
    }

    return null;
  }
}
