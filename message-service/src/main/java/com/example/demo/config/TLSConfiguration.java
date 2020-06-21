package com.example.demo.config;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

@Configuration
public class TLSConfiguration {

    public static final String URL = "https://zuul/";

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
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
                httpClient(keystoreType, keystore, keystorePassword, applicationKeyAlias,
                        truststoreType, truststore, truststorePassword)));
    }

    @Bean
    public HttpClient httpClient(String keystoreType, String keystore, String keystorePassword, String alias,
                                 String truststoreType, String truststore, String truststorePassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            keyStore.load(new FileInputStream(new File(keystore)), keystorePassword.toCharArray());

            KeyStore trustStore = KeyStore.getInstance(truststoreType);
            trustStore.load(new FileInputStream(new File(truststore)), truststorePassword.toCharArray());

            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, null)
                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> alias)
                    .build();

            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslcontext,
                    new String[]{algorithm},
                    null, (hostname, sslSession) -> true);

            return HttpClients.custom().setSSLSocketFactory(sslFactory).build();
        } catch (Exception e) {
            throw new IllegalStateException("Error while configuring http client", e);
        }
    }

    @Bean
    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws NoSuchAlgorithmException {
        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
        EurekaJerseyClientImpl.EurekaJerseyClientBuilder builder = new EurekaJerseyClientImpl.EurekaJerseyClientBuilder();
        builder.withClientName("advertisement");
        builder.withSystemSSLConfiguration();
        builder.withMaxTotalConnections(10);
        builder.withMaxConnectionsPerHost(10);
        args.setEurekaJerseyClient(builder.build());
        return args;
    }
}