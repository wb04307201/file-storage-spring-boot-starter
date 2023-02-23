package cn.wubo.file.storage.config.local;

import cn.wubo.file.storage.config.BaseStorage;
import lombok.Data;

/**
 * 本地存储
 */
@Data
public class Local extends BaseStorage {
    /**
     * 存储路径，上传的文件都会存储在这个路径下面，默认“/”，注意“/”结尾
     */
    private String storagePath = "/";
    /**
     * 本地存储访问路径
     */
    private String[] pathPatterns = new String[0];
}
