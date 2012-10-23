Bonita Extension for eXo Platform
================

This extension is created to make easier the integration of Bonita within eXo Platform.
We are going to explain the steps of integration in this document.

-------------------
System requirements
-------------------
   
* Recent Git client
* Java Development Kit 1.6
* Recent Maven 3
* eXo Platform server 3.5.5-SNAPSHOT.  {PLF_HOME}: The location of the unzipped eXo server.
* The eXo server will run on port 8080, make sure this port is not currently in use
* Bonita Community-5.7.2 : Download "Bonita Open Solution Deployement" and "Tomcat-6.0.33 includes BOS 5.7.2 " from http://www.bonitasoft.com/products/BPM_downloads .

Build instructions
==================

1) Clone this project
-----------------------

git clone git@github.com:exo-addons/bonita-extension.git
{PROJECT_HOME} refers to the directory where you cloned the project.
cd bonita-extension

2) Build the project
-----------------------

>mvn clean install -Dmaven.test.skip -Pexo-private,exo-staging,distrib

After a build success of the project you will have under the target folder:

* {PROJECT_HOME}/bonita-extension/webapp/target/bonita-extension.war  ->Contains the configuration files and the specified  gadgets of the integration with eXo platform on tomcat.
* {PROJECT_HOME}/bonita-extension/config/webapp/target/exo.platform.sample.bonita-website.config-3.5.5-SNAPSHOT.jar  -> it's the activation jar of our extension.
* {PROJECT_HOME}/bonita-extension/component/filter/target/exo.platform.bonita.component.filter-3.5.5-SNAPSHOT.jar  -> Contains the filter of authentication between plf and bonita.
* {PROJECT_HOME}/bonita-extension/component/services/target/exo.platform.bonita.component.services-3.5.5-SNAPSHOT.jar  -> Contains Rest services used by the gadget TodoList and Processlist.
* {PROJECT_HOME}/bonita-extension/component/uiextension/target/exo.platform.bonita.component.uiextension-3.5.5-SNAPSHOT.jar -> Contains the configuration of the UIExtension added in the GED.
* {PROJECT_HOME}/bonita-extension/portlet/target/bonita-portlet.war --> Contains the configuration and modification of UIIFramePortlet and UIParametrizedIFramePortlet.
* {PROJECT_HOME}/bonita-extension/samples/delivery/target/Workflow-Samples-3.5.5-SNAPSHOT.zip --> Contains some samples of process wich can deployed into  bonita and used through plf.

Bonita integration steps:
=======================

Modification in bonita.war
-------------


- Choose from "Bonita Open Solution Deployement" the bonita.war "with_execution_engine_without_client" and modifies it as follows:

     - Add "web authentication filter" in web.xml :    
	 
                   <filter>    
                        <filter-name>Web Authentication Filter</filter-name>
                       <filter-class>org.exoplatform.bonitasoft.filter.SetCredentialsInSessionFilter</filter-class>
                    </filter>
                  <filter-mapping>
                        <filter-name>Web Authentication Filter</filter-name>
                        <url-pattern>/*</url-pattern>
                  </filter-mapping>
				  
				  
	 - ADD the "exo.platform.bonita.component.filter-3.5.5-SNAPSHOT.jar" in "/WEB-INF/lib" of bonita.war.
	 
- After modification, Add bonita.war into  "{PLF_HOME}/webapp"
	 
	 
Modification in bonita-server-rest.war
------------- 

- ADD the jar "bonita-server-auth-5.7.2.jar" in "/WEB-INF/lib" of bonita-server-rest.war.

- After modification, Add bonita-server-rest.war into  "{PLF_HOME}/webapp".



Modification of plf configuration files
------------- 

- {PLF_HOME}/conf/context.xml :

Add the datasource configuration of bonita
   
   
             <!-- Configure Datasource -->
    <Resource name="bonita/default/journal"  
              auth="Container"  
              type="javax.sql.DataSource" 
              maxActive="100"  
              minIdle="10"  
              maxWait="10000"  
              initialSize="1" 
              maxPoolSize="15" 
              minPoolSize="3"
              maxConnectionAge="0"
              maxIdleTime="1800"
              maxIdleTimeExcessConnections="120"
              idleConnectionTestPeriod="30"
              acquireIncrement="3"
              testConnectionOnCheckout="true"
              removeAbandoned="true" 
              logAbandoned="true" 
              username="bonita" 
              password="bpm"
              
              driverClassName="org.h2.Driver" 
              url="jdbc:h2:file:${BONITA_HOME}/server/default/work/databases/bonita_journal.db;FILE_LOCK=NO;MVCC=TRUE;DB_CLOSE_ON_EXIT=TRUE"/>
   
      
      <Resource name="bonita/default/history"  
              auth="Container"  
              type="javax.sql.DataSource" 
              maxActive="100"  
              minIdle="10"  
              maxWait="10000"  
              initialSize="1" 
              maxPoolSize="15" 
              minPoolSize="3"
              maxConnectionAge="0"
              maxIdleTime="1800"
              maxIdleTimeExcessConnections="120"
              idleConnectionTestPeriod="30"
              acquireIncrement="3"
              testConnectionOnCheckout="true"
              removeAbandoned="true" 
              logAbandoned="true" 
              username="bonita" 
              password="bpm"
              driverClassName="org.h2.Driver" 
              url="jdbc:h2:file:${BONITA_HOME}/server/default/work/databases/bonita_history.db;FILE_LOCK=NO;MVCC=TRUE;DB_CLOSE_ON_EXIT=TRUE"/>







- {PLF_HOME}/conf/jaas.xml

Add the authentication's configuration of bonita  


								/**
				* Performs the authentication of the users using the authentication
				* service configured in Bonita environment configuration file
				*/
				BonitaAuth {
				  org.ow2.bonita.identity.auth.BonitaIdentityLoginModule required;
				};

				/**
				* Used to retrieve the credentials of the user and save them in the 
				* context shared between the LoginModules stacked in the LoginContext
				*/

				BonitaStore {
				  org.ow2.bonita.identity.auth.BonitaRESTLoginModule required restUser="restuser" restPassword="restbpm";
				};

				/**
				 * Used by the REST server
				 */
				BonitaRESTServer {
				  org.ow2.bonita.identity.auth.BonitaRESTServerLoginModule required logins="restuser" passwords="restbpm" roles="restuser";
				};


