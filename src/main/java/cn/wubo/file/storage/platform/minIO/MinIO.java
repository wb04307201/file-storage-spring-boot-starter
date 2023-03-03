package cn.wubo.file.storage.platform.minIO;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * MinIO
 */
@Data
public class MinIO extends BasePlatform {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}