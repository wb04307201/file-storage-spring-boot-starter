package cn.wubo.file.storage.platform.aliyunOSS;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.UrlUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


public class AliyunOSSFileStorage extends BaseFileStorage {
    private final String accessKey;
    private final String secretKey;
    private final String endPoint;
    private final String bucketName;
    private OSS client;

    public AliyunOSSFileStorage(AliyunOSS prop) {
        super(prop.getBasePath(), prop.getAlias(), "AliyunOSS");
        this.accessKey = prop.getAccessKey();
        this.secretKey = prop.getSecretKey();
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    private OSS getClient() {
        if (client == null) {
            client = new OSSClientBuilder().build(endPoint, accessKey, secretKey);
        }
        return client;
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String filePath = UrlUtils.join(basePath, fileWrapper.getPath(), fileWrapper.getName());

        try (InputStream is = fileWrapper.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileWrapper.getSize());
            metadata.setContentType(fileWrapper.getContentType());
            getClient().putObject(bucketName, filePath, is, metadata);
        } catch (IOException e) {
            getClient().deleteObject(bucketName, filePath);
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }

        return new FileInfo(fileWrapper.getName(), basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) getClient().deleteObject(bucketName, getUrlPath(fileInfo));
        return false;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return getClient().doesObjectExist(bucketName, getUrlPath(fileInfo));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        OSSObject object = getClient().getObject(bucketName, getUrlPath(fileInfo));
        try (InputStream is = object.getObjectContent()) {
            return new MultipartFileStorage(fileInfo.getFilename(), fileInfo.getOriginalFilename(), fileInfo.getContentType(), is);
        } catch (IOException e) {
            throw new FileStorageRuntimeException(String.format("下载文件失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }
}
