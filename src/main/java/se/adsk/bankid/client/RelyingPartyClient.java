package se.adsk.bankid.client;

import com.bankid.rpservice.v4_0_0.RpFault;
import com.bankid.rpservice.v4_0_0.RpService;
import com.bankid.rpservice.v4_0_0.RpServicePortType;
import com.bankid.rpservice.v4_0_0.types.*;
import se.adsk.bankid.api.*;

public class RelyingPartyClient {

  private final RpServicePortType rpServicePort;

  public RelyingPartyClient(ServiceConfigurator serviceConfigurator) {
    this.rpServicePort = createServicePort(serviceConfigurator);
  }

  public AuthenticateResponse authenticate(PersonalNumber personalNumber) throws FaultException {
    try {
      return toAuthenticateResponse(rpServicePort.authenticate(createAuthenticateRequest(personalNumber)));
    } catch (RpFault rpFault) {
      throw toFaultException(rpFault);
    }
  }

  public CollectResponse collect(String orderReference) throws FaultException {
    try {
      return toCollectResponse(rpServicePort.collect(orderReference));
    } catch (RpFault rpFault) {
      throw toFaultException(rpFault);
    }
  }

  private FaultException toFaultException(RpFault rpFault) {
    RpFaultType faultInfo = rpFault.getFaultInfo();
    FaultStatus faultStatus = FaultStatus.valueOf(faultInfo.getFaultStatus().name());
    String detailedDescription = faultInfo.getDetailedDescription();
    return new FaultException(faultStatus, detailedDescription);
  }

  private CollectResponse toCollectResponse(CollectResponseType collectResponseType) {
    return new CollectResponse(ProgressStatus.valueOf(collectResponseType.getProgressStatus().name()),
        toUser(collectResponseType.getUserInfo()), collectResponseType.getSignature(), collectResponseType.getOcspResponse());
  }

  private User toUser(UserInfoType userInfo) {
    User user = null;
    if (userInfo != null) {
      user = new User(userInfo.getGivenName(),
          userInfo.getSurname(),
          userInfo.getName(),
          userInfo.getPersonalNumber(),
          userInfo.getNotBefore().toGregorianCalendar().toZonedDateTime().toLocalDate(),
          userInfo.getNotAfter().toGregorianCalendar().toZonedDateTime().toLocalDate(),
          userInfo.getIpAddress());
    }
    return user;
  }

  private AuthenticateResponse toAuthenticateResponse(OrderResponseType orderResponseType) {
    return new AuthenticateResponse(orderResponseType.getOrderRef(), orderResponseType.getAutoStartToken());
  }

  private AuthenticateRequestType createAuthenticateRequest(PersonalNumber personalNumber) {
    AuthenticateRequestType authenticateRequestType = new AuthenticateRequestType();
    authenticateRequestType.setPersonalNumber(personalNumber.toString());
    return authenticateRequestType;
  }

  private RpServicePortType createServicePort(ServiceConfigurator serviceConfigurator) {
    RpServicePortType rpServiceSoapPort = new RpService().getRpServiceSoapPort();
    try {
      serviceConfigurator.configure(rpServiceSoapPort);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return rpServiceSoapPort;
  }
}
