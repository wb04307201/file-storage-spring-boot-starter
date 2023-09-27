package cn.wubo.file.storage.platform.amazonS3;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * MinIO
 */
@Data
public class AmazonS3 extends BasePlatform {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}