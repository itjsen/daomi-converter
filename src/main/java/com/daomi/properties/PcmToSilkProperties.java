package com.daomi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * pcm转silk的配置获取
 */
@Component
@ConfigurationProperties(prefix = "daomi-converter.pcm-to-silk")
@Data
public class PcmToSilkProperties {

    // 比特率
    private String rate;

    // api采样率
    private String apiSampleRate;

    // 最大间隔采样率
    private String maxIntervalSampleRate;

}
