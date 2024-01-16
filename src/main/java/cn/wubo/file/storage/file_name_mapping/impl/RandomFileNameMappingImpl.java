package cn.wubo.file.storage.file_name_mapping.impl;

import cn.wubo.file.storage.file_name_mapping.IFileNameMapping;
import cn.wubo.file.storage.utils.FileUtils;

import java.util.UUID;

import static cn.wubo.file.storage.utils.FileUtils.DOT;

public class RandomFileNameMappingImpl implements IFileNameMapping {
    @Override
    public String mapping(String originalFilename) {
        return UUID.randomUUID() + DOT + FileUtils.extName(originalFilename);
    }
}
