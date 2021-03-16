package com.hust.zp.seckill.service;

import com.hust.zp.seckill.dao.SeckillDao;
import com.hust.zp.seckill.dto.Exposer;
import com.hust.zp.seckill.dto.SeckillExecution;
import com.hust.zp.seckill.entity.Seckill;
import com.hust.zp.seckill.exception.RepeatSeckillException;
import com.hust.zp.seckill.exception.SeckillCloseException;
import com.hust.zp.seckill.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zp on 2018/3/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.queryById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void testSeckillLogic() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);

        if (exposer.isExposed()) {
            long userPhone = 1245380L;
            String md5 = exposer.getMd5();
            logger.info("md5={}", md5);

            try {
                SeckillExecution seckillExecution = seckillService.seckillExecution(id, userPhone, md5);
                logger.info("seckillExecution={}", seckillExecution);
            } catch (RepeatSeckillException e1) {
                logger.error(e1.getMessage());
            } catch (SeckillCloseException e2) {
                logger.error(e2.getMessage());
            }
        }else{
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }


    }
}