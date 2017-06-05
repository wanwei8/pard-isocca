package com.pard.common;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * Created by wawe on 17/4/29.
 */
@ConditionalOnExpression(value = "${tomcat.ssl.enable:false}")
@Configuration
public class CustomTomcatSSLConfiguration {
    @Value("${tomcat.ssl.port:8443}")
    private int tomcatSSLPort;

    @Value("${tomcat.absoluteKeystoreFile:}")
    private String absoluteKeystoreFile;

    @Value("${tomcat.keystorePassword:}")
    private String keystorePassword;

    @Value("${tomcat.keystoreType:PKCS12}")
    private String keystoreType;

    @Value("${tomcat.keystoreAlias:keyalias}")
    private String keystoreAlias;

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() throws FileNotFoundException {
        final String absoluteKeyStoreFile = ResourceUtils.getFile(absoluteKeystoreFile).getAbsolutePath();

        final TomcatConnectorCustomizer customizer = new MyTomcatConnectionCustomizer(
                tomcatSSLPort, absoluteKeyStoreFile, keystorePassword, keystoreType, keystoreAlias);

        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory containerFactory =
                            (TomcatEmbeddedServletContainerFactory) container;
                    containerFactory.addConnectorCustomizers(customizer);
                }
            }

        };

    }

    private static class MyTomcatConnectionCustomizer implements TomcatConnectorCustomizer {

        private int tomcatSSLPort;
        private String absoluteKeystoreFile;
        private String keystorePassword;
        private String keystoreType;
        private String keystoreAlias;

        public MyTomcatConnectionCustomizer(int tomcatSSLPort, String absoluteKeystoreFile, String keystorePassword,
                                            String keystoreType, String keystoreAlias) {
            this.tomcatSSLPort = tomcatSSLPort;
            this.absoluteKeystoreFile = absoluteKeystoreFile;
            this.keystorePassword = keystorePassword;
            this.keystoreType = keystoreType;
            this.keystoreAlias = keystoreAlias;
        }

        @Override
        public void customize(Connector connector) {
            connector.setPort(tomcatSSLPort);
            connector.setSecure(true);
            connector.setScheme("https");

            connector.setAttribute("SSLEnabled", true);
            connector.setAttribute("sslProtocol", "TLS");
            connector.setAttribute("protocol", "org.apache.coyote.http11.Http11Protocol");
            connector.setAttribute("clientAuth", false);
            connector.setAttribute("keystoreFile", absoluteKeystoreFile);
            connector.setAttribute("keystoreType", keystoreType);
            connector.setAttribute("keystorePass", keystorePassword);
            connector.setAttribute("keystoreAlias", keystoreAlias);
            connector.setAttribute("keyPass", keystorePassword);
        }
    }
}
