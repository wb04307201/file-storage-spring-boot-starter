package cn.wubo.file.storage.platform.aliyunOSS;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * 阿里云 OSS
 */
@Data
public class AliyunOSS extends BasePlatform {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}
