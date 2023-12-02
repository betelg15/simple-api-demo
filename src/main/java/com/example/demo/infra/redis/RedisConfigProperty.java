package com.example.demo.infra.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnProperty(prefix = "redis", name = {"host-name"})
@ConfigurationProperties(prefix = "redis")
@AllArgsConstructor
@Data
@Getter
@Setter
public class RedisConfigProperty {
    private String hostName;
    private int port;
}
