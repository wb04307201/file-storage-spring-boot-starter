package cn.wubo.file.storage.platform.base;

import lombok.Data;

@Data
public class BasePlatform {
    /**
     * 平台别名（后续使用）
     */
    private String alias = "";
    /**
     * 启用存储
     */
    private Boolean enableStorage = false;
    /**
     * 基础路径
     */
    private String basePath = "";
    /**
     * 访问域名
     */
    private String domain = "";

}
