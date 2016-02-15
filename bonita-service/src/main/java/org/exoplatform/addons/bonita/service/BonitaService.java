package org.exoplatform.addons.bonita.service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import javax.inject.Inject;


import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.rest.resource.ResourceContainer;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.web.security.codec.AbstractCodec;
import org.exoplatform.web.security.codec.AbstractCodecBuilder;
import org.exoplatform.web.security.security.TokenServiceInitializationException;
import org.gatein.common.io.IOTools;
import org.hibernate.service.spi.Startable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 08/09/15.
 */

@Path("/bonitaService")
@Produces("application/json")
public class BonitaService implements ResourceContainer, Startable {
    private static final Log LOG = ExoLogger.getLogger(BonitaService.class);


    public static final String USER_BONITA_SERVER_URL_ATTRIBUTE = "bonita.server.url";
    public static final String USER_BONITA_USERNAME_ATTRIBUTE = "bonita.username";
    public static final String USER_BONITA_PASSWORD_ATTRIBUTE = "bonita.password";
    public static final String USER_BONITA_USE_SSO = "bonita.usesso";

    private final OrganizationService organizationService;


    private String bonitaServerUrl;



    private boolean useSSO;


    private static AbstractCodec codec;

    @Inject
    public BonitaService(OrganizationService organizationService) {

        this.organizationService = organizationService;
        this.useSSO = (System.getProperty(USER_BONITA_USE_SSO) != null && System.getProperty(USER_BONITA_USE_SSO).equals("true"));
        if (System.getProperty(USER_BONITA_SERVER_URL_ATTRIBUTE)!=null) {
            this.bonitaServerUrl=System.getProperty(USER_BONITA_SERVER_URL_ATTRIBUTE);
            try {
                URL uri = new URL(this.bonitaServerUrl);
                this.bonitaServerUrl = uri.getProtocol()+"://"+uri.getAuthority();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                this.bonitaServerUrl=null;
            }
        }

        if (this.useSSO && this.bonitaServerUrl == null) {
            LOG.error("Bonita service is configured to use sso but bonita.server.url is not set. Fix this problem before using bonita service.");
        }


    }

    @GET
    @Path("getTasks")
    public Response getTasks(@Context UriInfo uriInfo,@Context SecurityContext sc) {

        String userId = getUserId(sc);
        String userBonitaUrl;
        String userBonitaLogin;
        String userBonitaPassword;
        String url="";
        String result="";

        try {
            UserProfile userProfile = organizationService.getUserProfileHandler().findUserProfileByName(userId);
            userBonitaUrl=userProfile.getAttribute(USER_BONITA_SERVER_URL_ATTRIBUTE);
            userBonitaLogin=userProfile.getAttribute(USER_BONITA_USERNAME_ATTRIBUTE);
            userBonitaPassword=decodePassword(userProfile.getAttribute(USER_BONITA_PASSWORD_ATTRIBUTE));

            String cookie = loginBonita(userBonitaUrl,userBonitaLogin,userBonitaPassword);
            String bonitaUserId = getBonitaUserId(userBonitaUrl,cookie);
            url = userBonitaUrl+"/bonita/API/bpm/humanTask?f=assigned_id%3d"+bonitaUserId;


            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200  || responseCode == 201) {
                result= getStringFromInputStream(connection.getInputStream());
            }

            logoutBonita(userBonitaUrl,cookie);

        } catch (MalformedURLException e) {
            LOG.error(url + "is malFormed");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("IOException on "+url);
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error("Unable to get user information");
        }


        if (userId == null) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
        if (result.equals("")) {
            return Response.status(HTTPStatus.NOT_FOUND).build();
        }

