package se.adsk.bankid.api;

import org.junit.Test;

public class PersonalNumberTest {

  @Test
  public void testCorrectPersonalNumbers() throws Exception {
    new PersonalNumber("1111111111");
    new PersonalNumber("11111111111");
    new PersonalNumber("111111111111");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLong() throws Exception {
    new PersonalNumber("1111111111111");
  }
  @Test(expected = IllegalArgumentException.class)
  public void testToShort() throws Exception {
    new PersonalNumber("111111111");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCharacters() throws Exception {
    new PersonalNumber("111111-1111");
  }

}
