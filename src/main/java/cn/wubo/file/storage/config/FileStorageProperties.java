package cn.wubo.file.storage.config;


import cn.wubo.file.storage.platform.huaweiOBS.HuaweiOBS;
import cn.wubo.file.storage.platform.local.Local;
import cn.wubo.file.storage.platform.minIO.MinIO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

    private List<Local> locals = new ArrayList<>();
    private List<HuaweiOBS> huaweiOBS = new ArrayList<>();
    private List<MinIO> minIOS = new ArrayList<>();

}