- {PLF_HOME}/bin/setenv.bat

Remplace the configuration of the old setenv.bat by


				@REM
				@REM Copyright (C) 2011 eXo Platform SAS.
				@REM
				@REM This is free software; you can redistribute it and/or modify it
				@REM under the terms of the GNU Lesser General Public License as
				@REM published by the Free Software Foundation; either version 2.1 of
				@REM the License, or (at your option) any later version.
				@REM
				@REM This software is distributed in the hope that it will be useful,
				@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
				@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
				@REM Lesser General Public License for more details.
				@REM
				@REM You should have received a copy of the GNU Lesser General Public
				@REM License along with this software; if not, write to the Free
				@REM Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
				@REM 02110-1301 USA, or see the FSF site: http://www.fsf.org.
				@REM

				@REM production script to set environment variables for eXo Platform

				rem Sets some variables
				set LOG_OPTS=-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog
				set SECURITY_OPTS=-Djava.security.auth.login.config=..\conf\jaas.conf
				set EXO_OPTS=-Dexo.product.developing=false -Dexo.conf.dir.name=gatein\conf
				set IDE_OPTS=-Djavasrc=$JAVA_HOME/src.zip -Djre.lib=$JAVA_HOME/jre/lib

				set res=false
				if "%EXO_PROFILES%" == "" set res=true
				if "%EXO_PROFILES%" == "-Dexo.profiles=default" set res=true
				if "%res%"=="true" (
					set EXO_PROFILES=-Dexo.profiles=default
				)

				set BPM_HOSTNAME=localhost
				set BPM_HTTP_PORT=8080
				set BPM_URI=http://%BPM_HOSTNAME%:%BPM_HTTP_PORT%
				rem set BPM_URI=http://%BPM_HOSTNAME%

				set BPM_OPTS=-Dorg.exoplatform.runtime.conf.gatein.host=%BPM_HOSTNAME% %BPM_OPTS%
				set BPM_OPTS=-Dorg.exoplatform.runtime.conf.gatein.port=%BPM_HTTP_PORT% %BPM_OPTS%
				set BPM_OPTS=-Dorg.exoplatform.runtime.conf.gatein.portal=portal %BPM_OPTS%

				set BPM_OPTS=-Dorg.exoplatform.runtime.conf.cas.server.name=%BPM_URI% %BPM_OPTS%

				set BONITA_HOME=-DBONITA_HOME=%CATALINA_HOME%\bonita
				set REST=-Dorg.ow2.bonita.rest-server-address=%BPM_URI%/bonita-server-rest -Dorg.ow2.bonita.api-type=REST

				set CATALINA_OPTS=-Xms256m -Xmx1024m -XX:MaxPermSize=256m %CATALINA_OPTS% %LOG_OPTS% %SECURITY_OPTS% %EXO_OPTS% %IDE_OPTS% %EXO_PROFILES% %BONITA_HOME% %REST% %BPM_OPTS%


Add Bonita Folder
------------- 
Move the folder "bonita"wich contains the bonita database configuration under {"PROJECT_HOME}/ressources/" to  "/{PLF_HOME}"


Add h2 jar
------------- 
Move "h2-1.2.139.jar" i under "/BOS-5.7.2-Tomcat-6.0.33/lib/bonita" to "/{PLF_HOME}/lib"



Going Further
=============
learn more about using the features in the UserGuide and InstallGuide Manuals.
