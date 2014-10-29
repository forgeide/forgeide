forgeide
========

Forge IDE is an xPaas tool for generating and deploying applications.

WildFly 8.1 is required.

Setup
-----

To use some features of Forge IDE it is required that you add an entry to your operating system's hosts file (generally /etc/hosts in a *nix-based system):

127.0.0.1	forgeide.org

You will also need to configure WildFly to support HTTPS connections:

1. In the wildfly/standalone/configuration directory run the keytool utility to generate a new keystore file:

  keytool -genkey -alias forgeide -keyalg RSA -keystore forgeide.keystore -validity 10950

2. Answer the questions prompted by the keytool wizard - the following is provided as an example only:

  Enter keystore password: secret
  Re-enter new password: secret
  What is your first and last name?
    [Unknown]:  forgeide.org
  What is the name of your organizational unit?
    [Unknown]:  FORGEIDE
  What is the name of your organization?
    [Unknown]:  JBoss
  What is the name of your City or Locality?
    [Unknown]:  Brisbane
  What is the name of your State or Province?
    [Unknown]:  QLD
  What is the two-letter country code for this unit?
    [Unknown]:  AU
  Is CN=forgeide.org, OU=FORGEIDE, O=JBoss, L=Brisbane, ST=QLD, C=AU correct?
    [no]:  yes

  Enter key password for <forgeide>
  	(RETURN if same as keystore password):  

3. Configure WildFly to accept HTTPS connections.  Edit wildfly/standalone/configuration/standalone.xml and add the following under the <management><security-realms> section:

  <security-realm name="ForgeIDERealm">
      <server-identities>
          <ssl>
              <keystore path="forgeide.keystore" relative-to="jboss.server.config.dir" keystore-password="secret"/>
          </ssl>
      </server-identities>
  </security-realm>

4. In the same file, locate the subsystem configuration for undertow (<subsystem xmlns="urn:jboss:domain:undertow:1.1">) and add the following under the <server> element:

  <https-listener name="https" socket-binding="https" security-realm="ForgeIDERealm"/>           
  <host name="forgeide-host" alias="forgeide.org"/>            


5. Also in the same file, update the socket-binding values within the <socket-binding-group> element with these new values for http and https - this is assuming you wish to serve the application via the standard ports (80 and 443):

        <socket-binding name="http" port="${jboss.http.port:80}"/>
        <socket-binding name="https" port="${jboss.https.port:443}"/>

6. Save the configuration file and restart WildFly.  If you wish to use the standard ports (as in step 5) you may need to run WildFly with elevated privileges:

  sudo bin/standalone.sh
  
7. Browse to http://forgeide.org to access the application.

