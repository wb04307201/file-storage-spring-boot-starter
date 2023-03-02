package cn.wubo.file.storage.exception;

public class IORuntimeException extends RuntimeException{

    public IORuntimeException(String message, Throwable throwable) {
        super(String.format("发生IO异常:%s",message), throwable);
    }
}
