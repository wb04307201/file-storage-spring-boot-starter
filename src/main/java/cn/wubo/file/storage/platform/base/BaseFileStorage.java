package cn.wubo.file.storage.platform.base;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.platform.IFileStorage;
import cn.wubo.file.storage.utils.UrlUtils;

import java.nio.file.Paths;

public abstract class BaseFileStorage implements IFileStorage {

    /**
     * 基础路径
     */
    protected String basePath;

    /**
     * 平台别名
     */
    protected String alias;

    /**
     * 平台
     */
    protected String platform;

    protected BaseFileStorage(String basePath, String alias, String platform) {
        this.basePath = basePath;
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


    protected String getFilePath(FileInfo fileInfo) {
        return Paths.get(basePath, fileInfo.getPath(), fileInfo.getFilename()).toString();
    }

    protected String getUrlPath(FileInfo fileInfo) {
        return UrlUtils.join(basePath, fileInfo.getPath(), fileInfo.getFilename());
    }
}
