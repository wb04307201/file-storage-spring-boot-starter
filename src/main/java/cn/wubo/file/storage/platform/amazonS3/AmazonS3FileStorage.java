package cn.wubo.file.storage.platform.amazonS3;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.FileUtils;
import cn.wubo.file.storage.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.Date;

@Slf4j
public class AmazonS3FileStorage extends BaseFileStorage {

    private final String accessKeyId;
    private final String secretAccessKey;
    private final Region region;
    private final String endPoint;
    private final String bucketName;
    private S3Client client;

    public AmazonS3FileStorage(AmazonS3 prop) {
        super(prop.getBasePath(), prop.getAlias(), "AmazonS3");
        this.accessKeyId = prop.getAccessKeyId();
        this.secretAccessKey = prop.getSecretAccessKey();
        this.region = Region.of(prop.getRegion());
        this.endPoint = prop.getEndPoint();
        this.bucketName = prop.getBucketName();
    }

    private S3Client getClient() {
        if (client == null) {
            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            S3Configuration s3Config = S3Configuration.builder().pathStyleAccessEnabled(true).build();
            S3ClientBuilder s3ClientBuilder = S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).region(region).serviceConfiguration(s3Config);
            if (StringUtils.hasText(endPoint)) s3ClientBuilder.endpointOverride(URI.create(endPoint));
            client = s3ClientBuilder.build();
        }
        return client;
    }


    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileName = FileUtils.getRandomFileName(fileWrapper.getOriginalFilename());
        String filePath = UrlUtils.join(basePath, fileWrapper.getPath(), fileName);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(filePath).contentType(fileWrapper.getContentType()).build();
        RequestBody requestBody = RequestBody.fromBytes(fileWrapper.getBytes());
        getClient().putObject(putObjectRequest, requestBody);
        return new FileInfo(fileName, basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(getUrlPath(fileInfo)).build();
            getClient().deleteObject(deleteObjectRequest);
        }
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucketName).key(getUrlPath(fileInfo)).build();
        HeadObjectResponse headObjectResponse = getClient().headObject(headObjectRequest);
        return headObjectResponse != null && headObjectResponse.contentLength() != null;
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(getUrlPath(fileInfo)).build();
        ResponseInputStream<GetObjectResponse> responseResponseInputStream = getClient().getObject(getObjectRequest);
        return new MultipartFileStorage(fileInfo.getOriginalFilename(), responseResponseInputStream);
    }

    @Override
    public void close() {
        client.close();
    }
}
