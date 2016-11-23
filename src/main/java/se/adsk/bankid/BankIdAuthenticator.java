package se.adsk.bankid;

import lombok.extern.slf4j.Slf4j;
import se.adsk.bankid.api.*;
import se.adsk.bankid.client.RelyingPartyClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Performs the BankID authentication process.
 * If the authentication process is aborted due to connectivity issues
 * it can be resumed by calling authenticate() again on the same instance
 * of BankIdAuthenticator with the same personal number.
 */
@Slf4j
public class BankIdAuthenticator {

  private static final long DEFAULT_COLLECT_INTERVAL_MILLIS = 2000;

  private final RelyingPartyClient relyingPartyClient;

  private final Map<String, String> orderReferenceCache = new ConcurrentHashMap<>();

  private long collectIntervalMillis = DEFAULT_COLLECT_INTERVAL_MILLIS;

  /**
   * Create a new BankIdAuthenticator using the given RelyingPartyClient to connect to BankID.
   * @param relyingPartyClient
   */
  public BankIdAuthenticator(RelyingPartyClient relyingPartyClient) {
    this.relyingPartyClient = relyingPartyClient;
  }

  /**
   * Attempt to authenticate the given personal number.
   * This method will block until the authentication process is completed.
   * If authentication was successful a User object will be returned,
   * otherwise a FaultException will be thrown.
   * @param personalNumber
   * @return
   * @throws FaultException
   */
  public User authenticate(PersonalNumber personalNumber) throws FaultException {
    log.debug("authenticate: {}", personalNumber);

    String orderReference = orderReferenceCache.get(personalNumber.getValue());

    if (orderReference == null) {
      AuthenticateResponse authenticateResponse = relyingPartyClient.authenticate(personalNumber);
      log.debug("authenticate response: {}", authenticateResponse.getOrderReference());
      orderReference = authenticateResponse.getOrderReference();
      orderReferenceCache.put(personalNumber.getValue(), orderReference);
    }

    try {
      User user = collect(orderReference);
      log.debug("collect response: {}", user);
      return user;
    } finally {
      orderReferenceCache.remove(personalNumber.getValue());
    }
  }

  private User collect(String orderReference) throws FaultException {
    while (true) {
      log.debug("collect: {}", orderReference);
      CollectResponse collect = relyingPartyClient.collect(orderReference);
      log.debug("collect response: {}", collect);
      if (collect.getProgressStatus() == ProgressStatus.COMPLETE) {
        return collect.getUser();
      }
      try {
        Thread.sleep(collectIntervalMillis);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Set the time between calls to the collect service method.
   * If not set 2000 milliseconds is used.
   * @param millis Must be between 1000 and 5000 milliseconds.
   */
  public void setCollectInterval(long millis) {
    if (millis < 1000 || millis > 5000) {
      throw new IllegalArgumentException("Collect interval must be between 1000 and 5000 milliseconds.");
    }
    this.collectIntervalMillis = millis;
  }
}
