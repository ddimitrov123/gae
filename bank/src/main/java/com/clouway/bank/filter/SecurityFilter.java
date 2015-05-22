package com.clouway.bank.filter;

import com.clouway.bank.db.SessionRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@Singleton
public class SecurityFilter implements Filter {
  private SessionRepository sessionRepository;

  @Inject
  public SecurityFilter(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    String sid = getSid((HttpServletRequest) servletRequest);
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    if (sid == null) {
      httpResponse.sendRedirect("/login");
      return;
    }
    if(!isValidTime(sid)){
      sessionRepository.deleteSession(sessionRepository.getSession(sid).username);
      Cookie cookie = httpRequest.getCookies()[0];
      cookie.setMaxAge(0);
      httpResponse.addCookie(cookie);
      httpResponse.sendRedirect("/login");
      return;
    }
    sessionRepository.refreshSession(sessionRepository.getSession(sid).username, new Date().getTime());
    filterChain.doFilter(servletRequest, servletResponse);

  }

  @Override
  public void destroy() {

  }

  private boolean isValidTime(String sid){
    long expTime = sessionRepository.getSession(sid).expDate;
    long limit = expTime + 60000;
    long currentTime = new Date().getTime();
    return currentTime < limit;
  }

  private String getSid(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
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
