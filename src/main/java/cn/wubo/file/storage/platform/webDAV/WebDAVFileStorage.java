package cn.wubo.file.storage.platform.webDAV;

import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.utils.FileUtils;
import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.PathUtils;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class WebDAVFileStorage extends BaseFileStorage {

    private String server;
    private String user;
    private String password;
    private String storagePath;
    private Sardine client;

    public WebDAVFileStorage(WebDAV prop) {
        super(prop.getBasePath(), prop.getDomain(), prop.getAlias(), "WebDAV");
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
            createDirectory(PathUtils.join(PathUtils.getParent(path), "/"));
            getClient().createDirectory(path);
        }
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        String fileName = FileUtils.getRandomFileName(fileWrapper.getOriginalFilename());
        String fileDir = PathUtils.join(server, storagePath, basePath, fileWrapper.getPath());
        String filePath = PathUtils.join(server, storagePath, basePath, fileWrapper.getPath(), fileName);

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

        return new FileInfo(PathUtils.join(domain, basePath, fileWrapper.getPath(), fileName), fileName, basePath, fileWrapper);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            try {
                getClient().delete(PathUtils.join(server, storagePath, getFilePath(fileInfo)));
            } catch (IOException e) {
                throw new FileStorageRuntimeException(String.format("删除文件失败,%s", e.getMessage()), e);
            }
        }
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        try {
            return getClient().exists(PathUtils.join(server, storagePath, getFilePath(fileInfo)));
        } catch (IOException e) {
            throw new FileStorageRuntimeException(String.format("查询文件是否存在失败,%s", e.getMessage()), e);
        }
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        try (InputStream is = getClient().get(PathUtils.join(server, storagePath, getFilePath(fileInfo)))) {
            return new MultipartFileStorage(fileInfo.getFilename(), is);
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
