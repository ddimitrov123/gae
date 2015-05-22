package com.clouway.bank.validator;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class RegExUserValidatorTest {
  private RegExUserValidator userValidator;

  @Before
  public void setUp() throws Exception {
    userValidator = new RegExUserValidator();
  }

  @Test
  public void validUsernameAndPassword(){
    String username = "Petar";
    String password = "123456";
    boolean isValid = userValidator.validate(username, password);
    assertThat(isValid, is(true));
  }

  @Test
  public void validUsernameInvalidPassword(){
    String username = "Ivan";
    String password = "+_|?>+&^*&^";
    boolean isValid = userValidator.validate(username, password);
    assertThat(isValid, is(false));
  }

  @Test
  public void invalidUsernameValidPassword(){
    String username = "*/?><:}{|";
    String password = "1234567";
    boolean isValid = userValidator.validate(username, password);
    assertThat(isValid, is(false));
  }

  @Test
  public void invalidUsernameAndPassword(){
    String username = "*/?><:}{|";
    String password = ")(*&^^%%";
    boolean isValid = userValidator.validate(username, password);
    assertThat(isValid, is(false));
  }

  @Test
  public void validateWhiteSpacesInUsername(){
    String username = "Pana yot  ";
    String password = "1234567";
    boolean isValid = userValidator.validate(username, password);
    assertThat(isValid, is(false));
  }

  @Test
  public void validateWhiteSpacesInPassword(){
    String username = "Panayot";
    String password = "123 4567  ";
    boolean isValid = userValidator.validate(username, password);
    assertThat(isValid, is(false));
  }

}