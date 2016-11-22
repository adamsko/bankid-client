package se.adsk.bankid.api;

/**
 * The different states an authentication or signature order can be in.
 * Refer to the BankID documentation for the meaning of these.
 */
public enum ProgressStatus {
  OUTSTANDING_TRANSACTION, NO_CLIENT, STARTED, USER_SIGN, USER_REQ, COMPLETE
}
