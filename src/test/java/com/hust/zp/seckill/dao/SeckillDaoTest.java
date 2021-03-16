package com.hust.zp.seckill.dao;

import com.hust.zp.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zp on 2018/3/3.
 */

//配置spring和junit的整合，junit启动时夹在springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit，spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao的实现类
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        System.out.println(seckillDao.reduceNumber(1000L,killTime));
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000L;
        System.out.println(seckillDao.queryById(id));
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> list = new ArrayList<Seckill>();
        list = seckillDao.queryAll(0,100);
        for (Seckill seckill:list) {
            System.out.println(seckill);
        }
    }

}