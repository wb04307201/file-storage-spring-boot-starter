<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>存储文件记录</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/bootstrap/5.1.3/css/bootstrap.css"/>
    <script type="text/javascript" src="${contextPath}/bootstrap/5.1.3/js/bootstrap.bundle.js"></script>
    <style>
        .table tbody tr td{
            overflow: hidden;
            text-overflow:ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <form class="row g-3 mb-3 mt-3" method="POST">
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