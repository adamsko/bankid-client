package se.adsk.bankid.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class AuthenticateResponse {
  private final String orderReference;
  private final String autoStartToken;
}
