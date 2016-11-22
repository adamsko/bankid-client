package se.adsk.bankid.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import se.adsk.bankid.BankIdAuthenticator;

import java.net.URISyntaxException;

@Builder
@Getter
@ToString
public class Configuration {
  @NonNull private final String trustStorePath;
  @NonNull private final String trustStoreType;
  @NonNull private final String trustStorePassword;

  @NonNull private final String keyStorePath;
  @NonNull private final String keyStoreType;
  @NonNull private final String keyStorePassword;

  private final String endpointAddress;

  private final long connectionTimeout;
  private final long receiveTimeout;

  private final boolean loggingInterceptors;

  /**
   * Returns a configuration that is capable of connecting to and performing
   * authentication with the BankID TEST environment.
   * @return
   */
  public static Configuration testConfiguration() {
    String contextPath;
    try {
      contextPath = BankIdAuthenticator.class.getResource("/certs").toURI().getPath();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return Configuration.builder()
        .endpointAddress("https://appapi.test.bankid.com/rp/v4")
        .trustStorePath(contextPath + "/truststore_test.jks")
        .trustStoreType("JKS")
        .trustStorePassword("changeit")
        .keyStorePath(contextPath + "/FPTestcert2_20150818_102329.pfx")
        .keyStoreType("pkcs12")
        .keyStorePassword("qwerty123")
        .loggingInterceptors(true)
        .build();
  }
}
