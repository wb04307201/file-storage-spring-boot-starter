package cn.wubo.file.storage.platform.git;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.platform.base.BaseFileStorage;
import cn.wubo.file.storage.utils.FileUtils;
import cn.wubo.file.storage.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class GitFileStorage extends BaseFileStorage {

    private String storagePath;
    private String repo;
    private String username;
    private String password;
    private org.eclipse.jgit.api.Git client;

    public GitFileStorage(Git prop) {
        super(prop.getBasePath(), prop.getDomain(), prop.getAlias(), "Git");
        this.storagePath = prop.getStoragePath();
        this.repo = prop.getRepo();
        this.username = prop.getUsername();
        this.password = prop.getPassword();
    }

    private org.eclipse.jgit.api.Git getClient() {
        if (client == null) {
            Path path = Paths.get(storagePath);
            try {
                if (Files.exists(path)) {
                    Files.createDirectories(path);
                    this.client = org.eclipse.jgit.api.Git.cloneRepository().setURI(repo).setDirectory(path.toFile()).call();
                } else {
                    this.client = org.eclipse.jgit.api.Git.wrap(new FileRepositoryBuilder().setGitDir(path.toFile()).build());
                }
            } catch (GitAPIException | IOException e) {
                throw new FileStorageRuntimeException(String.format("获取repo失败,%s", e.getMessage()), e);
            }
        }
        return client;
    }

    private void pull() {
        try {
            getClient().pull().call();
        } catch (GitAPIException e) {
            throw new FileStorageRuntimeException(String.format("从repo拉取变更失败,%s", e.getMessage()), e);
        }
    }

    private CredentialsProvider provider() {
        return new UsernamePasswordCredentialsProvider(username, password);
    }

    @Override
    public FileInfo save(MultipartFileStorage fileWrapper) {
        pull();
        String fileName = FileUtils.getRandomFileName(fileWrapper.getOriginalFilename());
        String filePath = Paths.get(basePath, fileWrapper.getPath(), fileName).toString();
        fileWrapper.transferTo(Paths.get(this.storagePath, filePath).toFile());
        try {
            getClient().add().addFilepattern(filePath).call();
            getClient().commit().setMessage(String.format("提交文件%s", fileName)).call();
            getClient().push().setCredentialsProvider(provider()).call();
        } catch (GitAPIException e) {
            throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
        }
        return new FileInfo(UUID.randomUUID().toString(), domain + filePath, fileName, basePath, fileWrapper);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        if (exists(fileInfo)) {
            try {
                getClient().rm().addFilepattern(getFilePath(fileInfo)).call();
                getClient().commit().setMessage(String.format("删除文件%s", fileInfo.getFilename())).call();
                getClient().push().setCredentialsProvider(provider()).call();
            } catch (GitAPIException e) {
                throw new FileStorageRuntimeException(String.format("存储文件失败,%s", e.getMessage()), e);
            }
        }
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        pull();
        return Files.exists(Paths.get(storagePath, getFilePath(fileInfo)));
    }

    @Override
    public MultipartFileStorage download(FileInfo fileInfo) {
        pull();
        return new MultipartFileStorage(Paths.get(this.storagePath, getFilePath(fileInfo)).toFile());
    }

    @Override
    public void close() {
        IoUtils.close(client);
    }
}
