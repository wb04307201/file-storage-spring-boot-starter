package cn.wubo.file.storage.config;


import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.core.MultipartFileStorage;
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
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.*;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
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

    @Bean("wb04307201_file_storage_router")
    public RouterFunction<ServerResponse> fileStorageRouter(FileStorageService fileStorageService) {
        BiFunction<ServerRequest, FileStorageService, ServerResponse> listFunction = (request, service) -> {
            String contextPath = request.requestPath().contextPath().value();
            Map<String, Object> data = new HashMap<>();
            FileInfo fileInfo = new FileInfo();
            try {
                if (HttpMethod.POST.equals(request.method())) {
                    MultiValueMap<String, String> params = request.params();
                    fileInfo.setPlatform(params.getFirst("platform"));
                    fileInfo.setAlias(params.getFirst("alias"));
                    fileInfo.setOriginalFilename(params.getFirst("originalFilename"));
                }
                data.put("list", service.list(fileInfo));
                data.put("contextPath", contextPath);
                fileInfo.setAlias(HtmlUtils.htmlEscape(fileInfo.getAlias() == null ? "" : fileInfo.getAlias()));
                fileInfo.setOriginalFilename(HtmlUtils.htmlEscape(fileInfo.getOriginalFilename() == null ? "" : fileInfo.getOriginalFilename()));
                data.put("query", fileInfo);
                StringWriter sw = new StringWriter();
                freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
                cfg.setClassForTemplateLoading(this.getClass(), "/template");
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, sw);
                return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(sw.toString());
            } catch (IOException | TemplateException e) {
                throw new FileStorageRuntimeException(e.getMessage(), e);
            }
        };

        return RouterFunctions.route().GET("/file/storage/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> listFunction.apply(request, fileStorageService)).POST("/file/storage/list", RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED), request -> listFunction.apply(request, fileStorageService)).GET("/file/storage/delete", RequestPredicates.accept(MediaType.TEXT_HTML), request -> {
            Optional<String> optionalId = request.param("id");
            if (optionalId.isPresent()) fileStorageService.delete(optionalId.get());
            else throw new IllegalArgumentException("请求参数id不能为空");
            return listFunction.apply(request, fileStorageService);
        }).GET("/file/storage/download", RequestPredicates.accept(MediaType.TEXT_HTML), request -> {
            Optional<String> optionalId = request.param("id");
            if (optionalId.isPresent()) {
                MultipartFileStorage file = fileStorageService.download(optionalId.get());
                try (InputStream is = new ByteArrayInputStream(file.getBytes())) {
                    return ServerResponse.ok().contentType(MediaType.parseMediaType(file.getContentType())).contentLength(file.getSize()).header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(file.getOriginalFilename()).getBytes(), StandardCharsets.ISO_8859_1)).body(is);
                }
            } else throw new IllegalArgumentException("请求参数id不能为空");
        }).build();
    }
}
