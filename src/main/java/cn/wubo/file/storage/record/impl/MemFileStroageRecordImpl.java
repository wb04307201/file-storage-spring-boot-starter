package cn.wubo.file.storage.record.impl;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.record.IFileStroageRecord;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemFileStroageRecordImpl implements IFileStroageRecord {

    private static List<FileInfo> fileInfos = new ArrayList<>();

    @Override
    public FileInfo save(FileInfo fileInfo) {
        if (StringUtils.hasLength(fileInfo.getId())) {
            FileInfo old = findById(fileInfo.getId());
            old = fileInfo;
        } else {
            fileInfo.setId(UUID.randomUUID().toString());
            fileInfos.add(fileInfo);
        }
        return fileInfo;
    }

    @Override
    public List<FileInfo> list(FileInfo fileInfo) {
        return fileInfos.stream().filter(e -> !StringUtils.hasLength(fileInfo.getId()) || fileInfo.getId().equals(e.getId())).filter(e -> !StringUtils.hasLength(fileInfo.getPlatform()) || fileInfo.getPlatform().equals(e.getPlatform())).filter(e -> !StringUtils.hasLength(fileInfo.getAlias()) || fileInfo.getAlias().contains(e.getAlias())).filter(e -> !StringUtils.hasLength(fileInfo.getOriginalFilename()) || fileInfo.getOriginalFilename().contains(e.getOriginalFilename())).collect(Collectors.toList());
    }

    @Override
    public FileInfo findById(String id) {
        Optional<FileInfo> optionalFileInfo = fileInfos.stream().filter(e -> e.getId().equals(id)).findAny();
        if (optionalFileInfo.isPresent()) return optionalFileInfo.get();
        else throw new FileStorageRuntimeException("文件记录未找到!");
    }

    @Override
    public Boolean delete(FileInfo fileInfo) {
        return fileInfos.removeAll(list(fileInfo));
    }

    @Override
    public void init() {}
}
