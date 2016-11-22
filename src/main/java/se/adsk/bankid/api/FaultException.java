package se.adsk.bankid.api;

import lombok.Getter;

@Getter
public class FaultException extends Exception {
  private final FaultStatus faultStatus;
  private final String detailedDescription;

  public FaultException(FaultStatus faultStatus, String detailedDescription) {
    super(faultStatus.toString());
    this.faultStatus = faultStatus;
    this.detailedDescription = detailedDescription;
  }
}
