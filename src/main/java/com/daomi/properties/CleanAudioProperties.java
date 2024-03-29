package com.daomi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 清理源音频、pcm、silk的配置获取
 */
@Component
@ConfigurationProperties(prefix = "daomi-converter.clean-audio")
@Data
public class CleanAudioProperties {

    // silk文件的存活时长
    private Long silkMaxAge;

}
