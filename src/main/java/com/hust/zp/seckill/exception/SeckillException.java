package com.hust.zp.seckill.exception;

/**
 * Created by Zp on 2018/3/3.
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
