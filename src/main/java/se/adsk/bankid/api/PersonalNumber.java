package se.adsk.bankid.api;

import static java.util.Objects.requireNonNull;

import java.util.regex.Pattern;

/**
 * A personal number is a string consisting of 10 to 12 decimal characters.
 */
public class PersonalNumber {

  private static final Pattern PERSONAL_NUMBER_PATTERN = Pattern.compile("\\d{10,12}");

  private final String value;

  private PersonalNumber(String personalNumber) {
    this.value = requireNonNull(personalNumber, "personalNumber cannot be null");

    if (!personalNumber.matches(PERSONAL_NUMBER_PATTERN.pattern())) {
      throw new IllegalArgumentException("The personal number <" + personalNumber + "> did not match the pattern <" + PERSONAL_NUMBER_PATTERN.pattern() + ">");
    }
  }

  public static PersonalNumber parse(String value) {
    return new PersonalNumber(value);
  }


  @Override public String toString() {
    return value;
  }
}
