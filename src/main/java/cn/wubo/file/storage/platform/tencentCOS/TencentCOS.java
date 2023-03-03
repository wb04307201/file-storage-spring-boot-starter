package cn.wubo.file.storage.platform.tencentCOS;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;


/**
 * 腾讯云 COS
 */
@Data
public class TencentCOS extends BasePlatform {
    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;
}
