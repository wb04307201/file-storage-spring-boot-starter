package cn.wubo.file.storage.platform;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileWrapper;
import lombok.Data;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 文件存储接口，对应各个平台
 */
@Data
public interface FileStorage {

    /**
     * 支持的平台
     */
    Boolean supportPalform(String platform);

    /**
     * 保存文件
     */
    boolean save(FileInfo fileInfo, MultipartFileWrapper fileWrapper);


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
    void download(FileInfo fileInfo, Consumer<InputStream> consumer);

    /**
     * 释放相关资源
     */
    void close();

}
