package com.clouway.bank.db;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class Session {
  @Key public String username;
  public String value;
  public long expDate;

  public Session() {
  }

  public Session(String username, String value, long expDate) {
    this.username = username;
    this.value = value;
    this.expDate = expDate;
  }
}
