
package org.exoplatform.services.rest.bonita;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;

/**
 * Created with IntelliJ IDEA.
 * User: Romain Denarie
 * Date: 04/10/13
 * Time: 09:40
 * To change this template use File | Settings | File Templates.
 */
public class ExoProcessDeploymentInfo {
	private String username;
	private String host;

	private int port;

	//This class stores a ProcessDeploymentInfo object
	//and extract all information which are Long to transform it to String
	//because this object will be sent by JSON and read by  javascript
	//Javascript doesnt read correctly Long, it round it.

	private String processId;

	private ProcessDeploymentInfo processDeploymentInfo;

	private String deployedBy;

	public ProcessDeploymentInfo getProcessDeploymentInfo() {
		return processDeploymentInfo;
	}

	public void setProcessDeploymentInfo(ProcessDeploymentInfo processDeploymentInfo) {
		this.processDeploymentInfo = processDeploymentInfo;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getDeployedBy() {
		return deployedBy;
	}

	public void setDeployedBy(String deployedBy) {
		this.deployedBy = deployedBy;
	}


	public ExoProcessDeploymentInfo(ProcessDeploymentInfo pdi, String host, int port, String username) {
		this.processId=String.valueOf(pdi.getProcessId());
		this.deployedBy=String.valueOf(pdi.getDeployedBy());
		this.username=username;
		this.processDeploymentInfo=pdi;
		this.host = host;
		this.port = port;

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
