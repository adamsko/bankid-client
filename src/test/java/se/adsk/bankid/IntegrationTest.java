package se.adsk.bankid;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import se.adsk.bankid.api.FaultException;
import se.adsk.bankid.api.PersonalNumber;
import se.adsk.bankid.api.User;
import se.adsk.bankid.client.Configuration;
import se.adsk.bankid.client.RelyingPartyClient;
import se.adsk.bankid.client.ServiceConfigurator;

@Slf4j
public class IntegrationTest {

  //@Test
  public void testAuthentication() throws Exception {
    Configuration testConfiguration = Configuration.testConfiguration();
    log.debug("configuration: {}", testConfiguration);

    ServiceConfigurator serviceConfigurator = new ServiceConfigurator(testConfiguration);

    RelyingPartyClient relyingPartyClient = new RelyingPartyClient(serviceConfigurator);

    BankIdAuthenticator bankIdAuthenticator = new BankIdAuthenticator(relyingPartyClient);

    try {
      User user = bankIdAuthenticator.authenticate(PersonalNumber.parse("7511307816"));
      log.info("Authenticated user: {}", user);
    } catch (FaultException e) {
      log.info("Authentication fault: {}", e);
    }
  }
}
