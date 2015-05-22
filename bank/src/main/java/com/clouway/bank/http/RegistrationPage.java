package com.clouway.bank.http;

import com.clouway.bank.db.UserRepository;
import com.clouway.bank.validator.UserValidator;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Post;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
@At("/registration")
@Show("registration.html")
public class RegistrationPage {
  private final UserRepository userRepository;
  private final UserValidator userValidator;
  public String username;
  public String password;
  public String message;

  @Inject
  public RegistrationPage(UserRepository userRepository, UserValidator userValidator) {
    this.userRepository = userRepository;
    this.userValidator = userValidator;
  }

  @Post
  public void registerUser() {
    boolean isValid = userValidator.validate(username, password);
    if (!isValid) {
      message = "The Registration is NOT successful";
      return;
    }
    if(userRepository.usernameIsInDB(username)){
      message = "The username already exists";
      return;
    }
    userRepository.registerUser(username, password);
    message = "The Registration is successful";
  }
}
