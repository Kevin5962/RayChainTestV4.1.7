package cn.mrray.raybaas.demo.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.channels.Channels;
import java.util.List;
import java.util.Map;

/**
 * RayBaas配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix="raybaas")
public class RayBaasProperties {
    private  App app;
    private  Channels channels;
    private SslConfig ssl;

    @Getter
    @Setter
    public static class Channels {
        private List<ChannelAttribute> channel;
    }
    @Getter
    @Setter
    public static class App {
        private String privateKeyPath;
        private String appId;
    }

    @Getter
    @Setter
    public static class ChannelAttribute {
        private Peer peer;
        private Order order;
        private String consensus;
        private  String name;
        private String signatureType;
    }

    @Getter
    @Setter
    public static class SslConfig{
        private  boolean sslMutual;
        private String sslTrustCertFilePath;
        private String sslCertFilePath;
        private String sslPrivateKeyPath;
    }


    @Getter
    @Setter
    public static class Peer{
        private String hostAndPort;
    }

    @Getter
    @Setter
    public static class Order{
        private String hostAndPort;
    }
}

