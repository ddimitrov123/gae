package com.clouway.bank.validator;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface UserValidator {
  boolean validate(String username, String password);
}
