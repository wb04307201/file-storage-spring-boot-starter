package cn.wubo.file.storage.core;

import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.IFileStorage;
import cn.wubo.file.storage.record.IFileStroageRecord;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class FileStorageService implements DisposableBean {

    CopyOnWriteArrayList<IFileStorage> fileStorageList;

    @Getter
    IFileStroageRecord fileStroageRecord;

    public FileStorageService(List<IFileStorage> fileStorageList, IFileStroageRecord fileStroageRecord) {
        this.fileStorageList = new CopyOnWriteArrayList<>(fileStorageList);
        this.fileStroageRecord = fileStroageRecord;
    }

    private IFileStorage getFileStorage(String alias) {
        Optional<IFileStorage> fileStorageOptional = fileStorageList.stream().filter(fileStorage -> fileStorage.supportAlias(alias)).findAny();
        if (fileStorageOptional.isPresent()) {
            IFileStorage fileStorage = fileStorageOptional.get();
            log.debug("找到存储 {} 成功", fileStorage.getPlatformAlias());
            return fileStorage;
        } else {
            throw new FileStorageRuntimeException(String.format("未找到别名【%s】对应的平台配置", alias));
        }
    }

    public FileInfo save(MultipartFileStorage fileWrapper) {
        FileInfo fileInfo = getFileStorage(fileWrapper.getAlias()).save(fileWrapper);
        return fileStroageRecord.save(fileInfo);
    }

    public List<FileInfo> list(FileInfo fileInfo) {
        return fileStroageRecord.list(fileInfo);
    }

    public Boolean delete(String id) {
        FileInfo fileInfo = fileStroageRecord.findById(id);
        return getFileStorage(fileInfo.getAlias()).delete(fileInfo) && fileStroageRecord.delete(fileInfo);
    }

    public Boolean exists(String id) {
        FileInfo fileInfo = fileStroageRecord.findById(id);
        return getFileStorage(fileInfo.getAlias()).exists(fileInfo);
    }

    public MultipartFileStorage download(String id) {
        FileInfo fileInfo = fileStroageRecord.findById(id);
        return getFileStorage(fileInfo.getAlias()).download(fileInfo);
    }

    @Override
    public void destroy() throws Exception {
        for (IFileStorage fileStorage : fileStorageList) {
            try {
                fileStorage.close();
                log.debug("销毁存储 {} 成功", fileStorage.getPlatformAlias());
            } catch (Exception e) {
                log.error("销毁存储 {} 失败，{}", fileStorage.getPlatformAlias(), e.getMessage(), e);
            }
        }
    }
}
