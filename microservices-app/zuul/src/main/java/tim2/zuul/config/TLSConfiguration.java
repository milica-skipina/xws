package tim2.zuul.config;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

@Configuration
public class TLSConfiguration {


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
    private String alias;

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
    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws NoSuchAlgorithmException {
        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
        EurekaJerseyClientImpl.EurekaJerseyClientBuilder builder = new EurekaJerseyClientImpl.EurekaJerseyClientBuilder();
        builder.withClientName("zuul");
        builder.withSystemSSLConfiguration();
        builder.withMaxTotalConnections(10);
        builder.withMaxConnectionsPerHost(10);
        args.setEurekaJerseyClient(builder.build());
        return args;
    }

    @Bean
    public CloseableHttpClient httpClient() {
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
}