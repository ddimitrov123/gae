package com.clouway.bank;

import com.clouway.bank.db.PersistenceSessionRepository;
import com.clouway.bank.db.PersistenceUserRepository;
import com.clouway.bank.db.SessionRepository;
import com.clouway.bank.db.UserRepository;
import com.clouway.bank.validator.RegExUserValidator;
import com.clouway.bank.validator.UserValidator;
import com.google.inject.AbstractModule;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class Module extends AbstractModule {
  @Override
  protected void configure() {
    bind(UserRepository.class).to(PersistenceUserRepository.class);
    bind(SessionRepository.class).to(PersistenceSessionRepository.class);
    bind(UserValidator.class).to(RegExUserValidator.class);
  }
}
