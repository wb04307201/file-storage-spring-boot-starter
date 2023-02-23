package cn.wubo.file.storage.platform.local;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileWrapper;
import cn.wubo.file.storage.platform.FileStorage;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 本地文件存储升级版
 */
@Getter
@Setter
public class LocalFileStorage implements FileStorage {

    /* 基础路径 */
    private String basePath;
    /* 本地存储路径*/
    private String storagePath;
    /* 访问域名 */
    private String domain;

    @Override
    public Boolean supportPalform(String platform) {
        return null;
    }

    @Override
    public boolean save(FileInfo fileInfo, MultipartFileWrapper fileWrapper) {
        return false;
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return false;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return false;
    }

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {

    }

    @Override
    public void close() {

    }
}
