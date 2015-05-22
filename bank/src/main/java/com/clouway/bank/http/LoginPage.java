package com.clouway.bank.http;

import com.clouway.bank.CurrentUser;
import com.clouway.bank.db.UserRepository;
import com.clouway.bank.validator.UserValidator;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Post;

import javax.inject.Provider;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/login")
@Show("login.html")
public class LoginPage {
  private final CurrentUser currentUser;
  private final UserValidator userValidator;
  private final HttpServletResponse response;

  public String username;
  public String password;
  public String message;

  @Inject
  public LoginPage(CurrentUser currentUser, UserValidator userValidator, HttpServletResponse response) {
    this.currentUser = currentUser;
    this.userValidator = userValidator;
    this.response = response;
  }

  @Post
  public String login() {
    boolean isValid = userValidator.validate(username, password);
    if (!isValid) {
      message = "Invalid username or password";
      return null;
    }
    boolean isExisting = currentUser.checkForExistingUser(username, password);
    if (!isExisting) {
      message = "Invalid username or password";
      return null;
    }
    String sid = currentUser.getSid();
    if (sid == null) {
      sid = currentUser.generateSid(username);
      response.addCookie(new Cookie("sid", sid));
      long time = currentUser.generateTime();
      currentUser.registerSession(username, sid, time);
    }

    return "/welcome";
  }
}
