package cn.wubo.file.storage.platform;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;

/**
 * 文件存储接口，对应各个平台的别名
 */
public interface IFileStorage {

    /**
     * 支持的平台别名
     */
    Boolean supportAlias(String alias);

    /**
     * 支持的平台类型
     */
    Boolean supportPlatform(String platform);

    /**
     * 平台和别名
     */
    String getPlatformAlias();

    /**
     * 保存文件
     */
    FileInfo save(MultipartFileStorage fileWrapper);


    /**
     * 删除文件
     */
    boolean delete(FileInfo fileInfo);

    /**
     * 文件是否存在
     */
    boolean exists(FileInfo fileInfo);

    /**
     * 下载文件
     */
    MultipartFileStorage download(FileInfo fileInfo);

    /**
     * 释放相关资源
     */
    void close();

    String getFilePath(FileInfo fileInfo);

}
