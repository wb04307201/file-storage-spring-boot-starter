package cn.wubo.file.storage.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class IoUtils {

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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        } finally {
            if (isClose) {
                close(is);
            }
        }
    }

    public static byte[] close(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
