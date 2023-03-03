package cn.wubo.file.storage.platform.webDAV;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

/**
 * WebDAV
 */
@Data
public class WebDAV extends BasePlatform {
    /**
     * 服务器地址，注意“/”结尾，例如：http://192.168.1.105:8405/
     */
    private String server;
    /**
     * 用户名
     */
    private String user;
    /**
     * 密码
     */
    private String password;
    /**
     * 存储路径，上传的文件都会存储在这个路径下面，默认“/”，注意“/”结尾
     */
    private String storagePath = "/";
}
