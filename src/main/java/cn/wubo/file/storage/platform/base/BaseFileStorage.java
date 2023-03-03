package cn.wubo.file.storage.platform.base;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.platform.IFileStorage;
import cn.wubo.file.storage.utils.PathUtils;

public abstract class BaseFileStorage implements IFileStorage {

    /**
     * 基础路径
     */
    protected String basePath;

    /**
     * 访问域名
     */
    protected String domain;

    /**
     * 平台别名
     */
    protected String alias;

    /**
     * 平台
     */
    protected String platform;

    protected BaseFileStorage(String basePath, String domain, String alias, String platform) {
        this.basePath = basePath;
        this.domain = domain;
        this.alias = alias;
        this.platform = platform;
    }

    @Override
    public Boolean supportAlias(String alias) {
        return this.alias.equals(alias);
    }


    @Override
    public Boolean supportPlatform(String platform) {
        return this.platform.equals(platform);
    }


    @Override
    public String getPlatformAlias() {
        return String.format("平台【%s】别名【%s】", platform, alias);
    }


    @Override
    public String getFilePath(FileInfo fileInfo){
        return basePath + fileInfo.getPath() + fileInfo.getFilename();
    }
}
