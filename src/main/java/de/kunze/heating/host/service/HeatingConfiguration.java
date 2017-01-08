package de.kunze.heating.host.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "heating")
public class HeatingConfiguration {

    private String relaisids;

}
