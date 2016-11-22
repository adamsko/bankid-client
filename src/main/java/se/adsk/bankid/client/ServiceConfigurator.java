package se.adsk.bankid.client;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class ServiceConfigurator {

  private final Configuration configuration;

  public ServiceConfigurator(Configuration configuration) {
    this.configuration = configuration;
  }

  void configure(Object service)
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

    Client client = ClientProxy.getClient(service);

    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    TLSClientParameters tlsCP = new TLSClientParameters();

    String keyStorePassword = configuration.getKeyStorePassword();
    KeyStore keyStore = KeyStore.getInstance(configuration.getKeyStoreType());
    keyStore.load(new FileInputStream(configuration.getKeyStorePath()), keyStorePassword.toCharArray());
    KeyManager[] keyManagers = getKeyManagers(keyStore, keyStorePassword);
    tlsCP.setKeyManagers(keyManagers);

    String trustStorePassword = configuration.getTrustStorePassword();
    KeyStore trustStore = KeyStore.getInstance(configuration.getTrustStoreType());
    trustStore.load(new FileInputStream(configuration.getTrustStorePath()), trustStorePassword.toCharArray());
    TrustManager[] trustManagers = getTrustManagers(trustStore);
    tlsCP.setTrustManagers(trustManagers);

    httpConduit.setTlsClientParameters(tlsCP);

    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

    if (configuration.getConnectionTimeout() > 0) {
      httpClientPolicy.setConnectionTimeout(configuration.getConnectionTimeout());
    }
    if (configuration.getReceiveTimeout() > 0) {
      httpClientPolicy.setReceiveTimeout(configuration.getReceiveTimeout());
    }

    httpConduit.setClient(httpClientPolicy);

    if (configuration.isLoggingInterceptors()) {
      client.getOutInterceptors().add(new LoggingOutInterceptor());
      client.getOutFaultInterceptors().add(new LoggingOutInterceptor());
      client.getInInterceptors().add(new LoggingInInterceptor());
      client.getInFaultInterceptors().add(new LoggingInInterceptor());
    }

    if (configuration.getEndpointAddress() != null) {
      BindingProvider bindingProvider = (BindingProvider) service;
      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, configuration.getEndpointAddress());
    }
  }

  private TrustManager[] getTrustManagers(KeyStore trustStore) throws NoSuchAlgorithmException, KeyStoreException {
    String alg = KeyManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory fac = TrustManagerFactory.getInstance(alg);
    fac.init(trustStore);
    return fac.getTrustManagers();
  }

  private KeyManager[] getKeyManagers(KeyStore keyStore, String keyPassword)
      throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
    String alg = KeyManagerFactory.getDefaultAlgorithm();
    char[] keyPass = keyPassword != null ? keyPassword.toCharArray() : null;
    KeyManagerFactory fac = KeyManagerFactory.getInstance(alg);
    fac.init(keyStore, keyPass);
    return fac.getKeyManagers();
  }
}
