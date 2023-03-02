package cn.wubo.file.storage.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 文件大小，单位字节
     */
    private Long size;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 基础存储路径
     */
    private String basePath;

    /**
     * 存储路径
     */
    private String path;

    /**
     * MIME 类型
     */
    private String contentType;

    /**
     * 别名
     */
    private String alais;

    /**
     * 创建时间
     */
    private Date createTime;

    public FileInfo(String url, String filename,String basePath,MultipartFileStorage fileWrapper) {
        this.url = url;
        this.size = fileWrapper.getSize();
        this.filename = filename;
        this.originalFilename = fileWrapper.getOriginalFilename();
        this.basePath = basePath;
        this.path = fileWrapper.getPath();
        this.contentType = fileWrapper.getContentType();
        this.alais = fileWrapper.getAlais();
        this.createTime = new Date();
    }
}
