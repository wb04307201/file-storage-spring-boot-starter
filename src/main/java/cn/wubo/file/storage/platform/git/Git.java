package cn.wubo.file.storage.platform.git;

import cn.wubo.file.storage.platform.base.BasePlatform;
import lombok.Data;

@Data
public class Git extends BasePlatform {
    private String storagePath;
    private String repo;
    private String username;
    private String password;
    private String branch;
}
