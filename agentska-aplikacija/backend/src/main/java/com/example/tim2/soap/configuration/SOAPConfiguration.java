package com.example.tim2.soap.configuration;

import com.example.tim2.soap.clients.AdvertisementClient;
import com.example.tim2.soap.clients.OrderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class SOAPConfiguration {

    private String keystoreType = "PKCS12";
    /**
     * TLS version.
     */
    @Value("${server.ssl.algorithm}")
    private String algorithm;

    /**
     * Application keystore path.
     */
    @Value("${server.ssl.key-store}")
    private String keystore;

    /**
     * Application keystore password.
     */
    @Value("${server.ssl.key-store-password}")
    private String keystorePassword;

    /**
     * Keystore alias for application client credential.
     */
    @Value("${server.ssl.key-alias}")
    private String applicationKeyAlias;

    /**
     * Application truststore path.
     */
    @Value("${server.ssl.trust-store}")
    private String truststore;

    private String truststoreType = "PKCS12";

    /**
     * Application truststore password.
     */
    @Value("${server.ssl.trust-store-password}")
    private String truststorePassword;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("com.example.tim2.soap.gen");
        return marshaller;
    }

    @Bean
    public AdvertisementClient advertisementClient(Jaxb2Marshaller marshaller) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        AdvertisementClient client = new AdvertisementClient();
        client.setDefaultUri("https://localhost:8082/advertisement/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(messageSender());
        return client;
    }

    @Bean
    public OrderClient orderClient(Jaxb2Marshaller marshaller) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        OrderClient client = new OrderClient();
        client.setDefaultUri("https://localhost:8082/orders/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(messageSender());
        return client;
    }

    @Bean
    public HttpsUrlConnectionMessageSender messageSender() throws IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(keystoreType);
        keyStore.load(new FileInputStream(new File(keystore)), keystorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(truststoreType);
        trustStore.load(new FileInputStream(new File(truststore)), truststorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        HttpsUrlConnectionMessageSender messageSender = new HttpsUrlConnectionMessageSender();
        messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
        messageSender.setTrustManagers(trustManagerFactory.getTrustManagers());

        // otherwise: java.security.cert.CertificateException: No name matching localhost found
        messageSender.setHostnameVerifier((hostname, sslSession) -> {
            if (hostname.equals("localhost")) {
                return true;
            }
            return false;
        });
        return messageSender;
    }

}
