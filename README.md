# Bonita Extension

Add some tools in eXo to interact with an instance of Bonita. Bonita server and eXo server are 2 different instances.

# Prerequisites

Version 3.0.0 of this extension is design to work on PLF 4.3.0.
Bonita version tested is 7.0.2 Community Edition. It should work without problem with enterprise version. It is also tested with Bonita 7.1.5. 
Extension use Bonita Rest API to get user task list and process launchable by current user.

# Install the extension
With PLF 4.3.0, you can install the extension with 
	./addon install exo-bonita-extension
	
You can also clone this repository and build the extension with
	mvn clean install

Then unzip the file in bonita-packaging/target in your EXO_HOME.

# How it works ?
In this version, we designed 2 modes :
- One server for eXo, one server for Bonita, no SSO 
- One server for eXo, one server for Bonita, SSO configured
