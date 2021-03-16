package com.hust.zp.seckill.exception;

import com.hust.zp.seckill.dto.SeckillExecution;
import com.hust.zp.seckill.entity.SuccessKilled;

/**
 * 重复秒杀异常
 * Created by Zp on 2018/3/3.
 */
public class RepeatSeckillException extends SeckillException {
    public RepeatSeckillException(String message) {
        super(message);
    }

    public RepeatSeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
