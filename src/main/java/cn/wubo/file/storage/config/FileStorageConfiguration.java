package cn.wubo.file.storage.config;


import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.file_name_mapping.IFileNameMapping;
import cn.wubo.file.storage.file_name_mapping.impl.RandomFileNameMappingImpl;
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
import cn.wubo.file.storage.result.Result;
import cn.wubo.file.storage.utils.IoUtils;
import cn.wubo.file.storage.utils.PageUtils;
import jakarta.servlet.http.Part;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    @Bean
    public IFileNameMapping fileNameMapping() {
        return new RandomFileNameMappingImpl();
    }

    /**
     * 本地存储
     */
    @Bean
    public List<LocalFileStorage> localFileStorageList() {
        return properties.getLocal().stream().filter(BasePlatform::getEnableStorage).map(LocalFileStorage::new).toList();
    }

    /**
     * AmazonS3 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "software.amazon.awssdk.services.s3.S3Client")
    public List<AmazonS3FileStorage> amazonS3FileStorageList() {
        return properties.getAmazonS3().stream().filter(BasePlatform::getEnableStorage).map(AmazonS3FileStorage::new).toList();
    }

    /**
     * MinIO 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "io.minio.MinioClient")
    public List<MinIOFileStorage> minioFileStorageList() {
        return properties.getMinIO().stream().filter(BasePlatform::getEnableStorage).map(MinIOFileStorage::new).toList();
    }

    /**
     * 华为云 OBS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.obs.services.ObsClient")
    public List<HuaweiOBSFileStorage> huaweiObsFileStorageList() {
        return properties.getHuaweiOBS().stream().filter(BasePlatform::getEnableStorage).map(HuaweiOBSFileStorage::new).toList();
    }

    /**
     * 百度云 BOS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.baidubce.services.bos.BosClient")
    public List<BaiduBOSFileStorage> baiduBosFileStorageList() {
        return properties.getBaiduBOS().stream().filter(BasePlatform::getEnableStorage).map(BaiduBOSFileStorage::new).toList();
    }

    /**
     * 阿里云 OSS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.aliyun.oss.OSS")
    public List<AliyunOSSFileStorage> aliyunOssFileStorageList() {
        return properties.getAliyunOSS().stream().filter(BasePlatform::getEnableStorage).map(AliyunOSSFileStorage::new).toList();
    }

    /**
     * 腾讯云 COS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.qcloud.cos.COSClient")
    public List<TencentCOSFileStorage> tencentCosFileStorageList() {
        return properties.getTencentCOS().stream().filter(BasePlatform::getEnableStorage).map(TencentCOSFileStorage::new).toList();
    }

    /**
     * WebDAV 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.github.sardine.Sardine")
    public List<WebDAVFileStorage> webDavFileStorageList() {
        return properties.getWebDAV().stream().filter(BasePlatform::getEnableStorage).map(WebDAVFileStorage::new).toList();
    }

    /**
     * Git 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "org.eclipse.jgit.api.Git")
    public List<GitFileStorage> gitFileStorageList() {
        return properties.getGit().stream().filter(BasePlatform::getEnableStorage).map(GitFileStorage::new).toList();
    }

    @Bean
    public FileStorageService fileStorageService(List<List<? extends IFileStorage>> fileStorageLists, List<IFileStroageRecord> fileStroageRecordList, List<IFileNameMapping> fileNameMappingList) {
        IFileStroageRecord fileStroageRecord = fileStroageRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getFileStorageRecord())).findAny().orElseThrow(() -> new FileStorageRuntimeException(String.format("未找到%s对应的bean，无法加载IFileStroageRecord！", properties.getFileStorageRecord())));
        fileStroageRecord.init();
        IFileNameMapping fileNameMapping = fileNameMappingList.stream().filter(obj -> obj.getClass().getName().equals(properties.getFileNameMapping())).findAny().orElseThrow(() -> new FileStorageRuntimeException(String.format("未找到%s对应的bean，无法加载IFileNameMapping！", properties.getFileNameMapping())));
        return new FileStorageService(new CopyOnWriteArrayList<>(fileStorageLists.stream().flatMap(Collection::stream).toList()), fileStroageRecord, fileNameMapping);
    }

    /**
     * 配置文件存储的路由功能。
     *
     * @param fileStorageService 文件存储服务，用于处理文件的存储和检索操作。
     * @return RouterFunction 对象，用于路由文件存储相关的HTTP请求。
     */
    @Bean("wb04307201FileStorageRouter")
    public RouterFunction<ServerResponse> fileStorageRouter(FileStorageService fileStorageService) {
        // 创建路由函数构建器
        RouterFunctions.Builder builder = RouterFunctions.route();

        // 当Web和REST接口都启用时，配置HTML页面的路由
        if (properties.getEnableWeb() && properties.getEnableRest()) {
            builder.GET("/file/storage/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> {
                // 构建包含上下文路径的数据Map
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.requestPath().contextPath().value());
                // 返回渲染后的HTML页面
                return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(PageUtils.write("list.ftl", data));
            });
        }

        // 配置REST接口的路由
        if (properties.getEnableRest()) {
            // 配置列出文件的路由
            builder.POST("/file/storage/list", request -> {
                FileInfo fileInfo = request.body(FileInfo.class);
                // 返回文件列表
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(fileStorageService.list(fileInfo)));
            });

            // 配置上传文件的路由
            builder.POST("/file/storage/upload", request -> {
                Part part = request.multipartData().getFirst("file");
                // 校验配置项defaultAlias和defaultPath是否为空，为空则抛出异常
                if (!StringUtils.hasText(properties.getDefaultAlias()))
                    throw new FileStorageRuntimeException("请配置defaultAlias属性!");
                if (!StringUtils.hasText(properties.getDefaultPath()))
                    throw new FileStorageRuntimeException("请配置defaultPath属性!");
                // 保存文件并返回保存结果
                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                Result.success(
                                        fileStorageService.save(
                                                new MultipartFileStorage(part.getSubmittedFileName(), null, null, part.getInputStream())
                                                .setAlias(properties.getDefaultAlias())
                                                .setPath(properties.getDefaultPath())
                                        )
                                )
                        );
            });

            // 配置删除文件的路由
            builder.GET("/file/storage/delete", request -> {
                String id = request.param("id").orElseThrow(() -> new IllegalArgumentException("请求参数id不能为空"));
                // 删除文件并返回结果
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(fileStorageService.delete(id)));
            });

            // 配置下载文件的路由
            builder.GET("/file/storage/download", request -> {
                String id = request.param("id").orElseThrow(() -> new IllegalArgumentException("请求参数id不能为空"));
                MultipartFileStorage file = fileStorageService.download(id);
                // 构建文件下载响应
                return ServerResponse.ok().contentType(MediaType.parseMediaType(file.getContentType())).contentLength(file.getSize()).header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(file.getOriginalFilename()).getBytes(), StandardCharsets.ISO_8859_1)).build((req, res) -> {
                    try (OutputStream os = res.getOutputStream()) {
                        IoUtils.writeToStream(file.getBytes(), os);
                    } catch (IOException e) {
                        throw new FileStorageRuntimeException(e.getMessage(), e);
                    }
                    return null;
                });
            });
        }

        // 构建并返回最终的路由函数
        return builder.build();
    }
}
