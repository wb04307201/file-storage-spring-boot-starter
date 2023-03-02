package cn.wubo.file.storage.platform.minIO;

import cn.wubo.file.storage.common.FileUtils;
import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.BaseFileStorage;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
public class MinIOFileStorage extends BaseFileStorage {

    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
    private MinioClient client;

    public MinIOFileStorage(MinIO prop) {
        super(prop.getBasePath(), prop.getDomain(), prop.getAlias(), "minio");
        this.accessKey = prop.getAccessKey();
        this.secretKey = prop.getSecretKey();
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    public MinioClient getClient() {
        if (client == null) {
            client = new MinioClient.Builder().credentials(accessKey, secretKey).endpoint(endPoint).build();
        }
        return client;
    }


    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileName = UUID.randomUUID() + FileUtils.extName(fileWrapper.getOriginalFilename());
        String filePath = basePath + fileWrapper.getPath() + fileName;
        MinioClient client = getClient();
        try (InputStream is = fileWrapper.getInputStream()) {
            client.putObject(PutObjectArgs.builder().bucket(bucketName).object(filePath)
                    .stream(is, fileWrapper.getSize(), -1)
                    .contentType(fileWrapper.getContentType()).build());
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            try {
                client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(filePath).build());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }

        return new FileInfo(domain + filePath, fileName, basePath, fileWrapper);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        MinioClient client = getClient();
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName).
                            object(getFilePath(fileInfo))
                            .build()
            );
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                 InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException e) {
            throw new FileStorageRuntimeException(String.format("删除文件失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        MinioClient client = getClient();
        try {
            StatObjectResponse stat = client.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName).
                            object(getFilePath(fileInfo))
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
        MinioClient client = getClient();
        try (InputStream is = client.getObject(GetObjectArgs.builder().bucket(bucketName).object(getFilePath(fileInfo)).build())) {
            return new MultipartFileStorage(fileInfo.getFilename(), is);
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
