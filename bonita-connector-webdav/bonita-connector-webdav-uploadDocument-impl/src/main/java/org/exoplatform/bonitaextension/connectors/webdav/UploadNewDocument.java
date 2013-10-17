/**
 * Copyright (C) 2013 BonitaSoft S.A.
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

package org.exoplatform.bonitaextension.connectors.webdav;

import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.exoplatform.bonitaextension.connectors.webdav.common.WebDAVClient;
import org.exoplatform.bonitaextension.connectors.webdav.common.WebDAVResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UploadNewDocument extends AbstractUploadNewDocument {
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String HOST = "host";
	public static final String PORT = "port";


	public static final String DESTINATION_URI ="destinationURI";
	public static final String FILESTREAM = "fileStream";
	public static final String CONTENTTYPE="contentType";

	public static final String RESPONSE_DOCUMENT = "responseDocument";
	public static final String STATUS_CODE = "statusCode";
	public static final String STATUS_TEXT = "statusText";

	private String host;
	private Long port;
	private String username;
	private String password;

	private String destinationUri;
	private InputStream fileStream;
	private String contentType;


	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public UploadNewDocument() {

	}

	@Override
	public void setInputParameters(Map<String, Object> parameters) {
		this.host = (String) parameters.get(HOST);
		LOGGER.info(HOST + " " + host);
		this.port = (Long) parameters.get(PORT);
		LOGGER.info(PORT + " " + port);
		this.username = (String) parameters.get(USERNAME);
		LOGGER.info(USERNAME + " " + username);
		this.password = (String) parameters.get(PASSWORD);
		LOGGER.info(PASSWORD + " **** (don't dream ...)");

		this.destinationUri = (String) parameters.get(DESTINATION_URI);
		LOGGER.info(DESTINATION_URI + " " + destinationUri);
		this.fileStream = (InputStream) parameters.get(FILESTREAM);
		LOGGER.info(FILESTREAM + " " + this.fileStream);
		this.contentType = (String) parameters.get(CONTENTTYPE);
		LOGGER.info(contentType + " " + contentType);
	}

	@Override
	protected void executeBusinessLogic() throws ConnectorException {

		WebDAVClient webDAVClient = new WebDAVClient(this.host,this.port,this.username,this.password);
		WebDAVResponse response = null;
		try {
			response = webDAVClient.uploadFile(this.destinationUri, this.fileStream, this.contentType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response!=null) {
			setOutputParameter(RESPONSE_DOCUMENT, response.getResponse());
			setOutputParameter(STATUS_CODE, response.getStatusCode());
			setOutputParameter(STATUS_TEXT, response.getStatusText());
		} else {
			setOutputParameter(RESPONSE_DOCUMENT, null);
			setOutputParameter(STATUS_CODE, "404");
			setOutputParameter(STATUS_TEXT, "No response founded");
		}



	}


	@Override
	public void validateInputParameters() throws ConnectorValidationException {

		final List<String> errors = new ArrayList<String>();
		if (this.port != null) {
			if (this.port < 0) {
				errors.add("proxyPort cannot be less than 0!");
			} else if (this.port > 65535) {
				errors.add("proxyPort cannot be greater than 65535!");
			}
		}

		if (!this.destinationUri.startsWith("/rest/private/jcr/repository")) {
			errors.add("destinationUri must start by /rest/private/jcr/repository");
		}


		if (!errors.isEmpty()) {
			throw new ConnectorValidationException(this, errors);
		}
	}
}
