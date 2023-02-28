package cn.wubo.file.storage.config.baiduBOS;

import lombok.Data;

/**
 * 百度云 BOS
 */
@Data
public class BaiduBOS {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}
