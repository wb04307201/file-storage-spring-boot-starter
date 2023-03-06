package cn.wubo.file.storage.page;

import cn.wubo.file.storage.core.FileStorageService;
import cn.wubo.file.storage.core.MultipartFileStorage;
import cn.wubo.file.storage.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class FileStorageDownloadServlet extends HttpServlet {

    private FileStorageService fileStorageService;

    public FileStorageDownloadServlet(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        if (fileStorageService != null) {
            String id = req.getParameter("id");
            MultipartFileStorage file = fileStorageService.download(id);
            resp.setContentType(file.getContentType());
            resp.addHeader("Content-Length", String.valueOf(file.getSize()));
            resp.addHeader("Content-Disposition", "attachment;filename=" + file.getOriginalFilename());
            try (OutputStream os = resp.getOutputStream()) {
                IoUtils.writeToStream(file.getBytes(), os);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
