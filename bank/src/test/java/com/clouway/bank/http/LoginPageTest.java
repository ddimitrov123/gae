package com.clouway.bank.http;

import com.clouway.bank.CurrentUser;
import com.clouway.bank.db.SessionRepository;
import com.clouway.bank.db.UserRepository;
import com.clouway.bank.validator.UserValidator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class LoginPageTest {
  private LoginPage loginPage;
  private FakeHttpServletResponse response;

  @Mock
  UserRepository userRepository;
  @Mock
  UserValidator userValidator;
  @Mock
  HttpServletRequest request;
  @Mock
  SessionRepository sessionRepository;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Before
  public void setUp() throws Exception {
    response = new FakeHttpServletResponse();
    CurrentUser currentUser = new CurrentUser(request, userRepository, sessionRepository);
    loginPage = new LoginPage(currentUser, userValidator, response);
  }

  @Test
  public void loginWithValidUsernameAndPassword(){
    final String username = loginPage.username = "John";
    final String password = loginPage.password = "1234567";
    final Cookie[] cookies = new Cookie[]{new Cookie("sid", "asdasd")};
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(true));
      oneOf(userRepository).findUser(username, password);
      will(returnValue(true));
      oneOf(request).getCookies();
      will(returnValue(cookies));
    }});
    String redirectTo = loginPage.login();
    assertThat(redirectTo, is("/welcome"));
  }

  @Test
  public void loginWithNoCookie(){
    loginPage.username = "Petko";
    loginPage.password = "Ivanov";
    final String sid = "abc123456";
    final long time = 123456789l;
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(loginPage.username, loginPage.password);
      will(returnValue(true));
      oneOf(userRepository).findUser(loginPage.username, loginPage.password);
      will(returnValue(true));
      oneOf(request).getCookies();
      will(returnValue(null));
      oneOf(sessionRepository).generateSid(loginPage.username);
      will(returnValue(sid));
      oneOf(sessionRepository).generateTime();
      will(returnValue(time));
      oneOf(sessionRepository).registerSession(loginPage.username, sid, time);
    }});
    String redirectTo = loginPage.login();
    assertThat(redirectTo, is("/welcome"));
    assertThat(response.getCookie().getValue(), is(sid));
    assertThat(response.getCookie().getName(), is("sid"));
  }

  @Test
  public void loginWithUnallowedSymbols(){
    final String username = loginPage.username = "$@#&$%*(#>?89+-";
    final String password = loginPage.password = "+}|+_)*(*&*^&";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(false));
      never(userRepository).findUser(username, password);
    }});
    String redirectTo = loginPage.login();
    assertThat(redirectTo, is((String) null));
    assertThat(loginPage.message, is("Invalid username or password"));
  }

  @Test
  public void loginWithExistingUsernameAndWrongPassword(){
    final String username = loginPage.username = "Ivan";
    final String password = loginPage.password = "256978";
    context.checking(new Expectations(){{
      oneOf(userValidator).validate(username, password);
      will(returnValue(true));
      oneOf(userRepository).findUser(username, password);
      will(returnValue(false));
    }});
    String redirectTo = loginPage.login();
    assertThat(redirectTo, is((String) null));
    assertThat(loginPage.message, is("Invalid username or password"));
  }

  @Test
  public void loginWithNotExistingUsernameAndExistingPassword(){
    final String username = loginPage.username = "Petar";
    final String password = loginPage.password = "123456";
    context.checking(new Expectations() {{
      oneOf(userValidator).validate(username, password);
      will(returnValue(true));
      oneOf(userRepository).findUser(username, password);
      will(returnValue(false));
    }});
    String redirectTo = loginPage.login();
    assertThat(redirectTo, is((String) null));
    assertThat(loginPage.message, is("Invalid username or password"));
  }

}
