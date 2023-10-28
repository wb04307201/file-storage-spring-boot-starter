# file-storage-spring-boot-starter

[![](https://jitpack.io/v/com.gitee.wb04307201/file-storage-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/file-storage-spring-boot-starter)

> 一个适配多平台文件存储的中间件  
> 可通过简单的配置既可集成到springboot中  
> 将文件存储到本地、AmazonS3、MinIO、华为云OBS、百度云 BOS、阿里云OSS、腾讯云COS、WebDAV、Git等平台 

## [代码示例](https://gitee.com/wb04307201/file-storage-demo)

## 第一步 增加 JitPack 仓库

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## 第二步 引入jar

```xml
<dependency>
    <groupId>com.gitee.wb04307201</groupId>
    <artifactId>file-storage-spring-boot-starter</artifactId>
    <version>1.0.11</version>
</dependency>
```

## 第三步 在启动类上加上`@EnableFileStorage`注解

```java
@EnableFileStorage
@SpringBootApplication
public class FileStorageDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageDemoApplication.class, args);
    }

}
```

## 第四步 `application.yml`配置文件中添加以下相关配置，可以配置多个存储

```yaml
file:
  storage: #文件存储配置，不使用的情况下可以不写
    local: # 本地存储
      - enable-storage: true  #启用存储
        base-path: local/ # 基础路径
        storage-path: D:/Temp/ # 存储路径
        alias: local-1 # 别名
    amazonS3: # amazonS3 以及其他兼容AWS S3标准网盘
      - enable-storage: true  # 启用存储
        access-key-id: ??
        secret-access-key: ??
        # 地域区域，例如 cn-north-1
        region: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: amazonS3-1 # 别名
    minIO: # MinIO
      - enable-storage: true  # 启用存储
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: minio-1 # 别名
    huaweiOBS: #HuaweiOBS
      - enable-storage: true  # 启用存储
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: huaweiOBS-1 # 别名
    baiduBOS: #BaiduBOS
      - enable-storage: true  # 启用存储
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: baiduBOS-1 # 别名
    aliyunOSS: #AliyunOSS
      - enable-storage: true  # 启用存储
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: aliyunOSS-1 # 别名
    tencentCOS: #TencentCOS
      - enable-storage: true  # 启用存储
        secret-id: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: tencentCOS-1 # 别名
    webDAV: #WebDAV
      - enable-storage: true  #启用存储
        base-path: temp/ # 基础路径
        storage-path: /aliyun/ # 存储路径
        server: http://127.0.0.1:5244 # Git仓库地址
        user: admin # 用户名
        password: q54U4YJb # 密码
        alias: webDAV-1 # 别名
    git: #Git
      - enable-storage: true  #启用存储
        base-path: git/ # 基础路径
        storage-path: D:/GitTemp/ # 存储路径,会将仓库clone到这个目录
        repo: https://gitee.com/??/?? # Git仓库地址
        username: ?? # 用户名
        password: ?? # 密码
        alias: git-1 # 别名
```

Amazon S3 SDK 与其他平台兼容性

| 平台          | 说明                                                                                                                        |
|-------------|---------------------------------------------------------------------------------------------------------------------------|
| MinIO       | [查看](https://www.minio.org.cn/docs/minio/kubernetes/upstream/index.html)                                                  |
| 阿里云 OSS     | [查看](https://help.aliyun.com/document_detail/64919.html#title-cds-fai-yxp)                                                |
| 华为云 OBS     | [查看](https://support.huaweicloud.com/sdk-java-devg-obs/obs_21_2123.html)                                                  |
| 七牛云 Kodo    | [查看](https://developer.qiniu.com/kodo/4086/amazon-s3-compatible)                                                          |
| 腾讯云 COS     | [查看](https://cloud.tencent.com/document/product/436/37421)                                                                |
| 百度云 BOS     | [查看](https://cloud.baidu.com/doc/BOS/s/Fjwvyq9xo)                                                                         |
| 金山云 KS3     | [查看](https://docs.ksyun.com/documents/959)                                                                                |
| 美团云 MSS     | [查看](https://www.mtyun.com/doc/products/storage/mss/zhu-yao-gong-neng#%E5%85%BC%E5%AE%B9%20AWS%20S3%20%E5%8D%8F%E8%AE%AE) |
| 京东云 OSS     | [查看](https://docs.jdcloud.com/cn/object-storage-service/compatibility-api-overview)                                       |
| 天翼云 OOS     | [查看](https://www.ctyun.cn/h5/help2/10000101/10001711)                                                                     |
| 移动云 EOS     | [查看](https://ecloud.10086.cn/op-help-center/doc/article/24569)                                                            |
| 沃云 OSS      | [查看](https://support.woyun.cn/document.html?id=133&arcid=127)                                                             |
| 网易数帆 NOS    | [查看](https://www.163yun.com/help/documents/89796157866430464)                                                             |
| Ucloud US3  | [查看](https://docs.ucloud.cn/ufile/s3/s3_introduction)                                                                     |
| 青云 QingStor | [查看](https://docs.qingcloud.com/qingstor/s3/)                                                                             |
| 平安云 OBS     | [查看](https://yun.pingan.com/ssr/help/storage/obs/OBS_SDK_.Java_SDK_)                                                      |
| 首云 OSS      | [查看](http://www.capitalonline.net.cn/zh-cn/service/distribution/oss-new/#product-adv)                                     |
| IBM COS     | [查看](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api)                        |
| 又拍云 USS     | [查看](https://help.upyun.com/knowledge-base/aws-s3%e5%85%bc%e5%ae%b9/)                                                     |

## 第五步 注入FileStorageServiceb并调用文件存储功能

```java
@Controller
public class PageController {

    @Autowired
    FileStorageService fileStorageService;

    /**
     * 测试用平台别名
     **/
    private String alias = "local-1";
    //private String alias = "minio-1";
    //private String alias = "webDAV-1";
    //private String alias = "git-1";
    //private String alias = "amazonS3-1";

    /**
     * 查询显示列表
     *
     * @param model
     * @param fileInfo
     * @return
     */
    @PostMapping(value = "/list")
    public String upload(Model model, FileInfo fileInfo) {
        model.addAttribute("list", fileStorageService.getFileStroageRecord().list(fileInfo));
        model.addAttribute("query", fileInfo);
        return "list";
    }

    /**
     * 上传文件
     *
     * @param file
     * @param model
     * @return
     */
    @PostMapping(value = "/upload")
    public String upload(MultipartFile file, Model model) {
        fileStorageService.save(new MultipartFileStorage(file).setAlias(alias).setPath("ttt"));
        FileInfo fileInfo = new FileInfo();
        model.addAttribute("list", fileStorageService.getFileStroageRecord().list(fileInfo));
        model.addAttribute("query", fileInfo);
        return "list";
    }

    /**
     * 显示列表
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/list")
    public String upload(Model model) {
        FileInfo fileInfo = new FileInfo();
        model.addAttribute("list", fileStorageService.getFileStroageRecord().list(fileInfo));
        model.addAttribute("query", fileInfo);
        return "list";
    }

    /**
     * 删除文件
     *
     * @param req
     * @param model
     * @return
     */
    @GetMapping(value = "/delete")
    public String delete(HttpServletRequest req, Model model) {
        String id = req.getParameter("id");
        fileStorageService.delete(id);
        FileInfo fileInfo = new FileInfo();
        model.addAttribute("list", fileStorageService.getFileStroageRecord().list(fileInfo));
        model.addAttribute("query", fileInfo);
        return "list";
    }

    /**
     * 下载文件
     *
     * @param req
     * @param resp
     */
    @GetMapping(value = "/download")
    public void download(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        MultipartFileStorage file = fileStorageService.download(id);
        resp.reset();
        resp.setContentType(file.getContentType());
        resp.addHeader("Content-Length", String.valueOf(file.getSize()));
        try (OutputStream os = resp.getOutputStream()) {
            resp.addHeader("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(file.getOriginalFilename()).getBytes(), StandardCharsets.ISO_8859_1));
            IoUtils.writeToStream(file.getBytes(), os);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
```

## 其他1：内置界面
上传的文件可通过http://ip:端口/file/storage/list进行查看  
注意：如配置了context-path需要在地址中对应添加  
![img.png](img.png)

## 其他2：实际使用中，可通过配置和实现文件存储记录接口方法将数据持久化到数据库中
继承IFileStroageRecord并实现方法，例如

```java
@Component
public class H2FileStroageRecordImpl implements IFileStroageRecord {

    private static final String HISTORY = "file_storage_history";

    private static ConnectionPool connectionPool = new ConnectionPool(new ConnectionParam());

    @Override
    public FileInfo save(FileInfo fileInfo) {
        try {
            Connection conn = connectionPool.getConnection();
            if (!StringUtils.hasLength(fileInfo.getId())) {
                fileInfo.setId(UUID.randomUUID().toString());
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.insertSql(HISTORY, fileInfo), new HashMap<>());
            } else {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.updateByIdSql(HISTORY, fileInfo), new HashMap<>());
            }
            connectionPool.returnConnection(conn);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return fileInfo;
    }

    @Override
    public List<FileInfo> list(FileInfo fileInfo) {
        try {
            Connection conn = connectionPool.getConnection();
            String sql = ModelSqlUtils.selectSql(HISTORY, new FileInfo());

            List<String> condition = new ArrayList<>();
            if (StringUtils.hasLength(fileInfo.getId())) condition.add(" id = '" + fileInfo.getId() + "'");
            if (StringUtils.hasLength(fileInfo.getPlatform()))
                condition.add(" platform = '" + fileInfo.getPlatform() + "'");
            if (StringUtils.hasLength(fileInfo.getAlias()))
                condition.add(" alias like '%" + fileInfo.getAlias() + "%'");
            if (StringUtils.hasLength(fileInfo.getOriginalFilename()))
                condition.add(" originalFilename like '%" + fileInfo.getOriginalFilename() + "%'");

            if (!condition.isEmpty()) sql = sql + " where " + String.join("and", condition);

            List<FileInfo> res = ExecuteSqlUtils.executeQuery(conn, sql, new HashMap<>(), FileInfo.class);
            connectionPool.returnConnection(conn);
            return res;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileInfo findById(String s) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(s);
        String sql = ModelSqlUtils.selectSql(HISTORY, fileInfo);
        try {
            Connection conn = connectionPool.getConnection();
            List<FileInfo> res = ExecuteSqlUtils.executeQuery(conn, sql, new HashMap<>(), FileInfo.class);
            connectionPool.returnConnection(conn);
            return res.get(0);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(FileInfo fileInfo) {
        List<FileInfo> fileInfos = list(fileInfo);
        try {
            Connection conn = connectionPool.getConnection();
            conn.setAutoCommit(false);
            AtomicInteger count = new AtomicInteger();
            fileInfos.stream().forEach(e -> {
                FileInfo delete = new FileInfo();
                delete.setId(e.getId());
                String sql = ModelSqlUtils.deleteByIdSql(HISTORY, delete);
                count.addAndGet(ExecuteSqlUtils.executeUpdate(conn, sql, new HashMap<>()));
            });
            conn.commit();
            connectionPool.returnConnection(conn);
            return count.get() > 0;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            Connection conn = connectionPool.getConnection();
            if (!ExecuteSqlUtils.isTableExists(conn, HISTORY, DbType.h2)) {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.createSql(HISTORY, new FileInfo()), new HashMap<>());
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

并添加配置指向类
```yaml
file:
  storage:
    file-storage-record: cn.wubo.file.storage.demo.H2FileStroageRecordImpl
```

## 其他3：docker安装MinIO
docker安装minio 详细内容请查看 [Download](https://min.io/download#/docker) 或者 [MinIO Object Storage for Container](https://min.io/docs/minio/container/index.html)
```bash
docker run -p 9000:9000 -p 9090:9090 --name minio1 -v D:\minio\data:/data -e "MINIO_ROOT_USER=ROOTUSER" -e "MINIO_ROOT_PASSWORD=CHANGEME123" quay.io/minio/minio server /data --console-address ":9090"
```
用户名 ROOTUSER 密码 CHANGEME123

## 其他4：WebDAV——通过Alist支持更多存储平台
[Alist --一个支持多种存储的文件列表程序](https://alist.nn.ci)  
[sardine --an easy to use webdav client for java](https://github.com/lookfirst/sardine)

```bash
# docker安装
docker run -d --restart=always -v /etc/alist:/opt/alist/data -p 5244:5244 -e PUID=0 -e PGID=0 -e UMASK=022 --name="alist" xhofe/alist:latest
# 查看用户名和密码
docker exec -it alist ./alist admin
```

## 待办

- [ ] *其他云存储SDK*

- [ ] *谷歌云存储*

- [ ] *扩展对FTP的支持*

