# file-storage-spring-boot-starter

[![](https://jitpack.io/v/com.gitee.wb04307201/file-storage-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/file-storage-spring-boot-starter)

> 一个适配多平台文件存储的中间件  
> 可通过简单的配置既可集成到springboot中  
> 将文件存储到本地、AmazonS3、MinIO、华为云OBS、百度云 BOS、阿里云OSS、腾讯云COS、WebDAV、Git等平台

## 代码示例
1. 使用[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具](https://gitee.com/wb04307201/sql-util)实现的[文件存储Demo](https://gitee.com/wb04307201/file-storage-demo)
2. 使用[文档在线预览](https://gitee.com/wb04307201/file-preview-spring-boot-starter)、[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具](https://gitee.com/wb04307201/sql-util)实现的[文件预览Demo](https://gitee.com/wb04307201/file-preview-demo)
3. 使用[文档在线预览](https://gitee.com/wb04307201/file-preview-spring-boot-starter)、[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具](https://gitee.com/wb04307201/sql-util)实现的[文件预览VUE Demo](https://gitee.com/wb04307201/file-preview-vue)

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
1.1.0版本后升级到jdk17 SpringBoot3+
继续使用jdk 8请查看jdk8分支
```xml
<dependency>
    <groupId>com.gitee.wb04307201</groupId>
    <artifactId>file-storage-spring-boot-starter</artifactId>
    <version>1.1.3</version>
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
    defaultAlias: minio-1  # 内嵌页面默认使用alias
    defaultPath: ttt/  # 内嵌页面默认使用path
    enableWeb: true  # 默认为true，加载内置页面
    enableRest: true  # 默认为true, 加载内置接口
    local: # 本地存储
      - enable-storage: true  #启用存储，默认为true，关闭false
        base-path: temp/ # 基础路径
        storage-path: D:/local/ # 存储路径
        alias: local-1 # 别名
    amazonS3: # amazonS3 以及其他兼容AWS S3标准网盘
      - enable-storage: true  # 启用存储，默认为true，关闭false
        access-key-id: ??
        secret-access-key: ??
        # 地域区域，例如 cn-north-1
        region: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: amazonS3-1 # 别名
    minIO: # MinIO
      - enable-storage: true  # 启用存储，默认为true，关闭false
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: minio-1 # 别名
    huaweiOBS: #HuaweiOBS
      - enable-storage: true  # 启用存储，默认为true，关闭false
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: huaweiOBS-1 # 别名
    baiduBOS: #BaiduBOS
      - enable-storage: true  # 启用存储，默认为true，关闭false
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: baiduBOS-1 # 别名
    aliyunOSS: #AliyunOSS
      - enable-storage: true  # 启用存储，默认为true，关闭false
        access-key: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: aliyunOSS-1 # 别名
    tencentCOS: #TencentCOS
      - enable-storage: true  # 启用存储，默认为true，关闭false
        secret-id: ??
        secret-key: ??
        end-point: ??
        bucket-name: ??
        base-path: temp/ # 基础路径
        alias: tencentCOS-1 # 别名
    webDAV: #WebDAV
      - enable-storage: true  #启用存储，默认为true，关闭false
        base-path: temp/ # 基础路径
        storage-path: /aliyun/ # 存储路径
        server: http://127.0.0.1:5244 # Git仓库地址
        user: admin # 用户名
        password: q54U4YJb # 密码
        alias: webDAV-1 # 别名
    git: #Git
      - enable-storage: true  #启用存储，默认为true，关闭false
        base-path: temp/ # 基础路径
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

## 第五步 访问内置界面使用文件上传
上传的文件可通过http://ip:端口/file/storage/list进行查看  
注1：如配置了context-path需要在地址中对应添加  
注2：使用内置界面，默认使用的alias和path通过defaultAlias和defaultPath进行配置
![img.png](img.png)

## 其他1：实际使用中，可通过配置和实现文件存储记录接口方法将数据持久化到数据库中
继承IFileStroageRecord并实现方法，例如

```java
@Component
public class H2FileStroageRecordImpl implements IFileStroageRecord {

    static {
        MutilConnectionPool.init("main", "jdbc:h2:file:./data/demo;AUTO_SERVER=TRUE", "sa", "");
    }

    @Override
    public FileInfo save(FileInfo fileInfo) {
        FileStorageRecord fileStorageRecord = FileStorageRecord.trans(fileInfo);
        if (!StringUtils.hasLength(fileStorageRecord.getId())) {
            fileStorageRecord.setId(UUID.randomUUID().toString());
            MutilConnectionPool.run("main", conn -> ModelSqlUtils.insertSql(fileStorageRecord).executeUpdate(conn));
        } else MutilConnectionPool.run("main", conn -> ModelSqlUtils.updateSql(fileStorageRecord).executeUpdate(conn));
        return fileStorageRecord.getFileInfo();
    }

    @Override
    public List<FileInfo> list(FileInfo fileInfo) {
        FileStorageRecord fileStorageRecord = FileStorageRecord.trans(fileInfo);
        return MutilConnectionPool.run("main", conn -> ModelSqlUtils.selectSql(fileStorageRecord).executeQuery(conn)).stream().map(FileStorageRecord::getFileInfo).collect(Collectors.toList());
    }

    @Override
    public FileInfo findById(String s) {
        FileInfo query = new FileInfo();
        query.setId(s);
        List<FileInfo> list = list(query);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Boolean delete(FileInfo fileInfo) {
        FileStorageRecord fileStorageRecord = FileStorageRecord.trans(fileInfo);
        return MutilConnectionPool.run("main", conn -> ModelSqlUtils.deleteSql(fileStorageRecord).executeUpdate(conn)) > 0;
    }

    @Override
    public void init() {
        if (Boolean.FALSE.equals(MutilConnectionPool.run("main", conn -> new SQL<FileStorageRecord>() {
        }.isTableExists(conn)))) MutilConnectionPool.run("main", conn -> new SQL<FileStorageRecord>() {
        }.create().parse().createTable(conn));
    }
}
```

```yaml
file:
  storage:
    file-storage-record: cn.wubo.file.storage.demo.H2FileStroageRecordImpl
```

## 其他2：通过内置Rest接口实现自定义页面
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <title>存储文件记录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="/layui/2.9.6/css/layui.css"/>
    <script type="text/javascript" src="/layui/2.9.6/layui.js"></script>
    <style>
        body {
            padding: 10px 20px 10px 20px;
        }
    </style>
</head>
<body>
<form class="layui-form layui-row layui-col-space16">
    <div class="layui-col-md4">
        <div class="layui-form-item">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-block">
                <select name="platform">
                    <option value="" selected>全部</option>
                    <option value="Local">本地</option>
                    <option value="AmazonS3">AmazonS3</option>
                    <option value="MinIO"></option>
                    <option value="HuaweiOBS">HuaweiOBS</option>
                    <option value="BaiduBOS">BaiduBOS</option>
                    <option value="AliyunOSS">AliyunOSS</option>
                    <option value="TencentCOS">TencentCOS</option>
                    <option value="WebDAV">WebDAV</option>
                    <option value="Git">Git</option>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-col-md4">
        <div class="layui-form-item">
            <label class="layui-form-label">别名</label>
            <div class="layui-input-block">
                <input type="text" name="alias" placeholder="请输入" class="layui-input" lay-affix="clear">
            </div>
        </div>
    </div>
    <div class="layui-col-md4">
        <div class="layui-form-item">
            <label class="layui-form-label">原始文件名</label>
            <div class="layui-input-block">
                <input type="text" name="originalFilename" placeholder="请输入" class="layui-input" lay-affix="clear">
            </div>
        </div>
    </div>
    <div class="layui-btn-container layui-col-xs12">
        <button class="layui-btn" lay-submit lay-filter="table-search">查询</button>
        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
</form>
<!-- 拖拽上传 -->
<div class="layui-upload-drag" style="display: block;" id="ID-upload-demo-drag">
    <i class="layui-icon layui-icon-upload"></i>
    <div>点击上传，或将文件拖拽到此处</div>
    <div class="layui-hide" id="ID-upload-demo-preview">
        <hr>
        <img src="" alt="上传成功后渲染" style="max-width: 100%">
    </div>
</div>
<!-- 原始容器 -->
<table class="layui-hide" id="table"></table>
<!-- 操作列 -->
<script type="text/html" id="table-templet-operator">
    <div class="layui-clear-space">
        <a class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
        <a class="layui-btn layui-btn-xs" lay-event="download">下载</a>
    </div>
</script>
<script>
    layui.use(['table', 'form', 'util'], function () {
        let table = layui.table, form = layui.form, layer = layui.layer, $ = layui.$, laydate = layui.laydate,
            upload = layui.upload;

        // 搜索提交
        form.on('submit(table-search)', function (data) {
            let field = data.field; // 获得表单字段
            // 执行搜索重载
            table.reloadData('table', {
                where: field // 搜索的字段
            });
            return false; // 阻止默认 form 跳转
        });

        // 渲染
        upload.render({
            elem: '#ID-upload-demo-drag', // 绑定多个元素
            url: '/file/storage/upload', // 此处配置你自己的上传接口即可
            accept: 'file', // 普通文件
            done: function (res) {
                if (res.code === 200)
                    table.reloadData('table', {});
                layer.msg(res.message);
            }
        });

        var inst = table.render({
            elem: '#table',
            cols: [[ //标题栏
                {type: 'checkbox', fixed: 'left'},
                {type: 'numbers', fixed: 'left'},
                {field: 'id', title: 'ID', width: 150, fixed: 'left', hide: true},
                {field: 'platform', title: '平台', width: 200},
                {field: 'alias', title: '别名', width: 200},
                {field: 'filename', title: '文件名称', width: 200},
                {field: 'originalFilename', title: '原始文件名', width: 200},
                {field: 'size', title: '文件大小', width: 200},
                {field: 'contentType', title: 'MIME', width: 200},
                {field: 'basePath', title: '基础存储路径', width: 200},
                {field: 'path', title: '存储路径', width: 200},
                {field: 'operator', title: '操作', width: 200, fixed: 'right', templet: '#table-templet-operator'},
            ]],
            url: '/file/storage/list',
            method: 'post',
            contentType: 'application/json',
            parseData: function (res) { // res 即为原始返回的数据
                return {
                    "code": res.code === 200 ? 0 : res.code, // 解析接口状态
                    "msg": res.message, // 解析提示文本
                    "count": res.data.length, // 解析数据长度
                    "data": res.data // 解析数据列表
                };
            },
        });

        // 操作列事件
        table.on('tool(table)', function (obj) {
            let data = obj.data; // 获得当前行数据
            switch (obj.event) {
                case 'delete':
                    deleteRow(data.id)
                    break;
                case 'download':
                    downloadRow(data.id)
                    break;
            }
        })

        function deleteRow(id) {
            layer.confirm('确定要删除么？', {icon: 3}, function (index, layero, that) {
                fetch("/file/storage/delete?id=" + id)
                    .then(response => response.json())
                    .then(res => {
                        if (res.code === 200)
                            table.reloadData('table', {});
                        layer.close(index);
                        layer.msg(res.message);
                    })
                    .catch(err => {
                        layer.msg(err)
                        layer.close(index);
                    })
            }, function (index, layero, that) {
            });
        }

        function downloadRow(id) {
            window.open("/file/storage/download?id=" + id);
        }
    })
</script>
</body>
</html>
```

## 其他3：通过注入FileStorageService实现自定义Rest接口和自定义页面
注意：使用该方式，可更加灵活的使用alias和path属性
```java
@Controller
public class Demo2Controller {

    @Autowired
    FileStorageService fileStorageService;

    /**
     * 测试用平台别名
     **/
    //private String alias = "local-1";
    private String alias = "minio-1";
    //private String alias = "webDAV-1";
    //private String alias = "git-1";
    //private String alias = "amazonS3-1";

    /**
     * 上传文件列表
     *
     * @param model    模型对象
     * @param fileInfo 文件信息对象
     * @return 返回视图名称
     */
    @PostMapping(value = "/demo2")
    public String upload(Model model, FileInfo fileInfo) {
        // 获取文件存储记录列表
        model.addAttribute("list", fileStorageService.getFileStroageRecord().list(fileInfo));
        // 设置查询参数
        model.addAttribute("query", fileInfo);
        return "demo2";
    }

    /**
     * 上传文件
     *
     * @param file  上传的文件
     * @param model 模型对象
     * @return 返回上传结果页面
     */
    @PostMapping(value = "/upload")
    public String upload(MultipartFile file, Model model) {
        // 保存上传的文件
        fileStorageService.save(new MultipartFileStorage(file).setAlias(alias).setPath("/ttt"));

        // 获取文件列表
        FileInfo fileInfo = new FileInfo();
        model.addAttribute("list", fileStorageService.getFileStroageRecord().list(fileInfo));

        // 设置查询条件
        model.addAttribute("query", fileInfo);

        // 返回上传结果页面
        return "demo2";
    }

    /**
     * 获取文件列表
     *
     * @param model 模型对象
     * @return 返回页面名称
     */
    @GetMapping(value = "/demo2")
    public String upload(Model model) {
        // 创建文件信息对象
        FileInfo fileInfo = new FileInfo();
        // 获取文件存储记录列表
        List<FileInfo> list = fileStorageService.getFileStroageRecord().list(fileInfo);
        // 将文件存储记录列表和文件信息对象添加到模型对象中
        model.addAttribute("list", list);
        model.addAttribute("query", fileInfo);
        // 返回页面名称
        return "demo2";
    }

    /**
     * 删除文件
     *
     * @param req   请求对象
     * @param model 模型对象
     * @return 返回页面名称
     */
    @GetMapping(value = "/delete")
    public String delete(HttpServletRequest req, Model model) {
        // 获取要删除的文件id
        String id = req.getParameter("id");
        // 调用文件存储服务删除文件
        fileStorageService.delete(id);
        // 创建文件信息对象
        FileInfo fileInfo = new FileInfo();
        // 获取文件存储记录列表
        List<FileInfo> list = fileStorageService.getFileStroageRecord().list(fileInfo);
        // 将文件存储记录列表和文件信息对象添加到模型对象中
        model.addAttribute("list", list);
        model.addAttribute("query", fileInfo);
        // 返回页面名称
        return "demo2";
    }

    /**
     * 下载文件
     *
     * @param req  请求对象
     * @param resp 响应对象
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
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>存储文件记录</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/5.3.2/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <style>
        .table tbody tr td {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row g-3">
        <div class="col">
            <form method="post" enctype="multipart/form-data" action="/upload">
                <div class="mb-3">
                    <label for="fileInput" class="form-label">文件上传</label>
                    <input type="file" class="form-control" id="fileInput" aria-describedby="fileHelp" name="file">
                </div>
                <button type="submit" class="btn btn-primary">提交</button>
            </form>
        </div>
    </div>
    <form class="row g-3 mb-3 mt-3" method="POST" action="/demo2">
        <div class="col-6">
            <label class="form-check-label" for="platform">平台</label>
            <select class="form-select" id="platform" name="platform" aria-label="选择平台">
                <option value="" <#if ((query.platform)!'') == ''>selected</#if>>All</option>
                <option value="Local" <#if ((query.platform)!'') == 'Local'>selected</#if>>本地
                </option>
                <option value="MinIO" <#if ((query.platform)!'') == 'MinIO'>selected</#if>>MinIO
                </option>
                <option value="HuaweiOBS" <#if ((query.platform)!'') == 'HuaweiOBS'>selected</#if>>HuaweiOBS</option>
                <option value="BaiduBOS" <#if ((query.platform)!'') == 'BaiduBOS'>selected</#if>>BaiduBOS</option>
                <option value="AliyunOSS" <#if ((query.platform)!'') == 'AliyunOSS'>selected</#if>>AliyunOSS</option>
                <option value="TencentCOS" <#if ((query.platform)!'') == 'TencentCOS'>selected</#if>>TencentCOS</option>
                <option value="WebDAV" <#if ((query.platform)!'') == 'WebDAV'>selected</#if>>WebDAV</option>
                <option value="Git" <#if ((query.platform)!'') == 'Git'>selected</#if>>Git</option>
            </select>
        </div>
        <div class="col-6">
            <label class="form-check-label" for="alias">别名</label>
            <input type="text" class="form-control" id="alias" name="alias" aria-describedby="平台别名"
                   value="${(query.alias)!''}">
        </div>
        <div class="col-6">
            <label class="form-check-label" for="originalFilename">原始文件名</label>
            <input type="text" class="form-control" id="originalFilename" name="originalFilename"
                   aria-describedby="原始文件名"
                   value="${(query.originalFilename)!''}">
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-primary">查询</button>
        </div>
    </form>
    <div class="row">
        <div class="col-12" style="overflow-x: auto">
            <table class="table table-striped table-border">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">平台</th>
                    <th scope="col">别名</th>
                    <th scope="col">文件名称</th>
                    <th scope="col">原始文件名</th>
                    <th scope="col">文件大小</th>
                    <th scope="col">MIME</th>
                    <th scope="col">基础存储路径</th>
                    <th scope="col">存储路径</th>
                    <th scope="col">删除</th>
                    <th scope="col">下载</th>
                </tr>
                </thead>
                <tbody>
                <#if list?? && (list?size > 0)>
                    <#list list as row>
                        <tr>
                            <#--<th scope="row">${row.id}</th>-->
                            <th scope="row">${row_index + 1}</th>
                            <td>${row.platform!'-'}</td>
                            <td>${row.alias!'-'}</td>
                            <td>${row.filename!'-'}</td>
                            <td>${row.originalFilename!'-'}</td>
                            <td>${row.size!'-'}</td>
                            <td>${row.contentType!'-'}</td>
                            <td>${row.basePath!'-'}</td>
                            <td>${row.path!'-'}</td>
                            <td><a href="/delete?id=${row.id}" class="link-primary">@删除</a></td>
                            <td><a href="/download?id=${row.id}" class="link-primary">@下载</a></td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
```

## 其他4：docker安装MinIO
docker安装minio 详细内容请查看 [Download](https://min.io/download#/docker) 或者 [MinIO Object Storage for Container](https://min.io/docs/minio/container/index.html)
```bash
docker run -p 9000:9000 -p 9090:9090 --name minio -v D:\minio\data:/data -e "MINIO_ROOT_USER=ROOTUSER" -e "MINIO_ROOT_PASSWORD=CHANGEME123" quay.io/minio/minio server /data --console-address ":9090"
```
用户名 ROOTUSER 密码 CHANGEME123

## 其他5：WebDAV——通过Alist支持更多存储平台
[Alist --一个支持多种存储的文件列表程序](https://alist.nn.ci)  
[sardine --an easy to use webdav client for java](https://github.com/lookfirst/sardine)

```bash
# docker安装
docker run -d --restart=always -v /etc/alist:/opt/alist/data -p 5244:5244 -e PUID=0 -e PGID=0 -e UMASK=022 --name="alist" xhofe/alist:latest
# 查看用户名和密码
docker exec -it alist ./alist admin
```

## 其他6：自定义存储文件命名方法
```yaml
file:
  storage:
    fileNameMapping: cn.wubo.file.storage.demo.MD5FileNameMappingImpl
```

```java
@Component
public class MD5FileNameMappingImpl implements IFileNameMapping {
    @Override
    public String mapping(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(s.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
```

