package cn.wubo.file.storage.platform.local;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * 本地存储
 */
@Data
public class Local extends BasePlatform {
    /**
     * 存储路径，上传的文件都会存储在这个路径下面，默认“/”，注意“/”结尾
     */
    private String storagePath = "/";
}
