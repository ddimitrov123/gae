package com.clouway.bank.db;

import com.vercer.engine.persist.annotation.Child;
import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class User {
  @Key public String id;
  public String username;
  public String password;
  public Double balance;

  public User(String username, String password, Double balance) {
    this.username = username;
    this.password = password;
    this.balance = balance;
    this.id = username;
  }

  public User() {
  }
}
