package cn.wubo.file.storage.config.aliyunOSS;

import cn.wubo.file.storage.config.BaseStorage;
import lombok.Data;

/**
 * 阿里云 OSS
 */
@Data
public class AliyunOSS extends BaseStorage {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}
