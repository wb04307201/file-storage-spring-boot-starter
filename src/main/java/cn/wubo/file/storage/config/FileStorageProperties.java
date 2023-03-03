package cn.wubo.file.storage.config;


import cn.wubo.file.storage.platform.aliyunOSS.AliyunOSS;
import cn.wubo.file.storage.platform.baiduBOS.BaiduBOS;
import cn.wubo.file.storage.platform.huaweiOBS.HuaweiOBS;
import cn.wubo.file.storage.platform.local.Local;
import cn.wubo.file.storage.platform.minIO.MinIO;
import cn.wubo.file.storage.platform.tencentCOS.TencentCOS;
import cn.wubo.file.storage.platform.webDAV.WebDAV;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

    private List<Local> local = new ArrayList<>();
    private List<MinIO> minIOS = new ArrayList<>();
    private List<HuaweiOBS> huaweiOBS = new ArrayList<>();
    private List<BaiduBOS> baiduBOS = new ArrayList<>();
    private List<AliyunOSS> aliyunOSS = new ArrayList<>();
    private List<TencentCOS> tencentCOS = new ArrayList<>();
    private List<WebDAV> webDAV = new ArrayList<>();

}
