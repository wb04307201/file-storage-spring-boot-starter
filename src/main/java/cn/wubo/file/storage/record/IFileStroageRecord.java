package cn.wubo.file.storage.record;

import cn.wubo.file.storage.core.FileInfo;

import java.util.List;

public interface IFileStroageRecord {

    FileInfo save(FileInfo fileInfo);

    List<FileInfo> list(FileInfo fileInfo);

    FileInfo findById(String id);

    Boolean deleteById(String id);

    void init();
}
