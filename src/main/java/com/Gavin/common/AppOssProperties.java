package com.Gavin.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: Gavin
 * @description:
 * @className: AppOssProperties
 * @date: 2022/6/22 15:04
 * @version:0.1
 * @since: jdk14.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "app-oss")
public class AppOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String returnServerPath;
}