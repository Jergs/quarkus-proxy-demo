package at.yehor.havryliuk.rest.proxy.demo.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "quarkus.http")
public interface HttpConfig {

    @WithName("host")
    String host();

    @WithName("port")
    int port();

    @WithName("ssl-port")
    int sslPort();
}
