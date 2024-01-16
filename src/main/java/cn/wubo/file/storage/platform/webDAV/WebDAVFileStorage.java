package cn.wubo.file.storage.platform.webDAV;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.UrlUtils;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class WebDAVFileStorage extends BaseFileStorage {

    private final String server;
    private final String user;
    private final String password;
    private final String storagePath;
    private Sardine client;

    public WebDAVFileStorage(WebDAV prop) {
        super(prop.getBasePath(), prop.getAlias(), "WebDAV");
        this.server = prop.getServer();
        this.user = prop.getUser();
        this.password = prop.getPassword();
        this.storagePath = prop.getStoragePath();
    }

    public Sardine getClient() {
        if (client == null) {
            client = SardineFactory.begin(user, password);
        }
        return client;
    }

    public void createDirectory(String path) throws IOException {
        if (!getClient().exists(path)) {
            createDirectory(UrlUtils.join(UrlUtils.getParent(path), "/"));
            getClient().createDirectory(path);
        }
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileDir = UrlUtils.join(server, storagePath, basePath, fileWrapper.getPath());
        String filePath = UrlUtils.join(server, storagePath, basePath, fileWrapper.getPath(), fileWrapper.getName());

        try {
            createDirectory(fileDir);
            getClient().put(filePath, fileWrapper.getBytes());
        } catch (IOException e) {
            try {
                client.delete(filePath);
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }

        return new FileInfo(fileWrapper.getName(), basePath, new Date(), fileWrapper, platform);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            try {
                getClient().delete(UrlUtils.join(server, storagePath, getUrlPath(fileInfo)));
            } catch (IOException e) {
                throw new FileStorageRuntimeException(String.format("删除文件失败,%s", e.getMessage()), e);
            }
        }
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        try {
            return getClient().exists(UrlUtils.join(server, storagePath, getUrlPath(fileInfo)));
        } catch (IOException e) {
            throw new FileStorageRuntimeException(String.format("查询文件是否存在失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        try (InputStream is = getClient().get(UrlUtils.join(server, storagePath, getUrlPath(fileInfo)))) {
            return new MultipartFileStorage(fileInfo.getFilename(), fileInfo.getOriginalFilename(), fileInfo.getContentType(), is);
        } catch (IOException e) {
            throw new FileStorageRuntimeException(String.format("下载文件失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public void close() {
        if (client != null) {
            try {
                client.shutdown();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            client = null;
        }
    }
}
