package com.clouway.bank.db;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistenceUserRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private PersistenceUserRepository userRepository;

  @Before
  public void setUp() {
    helper.setUp();
    userRepository = new PersistenceUserRepository();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void registerUser(){
    String username = "John";
    String password = "1234567";
    userRepository.registerUser(username, password);
    List<User> users = userRepository.findAll();
    assertThat(users.size(), is(1));
    assertThat(users.get(0).username, is(username));
    assertThat(users.get(0).password, is(password));
  }

  @Test
  public void registerTwoUsers(){
    String username = "Ivan";
    String password = "1234567";
    userRepository.registerUser(username, password);

    String username1 = "Petar";
    String password1 = "asdfghj";
    userRepository.registerUser(username1, password1);

    List<User> users = userRepository.findAll();
    assertThat(users.size(), is(2));
    assertThat(users.get(0).username, is(username));
    assertThat(users.get(0).password, is(password));
    assertThat(users.get(1).username, is(username1));
    assertThat(users.get(1).password, is(password1));
  }

  @Test
  public void findUser(){
    String username = "Panayot";
    String password = "qwerty";
    userRepository.registerUser(username, password);
    boolean isValid = userRepository.findUser(username, password);
    assertThat(isValid, is(true));
  }

  @Test
  public void findNotExistingUser(){
    String username = "Tihomir";
    String password = "tyuiop";
    userRepository.registerUser(username, password);
    boolean isValid = userRepository.findUser("Hasan", "123456");
    assertThat(isValid, is(false));
  }

  @Test
  public void findUserWithValidUsernameAndInvalidPassword(){
    String username = "Coni";
    String password = "poiuyt";
    userRepository.registerUser(username, password);
    boolean isValid = userRepository.findUser(username, "1234567");
    assertThat(isValid, is(false));
  }

  @Test
  public void checkForUserWithTheSameUsername(){
    String username = "Stefan";
    String password = "zxcvbn";
    userRepository.registerUser(username, password);
    boolean usernameIsInDB = userRepository.usernameIsInDB(username);
    assertThat(usernameIsInDB, is(true));
  }

  @Test
  public void anotherCheckForUserWithNotExistingUsername(){
    String username = "Georgi";
    String password = "lkjhgf";
    userRepository.registerUser(username, password);
    boolean usernameIsInDB = userRepository.usernameIsInDB("Mihail");
    assertThat(usernameIsInDB, is(false));
  }

  @Test
  public void validateUserWithExistingPasswordAndDifferentUsername(){
    String username = "Tihomir";
    String password = "tyuiop";
    userRepository.registerUser(username, password);
    boolean isValid = userRepository.findUser("Hasan", "tyuiop");
    assertThat(isValid, is(false));
  }

}