package cn.wubo.file.storage.config;


import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.platform.local.LocalFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
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
                .map(local -> {
                    LocalFileStorage localFileStorage = new LocalFileStorage();
                    localFileStorage.setBasePath(local.getBasePath());
                    localFileStorage.setDomain(local.getDomain());
                    localFileStorage.setStoragePath(localFileStorage.getStoragePath());
                    return localFileStorage;
                })
                .collect(Collectors.toList());
    }

    @Bean
    public FileStorageService fileStorageService() {
        return new FileStorageService();
    }

}
