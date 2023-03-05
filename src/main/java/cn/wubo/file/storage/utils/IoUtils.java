package cn.wubo.file.storage.utils;

import cn.wubo.file.storage.exception.IORuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@Slf4j
public class IoUtils {

    /**
     * 默认缓存大小 8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    public static byte[] readBytes(MultipartFile multipartFile) {
        try (InputStream is = multipartFile.getInputStream()) {
            return readBytes(is, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] readBytes(InputStream is) {
        return readBytes(is, true);
    }

    public static byte[] readBytes(InputStream is, Boolean isClose) {
        try {
            int available = is.available();
            byte[] result;
            result = new byte[available];
            int readLength = is.read(result);
            if (readLength != available) {
                throw new IOException(String.format("File length is [%s] but read [%s]!", available, readLength));
            }
            return result;
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        } finally {
            if (isClose) {
                close(is);
            }
        }
    }

    public static void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
        }
    }

    public static void writeToStream(File file, OutputStream os) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        }
    }

    public static void writeToStream(byte[] bytes, OutputStream os) throws IOException {
        int len = bytes.length;
        int rem = len;
        while (rem > 0) {
            int n = Math.min(rem, DEFAULT_BUFFER_SIZE);
            os.write(bytes, (len - rem), n);
            rem -= n;
        }
    }
}
