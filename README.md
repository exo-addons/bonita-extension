1) Get Bonita Community : http://www.bonitasoft.com/products/download-bpm-software-and-documentation
J'ai utilisé la 6.0.3, mais ca devrait fonctionner avec la 6.0.4 => le placer dans $BONITA_HOME

2) Get eXoPlatform : jai utilisé la 4.0.2-CP01 => le placer dans $EXO_HOMEa

3) copy :
	$BONITA_HOME/bonita/client dans $EXO_HOME/bonita
	$BONITA_HOME/webapps/bonita/WEB-INF/lib/bonita-client-6.0.x.jar to $EXO_HOME/lib/
	$BONITA_HOME/webapps/bonita/WEB-INF/lib/bonita-common-6.0.x.jar to $EXO_HOME/lib/
	$BONITA_HOME/webapps/bonita/WEB-INF/lib/httpmime-4.2.5.jar to $EXO_HOME/lib/

4) Replace :
	$EXO_HOME/lib/httpcore-4.1.2.jar by $BONITA_HOME/webapps/bonita/WEB-INF/lib/httpcore-4.2.4.jar
	$EXO_HOME/lib/httpclient-4.1.2.jar by $BONITA_HOME/webapps/bonita/WEB-INF/lib/httpclient-4.2.5.jar

5) Configuration :
	Edit file $EXO_HOME/bonita/client/conf/bonita-client.properties. Comment this :
        	#org.bonitasoft.engine.api-type = LOCAL
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
	This indicate to eXo how to contact bonita server, and with which service account. john must exists in Bonita Organization

		org.exoplatform.bonita.default.password=!p@ssw0rd!
		org.exoplatform.bonita.default.group=Consulting
		org.exoplatform.bonita.default.role=member
	This indicate how to manage a exo user which not exists on Bonita. At first call with this user, he will be created in Bonita, with default.password password. In addition, he will be put in default.group, with default.role.
	
	CAREFUL : default.group, default.role and systemuser MUST exists in Bonita, you have to create it manually in bonita console. At first start, default account to create first user is install/install. Create your system user with this account.
	
6) Compile the extension, and 
	copy webapps/target/bonita-extension.war to $EXO_HOME/webapps/
	copy config/target/bonita-extension-config-1.0.0-SNAPSHOT.jar to $EXO_HOME/lib/
	copy bonita-services/target/bonita-extension-services-1.0.0-SNAPSHOT.jar to $EXO_HOME/lib/
	copy bonita-filter/target/bonita-extension-filter-1.0.0-SNAPSHOT.jar to $BONITA_HOME/webapps/bonita/WEB-INF/lib/

7) Edit $BONITA_HOME/webapps/bonita/WEB-INF/web.xml, and add filter declaration :
		<filter>
			<filter-name>GenerateSessionFilter</filter-name>
			<filter-class>org.exoplatform.bonita.filter.GenerateSessionFilter</filter-class>
		</filter>
	and filter mapping :
		<filter-mapping>
			<filter-name>GenerateSessionFilter</filter-name>
			<url-pattern>/getForm</url-pattern>
		</filter-mapping>
		
8) Process example :
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
	
	
9) Launch Bonita Server, eXo server, and connect on eXo server. On main page of intranet portal, you have 2 new gadgets : ProcessList and TaskList. Theses gadgets ask information about process and task to Bonita Server and diplay links to do actions. Actually link send to page workflow which contain iframe displaying  the form (some visual corrections are needed). It can be modified in the gadget to have a target _blank link opening a new tab with only the form.


TODO : 
Finalize documentation (add part about connectors)
Add connector for activity stream 
Do packaging
Improve default params utilisation
Css corrections on iframe display
	
