package com.clouway.bank.db;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistenceSessionRepositoryTest {
  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private PersistenceSessionRepository sessionRepository;

  @Before
  public void setUp() {
    helper.setUp();
    sessionRepository = new PersistenceSessionRepository();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void registerSession() {
    String username = "Ivan";
    String sid = sessionRepository.generateSid(username);
    long time = sessionRepository.generateTime();
    sessionRepository.registerSession(username, sid, time);
    Session session = sessionRepository.getSession(sid);
    assertThat(session.username, is(username));
    assertThat(session.value, is(sid));
    assertThat(session.expDate, is(time));
  }

  @Test
  public void anotherRegisterSession() {
    String username = "Petar";
    String sid = sessionRepository.generateSid(username);
    long time = sessionRepository.generateTime();
    sessionRepository.registerSession(username, sid, time);
    Session session = sessionRepository.getSession(sid);
    assertThat(session.username, is(username));
    assertThat(session.value, is(sid));
    assertThat(session.expDate, is(time));
  }

  @Test(expected = NoSuchElementException.class)
  public void deleteSession() {
    String username = "Stefan";
    String sid = sessionRepository.generateSid(username);
    long time = sessionRepository.generateTime();
    sessionRepository.registerSession(username, sid, time);
    sessionRepository.deleteSession(username);
    sessionRepository.getSession(sid);
  }

  @Test
  public void refreshSession(){
    String username = "Coni";
    String sid = sessionRepository.generateSid(username);
    long time = sessionRepository.generateTime();
    sessionRepository.registerSession(username, sid, time);
    Session session = sessionRepository.getSession(sid);
    assertThat(session.value, is(sid));
    assertThat(session.expDate, is(time));

    long time1 = sessionRepository.generateTime();
    sessionRepository.refreshSession(username, time1);
    Session session1 = sessionRepository.getSession(sid);

    assertThat(session1.value, is(sid));
    assertThat(session1.expDate, is(time1));
  }
}
