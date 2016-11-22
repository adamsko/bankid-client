package se.adsk.bankid.api;

import java.util.regex.Pattern;

/**
 * A personal number is a string consisting of 10 to 12 decimal characters.
 */
public class PersonalNumber {

  private static final Pattern PERSONAL_NUMBER_PATTERN = Pattern.compile("\\d{10,12}");

  private final String value;

  public PersonalNumber(String personalNumber) {
    if (personalNumber == null) {
      throw new NullPointerException("personalNumber cannot be null");
    }
    if (!personalNumber.matches(PERSONAL_NUMBER_PATTERN.pattern())) {
      throw new IllegalArgumentException("The personal number <" + personalNumber + "> did not match the pattern <" + PERSONAL_NUMBER_PATTERN.pattern() + ">");
    }
    this.value = personalNumber;
  }

  public String getValue() {
    return value;
  }

  @Override public String toString() {
    return value;
  }
}
