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
* eXo Platform  3.5.5 : Get it [here](http://www.exoplatform.com/exo-platform-3.5-trial/eXo-Platform-3.5.zip) and unzip it at {PLF_HOME}.
* The eXo server will run on port 8080, make sure this port is not currently in use
* Bonita Community-5.7.2 : Go to [Bonita Downloads](http://www.bonitasoft.com/products/BPM_downloads) and download the _Bonita Open Solution Deployement_ and the _Tomcat-6.0.33 bundle_.

Build instructions
==================

1) Clone this project
-----------------------

git clone git@github.com:exo-addons/bonita-extension.git
{PROJECT_HOME} refers to the directory where you cloned the project.
cd bonita-extension

2) Build the project
-----------------------
Before building, you will need to install the security-server-5.7.2.jar library in your local maven repository (it is required to build the module component/API/filter).
So, go to "../Tomcat-6.0.33 includes BOS 5.7.2/lib/bonita" and execute this command :

    >mvn install:install-file  -Dfile=security-server-5.7.2.jar -DgroupId=org.bonitasoft.console -DartifactId=security-server -Dversion=5.7.2 -Dpackaging=jar -DgeneratePom=true

Return to the {PROJECT_HOME} and execute the build command:

    >mvn clean install -Dmaven.test.skip -Pdistrib

After a build success of the project you will have under the target folder:
* component/config/target/bonita-extension-component-config-3.5.5-SNAPSHOT.jar  : the activation jar of our extension.
* component/API/filter/target/bonita-extension-component-api-filter-3.5.5-SNAPSHOT.jar  : the filter for authentication between plf and bonita.
* component/API/services/target/bonita-extension-component-api-services-3.5.5-SNAPSHOT.jar  : REST services used by the gadgets.
* component/API/uiextension/target/bonita-extension-component-api-uiextension-3.5.5-SNAPSHOT.jar : configuration of the UIExtension added in the Content Explorer.
* webapps/extension/target/bonita-extension.war  : the configuration files and the specified  gadgets of the integration with eXo Platfor.
* webapps/portlet/target/bonita-portlet.war : configuration and modification of UIIFramePortlet and UIParametrizedIFramePortlet.
* component/samples/delivery/target/Workflow-Samples-3.5.5-SNAPSHOT.zip : samples processes which can be deployed into  bonita and used through plf.
* component/API/authentication/target/bonita-server-auth-5.7.2.jar : a class for the authentication to bonita.
* ressources/bonita-extension-resources-3.5.5-SNAPSHOT.jar : Bonita database configuration.


Going Further
=============

* [Packaging Guide](https://github.com/exo-addons/bonita-extension/wiki/Packaging-Guide-:-Manual-Packaging-Steps) : to learn how to integrate bonita and eXo into a single package
* [Install Guide](https://github.com/exo-addons/bonita-extension/wiki/InstallGuide) : learn how to install the package and deploy your first processes
* [User Guide](https://github.com/exo-addons/bonita-extension/wiki/User-Guide) : Learn how to use the features of the extension
 