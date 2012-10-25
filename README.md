How to Build the Bonita Extension for eXo Platform
================


Before you start to build, visit our intro on [wiki](https://github.com/exo-addons/bonita-extension/wiki).


To build, make sure you have the following properly installed  on your system :   
* Recent Git client
* Java Development Kit 1.6
* Recent Maven 3

Download [Bonita Open Solution 5.7.2 - Tomcat bundle](http://www.bonitasoft.com/products/BPM_downloads/all) and unzip it. Then install the security-server lib in your local repository :

    cd BOS-5.7.2-Tomcat-6.0.33/lib/bonita
    mvn install:install-file -Dfile=security-server-5.7.2.jar -DgroupId=org.bonitasoft.console -DartifactId=security-server -Dversion=5.7.2 -Dpackaging=jar -DgeneratePom=true

Clone this project and build it with maven : 

    git clone git@github.com:exo-addons/bonita-extension.git
    cd bonita-extension
    mvn clean install -Dmaven.test.skip -Pdistrib


The following artefacts are produced :
* ```bonita-extension-component-config-3.5.5-SNAPSHOT.jar```: the activation jar of our extension.
* ```bonita-extension-component-api-filter-3.5.5-SNAPSHOT.jar```: the filter for authentication between plf and bonita.
* ```bonita-extension-component-api-services-3.5.5-SNAPSHOT.jar```: REST services used by the gadgets.
* ```bonita-extension-component-api-uiextension-3.5.5-SNAPSHOT.jar```: configuration of the UIExtension added in the Content Explorer.
* ```bonita-extension.war```: the configuration files and the specified  gadgets of the integration with eXo Platfor.
* ```bonita-portlet.war```: configuration and modification of UIIFramePortlet and UIParametrizedIFramePortlet.
* ```Workflow-Samples-3.5.5-SNAPSHOT.zip```: sample processes which can be deployed into  bonita and used through plf.
* ```bonita-server-auth-5.7.2.jar```: a class for the authentication to bonita.
* ```bonita-extension-resources-3.5.5-SNAPSHOT.jar```: Bonita database configuration.

> Continue with the [Packaging instructions](https://github.com/exo-addons/bonita-extension/wiki/Packaging-Guide-:-Manual-Packaging-Steps)
