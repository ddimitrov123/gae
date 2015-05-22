package com.clouway.bank;

import com.clouway.bank.filter.SecurityFilter;
import com.clouway.bank.http.LogOutPage;
import com.clouway.bank.http.LoginPage;
import com.clouway.bank.http.RegistrationPage;
import com.clouway.bank.http.WelcomePage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class AppConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule(){
      @Override
      protected void configureServlets() {
        filter("/welcome").through(SecurityFilter.class);
      }
    }, new SitebricksModule(){
      @Override
      protected void configureSitebricks() {
        at("/registration").show(RegistrationPage.class);
        at("/login").show(LoginPage.class);
        at("/welcome").show(WelcomePage.class);
        at("/logout").show(LogOutPage.class);
      }
    }, new Module());
  }
}
