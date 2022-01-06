package com.jruchel.stockmonitor.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JWTConfig {

    private String jwtHeaderName;

}
