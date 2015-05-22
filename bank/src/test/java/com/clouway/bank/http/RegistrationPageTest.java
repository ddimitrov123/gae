package com.clouway.bank.http;

import com.clouway.bank.db.UserRepository;
import com.clouway.bank.validator.UserValidator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class RegistrationPageTest {
  private RegistrationPage registrationPage;

  @Mock
  UserRepository userRepository;
  @Mock
  UserValidator userValidator;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Before
  public void setUp() {
    registrationPage = new RegistrationPage(userRepository, userValidator);
  }

  @Test
  public void registerUser(){
    final String username = registrationPage.username = "John";
    final String password = registrationPage.password = "123456";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(true));
      oneOf(userRepository).usernameIsInDB(username);
      will(returnValue(false));
      oneOf(userRepository).registerUser(username, password);
    }});
    registrationPage.registerUser();
    assertThat(registrationPage.message, is("The Registration is successful"));
  }

  @Test
  public void registerUserWithInvalidNameAndValidPassword(){
    final String username = registrationPage.username = ".";
    final String password = registrationPage.password = "123456";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(false));
      never(userRepository).usernameIsInDB(username);
      never(userRepository).registerUser(username, password);
    }});
    registrationPage.registerUser();
    assertThat(registrationPage.message, is("The Registration is NOT successful"));
  }

  @Test
  public void registerUserWithValidUsernameAndInvalidPassword(){
    final String username = registrationPage.username = "Petar";
    final String password = registrationPage.password = "!";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(false));
      never(userRepository).usernameIsInDB(username);
      never(userRepository).registerUser(username, password);
    }});
    registrationPage.registerUser();
    assertThat(registrationPage.message, is("The Registration is NOT successful"));
  }
  
  @Test
  public void registerUserWithUnallowedSymbols(){
    final String username = registrationPage.username = "?|}*-&^$$^*";
    final String password = registrationPage.password = "!&^%&^^+-+?!";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(false));
      never(userRepository).usernameIsInDB(username);
      never(userRepository).registerUser(username, password);
    }});
    registrationPage.registerUser();
    assertThat(registrationPage.message, is("The Registration is NOT successful"));
  }

  @Test
  public void registerTwoUsersWithSameUsername(){
    final String username = registrationPage.username = "John";
    final String password = registrationPage.password = "123456";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(true));
      oneOf(userRepository).usernameIsInDB(username);
      will(returnValue(true));
      never(userRepository).registerUser(username, password);
    }});
    registrationPage.registerUser();
    assertThat(registrationPage.message, is("The username already exists"));
  }

  @Test
  public void registerWithEmptyUsernameAndPassword(){
    final String username = registrationPage.username = "";
    final String password = registrationPage.password = "";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(false));
      never(userRepository).usernameIsInDB(username);
      never(userRepository).registerUser(username, password);
    }});
    registrationPage.registerUser();
    assertThat(registrationPage.message, is("The Registration is NOT successful"));
  }
}
