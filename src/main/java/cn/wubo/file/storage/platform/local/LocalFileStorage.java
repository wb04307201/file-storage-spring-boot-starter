package cn.wubo.file.storage.platform.local;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.IORuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 本地文件存储升级版
 */
@Slf4j
public class LocalFileStorage extends BaseFileStorage {

    private final String storagePath;

    public LocalFileStorage(Local prop) {
        super(prop.getBasePath(), prop.getAlias(), "Local");
        this.storagePath = prop.getStoragePath();
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String filePath = Paths.get(basePath, fileWrapper.getPath(), fileWrapper.getName()).toString();
        fileWrapper.transferTo(Paths.get(this.storagePath, filePath).toFile());
        return new FileInfo(fileWrapper.getName(), basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            try {
                Files.delete(Paths.get(this.storagePath, getFilePath(fileInfo)));
            } catch (IOException e) {
                throw new IORuntimeException(e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return Files.exists(Paths.get(this.storagePath, getFilePath(fileInfo)));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        return new MultipartFileStorage(fileInfo.getFilename(), fileInfo.getOriginalFilename(), fileInfo.getContentType(), IoUtils.readBytes(Paths.get(this.storagePath, getFilePath(fileInfo)).toFile()));
    }

    @Override
    public void close() {

    }
}
