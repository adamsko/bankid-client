package se.adsk.bankid.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@RequiredArgsConstructor
@ToString
@Getter
public class User {
  private final String firstName;
  private final String lastName;
  private final String name;
  private final String personalNumber;
  private final LocalDate notBefore;
  private final LocalDate notAfter;
  private final String ipAddress;
}
