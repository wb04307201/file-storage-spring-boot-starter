package cn.wubo.file.storage.config;


import cn.wubo.file.storage.config.huaweiOBS.HuaweiOBS;
import cn.wubo.file.storage.config.local.Local;
import cn.wubo.file.storage.config.minIO.MinIO;
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
