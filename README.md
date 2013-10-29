Bonita Extension
=================

Add some tools in eXo to interact with an instance of Bonita. Bonita server and eXo server are 2 different instances.

Prerequisites
================

This extension is actually tested on eXoPlatform 4.0.2. It should work will next 4.0.x version without modifications.
Bonita version used is 6.0.3. Some tests will be needed to use it on next Bonita version

Automatic build
=====================
Get Bonita Community : 
----------------------
http://www.bonitasoft.com/products/download-bpm-software-and-documentation : download the version bundled with tomcat.
I used 6.0.3. Unzip it on your disk. This will be the dependency source folder for bonita : $BONITA_DEPENDENCY_FOLDER. 

Get eXoPlatform : 
----------------- 
you can find it on community platform : http://community.exoplatform.com/portal/intranet/
Unzip it on your disk. This will be the dependency source folder : $EXO_DEPENDENCY_FOLDER

Build :
---------
In packaging/pom.xml, set properties exo.folder.name and bonita.folder.name with value above :
	
	<properties>
		<exo.folder.name>/home/romain/exo/exo-dependencies/platform-4.0.2-CP01</exo.folder.name>
		<bonita.folder.name>/home/romain/exo/exo-dependencies/BonitaBPMCommunity-6.0.3-Tomcat-6.0.35</bonita.folder.name>
	</properties>


Go at root folder of the extension, and type

	mvn clean install
	
When finished, go in packaging/target/, you have 2 zip : bonita-server.zip and eXo-Platform-bonita-extension.zip. These are the 2 servers used and preconfigured.

Start Bonita :
----------
Unzip bonita-server.zip in $BONITA_HOME
Launch $BONITA_HOME/bin/catalina.sh run
When server is started, go on http://localhost:9090/bonita

We first have to create the system user. System user is the user which eXo will use to get process and tasks. He is defined in $EXO_HOME/gatein/conf/configuration.properties :
	
	org.exoplatform.bonita.systemuser=john
	org.exoplatform.bonita.systempassword=!p@ssw0rd!

For first connection on Bonita, use technical user install/install  
Then create a new user : john with password : !p@ssw0rd!  
Go in tab "Configuration" -> "User Rights", and add John in profiles "Administrators" and "Users".  
Logout  
Login with john.  
We will define default group and membership for Bonita.   
With bonita, it is possible to synchronize these information from an existing LDAP.  

Switch to "Administrator" view. Add a new group, named "consulting". Add a new role named "member". This correspond to properties 

	org.exoplatform.bonita.default.group=consulting
	org.exoplatform.bonita.default.role=member
	
in $EXO_HOME/gatein/conf/configuration.properties

Go in "Configuration" -> "User rights", and select profile User. Edit it by adding a "Group Mapping" on group "consulting". With this, all users of consulting group will have profile "user" in bonita.

Go to "Apps Management" -> "Apps", and Install a new Apps. Select bar file in bonita-sample folder. Assign it ot group "consulting" -> all users in consulting group will be able to start this workflow.
Enable the workflow.

Start eXo :
---------
Unzip  eXo-Platform-bonita-extension.zip in $EXO_HOME
Launch $EXO_HOME/start_eXo.sh
When server is started, go on http://localhost:8080/portal
Create the first user with informations you want. Just set "password" as password for root (this is used in the sample workflow).

On Homepage, you can see the ProcessList Gadget. Click on the link, fill the form, attach a file, and save. Go in File explorer, drive "Collaboration". A new fodler "Documents" was created with subfolder in function of what you enter in the form.



Manual build
=====================

Get Bonita Community : 
----------------------
http://www.bonitasoft.com/products/download-bpm-software-and-documentation : download the version bundled with tomcat.
I used 6.0.3. Unzip it on your disk. Bonita working dir will be named BONITA_HOME.
Configure Bonita to use another port than 8080 (in $BONITA_HOME/conf/server.xml)

Get eXoPlatform : 
----------------- 
you can find it on community platform : http://community.exoplatform.com/portal/intranet/
Unzip it on your disk. eXo working dir will be named EXO_HOME

Copy :
------
- $BONITA_HOME/bonita/client dans $EXO_HOME/bonita
- $BONITA_HOME/webapps/bonita/WEB-INF/lib/bonita-client-6.0.x.jar to $EXO_HOME/lib/
- $BONITA_HOME/webapps/bonita/WEB-INF/lib/bonita-common-6.0.x.jar to $EXO_HOME/lib/
- $BONITA_HOME/webapps/bonita/WEB-INF/lib/httpmime-4.2.5.jar to $EXO_HOME/lib/

Replace :
--------
- $EXO_HOME/lib/httpcore-4.1.2.jar by $BONITA_HOME/webapps/bonita/WEB-INF/lib/httpcore-4.2.4.jar
- $EXO_HOME/lib/httpclient-4.1.2.jar by $BONITA_HOME/webapps/bonita/WEB-INF/lib/httpclient-4.2.5.jar

Configuration :  
-----------------
Edit file $EXO_HOME/bonita/client/conf/bonita-client.properties. Comment this :

    org.bonitasoft.engine.api-type = LOCAL  

and uncomment that :

	org.bonitasoft.engine.api-type = HTTP  
	server.url = http://localhost:8080 #change localhost and port to your bonita server name and port  
	application.name = bonita  
	  
This configuration will said to bonita-client (layer which access to bonita server) where is the bonita server


