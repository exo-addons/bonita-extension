/**
 * 
 */
package org.exoplatform.consulting;

import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.connector.ConnectorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 *The connector execution will follow the steps
 * 1 - setInputParameters() --> the connector receives input parameters values
 * 2 - validateInputParameters() --> the connector can validate input parameters values
 * 3 - connect() --> the connector can establish a connection to a remote server (if necessary)
 * 4 - executeBusinessLogic() --> execute the connector
 * 5 - getOutputParameters() --> output are retrieved from connector
 * 6 - disconnect() --> the connector can close connection to remote server (if any)
 */
public class CraUploadProcessVariablesImpl extends
		AbstractCraUploadProcessVariablesImpl {

	@Override
	protected void executeBusinessLogic() throws ConnectorException{
		//Get access to the connector input parameters
		//getBaseDestinationUri();
		//getSalesOrder();
		//getCustomer();
		//getYear();
		//getWeekOrMonth();
		//getWeekOrMonthNumber();
		//getProjectName();
		//getUserName();
		//getType();
		//getFile();
	
		//TODO execute your business logic here 
		
		
		//WARNING : Set the output of the connector execution. If outputs are not set, connector fails
		setDestinationUri(processDestinationUri(getBaseDestinationUri(),
							getSalesOrder(),
							getCustomer(),
							getYear(),
							getWeekOrMonth(),
							getWeekOrMonthNumber(),
							getProjectName(),
							getUserName(),
							getType(),
							getFile()));
		setMimeType(processMimeType(getFile()));
	
	 }

	@Override
	public void connect() throws ConnectorException{
		//[Optional] Open a connection to remote server
	
	}

	@Override
	public void disconnect() throws ConnectorException{
		//[Optional] Close connection to remote server
	
	}
	
	private String processMimeType (DocumentValue file) {
		try {
			File fileOnDisk = new File(file.getFileName());
			fileOnDisk.createNewFile();
			//	convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream(fileOnDisk);
			fileOuputStream.write(file.getContent());
			fileOuputStream.close();
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String mimeType = fileNameMap.getContentTypeFor(fileOnDisk.getName());
			System.out.println("Mimetype : "+mimeType);
			return mimeType;
		} catch (Exception e) {
			e.printStackTrace();
			return "application/octet-stream";
		}
	}
	
	private String processDestinationUri(String baseDestinationUri, 
											String salesOrder, 
											String customer, 
											Integer year, 
											String weekOrMonth, 
											Integer weekOrMonthNumber,
											String projectName, 
											String username, 
											String type,
											DocumentValue file) {

		String destinationUri = baseDestinationUri;

		if (!destinationUri.startsWith("/rest/private/jcr/repository/collaboration")) {
			destinationUri = "/rest/private/jcr/repository/collaboration"+destinationUri;
		}

		if (!destinationUri.endsWith("/")) {
			destinationUri+="/";
		}

		try {
			destinationUri+= URLEncoder.encode(customer,"UTF-8")+"/";
		} catch (UnsupportedEncodingException e) {
			destinationUri+= customer+"/";
		}
		try {
			destinationUri+=URLEncoder.encode(salesOrder,"UTF-8")+"/";
		} catch (UnsupportedEncodingException e) {
			destinationUri+=salesOrder+"/";
		}


		String fileName = year + "-";
		if (weekOrMonth.equals("Week")) {
			fileName +="W";
		} else {
			fileName +="M";
		}
		fileName+=weekOrMonthNumber+"-";

		String customerEncoded = "";
		try {
			customerEncoded=URLEncoder.encode(customer,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			customerEncoded=customer;
		}

		String projectNameEncoded = "";
		try {
			projectNameEncoded=URLEncoder.encode(projectName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			projectNameEncoded=projectName;
		}

		String usernameEncoded = "";
		try {
			usernameEncoded=URLEncoder.encode(username,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			usernameEncoded=username;
		}


		fileName+=customerEncoded +"-"+projectNameEncoded+"-"+usernameEncoded+"-"+type;


		int lastIndex = file.getFileName().lastIndexOf(".");
		String extension = file.getFileName().substring(lastIndex);
		fileName+=extension;
		destinationUri+=fileName;
		System.out.println("destinationUri : "+destinationUri);
		return destinationUri;
	}
	

}
