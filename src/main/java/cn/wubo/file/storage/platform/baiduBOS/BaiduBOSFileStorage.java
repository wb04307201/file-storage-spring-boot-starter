package cn.wubo.file.storage.platform.baiduBOS;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.UrlUtils;
import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class BaiduBOSFileStorage extends BaseFileStorage {

    private final String accessKey;
    private final String secretKey;
    private final String endPoint;
    private final String bucketName;
    private BosClient client;

    public BaiduBOSFileStorage(BaiduBOS prop) {
        super(prop.getBasePath(), prop.getAlias(), "BaiduBOS");
        this.accessKey = prop.getAccessKey();
        this.secretKey = prop.getSecretKey();
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    private BosClient getClient() {
        if (client == null) {
            BosClientConfiguration config = new BosClientConfiguration();
            config.setCredentials(new DefaultBceCredentials(accessKey, secretKey));
            config.setEndpoint(endPoint);
            config.setProtocol(Protocol.HTTPS);
            client = new BosClient(config);
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
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return getClient().doesObjectExist(bucketName, getUrlPath(fileInfo));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        BosObject object = getClient().getObject(bucketName, getUrlPath(fileInfo));
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
