package at.yehor.havryliuk.rest.proxy.demo.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "remote")
public interface RemoteConfig {

    @WithName("base.uri")
    String baseUri();

    @WithName("connection.timeout")
    int connectionTimeout();

    @WithName("read.timeout")
    int readTimeout();

}
