package cn.wubo.file.storage.core;

import cn.wubo.file.storage.platform.IFileStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class FileStorageService {

    @Autowired
    List<IFileStorage> IFileStorageList;

    private IFileStorage findAlias(String alias) {
        Optional<IFileStorage> fileStorageOptional = IFileStorageList.stream()
                .filter(fileStorage -> fileStorage.supportAlias(alias))
                .findAny();
        if (fileStorageOptional.isPresent()) {
            return fileStorageOptional.get();
        } else {
            throw new RuntimeException();
        }
    }

    public FileInfo save(MultipartFileStorage fileWrapper) {
        return findAlias(fileWrapper.getAlais()).save(fileWrapper);
    }

    public Boolean delete(FileInfo fileInfo) {
        return findAlias(fileInfo.getAlais()).delete(fileInfo);
    }

    public Boolean exists(FileInfo fileInfo) {
        return findAlias(fileInfo.getAlais()).exists(fileInfo);
    }

    public MultipartFileStorage download(FileInfo fileInfo) {
        return findAlias(fileInfo.getAlais()).download(fileInfo);
    }
}
