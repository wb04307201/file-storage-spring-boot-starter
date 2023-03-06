package cn.wubo.file.storage.platform.local;

import cn.wubo.file.storage.utils.FileUtils;
import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.IORuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

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
        String fileName = FileUtils.getRandomFileName(fileWrapper.getOriginalFilename());
        String filePath = Paths.get(basePath, fileWrapper.getPath(), fileName).toString();
        fileWrapper.transferTo(Paths.get(this.storagePath, filePath).toFile());
        return new FileInfo(fileName, basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            try {
                Files.delete(Paths.get(this.storagePath, getFilePath(fileInfo)));
                return true;
            } catch (IOException e) {
                throw new IORuntimeException(e.getMessage(), e);
            }
        } else return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return Files.exists(Paths.get(this.storagePath, getFilePath(fileInfo)));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        return new MultipartFileStorage(Paths.get(this.storagePath, getFilePath(fileInfo)).toFile());
    }

    @Override
    public void close() {

    }
}
