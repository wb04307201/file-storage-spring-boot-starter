package cn.wubo.file.storage.page;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.record.IFileStroageRecord;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FileStorageDeleteServlet extends HttpServlet {

    private FileStorageService fileStorageService;

    public FileStorageDeleteServlet(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        if (fileStorageService != null) {
            String id = req.getParameter("id");
            fileStorageService.delete(id);
            resp.sendRedirect(contextPath + "/file/storage/list");
        } else {
            log.debug("contextPath========{}", contextPath);
            String servletPath = req.getServletPath();
            log.debug("servletPath========{}", servletPath);
            super.doGet(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
