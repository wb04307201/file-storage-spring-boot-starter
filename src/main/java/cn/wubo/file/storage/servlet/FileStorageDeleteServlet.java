package cn.wubo.file.storage.servlet;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.core.FileStorageService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class FileStorageDeleteServlet extends HttpServlet {

    private final FileStorageService fileStorageService;

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
