package com.daomi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 本地对象存储的配置获取
 */
@Component
@ConfigurationProperties(prefix = "daomi-converter.localoss")
@Data
public class LocalOssProperties {

    // 服务端的ip或域名
    private String host;
}