Edit file $EXO_HOME/gatein/conf/configuration.properties, and add theses properties :

		org.exoplatform.bonita.systemuser=john
		org.exoplatform.bonita.systempassword=!p@ssw0rd!
		org.exoplatform.bonita.port=8080
		org.exoplatform.bonita.host=localhost #change localhost and port to your bonita server name and port (must be same as below)
		
This indicate to eXo how to contact bonita server, and with which service account. john must exists in Bonita Organization. If you create another user for communication purpose, with different password, change it in this configuration file.

		org.exoplatform.bonita.default.password=!p@ssw0rd!
		org.exoplatform.bonita.default.group=Consulting
		org.exoplatform.bonita.default.role=member
		
This indicate how to manage a exo user which not exists on Bonita. At first call with this user, he will be created in Bonita, with default.password password. In addition, he will be put in default.group, with default.role.
	
CAREFUL : default.group, default.role and systemuser MUST exists in Bonita, you have to create it manually in bonita console. At first start, default account to create first user is install/install. Create your system user with this account.

Rename 

	$TOMCAT_HOME/bin/setenv-customize.sample.sh
	
to 

	$TOMCAT_HOME/bin/setenv-customize.sh
	
and add this in this file :

	BONITA_HOME="-Dbonita.home=${CATALINA_HOME}/bonita"
	CATALINA_OPTS="$CATALINA_OPTS ${BONITA_HOME}"
	
It's indicated to bonita-client in eXoServer where he can find his properties file.


Edit $BONITA_HOME/webapps/bonita/WEB-INF/web.xml, and add filter declaration :

		<filter>
			<filter-name>GenerateSessionFilter</filter-name>
			<filter-class>org.exoplatform.bonita.filter.GenerateSessionFilter</filter-class>
		</filter>
		
and filter mapping :
	
		<filter-mapping>
			<filter-name>GenerateSessionFilter</filter-name>
			<url-pattern>/getForm</url-pattern>
		</filter-mapping>

Compile the extension
---------------------

Type 

	mvn clean install
	

- copy webapps/target/bonita-extension.war to $EXO_HOME/webapps/
- copy config/target/bonita-extension-config-1.0.0-SNAPSHOT.jar to $EXO_HOME/lib/
- copy bonita-services/target/bonita-extension-services-1.0.0-SNAPSHOT.jar to $EXO_HOME/lib/
- copy bonita-filter/target/bonita-extension-filter-1.0.0-SNAPSHOT.jar to $BONITA_HOME/webapps/bonita/WEB-INF/lib/
		
Process example :
------------------
In extension, we have a process exemple, named CRAUpload. This process will allow eXo consultants to upload CRA on an eXo instance using webdav. This process is present on github as bos extension. You have to upload it bonita studio, then configure connecteurs to allow them to upload documents.
Default parameters are :
- baseDestinationUri : by default it is /Users/r___/ro___/roo___/root/Public
- host : exo server host : by default : 192.168.0.7
- port : exo server port : by default : 8080
- systemUser : the user which will really do the webdav upload : by default : root
- password : the password for this user : by default : gtn
	
Then, the document uploaded in form will be stored by systemUser in /baseDestinationUri/{customerName}/{SalesOrder}/year-{W-S}weekOrMonthNumber-customer-project-username-{CRA-AS}.docExtension
The username in the file name is the name of the actor of the workflow, not the name set in systemUser.	
	
If more than one Sales Order, the document is duplicated.	
	
Theses default parameter are to be improved, to have correct default settings
	
If you don't change this, you can install the bar file directly in your bonita instance. Else, modify connectors params, and export as bar file. Then upload it in bonita console. To modify default value, open the bos file in the Studio, click on the process, and choose tab "Datas". then you can modify default values.
		
Once the bar file is imported, don't forget to assign users who can launch it. This configuration can be done in bonita console. You can set that all member of group setted in parameter as defaultGroup can launch the process. This need to be improved to have the defaultGroup alreday selected when upload.
	
	
About users, in bonita 6, contrary to Bonita 5, the bonita server needs to have users in his own user base. So, Bonita provide some synchronizations tools from LDAP (with entreprise licence). In this extension, user is created in bonita if not exist, with default password and with eXo username. Then, if synchronization tool is used, this creation mechanism doesnt have to be remove : user already exists => do nothing. 
	
	
Launch : 
------
Launch Bonita Server, eXo server, and connect on eXo server. On main page of intranet portal, you have 2 new gadgets : ProcessList and TaskList. Theses gadgets ask information about process and task to Bonita Server and diplay links to do actions. Actually link send to page workflow which contain iframe displaying  the form (some visual corrections are needed). It can be modified in the gadget to have a target _blank link opening a new tab with only the form.


TODO : 
------
- Add connector for activity stream 
- Improve default params utilisation



Note about build 
-----------------
Actually, connectors building are deactivated from global mvn clean install. The reason is because there is a dependencies on 
	
	<groupId>org.ow2.bonita.connectors</groupId>
        <artifactId>bonita-connectors-assembly</artifactId>
        <version>${bonita.version}</version>
        
which is not on maven. To have this, you have to build this bonita project :
https://github.com/bonitasoft/bonita-connectors-assembly/tree/bonita-connectors-assembly-6.0.x
in the version corresponding to your.

You have not to build connectors to tests this extension, they are integrated in workflow example.
