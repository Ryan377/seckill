package com.hust.zp.seckill.exception;

import com.hust.zp.seckill.dto.SeckillExecution;
import com.hust.zp.seckill.entity.SuccessKilled;

/**
 * 秒杀关闭异常
 * Created by Zp on 2018/3/3.
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
