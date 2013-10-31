package org.exoplatform.services.rest.bonita;

import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;

/**
 * Created with IntelliJ IDEA.
 * User: Romain Denarie
 * Date: 04/10/13
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class ExoHumanTaskInstance {
	private String username;


	//This class stores a HumanTaskInstance object
	//and extract all information which are Long to transform it to String
	//because this object will be sent by JSON and read by  javascript
	//Javascript doesnt read correctly Long, it round it.


	private String host;
	private int port;


	private String taskId;

	private String assigneeId;
	private String executedBy;
	private String processDefinitionId;
	private String actorId;
	private String processName;
	private HumanTaskInstance humanTaskInstance;


	public String getAssigneeId() {
		return assigneeId;
	}

	public String getExecutedBy() {
		return executedBy;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public HumanTaskInstance getHumanTaskInstance() {
		return humanTaskInstance;
	}

	public void setHumanTaskInstance(HumanTaskInstance humanTaskInstance) {
		this.humanTaskInstance = humanTaskInstance;
	}



	public ExoHumanTaskInstance(HumanTaskInstance humanTask, String host, int port, String username, ProcessDefinition processDefinition) {
		this.humanTaskInstance = humanTask;
		this.username=username;
		this.actorId = String.valueOf(humanTask.getActorId());
		this.assigneeId = String.valueOf(humanTask.getAssigneeId());
		this.executedBy = String.valueOf(humanTask.getExecutedBy());
		this.processDefinitionId = String.valueOf(humanTask.getProcessDefinitionId());
		this.taskId = String.valueOf(humanTask.getId());

		if (processDefinition != null) {
			this.processName=processDefinition.getName()+"--"+processDefinition.getVersion();
		} else {
			this.processName = "";
		}

		this.host=host;
		this.port=port;

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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}
}
