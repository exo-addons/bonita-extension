# Bonita Extension

Add some tools in eXo to interact with an instance of Bonita. Bonita server and eXo server are 2 different instances.

# Prerequisites

This extension is actually tested on eXoPlatform 4.2.0-RC1. It should work will next 4.2.x version without modifications.
Bonita version used is 6.5.2. Some tests will be needed to use it on next Bonita version

# Build
You can manually build eXo and bonita packages. For this, follow [Manual Build Instructions](https://github.com/exo-addons/bonita-extension/wiki/Manual Build)

## Get Bonita Community : 
http://www.bonitasoft.com/products/download-bpm-software-and-documentation : download the version bundled with tomcat.
I used 6.2.1 Unzip it on your disk. This will be the dependency source folder for bonita : $BONITA_DEPENDENCY_FOLDER. 

## Get eXoPlatform : 
you can find it on community platform : http://community.exoplatform.com/portal/intranet/
Unzip it on your disk. This will be the dependency source folder : $EXO_DEPENDENCY_FOLDER

## Build :
In packaging/pom.xml, set properties exo.folder.name and bonita.folder.name with value above :
	
	<properties>
		<exo.folder.name>/home/romain/exo/exo-dependencies/platform-4.0.4</exo.folder.name>
		<bonita.folder.name>/home/romain/exo/exo-dependencies/BonitaBPMCommunity-6.2.1-Tomcat-6.0.37</bonita.folder.name>
	</properties>


Go at root folder of the extension, and type

	mvn clean install
	
When finished, go in packaging/target/, you have 3 zip : 
- bonita-server.zip (preconfigured bonita server)
- eXo-Platform-bonita-extension.zip (preconfigured eXo server, with extension already deployed)
- bonita-extension.zip (extension, to deploy in a fresh eXoPlatform bundle)


# Install


## Install Bonita
Unzip bonita-server.zip in $BONITA_HOME
Launch $BONITA_HOME/bin/catalina.sh run
When server is started, go on http://localhost:9090/bonita

We first have to create the system user. System user is the user which eXo will use to get process and tasks. He is defined in $EXO_HOME/gatein/conf/configuration.properties :
	
	org.exoplatform.bonita.systemuser=john
	org.exoplatform.bonita.systempassword=!p@ssw0rd!

For first connection on Bonita, use technical user install/install  
Then create a new user : john with password : !p@ssw0rd!  
Go in tab "Organization" -> "Profiles", and add John in profiles "Administrators" and "Users".
Logout  
Login with john.  
We will define default group and membership for Bonita.   
With bonita, it is possible to synchronize these information from an existing LDAP.  

Switch to "Administrator" view. Add a new group, named "consulting". Add a new role named "member". This correspond to properties 

	org.exoplatform.bonita.default.group=consulting
	org.exoplatform.bonita.default.role=member
	
in $EXO_HOME/gatein/conf/configuration.properties

Go in "Organization" -> "Profiles", and select profile User. Edit it by adding a "Group Mapping" on group "consulting". With this, all users of consulting group will have profile "user" in bonita.

Go to "Process Management" -> "Process", and Install a new Apps. Select bar file in bonita-sample folder. Assign it ot group "consulting" -> all users in consulting group will be able to start this workflow.
Enable the workflow.

## Install eXo :
Here, you can choose to deploy bonita extension in a fresh eXo server, or directly use the builded one.

### Deploy extension :
Copy bonita-extension.zip in $EXO_HOME/extensions
Then, in $EXO_HOME, type command 

	extensions.sh -i bonita
	
Result is :

	 # ===============================
	 # eXo Platform Extensions Manager
	 # ===============================

	Installing bonita extension ...
	     [copy] Copying 7 files to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/bonita-client-6.2.1.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/bonita-client-6.2.1.jar
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/bonita-common-6.2.1.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/bonita-common-6.2.1.jar
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/bonita-extension-config-2.0.0-SNAPSHOT.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/bonita-extension-config-2.0.0-SNAPSHOT.jar
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/bonita-extension-services-2.0.0-SNAPSHOT.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/bonita-extension-services-2.0.0-SNAPSHOT.jar
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/httpclient-4.2.5.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/httpclient-4.2.5.jar
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/httpcore-4.2.4.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/httpcore-4.2.4.jar
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/lib/httpmime-4.2.5.jar to /home/romain/exo/exo-working/eXoPlatform-4.0.4/lib/httpmime-4.2.5.jar
	     [copy] Copying 1 file to /home/romain/exo/exo-working/eXoPlatform-4.0.4/webapps
	     [copy] Copying /home/romain/exo/exo-working/eXoPlatform-4.0.4/extensions/bonita/webapps/bonita-extension.war to /home/romain/exo/exo-working/eXoPlatform-4.0.4/webapps/bonita-extension.war
	Done.

	 # ===============================
	 # Extension bonita installed.
	 # ===============================

Then, type command 

	rm lib/httpclient-4.1.2.jar lib/httpcore-4.1.2.jar
	cp extensions/bonita/bonita .
	cp extensions/bonita/bin/* bin/
	
Then edit file $EXO_HOME/gatein/conf/configuration.properties and add this at the end :
	
	#Bonita
	org.exoplatform.bonita.systemuser=john
	org.exoplatform.bonita.systempassword=!p@ssw0rd!
	org.exoplatform.bonita.port=9090
	org.exoplatform.bonita.host=localhost

	org.exoplatform.bonita.default.password=!p@ssw0rd!
	org.exoplatform.bonita.default.group=consulting
	org.exoplatform.bonita.default.role=member
	

### Builded version : 	
If you prefer to take the version builded :
Unzip  eXo-Platform-bonita-extension.zip in $EXO_HOME



# Start
Launch $EXO_HOME/start_eXo.sh
When server is started, go on http://localhost:8080/portal

Create the first user with informations you want. We will name him James. Just set "password" as password for root (this is used in the sample workflow).

# Sample Workflow Usage

## CraUpload 1.0
On Homepage, you can see the ProcessList Gadget. Click on the link, fill the form, attach a file, and save. Go in File explorer, drive "Collaboration". A new fodler "Documents" was created with subfolder in function of what you enter in the form.

## CraUpload 2.0
To use V2 of the workflow, you will need a second actor. In eXo, create a second user, for example Mary, and log with this account. 
Then, with John, in Bonita, edit James, the first user, to set his manager. Set Mary for this.
In eXo, with James, create a new space names "Consulting Space". Then in ProcessList Gadget, click on the process link. Fill the form, attach a file, and save. Then, Mary have a new task to do : validation. Click on the link, validate the document. The document is uploaded in Consulting Space documents folder. With John, you can see an activity in the stream of this space.


# Remarks 
## Note about Process example :
In extension, we have a process exemple, named CRAUpload. This process will allow eXo consultants to upload CRA on an eXo instance using webdav. This process is present on github as bos extension. You have to upload it bonita studio, then configure connecteurs to allow them to upload documents.
Default parameters are :
- baseDestinationUri : by default it is /Documents in V1, /Groups/spaces/consulting_space/Documents in V2
- host : exo server host : by default : localhost
- port : exo server port : by default : 8080
- systemUser : the user which will really do the webdav upload : by default : root
- password : the password for this user : by default : password
	
Then, the document uploaded in form will be stored by systemUser in /baseDestinationUri/{customerName}/{SalesOrder}/year-{W-S}weekOrMonthNumber-customer-project-username-{CRA-AS}.docExtension
The username in the file name is the name of the actor of the workflow, not the name set in systemUser.	
	
If more than one Sales Order, the document is duplicated.	
	
Theses default parameter are to be improved, to have correct default settings
	
If you don't change this, you can install the bar file directly in your bonita instance. Else, modify connectors params, and export as bar file. Then upload it in bonita console. To modify default value, open the bos file in the Studio, click on the process, and choose tab "Datas". then you can modify default values.
		
Once the bar file is imported, don't forget to assign users who can launch it. This configuration can be done in bonita console. You can set that all member of group setted in parameter as defaultGroup can launch the process. This need to be improved to have the defaultGroup alreday selected when upload.
	
	
About users, in bonita 6, contrary to Bonita 5, the bonita server needs to have users in his own user base. So, Bonita provide some synchronizations tools from LDAP (with entreprise licence). In this extension, user is created in bonita if not exist, with default password and with eXo username. Then, if synchronization tool is used, this creation mechanism doesnt have to be remove : user already exists => do nothing. 
	

## Note about build 
Actually, connectors building are deactivated from global mvn clean install. The reason is because there is a dependencies on 
	
	<groupId>org.ow2.bonita.connectors</groupId>
        <artifactId>bonita-connectors-assembly</artifactId>
        <version>${bonita.version}</version>
        
which is not on maven. To have this, you have to build this bonita project :
https://github.com/bonitasoft/bonita-connectors-assembly/tree/bonita-connectors-assembly-6.0.x
in the version corresponding to your.

You have not to build connectors to tests this extension, they are integrated in workflow example.



## TODO : 
- Add connector for activity stream 
- Improve default params utilisation


## Troubleshooting
- If you fail to start the process from the gadget *Process List*, and you get the message "Access denied: you do not have the rights to view this page", please logout user from the Bonita Console and retry again.