package org.bonitasoft.integration;

import java.util.logging.Logger;
import org.ow2.bonita.facade.exception.UserNotFoundException;
import org.ow2.bonita.services.AuthenticationService;

public class ExoDemoAuthenticationImpl
  implements AuthenticationService
{
  private static final Logger LOG = Logger.getLogger(ExoDemoAuthenticationImpl.class.getName());

  public ExoDemoAuthenticationImpl(String persistenceServiceName)
  {
  }

  public ExoDemoAuthenticationImpl()
  {
  }

  public boolean checkUserCredentials(String arg0, String arg1)
  {
    return true;
  }

  public boolean checkUserCredentialsWithPasswordHash(String arg0, String arg1)
  {
    return true;
  }

  public boolean isUserAdmin(String username) throws UserNotFoundException
  {
    return (username.equals("root")) || (username.equals("admin"));
  }
}