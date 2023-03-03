package cn.wubo.file.storage.platform.baiduBOS;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * 百度云 BOS
 */
@Data
public class BaiduBOS extends BasePlatform {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}
