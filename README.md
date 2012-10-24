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
Before the project builder you will copy the library "security-server-5.7.2.jar" in the Maven Local rest which is used in the build of the module "component / API / filter".
So, go to the location of this jar wich "../Tomcat-6.0.33 includes BOS 5.7.2/lib/bonita" and execute this command :

>mvn install:install-file  -Dfile=security-server-5.7.2.jar -DgroupId=org.bonitasoft.console -DartifactId=security-server -Dversion=5.7.2 -Dpackaging=jar -DgeneratePom=true

Return to the {PROJECT_HOME} and execute the build command:

>mvn clean install -Dmaven.test.skip -Pdistrib

After a build success of the project you will have under the target folder:

* {PROJECT_HOME}/bonita-extension/webapps/extension/target/bonita-extension.war  ->Contains the configuration files and the specified  gadgets of the integration with eXo platform on tomcat.
* {PROJECT_HOME}/bonita-extension/component/config/target/bonita-extension-component-config-3.5.5-SNAPSHOT.jar  -> it's the activation jar of our extension.
* {PROJECT_HOME}/bonita-extension/component/API/filter/target/bonita-extension-component-api-filter-3.5.5-SNAPSHOT.jar  -> Contains the filter of authentication between plf and bonita.
* {PROJECT_HOME}/bonita-extension/component/API/services/target/bonita-extension-component-api-services-3.5.5-SNAPSHOT.jar  -> Contains Rest services used by the gadget TodoList and Processlist.
* {PROJECT_HOME}/bonita-extension/component/API/uiextension/target/bonita-extension-component-api-uiextension-3.5.5-SNAPSHOT.jar -> Contains the configuration of the UIExtension added in the GED.
* {PROJECT_HOME}/bonita-extension/wabapps/portlet/target/bonita-portlet.war --> Contains the configuration and modification of UIIFramePortlet and UIParametrizedIFramePortlet.
* {PROJECT_HOME}/bonita-extension/component/samples/delivery/target/Workflow-Samples-3.5.5-SNAPSHOT.zip --> Contains some samples of process wich can deployed into  bonita and used through plf.
* {PROJECT_HOME}/bonita-extension/component/API/authentication/target/bonita-server-auth-5.7.2.jar -> Contains a class for the authentication to bonita.


Going Further
=============
learn more about using the features in the UserGuide ,InstallGuide  and Packaging Guide Manuals.
 