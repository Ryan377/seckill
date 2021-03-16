package com.hust.zp.seckill.service.impl;

import com.hust.zp.seckill.dao.SeckillDao;
import com.hust.zp.seckill.dao.SuccessKilledDao;
import com.hust.zp.seckill.dto.Exposer;
import com.hust.zp.seckill.dto.SeckillExecution;
import com.hust.zp.seckill.entity.Seckill;
import com.hust.zp.seckill.entity.SuccessKilled;
import com.hust.zp.seckill.enums.SeckillStateEnum;
import com.hust.zp.seckill.exception.RepeatSeckillException;
import com.hust.zp.seckill.exception.SeckillCloseException;
import com.hust.zp.seckill.exception.SeckillException;
import com.hust.zp.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Zp on 2018/3/4.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //盐值，用于混淆seckillId产生MD5
    private final String slat = "dhfj7787y298&^doiweu";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    /**
     * 查询所有秒杀商品列表
     *
     * @return
     */
    public List<Seckill> getSeckillList() {
        List<Seckill> list = new ArrayList<Seckill>();
        list = seckillDao.queryAll(0, 10);
        return list;
    }

    /**
     * 根据seckillId查询对应单个秒杀商品
     *
     * @param seckillId
     * @return
     */
    public Seckill queryById(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        return seckill;
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化
        Seckill seckill = seckillDao.queryById(seckillId);

        Date nowTime = new Date();

        Date startTime = seckill.getStartTime();

        Date endTime = seckill.getEndTime();


        if (seckill == null) {
            return new Exposer(false, seckillId);
        } else {
            if (nowTime.getTime() < startTime.getTime()
                    || nowTime.getTime() > endTime.getTime()) {
                return new Exposer(false, seckillId, nowTime.getTime(),
                        startTime.getTime(), endTime.getTime());
            } else {
                String md5 = getMD5(seckillId);
                return new Exposer(true, md5, seckillId);
            }
        }
    }

    /**
     * 产生md5
     *
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId) {
        String mix = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(mix.getBytes());
        return md5;
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatSeckillException
     * @throws SeckillCloseException
     */
    @Transactional//使用该注解声明事务，当抛出异常时执行回滚策略
    public SeckillExecution seckillExecution(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatSeckillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("Seckill data has been rewrited!");
        }

        try {

            //秒杀过程是减库存+插入秒杀记录
            Date killTime = new Date();

            int updateCount = seckillDao.reduceNumber(seckillId, killTime);

            //减库存失败，抛出秒杀关闭异常
            //这里不关心是库存为0，还是秒杀未开始或已经结束了
            //从用户层面来说，用户要知道秒杀关闭了就好了
            if (updateCount <= 0) {
                throw new SeckillCloseException("Seckill is closed!");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);

                //重复秒杀
                if (insertCount <= 0) {
                    throw new RepeatSeckillException("Seckill repeated!");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (RepeatSeckillException e1) {
            throw e1;
        } catch (SeckillCloseException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //将所有编译期异常转化为运行期异常
            throw new SeckillException("Seckill inner error: " + e.getMessage());
        }
    }
}
