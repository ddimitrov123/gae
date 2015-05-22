package com.clouway.bank.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class PersistenceUserRepository implements UserRepository {
  @Override
  public void registerUser(String username, String password) {
//    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//    Entity user = new Entity("users", username);
//    user.setProperty("password", password);
//    datastoreService.put(user);

    //with twig
    ObjectDatastore datastore = new AnnotationObjectDatastore();
    datastore.store(new User(username, password, 0.0));
  }

  @Override
  public boolean usernameIsInDB(String username) {
//    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//    Entity user = new Entity("users", username);
//    try {
//      datastoreService.get(user.getKey());
//    } catch (EntityNotFoundException e) {
//      return false;
//    }
//    return true;

    //with twig
    ObjectDatastore datastore = new AnnotationObjectDatastore();
//    return datastore.find().type(User.class).addFilter("username", FilterOperator.EQUAL, username).countResultsNow() != 0;
    return datastore.load(User.class, username) != null;
  }

  @Override
  public boolean findUser(String username, String password) {
//    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//    Entity user = new Entity("users", username);
//    try {
//      user = datastoreService.get(user.getKey());
//    } catch (EntityNotFoundException e) {
//      return false;
//    }
//    return user.getProperty("password").toString().equals(password);

    //with twig
    ObjectDatastore datastore = new AnnotationObjectDatastore();
//    return datastore.find().type(User.class).addFilter("password", FilterOperator.EQUAL, password).countResultsNow() != 0 && usernameIsInDB(username);
    return usernameIsInDB(username) && datastore.load(User.class, username).password.equals(password);
  }

  @Override
  public List<User> findAll() {
//    List<User> users = new ArrayList<>();
//    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//    Query allUsersQuery = new Query("users");
//    PreparedQuery allUsers = datastoreService.prepare(allUsersQuery);
//    Iterator<Entity> allUsersIter = allUsers.asIterator();
//    while (allUsersIter.hasNext()) {
//      Entity user = allUsersIter.next();
//      users.add(new User(user.getProperty("username").toString(), user.getProperty("password").toString()));
//    }
//    return users;

    //with twig
    List<User> users = new ArrayList<>();
    ObjectDatastore datastore = new AnnotationObjectDatastore();
    Iterator<User> user = datastore.find(User.class);
    while (user.hasNext()){
      users.add(user.next());
    }
    return users;
  }
}
