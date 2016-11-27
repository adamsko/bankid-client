package se.adsk.bankid.api;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class PersonalNumberTest {

  @Test
  public void successfullyCreate() throws Exception {
    PersonalNumber pn = PersonalNumber.parse("7811231818");
    assertEquals(pn.toString(), "7811231818");
  }

  @Test
  public void testCorrectPersonalNumbers() throws Exception {
    PersonalNumber.parse("1111111111");
    PersonalNumber.parse("11111111111");
    PersonalNumber.parse("111111111111");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToLong() throws Exception {
    PersonalNumber.parse("1111111111111");
  }
  @Test(expected = IllegalArgumentException.class)
  public void testToShort() throws Exception {
    PersonalNumber.parse("111111111");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCharacters() throws Exception {
    PersonalNumber.parse("111111-1111");
  }

}
