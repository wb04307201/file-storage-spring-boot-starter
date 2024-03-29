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
    private Boolean enableStorage = true;
    /**
     * 基础路径
     */
    private String basePath = "";

}
