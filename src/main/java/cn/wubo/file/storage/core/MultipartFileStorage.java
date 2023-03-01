package cn.wubo.file.storage.core;

import cn.wubo.file.storage.common.IoUtils;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@Data
public class MultipartFileStorage implements MultipartFile {

    private static final int BUFFER_SIZE = 8192;

    /**
     * 别名
     */
    private String alais;
    /**
     * 存储路径
     */
    private String path;
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

    public MultipartFileStorage(MultipartFile multipartFile) {
        this(multipartFile.getName(), multipartFile.getOriginalFilename(), multipartFile.getContentType(), IoUtils.readBytes(multipartFile));
    }

    public MultipartFileStorage(File file) {
        this(file.getName(), null, null, IoUtils.readBytes(file));
    }

    public MultipartFileStorage(String name, byte[] bytes) {
        this(name, null, null, bytes);
    }

    public MultipartFileStorage(String name, InputStream is) {
        this(name, null, null, IoUtils.readBytes(is));
    }

    public MultipartFileStorage(String name, String originalFilename, String contentType, byte[] bytes) {
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

    public void writeOutputStream(OutputStream os) throws IOException {
        IoUtils.writeToStream(this.bytes, os);
    }

    @Override
    public void transferTo(File dest) {
        try {
            Files.write(dest.toPath(), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MultipartFileStorage setAlais(String alais) {
        this.alais = alais;
        return this;
    }

    public MultipartFileStorage setPath(String path) {
        this.path = path;
        return this;
    }
}
