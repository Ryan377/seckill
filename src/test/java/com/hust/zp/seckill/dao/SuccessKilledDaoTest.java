package com.hust.zp.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Zp on 2018/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001;
        long userPhone = 13567896;
        System.out.println(successKilledDao.insertSuccessKilled(id,userPhone));
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        System.out.println(successKilledDao.queryByIdWithSeckill(1001L,13567896L));
    }

}