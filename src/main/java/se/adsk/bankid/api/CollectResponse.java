package se.adsk.bankid.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class CollectResponse {
  private final ProgressStatus progressStatus;
  private final User user;
  private final String signature;
  private final String ocspResponse;
}
