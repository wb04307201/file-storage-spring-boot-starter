package cn.wubo.file.storage.page;

import cn.wubo.file.storage.core.FileInfo;
import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.record.IFileStroageRecord;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FileStorageListServlet extends HttpServlet {

    private IFileStroageRecord fileStroageRecord;

    public FileStorageListServlet(IFileStroageRecord fileStroageRecord) {
        this.fileStroageRecord = fileStroageRecord;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        if (fileStroageRecord != null) {
            Map<String, Object> data = new HashMap<>();

            FileInfo fileInfo = new FileInfo();
            if (req.getMethod().equalsIgnoreCase("post")) {
                Map<String, String[]> map = req.getParameterMap();
                fileInfo.setPlatform(map.get("platform")[0]);
                fileInfo.setAlias(map.get("alias")[0]);
                fileInfo.setOriginalFilename(map.get("originalFilename")[0]);
            }

            data.put("list", fileStroageRecord.list(fileInfo));
            data.put("contextPath", contextPath);
            data.put("query", fileInfo);

            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            resp.setCharacterEncoding("UTF-8");
            try {
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, resp.getWriter());
            } catch (TemplateException | IOException e) {
                throw new FileStorageRuntimeException(e.getMessage(), e);
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
