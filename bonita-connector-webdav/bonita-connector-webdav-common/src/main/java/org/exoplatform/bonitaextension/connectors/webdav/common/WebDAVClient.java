/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.bonitaextension.connectors.webdav.common;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jordi Anguela
 *
 */
public class WebDAVClient {
	private static Logger LOGGER = Logger.getLogger(WebDAVClient.class.getName());
	private HttpClient client = null;

	/**
	 * Create a new webdav Client with a user name and a password to connect to eXo server
	 *
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public WebDAVClient(String host, Long port, String username, String password) {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("WebDAVConnector {host=" + host + ", port=" + port + ", username=" + username + ", password=******}");
		}

		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig.setHost(host, port.intValue());

		HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		int maxHostConnections = 20;
		params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
		connectionManager.setParams(params);

		client = new HttpClient(connectionManager);
		Credentials creds = new UsernamePasswordCredentials(username, password);
		client.getState().setCredentials(AuthScope.ANY, creds);
		client.setHostConfiguration(hostConfig);


	}

	public WebDAVResponse listFolder(String uri) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("listFolder '" + uri +"'");
		}

		GetMethod httpMethod = new GetMethod(uri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}

	public WebDAVResponse createFolder(String parentUri, String newFoldersName) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("createFolder '" + newFoldersName +"' into '" + parentUri +"'");
		}

		MkColMethod httpMethod = new MkColMethod(parentUri + newFoldersName);

		client.executeMethod(httpMethod);
		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}

	/* NOT TESTED
	public WebDAVResponse downloadFile(String fileUri, String outputFileFolder, String outputFileName) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("downloadFile '" + fileUri +"' and save it to '" + outputFileFolder + outputFileName + "'");
		}

		GetMethod httpMethod = new GetMethod(fileUri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, false);

		// Save output file
		if (httpMethod.getResponseContentLength() > 0) {
			InputStream inputStream = httpMethod.getResponseBodyAsStream();
			File responseFile = new File(outputFileFolder + outputFileName);
			OutputStream outputStream = new FileOutputStream(responseFile);
			byte buf[]=new byte[1024];
			int len;
			while ( (len = inputStream.read(buf)) > 0 ) {
				outputStream.write(buf,0,len);
			}
			outputStream.close();
			inputStream.close();
		}

		httpMethod.releaseConnection();
		return objResponse;
	} */

	public WebDAVResponse uploadFile(String destinationUri, String file, String contentType) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("uploadFile '" + file +"' with mimeType '" + contentType + "' to folder '" + destinationUri + "'");
		}


		FileInputStream fis = new FileInputStream(file);
		return uploadFile(destinationUri, fis, contentType);
	}

	public WebDAVResponse uploadFile(String destinationUri, InputStream fis, String contentType) throws Exception {

		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine("uploadFile '"+"' with mimeType '" + contentType + "' to folder '" + destinationUri + "'");
		}


		//we first must check if folder exists
		// if not, create it.

		//we assume that destinationUri starts with /rest/private/jcr/repository/collaboration/
		String pathToCheck = destinationUri.substring("/rest/private/jcr/repository/collaboration/".length(),destinationUri.lastIndexOf("/"));
		// we dont want file name
		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine("PathToCheck : "+pathToCheck);
		}

		String[] foldersToCheck = pathToCheck.split("/");
		String currentUriTest= "/rest/private/jcr/repository/collaboration";
		for (int i=0; i<foldersToCheck.length;i++) {
			String parentUri = currentUriTest+"/";
			currentUriTest+="/"+foldersToCheck[i];
			WebDAVResponse responseListFolder = listFolder(currentUriTest);

			if (responseListFolder.getStatusCode().equals("404")) {
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine("Need to create folder "+foldersToCheck[i]);
				}
				WebDAVResponse responseCreateFolder = createFolder(parentUri,foldersToCheck[i]);

				if (!responseCreateFolder.getStatusCode().equals("201")) {
					throw new Exception("Unable to create folder "+currentUriTest+" on destination platform");
				}
			}

		}

		PutMethod httpMethod = new PutMethod(destinationUri);


		RequestEntity requestEntity = new InputStreamRequestEntity(fis, contentType);
		httpMethod.setRequestEntity(requestEntity);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}



	/**
	 * To delete either a folder or a file
	 *
	 * @param uri
	 * @throws Exception
	 */
	/* NOT TESTED
	public WebDAVResponse deleteItem(String uri) throws Exception {
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("deleteItem '" + uri +"'");
		}

		DeleteMethod httpMethod = new DeleteMethod(uri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}

	*/

	/* NOT TESTED
	public WebDAVResponse checkout(String uri) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("checkout '" + uri +"'");
		}

		CheckoutMethod httpMethod = new CheckoutMethod(uri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}
    */

	/* NOT TESTED
	public WebDAVResponse checkin(String uri) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("checkin '" + uri +"'");
		}

		CheckinMethod httpMethod = new CheckinMethod(uri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}
	*/

	/* NOT TESTED

	public WebDAVResponse cancelCheckout(String uri) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("cancelCheckout '" + uri +"'");
		}

		UncheckoutMethod httpMethod = new UncheckoutMethod(uri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}
	*/

	/* NOT TESTED
	public WebDAVResponse report(String uri) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("report '" + uri +"'");
		}

		ReportInfo reportInfo = new ReportInfo(ReportType.VERSION_TREE);

		ReportMethod httpMethod = new ReportMethod(uri, reportInfo);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, false);

		MultiStatus multiStatus = httpMethod.getResponseBodyAsMultiStatus();
		MultiStatusResponse responses[] = multiStatus.getResponses();

		String responseAsString = "";
		for (int i = 0; i < responses.length; i++) {
			responseAsString += responses[i].getHref() + "\n";
		}

		objResponse.setResponse(responseAsString);
		httpMethod.releaseConnection();
		return objResponse;
	}
	*/
	/* NOT TESTED
	public WebDAVResponse versionControl(String uri) throws Exception {

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("versionControl '" + uri +"'");
		}

		VersionControlMethod httpMethod = new VersionControlMethod(uri);
		client.executeMethod(httpMethod);

		WebDAVResponse objResponse = processResponse(httpMethod, true);
		httpMethod.releaseConnection();
		return objResponse;
	}
	*/

	private WebDAVResponse processResponse(HttpMethod httpMethod, boolean getResponseAsString) {

		String statusCode = "-1";
		if (httpMethod.getStatusCode() > 0) {
			statusCode = String.valueOf(httpMethod.getStatusCode());
		}
		String statusText = httpMethod.getStatusText();

		String responseString = "";
		if (getResponseAsString) {
			try {
				responseString = httpMethod.getResponseBodyAsString();
				if (responseString == null) {
					responseString = "";
				}
			} catch (IOException e) {
				if (LOGGER.isLoggable(Level.WARNING)) {
					LOGGER.warning("IOException while getting responseAsString: " + e.getMessage());
				}
				e.printStackTrace();
			}
		}

		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("status CODE: " + statusCode + ", status TEXT: " + statusText + "\n");
		}

		WebDAVResponse response = new WebDAVResponse(statusCode, statusText, responseString);
		return response;
	}
}