package cn.wubo.file.storage.record.impl;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.record.IStroageRecord;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemStroageRecordImpl implements IStroageRecord {

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
        return fileInfos.stream()
                .filter(e -> !StringUtils.hasLength(fileInfo.getAlias()) || fileInfo.getAlias().equals(e.getAlias()))
                .collect(Collectors.toList());
    }

    @Override
    public FileInfo findById(String id) {
        Optional<FileInfo> optionalFileInfo = fileInfos.stream()
                .filter(e -> e.getId().equals(id))
                .findAny();
        if (optionalFileInfo.isPresent()) return optionalFileInfo.get();
        else throw new RuntimeException("not found!");
    }

    @Override
    public Boolean deleteById(String id) {
        FileInfo old = findById(id);
        return fileInfos.remove(old);
    }

    @Override
    public void init() {

    }
}
