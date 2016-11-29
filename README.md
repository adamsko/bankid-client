# BankID client library
A simple library for performing authentication with the Swedish BankID service.

In order to use this in production you will need to sign up for the BankID service with
your bank and get a valid client certificate. The certificate included is only valid for the TEST environment.

# Usage
### Building
mvn clean package
### Example code
```java
Configuration testConfiguration = Configuration.testConfiguration();

ServiceConfigurator serviceConfigurator = new ServiceConfigurator(testConfiguration);

RelyingPartyClient relyingPartyClient = new RelyingPartyClient(serviceConfigurator);

BankIdAuthenticator bankIdAuthenticator = new BankIdAuthenticator(relyingPartyClient);

try {
  User user = bankIdAuthenticator.authenticate(PersonalNumber.parse("7511307816"));
  // Authenticated
} catch (FaultException e) {
  // Authentication failed
}
```
