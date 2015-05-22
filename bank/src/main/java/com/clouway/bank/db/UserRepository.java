package com.clouway.bank.db;
import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface UserRepository {
  void registerUser(String username, String password);

  boolean usernameIsInDB(String username);

  boolean findUser(String username, String password);

  List<User> findAll();
}
