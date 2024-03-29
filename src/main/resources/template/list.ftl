<!DOCTYPE html>
<html lang="en">
<head>
    <title>存储文件记录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="${contextPath}/file/storage/static/layui/2.9.6/css/layui.css"/>
    <script type="text/javascript" src="${contextPath}/file/storage/static/layui/2.9.6/layui.js"></script>
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
                    <option value="MinIO">MinIO</option>
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
            url: '${contextPath}/file/storage/upload', // 此处配置你自己的上传接口即可
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
            url: '${contextPath}/file/storage/list',
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
                fetch("${contextPath}/file/storage/delete?id=" + id)
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
            window.open("${contextPath}/file/storage/download?id=" + id);
        }
    })
</script>
</body>
</html>