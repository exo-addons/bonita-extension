Bonita Extension for eXo Platform
================

This extension is an integration of Bonita with eXo Platform. It features :
* A Process List gadget that lists available business processes
* A Workflow Tasks gadget that lists tasks to take in the current process instances
* A Publication Process integrated with eXo Publication and the Activity Stream
* A UI extension for the eXo Content Explorer to display the publication status and comments
* A sample Leave Application process
* Access to workflow forms
* Access to the Bonita Administration console




Build instructions
==================

First, make sure this is installed on your system :   
* Recent Git client
* Java Development Kit 1.6
* Recent Maven 3

Download [Bonita Open Solution - 5.7.2 Tomcat bundle](http://www.bonitasoft.com/products/BPM_downloads/all) and unzip it.

    cd BOS-5.7.2-Tomcat-6.0.33/lib/bonita
    mvn install:install-file -Dfile=security-server-5.7.2.jar -DgroupId=org.bonitasoft.console -DartifactId=security-server -Dversion=5.7.2 -Dpackaging=jar -DgeneratePom=true

Clone this project and build it with maven

    git clone git@github.com:exo-addons/bonita-extension.git
    cd bonita-extension
    mvn clean install -Dmaven.test.skip -Pdistrib

After a successful build, you will have the following under the target folder:
* ```component/config/target/bonita-extension-component-config-3.5.5-SNAPSHOT.jar```  : the activation jar of our extension.
* ```component/API/filter/target/bonita-extension-component-api-filter-3.5.5-SNAPSHOT.jar```  : the filter for authentication between plf and bonita.
* ```component/API/services/target/bonita-extension-component-api-services-3.5.5-SNAPSHOT.jar```  : REST services used by the gadgets.
* ```component/API/uiextension/target/bonita-extension-component-api-uiextension-3.5.5-SNAPSHOT.jar``` : configuration of the UIExtension added in the Content Explorer.
* ```webapps/extension/target/bonita-extension.war```  : the configuration files and the specified  gadgets of the integration with eXo Platfor.
* ```webapps/portlet/target/bonita-portlet.war``` : configuration and modification of UIIFramePortlet and UIParametrizedIFramePortlet.
* ```component/samples/delivery/target/Workflow-Samples-3.5.5-SNAPSHOT.zip``` : sample processes which can be deployed into  bonita and used through plf.
* ```component/API/authentication/target/bonita-server-auth-5.7.2.jar``` : a class for the authentication to bonita.
* ```ressources/bonita-extension-resources-3.5.5-SNAPSHOT.jar``` : Bonita database configuration.


Getting Started
=============

1. Get [eXo Platform  3.5.5 (http://www.exoplatform.com/exo-platform-3.5-trial/eXo-Platform-3.5.zip) and unzip it.
2. Follow the [Packaging Instructions](https://github.com/exo-addons/bonita-extension/wiki/Packaging-Guide-:-Manual-Packaging-Steps) to learn how to package bonita and the extension within the eXo Platform bundle.
3. Follow the [Install Guide](https://github.com/exo-addons/bonita-extension/wiki/InstallGuide) to Learn how to install the package and the sample processes 
4. Finally, read the [User Guide](https://github.com/exo-addons/bonita-extension/wiki/User-Guide) to learn how to use the features of the extension.
 