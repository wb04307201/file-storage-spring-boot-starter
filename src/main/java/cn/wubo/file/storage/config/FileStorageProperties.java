package cn.wubo.file.storage.config;


import cn.wubo.file.storage.platform.aliyunOSS.AliyunOSS;
import cn.wubo.file.storage.platform.amazonS3.AmazonS3;
import cn.wubo.file.storage.platform.baiduBOS.BaiduBOS;
import cn.wubo.file.storage.platform.git.Git;
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
    private String defaultAlias;
    private String defaultPath;
    private Boolean enableWeb = Boolean.TRUE;
    private Boolean enableRest = Boolean.TRUE;
    private String fileStorageRecord = "cn.wubo.file.storage.record.impl.MemFileStroageRecordImpl";
    private String fileNameMapping = "cn.wubo.file.storage.file_name_mapping.impl.RandomFileNameMappingImpl";
    private List<Local> local = new ArrayList<>();
    private List<AmazonS3> amazonS3 = new ArrayList<>();
    private List<MinIO> minIO = new ArrayList<>();
    private List<HuaweiOBS> huaweiOBS = new ArrayList<>();
    private List<BaiduBOS> baiduBOS = new ArrayList<>();
    private List<AliyunOSS> aliyunOSS = new ArrayList<>();
    private List<TencentCOS> tencentCOS = new ArrayList<>();
    private List<WebDAV> webDAV = new ArrayList<>();
    private List<Git> git = new ArrayList<>();
}
