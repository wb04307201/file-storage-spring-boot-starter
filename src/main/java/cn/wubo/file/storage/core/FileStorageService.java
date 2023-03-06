package cn.wubo.file.storage.core;

import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.IFileStorage;
import cn.wubo.file.storage.record.IFileStroageRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class FileStorageService implements DisposableBean {

    CopyOnWriteArrayList<IFileStorage> fileStorageList;

    IFileStroageRecord fileStroageRecord;


    public FileStorageService(CopyOnWriteArrayList<IFileStorage> fileStorageList,IFileStroageRecord fileStroageRecord) {
        this.fileStorageList = fileStorageList;
        this.fileStroageRecord = fileStroageRecord;
    }

    private IFileStorage getFileStorage(String alias) {
        Optional<IFileStorage> fileStorageOptional = fileStorageList.stream()
                .filter(fileStorage -> fileStorage.supportAlias(alias))
                .findAny();
        if (fileStorageOptional.isPresent()) {
            IFileStorage fileStorage = fileStorageOptional.get();
            log.debug("找到存储 {} 成功", fileStorage.getPlatformAlias());
            return fileStorage;
        } else {
            throw new FileStorageRuntimeException(String.format("未找到别名【%s】对应的平台配置",alias));
        }
    }

    public FileInfo save(MultipartFileStorage fileWrapper) {
        return getFileStorage(fileWrapper.getAlias()).save(fileWrapper);
    }

    public Boolean delete(FileInfo fileInfo) {
        return getFileStorage(fileInfo.getAlias()).delete(fileInfo);
    }

    public Boolean exists(FileInfo fileInfo) {
        return getFileStorage(fileInfo.getAlias()).exists(fileInfo);
    }

    public MultipartFileStorage download(FileInfo fileInfo) {
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
