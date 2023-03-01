package cn.wubo.file.storage.platform.huaweiOBS;

import cn.wubo.file.storage.config.BaseStorage;
import lombok.Data;

/**
 * 华为云 OBS
 */
@Data
public class HuaweiOBS extends BaseStorage {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}