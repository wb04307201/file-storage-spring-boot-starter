package cn.wubo.file.storage.platform.minIO;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.FileUtils;
import cn.wubo.file.storage.utils.UrlUtils;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Slf4j
public class MinIOFileStorage extends BaseFileStorage {

    private final String accessKey;
    private final String secretKey;
    private final String endPoint;
    private final String bucketName;
    private MinioClient client;

    public MinIOFileStorage(MinIO prop) {
        super(prop.getBasePath(), prop.getAlias(), "MinIO");
        this.accessKey = prop.getAccessKey();
        this.secretKey = prop.getSecretKey();
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    private MinioClient getClient() {
        if (client == null) {
            client = new MinioClient.Builder().credentials(accessKey, secretKey).endpoint(endPoint).build();
        }
        return client;
    }


    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileName = FileUtils.getRandomFileName(fileWrapper.getOriginalFilename());
        String filePath = UrlUtils.join(basePath, fileWrapper.getPath(), fileName);

        try (InputStream is = fileWrapper.getInputStream()) {
            getClient().putObject(PutObjectArgs.builder().bucket(bucketName).object(filePath)
                    .stream(is, fileWrapper.getSize(), -1)
                    .contentType(fileWrapper.getContentType()).build());
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            try {
                getClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(filePath).build());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }

        return new FileInfo(fileName, basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            try {
                getClient().removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName).
                                object(getUrlPath(fileInfo))
                                .build()
                );
            } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                     InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                     IOException e) {
                throw new FileStorageRuntimeException(String.format("删除文件失败,%s", e.getMessage()), e);
            }
        }
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        try {
            StatObjectResponse stat = getClient().statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName).
                            object(getUrlPath(fileInfo))
                            .build()
            );
            return stat != null && stat.lastModified() != null;
        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                 InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException e) {
            throw new FileStorageRuntimeException(String.format("查询文件是否存在失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        try (InputStream is = getClient().getObject(GetObjectArgs.builder().bucket(bucketName).object(getUrlPath(fileInfo)).build())) {
            return new MultipartFileStorage(fileInfo.getOriginalFilename(), is);
        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                 InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException e) {
            throw new FileStorageRuntimeException(String.format("下载文件失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public void close() {
        client = null;
    }
}
