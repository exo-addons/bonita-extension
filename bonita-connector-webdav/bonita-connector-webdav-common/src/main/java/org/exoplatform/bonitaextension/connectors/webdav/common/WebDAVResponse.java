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

/**
 * 
 * @author Jordi Anguela
 *
 */
public class WebDAVResponse {

	private String statusCode = "";
	private String statusText = "";
	private String response   = "";
	
	public WebDAVResponse() {
		
	}
	
	public WebDAVResponse(String code, String text, String responseString) {
		statusCode = code;
		statusText = text;
		response   = responseString;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public String toString() {
		String result = "status code : " + statusCode + "\n" +
		                "status text : " + statusText + "\n" +
		                "   response : " + response;
		return result;
	}
}
