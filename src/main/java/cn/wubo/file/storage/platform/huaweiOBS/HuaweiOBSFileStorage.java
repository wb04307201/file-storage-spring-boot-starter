package cn.wubo.file.storage.platform.huaweiOBS;

import cn.wubo.file.storage.common.FileUtils;
import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.BaseFileStorage;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
public class HuaweiOBSFileStorage extends BaseFileStorage {

    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
    private ObsClient client;

    public HuaweiOBSFileStorage(HuaweiOBS prop) {
        super(prop.getBasePath(), prop.getDomain(), prop.getAlias(), "minio");
        this.accessKey = prop.getAccessKey();
        this.secretKey = prop.getSecretKey();
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    public ObsClient getClient() {
        if (client == null) {
            client = new ObsClient(accessKey, secretKey, endPoint);
        }
        return client;
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileName = UUID.randomUUID() + FileUtils.extName(fileWrapper.getOriginalFilename());
        String filePath = basePath + fileWrapper.getPath() + fileName;

        ObsClient client = getClient();
        try (InputStream is = fileWrapper.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileWrapper.getSize());
            metadata.setContentType(fileWrapper.getContentType());
            client.putObject(bucketName, filePath, is, metadata);
        } catch (IOException e) {
            try {
                client.deleteObject(bucketName, filePath);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }

        return null;
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return false;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return false;
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        return null;
    }

    @Override
    public void close() {

    }
}
