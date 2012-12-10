How to Build the Bonita Extension for eXo Platform
================


Before you start to build, visit our intro on [wiki](https://github.com/exo-addons/bonita-extension/wiki).


To build, make sure you have the following properly installed  on your system :   
* Recent Git client
* Java Development Kit 1.6
* Recent Maven 3.0.3

Download [Bonita Open Solution 5.7.2 - Tomcat bundle](http://www.bonitasoft.com/products/BPM_downloads/all) and unzip it. Then install the security-server lib in your local repository :

    cd BOS-5.7.2-Tomcat-6.0.33/lib/bonita
    mvn install:install-file -Dfile=security-server-5.7.2.jar -DgroupId=org.bonitasoft.console -DartifactId=security-server -Dversion=5.7.2 -Dpackaging=jar -DgeneratePom=true

Clone this project : 

    git clone https://github.com/exo-addons/bonita-extension.git
    cd bonita-extension

Before build it, change exo depencies in root pom.xml to match your target eXoPlatform version. Then build it with maven :
    mvn clean install -Dmaven.test.skip -Pdistrib


The following artefacts are produced :
* ```bonita-extension-component-config-3.5.5-SNAPSHOT.jar```: the activation jar of our extension.
* ```bonita-extension-component-api-filter-3.5.5-SNAPSHOT.jar```: the filter for authentication between plf and bonita.
* ```bonita-extension-component-api-services-3.5.5-SNAPSHOT.jar```: REST services used by the gadgets.
* ```bonita-extension-component-api-uiextension-3.5.5-SNAPSHOT.jar```: configuration of the UIExtension added in the Content Explorer.
* ```bonita-extension.war```: the configuration files and the specified  gadgets of the integration with eXo Platform.
* ```bonita-portlet.war```: configuration and modification of UIIFramePortlet and UIParametrizedIFramePortlet.
* ```Workflow-Samples-3.5.5-SNAPSHOT.zip```: sample processes which can be deployed into  bonita and used through plf.
* ```bonita-server-auth-5.7.2.jar```: a class for the authentication to bonita.
* ```bonita-extension-resources-3.5.5-SNAPSHOT.jar```: Bonita database configuration.

Troubleshooting
----

If the build complains about missing artifacts, please let us know [via our forums](http://forum.exoplatform.org).

As a quick workaround, you may try setting up eXo maven repositories as follows.

Edit your```$HOME/.m2/settings.xml```  (```%HOMEPATH%\.m2\settings.xml``` on Windows) as follows :

 ```xml
                             <settings>
                               <profiles>
                                 <profile>
                                   <id>exo-public-repository</id>
                                   <repositories>
                                     <repository>
                                       <id>exo-public-repository-group</id>
                                       <name>eXo Public Maven Repository Group</name>
                                       <url>http://repository.exoplatform.org/content/groups/public</url>
                                       <layout>default</layout>
                                       <releases>
                                         <enabled>true</enabled>
                                         <updatePolicy>never</updatePolicy>
                                       </releases>
                                       <snapshots>
                                         <enabled>true</enabled>
                                         <updatePolicy>never</updatePolicy>
                                       </snapshots>
                                     </repository>
                                   </repositories>
                                   <pluginRepositories>
                                     <pluginRepository>
                                       <id>exo-public-repository-group</id>
                                       <name>eXo Public Maven Repository Group</name>
                                       <url>http://repository.exoplatform.org/content/groups/public</url>
                                       <layout>default</layout>
                                       <releases>
                                         <enabled>true</enabled>
                                         <updatePolicy>never</updatePolicy>
                                       </releases>
                                       <snapshots>
                                         <enabled>true</enabled>
                                         <updatePolicy>never</updatePolicy>
                                       </snapshots>
                                     </pluginRepository>
                                   </pluginRepositories>
                                 </profile>
                               </profiles>
                               <activeProfiles>
                                 <activeProfile>exo-public-repository</activeProfile>
                               </activeProfiles>
                             </settings>
```


> Continue with the [Packaging](https://github.com/exo-addons/bonita-extension/wiki/Packaging) instructions.
