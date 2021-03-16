package com.hust.zp.seckill.dao;

import com.hust.zp.seckill.entity.Seckill;
import com.hust.zp.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Zp on 2018/3/2.
 */
public interface SuccessKilledDao {

    /**
     * 往表中插入秒杀成功记录
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    /**
     * 根据seckillId查询SuccessKilled的记录并包含seckill实体对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone") long userPhone);
}
