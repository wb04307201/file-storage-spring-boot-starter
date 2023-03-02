package cn.wubo.file.storage.config;


import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.platform.BaseStorage;
import cn.wubo.file.storage.platform.local.LocalFileStorage;
import cn.wubo.file.storage.platform.minIO.MinIOFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties({FileStorageProperties.class})
public class FileStorageConfiguration {

    @Autowired
    private FileStorageProperties properties;


    /**
     * 本地存储
     */
    @Bean
    public List<LocalFileStorage> localFileStorageList() {
        return properties.getLocals().stream()
                .filter(BaseStorage::getEnableStorage)
                .map(LocalFileStorage::new)
                .collect(Collectors.toList());
    }

    /**
     * MinIO 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "io.minio.MinioClient")
    public List<MinIOFileStorage> minioFileStorageList() {
        return properties.getMinIOS().stream()
                .filter(BaseStorage::getEnableStorage)
                .map(MinIOFileStorage::new)
                .collect(Collectors.toList());
    }

    @Bean
    public FileStorageService fileStorageService() {
        return new FileStorageService();
    }

}
