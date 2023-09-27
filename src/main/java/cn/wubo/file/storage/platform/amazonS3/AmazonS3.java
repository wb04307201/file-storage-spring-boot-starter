package cn.wubo.file.storage.platform.amazonS3;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * MinIO
 */
@Data
public class AmazonS3 extends BasePlatform {
    private String accessKeyId;
    private String secretAccessKey;
    private String region;
    private String endPoint;
    private String bucketName;
}