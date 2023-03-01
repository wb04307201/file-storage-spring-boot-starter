package cn.wubo.file.storage.platform.local;

import cn.wubo.file.storage.common.FileUtils;
import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.platform.IFileStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 本地文件存储升级版
 */
@Getter
@Setter
@Slf4j
public class LocalFileStorage implements IFileStorage {

    /* 基础路径 */
    private String basePath;
    /* 本地存储路径*/
    private String storagePath;
    /* 访问域名 */
    private String domain;

    @Override
    public Boolean supportAlias(String alias) {
        return "local".equals(alias);
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        FileInfo fileInfo = new FileInfo();
        String fileName = UUID.randomUUID() + FileUtils.extName(fileWrapper.getOriginalFilename());
        String filePath = basePath + fileWrapper.getPath() + fileName;
        fileWrapper.transferTo(Paths.get(this.storagePath, filePath).toFile());
        return fileInfo;
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            String filePath = basePath + fileInfo.getPath() + fileInfo.getFilename();
            try {
                Files.delete(Paths.get(this.storagePath, filePath));
                return true;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return false;
            }
        } else return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        String filePath = basePath + fileInfo.getPath() + fileInfo.getFilename();
        return Files.exists(Paths.get(this.storagePath, filePath));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        String filePath = basePath + fileInfo.getPath() + fileInfo.getFilename();
        return new MultipartFileStorage(Paths.get(this.storagePath, filePath).toFile());
    }

    @Override
    public void close() {

    }
}
