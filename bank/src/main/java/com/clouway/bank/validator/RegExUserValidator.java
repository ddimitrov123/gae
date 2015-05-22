package com.clouway.bank.validator;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class RegExUserValidator implements UserValidator {
  @Override
  public boolean validate(String username, String password) {
    return username.matches("^[a-zA-Z0-9]{3,10}$") && password.matches("^[a-zA-Z0-9]{6,12}$");
  }
}
