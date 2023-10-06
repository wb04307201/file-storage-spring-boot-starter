package cn.wubo.file.storage.config;


import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.IFileStorage;
import cn.wubo.file.storage.platform.aliyunOSS.AliyunOSSFileStorage;
import cn.wubo.file.storage.platform.amazonS3.AmazonS3FileStorage;
import cn.wubo.file.storage.platform.baiduBOS.BaiduBOSFileStorage;
import cn.wubo.file.storage.platform.base.BasePlatform;
import cn.wubo.file.storage.platform.git.GitFileStorage;
import cn.wubo.file.storage.platform.huaweiOBS.HuaweiOBSFileStorage;
import cn.wubo.file.storage.platform.local.LocalFileStorage;
import cn.wubo.file.storage.platform.minIO.MinIOFileStorage;
import cn.wubo.file.storage.platform.tencentCOS.TencentCOSFileStorage;
import cn.wubo.file.storage.platform.webDAV.WebDAVFileStorage;
import cn.wubo.file.storage.record.IFileStroageRecord;
import cn.wubo.file.storage.record.impl.MemFileStroageRecordImpl;
import cn.wubo.file.storage.servlet.FileStorageDeleteServlet;
import cn.wubo.file.storage.servlet.FileStorageDownloadServlet;
import cn.wubo.file.storage.servlet.FileStorageListServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties({FileStorageProperties.class})
public class FileStorageConfiguration {

    private FileStorageProperties properties;

    public FileStorageConfiguration(FileStorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    public IFileStroageRecord fileStroageRecord() {
        return new MemFileStroageRecordImpl();
    }

    /**
     * 本地存储
     */
    @Bean
    public List<LocalFileStorage> localFileStorageList() {
        return properties.getLocal().stream().filter(BasePlatform::getEnableStorage).map(LocalFileStorage::new).collect(Collectors.toList());
    }

    /**
     * AmazonS3 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "software.amazon.awssdk.services.s3.S3Client")
    public List<AmazonS3FileStorage> amazonS3FileStorageList() {
        return properties.getAmazonS3().stream().filter(BasePlatform::getEnableStorage).map(AmazonS3FileStorage::new).collect(Collectors.toList());
    }

    /**
     * MinIO 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "io.minio.MinioClient")
    public List<MinIOFileStorage> minioFileStorageList() {
        return properties.getMinIO().stream().filter(BasePlatform::getEnableStorage).map(MinIOFileStorage::new).collect(Collectors.toList());
    }

    /**
     * 华为云 OBS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.obs.services.ObsClient")
    public List<HuaweiOBSFileStorage> huaweiObsFileStorageList() {
        return properties.getHuaweiOBS().stream().filter(BasePlatform::getEnableStorage).map(HuaweiOBSFileStorage::new).collect(Collectors.toList());
    }

    /**
     * 百度云 BOS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.baidubce.services.bos.BosClient")
    public List<BaiduBOSFileStorage> baiduBosFileStorageList() {
        return properties.getBaiduBOS().stream().filter(BasePlatform::getEnableStorage).map(BaiduBOSFileStorage::new).collect(Collectors.toList());
    }

    /**
     * 阿里云 OSS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.aliyun.oss.OSS")
    public List<AliyunOSSFileStorage> aliyunOssFileStorageList() {
        return properties.getAliyunOSS().stream().filter(BasePlatform::getEnableStorage).map(AliyunOSSFileStorage::new).collect(Collectors.toList());
    }

    /**
     * 腾讯云 COS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.qcloud.cos.COSClient")
    public List<TencentCOSFileStorage> tencentCosFileStorageList() {
        return properties.getTencentCOS().stream().filter(BasePlatform::getEnableStorage).map(TencentCOSFileStorage::new).collect(Collectors.toList());
    }

    /**
     * WebDAV 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.github.sardine.Sardine")
    public List<WebDAVFileStorage> webDavFileStorageList() {
        return properties.getWebDAV().stream().filter(BasePlatform::getEnableStorage).map(WebDAVFileStorage::new).collect(Collectors.toList());
    }

    /**
     * Git 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "org.eclipse.jgit.api.Git")
    public List<GitFileStorage> gitFileStorageList() {
        return properties.getGit().stream().filter(BasePlatform::getEnableStorage).map(GitFileStorage::new).collect(Collectors.toList());
    }

    @Bean
    public FileStorageService fileStorageService(List<List<? extends IFileStorage>> fileStorageLists, List<IFileStroageRecord> fileStroageRecordList) {
        IFileStroageRecord fileStroageRecord = fileStroageRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getFileStorageRecord())).findAny().orElseThrow(() -> new FileStorageRuntimeException(String.format("未找到%s对应的bean，无法加载IFileStroageRecord！", properties.getFileStorageRecord())));
        fileStroageRecord.init();
        return new FileStorageService(new CopyOnWriteArrayList<>(fileStorageLists.stream().flatMap(Collection::stream).collect(Collectors.toList())), fileStroageRecord);
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> fileStorageListServlet(FileStorageService fileStorageService) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileStorageListServlet(fileStorageService));
        registration.addUrlMappings("/file/storage/list");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> fileStorageDeleteServlet(FileStorageService fileStorageService) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileStorageDeleteServlet(fileStorageService));
        registration.addUrlMappings("/file/storage/delete");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> fileStorageDownloadServlet(FileStorageService fileStorageService) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileStorageDownloadServlet(fileStorageService));
        registration.addUrlMappings("/file/storage/download");
        return registration;
    }

}
