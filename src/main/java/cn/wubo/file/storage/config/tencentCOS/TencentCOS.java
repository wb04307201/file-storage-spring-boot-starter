package cn.wubo.file.storage.config.tencentCOS;

import cn.wubo.file.storage.config.BaseStorage;
import lombok.Data;


/**
 * 腾讯云 COS
 */
@Data
public class TencentCOS extends BaseStorage {
    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;
}
