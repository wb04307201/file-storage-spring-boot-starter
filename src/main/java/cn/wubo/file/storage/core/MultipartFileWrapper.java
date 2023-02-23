package cn.wubo.file.storage.core;

import cn.wubo.file.storage.common.IoUtils;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Data
public class MultipartFileWrapper implements MultipartFile {

    /**
     * 存储平台
     */
    private String platform;
    /**
     * 文件名
     */
    private String name;
    /**
     * 原始文件名
     */
    private String originalFilename;
    /**
     * 内容类型
     */
    private String contentType;
    /**
     * 文件内容
     */
    private final byte[] bytes;

    public MultipartFileWrapper(MultipartFile multipartFile) {
        this(multipartFile.getName(), multipartFile.getOriginalFilename(), multipartFile.getContentType(), IoUtils.readBytes(multipartFile));
    }

    public MultipartFileWrapper(File file) {
        this(file.getName(), null, null, IoUtils.readBytes(file));
    }

    public MultipartFileWrapper(String name, byte[] bytes) {
        this(name, null, null, bytes);
    }

    public MultipartFileWrapper(String name, InputStream is) {
        this(name, null, null, IoUtils.readBytes(is));
    }

    public MultipartFileWrapper(String name, String originalFilename, String contentType, byte[] bytes) {
        this.name = (name != null ? name : "");
        this.originalFilename = (originalFilename != null ? originalFilename : "");
        this.contentType = contentType;
        this.bytes = (bytes != null ? bytes : new byte[0]);
    }

    @Override
    public boolean isEmpty() {
        return (this.bytes.length == 0);
    }

    @Override
    public long getSize() {
        return this.bytes.length;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public void transferTo(File dest) {
        try {
            Files.write(dest.toPath(), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
