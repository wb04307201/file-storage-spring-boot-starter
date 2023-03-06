# file-storage-spring-boot-starter

[Alist --一个支持多种存储的文件列表程序](https://alist.nn.ci)  
[WebDAV 存储支持](https://alist.nn.ci/zh/guide/webdav.html#webdav-%E5%AD%98%E5%82%A8%E6%94%AF%E6%8C%81)  
[sardine --an easy to use webdav client for java](https://github.com/lookfirst/sardine)

```yaml
spring:
  file-storage: #文件存储配置，不使用的情况下可以不写
    local: # 本地存储升级版
      - enable-storage: true  #启用存储
        enable-access: true #启用访问（线上请使用 Nginx 配置，效率更高）
        domain: "" # 访问域名，例如：“http://127.0.0.1:8030/”，注意后面要和 path-patterns 保持一致，“/”结尾，本地存储建议使用相对路径，方便后期更换域名
        base-path: local-plus/ # 基础路径
        path-patterns: /** # 访问路径
        storage-path: D:/Temp/ # 存储路径
        alias: local-1
    minIO: # MinIO，由于 MinIO SDK 支持 AWS S3，其它兼容 AWS S3 协议的存储平台也都可配置在这里
      - enable-storage: true  # 启用存储
        access-key: ??
        secret-key: ??
        end-point: http://localhost:9090/
        bucket-name: ??
        domain: ?? # 访问域名，注意“/”结尾，例如：http://minio.abc.com/abc/
        base-path: hy/ # 基础路径
        alias: minio-1
    huaweiOBS: # 华为云 OBS ，不使用的情况下可以不写
      - enable-storage: false  # 启用存储
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        domain: ?? # 访问域名，注意“/”结尾，例如：http://abc.obs.com/
        base-path: hy/ # 基础路径
        alias: huawei-obs-1
```

docker安装minio 详细内容请查看 [Download](https://min.io/download#/docker) 或者 [MinIO Object Storage for Container](https://min.io/docs/minio/container/index.html)
```bash
docker run -p 9000:9000 -p 9090:9090 --name minio1 -v D:\minio\data:/data -e "MINIO_ROOT_USER=ROOTUSER" -e "MINIO_ROOT_PASSWORD=CHANGEME123" quay.io/minio/minio server /data --console-address ":9090"
```
用户名 ROOTUSER 密码 CHANGEME123