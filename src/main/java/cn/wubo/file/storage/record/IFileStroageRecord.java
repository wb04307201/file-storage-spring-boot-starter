package cn.wubo.file.storage.record;

import cn.wubo.file.storage.core.FileInfo;

import java.util.List;

public interface IFileStroageRecord {

    /**
     * 新增或修改存储文件记录
     */
    FileInfo save(FileInfo fileInfo);

    /**
     * 根据入参查询存储文件记录
     */
    List<FileInfo> list(FileInfo fileInfo);

    /**
     * 根据id查询存储文件记录
     */
    FileInfo findById(String id);

    /**
     * 根据入参查询后删除存储文件记录
     */
    Boolean delete(FileInfo fileInfo);

    void init();
}
