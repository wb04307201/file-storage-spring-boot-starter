# file-storage-spring-boot-starter

[Alist --一个支持多种存储的文件列表程序](https://alist.nn.ci)  
[WebDAV 存储支持](https://alist.nn.ci/zh/guide/webdav.html#webdav-%E5%AD%98%E5%82%A8%E6%94%AF%E6%8C%81)  
[sardine --an easy to use webdav client for java](https://github.com/lookfirst/sardine)

```yaml
file:
  storage: #文件存储配置，不使用的情况下可以不写
    local: # 本地存储
      - enable-storage: true  #启用存储
        base-path: local/ # 基础路径
        storage-path: D:/Temp/ # 存储路径
        alias: local-1 # 别名
    minIO: # MinIO
      - enable-storage: true  # 启用存储
        access-key: 24d96fQlwjqp1snT
        secret-key: q01jDSLvAjeXW9vmMt4q1nHokJu2qecX
        end-point: http://localhost:9000
        bucket-name: testfilestorage
        base-path: temp/ # 基础路径
        alias: minio-1 # 别名
    git: #Git
      - enable-storage: true  #启用存储
        base-path: git/ # 基础路径
        storage-path: D:/Temp/ # 存储路径
        repo: https://gitee.com/wb04307201/file-storage-test # Git仓库地址
        username: wb04307201 # 用户名
        password: 1986z11z20Z! # 密码
        alias: git-1 # 别名
```

docker安装minio 详细内容请查看 [Download](https://min.io/download#/docker) 或者 [MinIO Object Storage for Container](https://min.io/docs/minio/container/index.html)
```bash
docker run -p 9000:9000 -p 9090:9090 --name minio1 -v D:\minio\data:/data -e "MINIO_ROOT_USER=ROOTUSER" -e "MINIO_ROOT_PASSWORD=CHANGEME123" quay.io/minio/minio server /data --console-address ":9090"
```
用户名 ROOTUSER 密码 CHANGEME123