package org.exoplatform.extension.portlet.bonita.task;

import juzu.*;
import juzu.impl.request.Request;
import juzu.template.Template;
import org.exoplatform.addons.bonita.service.BonitaService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 08/09/15.
 */
public class BonitaTaskApplication {
  @Inject
  @Path("task.gtmpl")
  Template                 index;

  @Inject
  @Path("taskWithService.gtmpl")
  Template                 indexNotSSO;

  @Inject
  @Path("configuration.gtmpl")
  Template                 configuration;

  private BonitaService    bonitaService;

  private static final Log LOG = ExoLogger.getLogger(BonitaTaskApplication.class);

  @Inject
  public void BonitaTaskApplication(BonitaService bonitaService) {
    this.bonitaService = bonitaService;
  }

  @View
  public Response index(String message) {

    if (message == null) {
      message = "";
    }
    String username = Request.getCurrent().getSecurityContext().getUserPrincipal().getName();
    if (bonitaService.isUseSSO()) {
      return index.with().set("bonitaUrl", bonitaService.getBonitaServerUrl()).ok();
    } else {
      if (!bonitaService.isConfigured(username)) {
        String bonitaUrl = System.getProperty(bonitaService.USER_BONITA_SERVER_URL_ATTRIBUTE);
        if (bonitaUrl == null)
          bonitaUrl = "";
        return configuration.with().set("message", message).set("bonitaUrl", bonitaUrl).ok();
      } else {
        return indexNotSSO.with()
                          .set("bonitaUrl",
                               bonitaService.getUserAttributeFromProfile(bonitaService.USER_BONITA_SERVER_URL_ATTRIBUTE,
                                                                         username))
                          .ok();
      }
    }
  }

  @Action
  public Response.View saveSettings(String bonitaServerName, String bonitaUsername, String bonitaPassword) {

    // 1) test login against bonita server

    String username = Request.getCurrent().getSecurityContext().getUserPrincipal().getName();

    String url = bonitaServerName.endsWith("/") ? bonitaServerName : bonitaServerName + "/";
    url += "bonita/loginservice?username=" + bonitaUsername + "&password=" + bonitaPassword + "&redirect=false";

    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setRequestMethod("POST");
      int responseCode = connection.getResponseCode();
      if (responseCode == 200 || responseCode == 201) {
        bonitaService.setConfigured(username, bonitaServerName, bonitaUsername, bonitaPassword);
        return BonitaTaskApplication_.index("Connexion OK");

      }
    } catch (MalformedURLException e) {
      LOG.error(url + "is malFormed");
      e.printStackTrace();
    } catch (IOException e) {
      LOG.error("IOException on " + url);
      e.printStackTrace();
    }

    // if nok : display index with message
    // if ok : store information and display index

    return BonitaTaskApplication_.index("Connexion Error");

  }

}
