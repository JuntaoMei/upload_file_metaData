package juntao.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by juntaomei on 7/17/17.
 */
@Configuration
@ConfigurationProperties("service")
public class ServiceProperties {

    private final String location = "upload-dir";

    public String getLocation() {
        return location;
    }

}
