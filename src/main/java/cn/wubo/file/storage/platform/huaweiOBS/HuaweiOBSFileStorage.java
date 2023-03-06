package cn.wubo.file.storage.platform.huaweiOBS;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.FileUtils;
import cn.wubo.file.storage.utils.IoUtils;
import cn.wubo.file.storage.utils.PathUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class HuaweiOBSFileStorage extends BaseFileStorage {

    private final String accessKey;
    private final String secretKey;
    private final String endPoint;
    private final String bucketName;
    private ObsClient client;

    public HuaweiOBSFileStorage(HuaweiOBS prop) {
        super(prop.getBasePath(), prop.getAlias(), "HuaweiOBS");
        this.accessKey = prop.getAccessKey();
        this.secretKey = prop.getSecretKey();
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    private ObsClient getClient() {
        if (client == null) {
            client = new ObsClient(accessKey, secretKey, endPoint);
        }
        return client;
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileName = FileUtils.getRandomFileName(fileWrapper.getOriginalFilename());
        String filePath = PathUtils.join(basePath, fileWrapper.getPath(), fileName);

        try (InputStream is = fileWrapper.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileWrapper.getSize());
            metadata.setContentType(fileWrapper.getContentType());
            getClient().putObject(bucketName, filePath, is, metadata);
        } catch (IOException e) {
            try {
                getClient().deleteObject(bucketName, filePath);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }

        return new FileInfo(fileName, basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) getClient().deleteObject(bucketName, getUrlPath(fileInfo));
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return getClient().doesObjectExist(bucketName, getUrlPath(fileInfo));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        ObsObject object = getClient().getObject(bucketName, getUrlPath(fileInfo));
        try (InputStream is = object.getObjectContent()) {
            return new MultipartFileStorage(fileInfo.getOriginalFilename(), is);
        } catch (IOException e) {
            throw new FileStorageRuntimeException(String.format("下载文件失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public void close() {
        IoUtils.close(client);
    }
}
