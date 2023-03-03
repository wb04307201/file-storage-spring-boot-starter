package cn.wubo.file.storage.platform.huaweiOBS;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * 华为云 OBS
 */
@Data
public class HuaweiOBS extends BasePlatform {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}