package cn.wubo.file.storage;

import cn.wubo.file.storage.config.FileStorageConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FileStorageConfiguration.class})
public @interface EnableFileStorage {
}
