package com.hust.zp.seckill.service;

import com.hust.zp.seckill.dto.Exposer;
import com.hust.zp.seckill.dto.SeckillExecution;
import com.hust.zp.seckill.entity.Seckill;
import com.hust.zp.seckill.exception.RepeatSeckillException;
import com.hust.zp.seckill.exception.SeckillCloseException;
import com.hust.zp.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在“使用者”的角度设计接口
 * 定义接口方法考虑三个方面：方法定义粒度，参数，返回类型（return/异常）
 * Created by Zp on 2018/3/3.
 */
public interface SeckillService {

    /**
     * 查询所有的秒杀商品
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询相应的秒杀商品
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址
     * 否则输出系统时间和秒杀开启时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution seckillExecution(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatSeckillException,SeckillCloseException;
}