        return renderJSON(result);

    }

    @GET
    @Path("getProcess")
    public Response getProcess(@Context UriInfo uriInfo,@Context SecurityContext sc) {

        String userId = getUserId(sc);
        String userBonitaUrl;
        String userBonitaLogin;
        String userBonitaPassword;
        String url="";
        String result="";

        try {
            UserProfile userProfile = organizationService.getUserProfileHandler().findUserProfileByName(userId);
            userBonitaUrl=userProfile.getAttribute(USER_BONITA_SERVER_URL_ATTRIBUTE);
            userBonitaLogin=userProfile.getAttribute(USER_BONITA_USERNAME_ATTRIBUTE);
            userBonitaPassword=decodePassword(userProfile.getAttribute(USER_BONITA_PASSWORD_ATTRIBUTE));

            String cookie = loginBonita(userBonitaUrl,userBonitaLogin,userBonitaPassword);
            String bonitaUserId = getBonitaUserId(userBonitaUrl,cookie);
            url = userBonitaUrl+"/bonita/API/bpm/process?f=activationState%3dENABLED&f=user_id%3d"+bonitaUserId;


            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200  || responseCode == 201) {
                result= getStringFromInputStream(connection.getInputStream());
            }

            logoutBonita(userBonitaUrl,cookie);

        } catch (MalformedURLException e) {
            LOG.error(url + "is malFormed");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("IOException on "+url);
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error("Unable to get user information");
        }


        if (userId == null) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }

        return renderJSON(result);

    }

    private void logoutBonita(String userBonitaUrl,String cookie) {
        String url=userBonitaUrl+"/bonita/logoutservice";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
        } catch (MalformedURLException e) {
            LOG.error(url + "is malFormed");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("IOException on "+url);
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error("unable to get bonita user id");
            e.printStackTrace();
        }

    
    }

    private String getBonitaUserId(String userBonitaUrl, String cookie) {
        String url=userBonitaUrl+"/bonita/API/system/session/unusedid";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cookie", cookie);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200  || responseCode == 201) {
                JSONObject json = new JSONObject(getStringFromInputStream(connection.getInputStream()));
                return (String)json.get("user_id");

            }
        } catch (MalformedURLException e) {
            LOG.error(url + "is malFormed");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("IOException on "+url);
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error("unable to get bonita user id");
            e.printStackTrace();
        }
        return "";

    }

    private String getStringFromInputStream(InputStream input) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

    private String loginBonita(String userBonitaUrl, String userBonitaLogin, String userBonitaPassword) {
        String url=userBonitaUrl+"/bonita/loginservice?username="+userBonitaLogin+"&password="+userBonitaPassword+"&redirect=false";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200  || responseCode == 201) {
                String cookie = connection.getHeaderField("Set-Cookie");
                return cookie;
            }
        } catch (MalformedURLException e) {
            LOG.error(url + "is malFormed");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("IOException on "+url);
            e.printStackTrace();
        }
        return "";

    }


    private String getUserId(SecurityContext sc) {
        try {
            return sc.getUserPrincipal().getName();
        } catch (Exception e) {
            return null;
        }
    }

    private Response renderJSON(Object result) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);

        return Response.ok(result, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
    }

    public boolean isConfigured(String username) {
        if (this.useSSO) return true;
        try {
            UserProfile profile = organizationService.getUserProfileHandler().findUserProfileByName(username);
            if (profile!= null && profile.getAttribute(USER_BONITA_SERVER_URL_ATTRIBUTE)!=null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setConfigured(String username, String serverUrl, String bonitaLogin, String bonitaPassword) {
        try {
            UserProfile userProfile = organizationService.getUserProfileHandler().findUserProfileByName(username);
            if (userProfile==null) {
                userProfile=organizationService.getUserProfileHandler().createUserProfileInstance(username);
                organizationService.getUserProfileHandler().saveUserProfile(userProfile, true);

            }

            userProfile.setAttribute(USER_BONITA_SERVER_URL_ATTRIBUTE, serverUrl);
            userProfile.setAttribute(USER_BONITA_USERNAME_ATTRIBUTE, bonitaLogin);
            userProfile.setAttribute(USER_BONITA_PASSWORD_ATTRIBUTE,encodePassword(bonitaPassword));

            organizationService.getUserProfileHandler().saveUserProfile(userProfile, false);
        } catch (Exception e) {
            LOG.error("Unable to store user information");
        }
    }

    @Override
    public void start() {
        
    }

    private static void initCodec() throws Exception {
        String builderType = PropertyManager.getProperty("gatein.codec.builderclass");
        Map<String, String> config = new HashMap<String, String>();

        if (builderType != null) {
            // If there is config for codec in configuration.properties, we read the
            // config parameters from config file
            // referenced in configuration.properties
            String configFile = PropertyManager.getProperty("gatein.codec.config");
            InputStream in = null;
            try {
                File f = new File(configFile);
                in = new FileInputStream(f);
                Properties properties = new Properties();
                properties.load(in);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    config.put((String) entry.getKey(), (String) entry.getValue());
                }
                config.put("gatein.codec.config.basedir", f.getParentFile().getAbsolutePath());
            } catch (IOException e) {
                throw new TokenServiceInitializationException("Failed to read the config parameters from file '" + configFile + "'.", e);
            } finally {
                IOTools.safeClose(in);
            }
        } else {
            // If there is no config for codec in configuration.properties, we
            // generate key if it does not exist and setup the
            // default config
            builderType = "org.exoplatform.web.security.codec.JCASymmetricCodecBuilder";
            String gtnConfDir = PropertyManager.getProperty("gatein.conf.dir");
            if (gtnConfDir == null || gtnConfDir.length() == 0) {
                throw new TokenServiceInitializationException("'gatein.conf.dir' property must be set.");
            }
            File f = new File(gtnConfDir + "/codec/codeckey.txt");
            if (!f.exists()) {
                File codecDir = f.getParentFile();
                if (!codecDir.exists()) {
                    codecDir.mkdir();
                }
                OutputStream out = null;
                try {
                    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                    keyGen.init(128);
                    SecretKey key = keyGen.generateKey();
                    KeyStore store = KeyStore.getInstance("JCEKS");
                    store.load(null, "gtnStorePass".toCharArray());
                    store.setEntry("gtnKey", new KeyStore.SecretKeyEntry(key), new KeyStore.PasswordProtection("gtnKeyPass".toCharArray()));
                    out = new FileOutputStream(f);
                    store.store(out, "gtnStorePass".toCharArray());
                } catch (Exception e) {
                    throw new TokenServiceInitializationException(e);
                } finally {
                    IOTools.safeClose(out);
                }
            }
            config.put("gatein.codec.jca.symmetric.keyalg", "AES");
            config.put("gatein.codec.jca.symmetric.keystore", "codeckey.txt");
            config.put("gatein.codec.jca.symmetric.storetype", "JCEKS");
            config.put("gatein.codec.jca.symmetric.alias", "gtnKey");
            config.put("gatein.codec.jca.symmetric.keypass", "gtnKeyPass");
            config.put("gatein.codec.jca.symmetric.storepass", "gtnStorePass");
            config.put("gatein.codec.config.basedir", f.getParentFile().getAbsolutePath());
        }

        try {
            codec = Class.forName(builderType).asSubclass(AbstractCodecBuilder.class).newInstance().build(config);
            LOG.info("Initialized CookieTokenService.codec using builder " + builderType);
        } catch (Exception e) {
            throw new TokenServiceInitializationException("Could not initialize CookieTokenService.codec.", e);
        }
    }

    public static String decodePassword(String password) {
        try {
            if (codec == null) {
                initCodec();
            }
            password = codec.decode(password);
        } catch (Exception e) {
            LOG.warn("Error while decoding password, it will be used in plain text", e);
        }
        return password;
    }

    public static String encodePassword(String password) {
        try {
            if (codec == null) {
                initCodec();
            }
            password = codec.encode(password);
        } catch (Exception e) {
            LOG.warn("Error while encoding password, it will be used in plain text", e);
        }
        return password;
    }

    public String getBonitaServerUrl() {
        return bonitaServerUrl;
    }
    public boolean isUseSSO() {
        return useSSO;
    }


    public String getUserAttributeFromProfile(String attribute, String username) {
        try {
            UserProfile profile = organizationService.getUserProfileHandler().findUserProfileByName(username);
            return profile.getAttribute(attribute);
        } catch (Exception e) {
            LOG.error("Unable to get user information");
        }
        return "";
    }
}
