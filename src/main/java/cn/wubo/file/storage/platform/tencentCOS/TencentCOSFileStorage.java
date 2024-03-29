package cn.wubo.file.storage.platform.tencentCOS;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.UrlUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class TencentCOSFileStorage extends BaseFileStorage {

    private final String secretId;
    private final String secretKey;
    private final String region;
    private final String bucketName;
    private COSClient client;

    public TencentCOSFileStorage(TencentCOS prop) {
        super(prop.getBasePath(), prop.getAlias(), "TencentCOS");
        this.secretId = prop.getSecretId();
        this.secretKey = prop.getSecretKey();
        this.region = prop.getRegion();
        this.bucketName = prop.getBucketName();
    }

    private COSClient getClient() {
        if (client == null) {
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            client = new COSClient(cred, clientConfig);
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
        COSObject object = getClient().getObject(bucketName, getUrlPath(fileInfo));
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
