package cn.wubo.file.storage.exception;

public class FileStorageRuntimeException extends RuntimeException {

    public FileStorageRuntimeException(String message) {
        super(message);
    }

    public FileStorageRuntimeException(String message,Throwable cause) {
        super(message,cause);
    }
}
