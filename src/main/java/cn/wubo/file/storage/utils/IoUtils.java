package cn.wubo.file.storage.utils;

import cn.wubo.file.storage.exception.IORuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@Slf4j
public class IoUtils {

    private IoUtils() {
    }

    /**
     * 默认缓存大小 8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    public static byte[] readBytes(MultipartFile multipartFile) {
        try (InputStream is = multipartFile.getInputStream()) {
            return readBytes(is, false);
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] readBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 从流中读取bytes，读取完毕后关闭流
     *
     * @param is {@link InputStream}
     * @return bytes
     * @throws IORuntimeException IO异常
     */
    public static byte[] readBytes(InputStream is) {
        return readBytes(is, true);
    }

    /**
     * 从流中读取bytes
     *
     * @param is      输入流
     * @param isClose 是否关闭输入流
     * @return bytes 内容bytes
     * @throws IORuntimeException IO异常
     */
    public static byte[] readBytes(InputStream is, Boolean isClose) {
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            copy(is, os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        } finally {
            if (Boolean.TRUE.equals(isClose)) {
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

    /**
     * 拷贝文件到输出流，拷贝后不关闭输出流
     *
     * @param file 文件
     * @param os   输出流
     * @throws IOException-IO异常
     */
    public static void writeToStream(File file, OutputStream os) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            copy(is, os);
        }
    }

    /**
     * 拷贝bytes到输出流，拷贝后不关闭输出流
     *
     * @param bytes 内容bytes
     * @param os    输出流
     * @throws IOException-IO异常
     */
    public static void writeToStream(byte[] bytes, OutputStream os) throws IOException {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            copy(is, os);
        }
    }

    /**
     * 拷贝流，拷贝后不关闭流
     * 文件流直接读取效率更高
     *
     * @param is 输入流
     * @param os 输出流
     * @throws IOException-IO异常
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
        if (is instanceof FileInputStream) {
            // 文件流的长度是可预见的，此时直接读取效率更高
            int available = is.available();
            byte[] result = new byte[available];
            int readLength = is.read(result);
            if (readLength != available) {
                throw new IOException(String.format("File length is [%s] but read [%s]!", available, readLength));
            }
            os.write(result);
        } else {
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        }
        os.flush();
    }
}
