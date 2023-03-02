package cn.wubo.file.storage.platform.minIO;

import cn.wubo.file.storage.platform.BaseStorage;
import lombok.Data;

/**
 * MinIO
 */
@Data
public class MinIO extends BaseStorage {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}