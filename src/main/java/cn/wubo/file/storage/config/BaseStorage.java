package cn.wubo.file.storage.config;

import lombok.Data;

@Data
public class BaseStorage {
    /**
     * 存储平台
     */
    private String platform = "";
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
