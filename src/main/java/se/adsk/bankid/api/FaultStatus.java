package se.adsk.bankid.api;

/**
 * Error codes that can be returned by the BankID api.
 * Refer to the BankID documentation for the meaning of these.
 */
public enum FaultStatus {
  INVALID_PARAMETERS,
  ACCESS_DENIED_RP,
  CLIENT_ERR,
  CERTIFICATE_ERR,
  RETRY,
  INTERNAL_ERROR,
  ALREADY_COLLECTED,
  EXPIRED_TRANSACTION,
  ALREADY_IN_PROGRESS,
  USER_CANCEL,
  CANCELLED,
  REQ_PRECOND,
  REQ_ERROR,
  REQ_BLOCKED,
  START_FAILED;
}
