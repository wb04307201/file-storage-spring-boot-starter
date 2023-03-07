# file-storage-spring-boot-starter

[Alist --一个支持多种存储的文件列表程序](https://alist.nn.ci)  
[sardine --an easy to use webdav client for java](https://github.com/lookfirst/sardine)

```bash
# docker安装
docker run -d --restart=always -v /etc/alist:/opt/alist/data -p 5244:5244 -e PUID=0 -e PGID=0 -e UMASK=022 --name="alist" xhofe/alist:latest
# 查看用户名和密码
docker exec -it alist ./alist admin
```

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
    webDAV: #Git
      - enable-storage: true  #启用存储
        base-path: git/ # 基础路径
        storage-path: D:/Temp/ # 存储路径
        server: https://gitee.com/wb04307201/file-storage-test # Git仓库地址
        user: wb04307201 # 用户名
        password: 1986z11z20Z! # 密码
        alias: git-1 # 别名
    git: #Git
      - enable-storage: true  #启用存储
        base-path: git/ # 基础路径
        storage-path: D:/GitTemp/ # 存储路径,会将仓库clone到这个目录
        repo: ?? # Git仓库地址
        username: ?? # 用户名
        password: ?? # 密码
        alias: git-1 # 别名
```

docker安装minio 详细内容请查看 [Download](https://min.io/download#/docker) 或者 [MinIO Object Storage for Container](https://min.io/docs/minio/container/index.html)
```bash
docker run -p 9000:9000 -p 9090:9090 --name minio1 -v D:\minio\data:/data -e "MINIO_ROOT_USER=ROOTUSER" -e "MINIO_ROOT_PASSWORD=CHANGEME123" quay.io/minio/minio server /data --console-address ":9090"
```
用户名 ROOTUSER 密码 CHANGEME123