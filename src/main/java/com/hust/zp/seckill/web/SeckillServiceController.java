package com.hust.zp.seckill.web;

import com.hust.zp.seckill.dto.Exposer;
import com.hust.zp.seckill.dto.SeckillExecution;
import com.hust.zp.seckill.dto.SeckillResult;
import com.hust.zp.seckill.entity.Seckill;
import com.hust.zp.seckill.enums.SeckillStateEnum;
import com.hust.zp.seckill.exception.RepeatSeckillException;
import com.hust.zp.seckill.exception.SeckillCloseException;
import com.hust.zp.seckill.exception.SeckillException;
import com.hust.zp.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Zp on 2018/3/6.
 */
@Controller //和@Service @Component 一样的注解，让Spring容器将其识别为Controller
@RequestMapping("/seckill") //设置url的规范，url:/模块/资源/{id}/... 如/seckill/list
public class SeckillServiceController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    //获取列表页
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        //list.jsp + model = ModelAndView
        //WEB-INF/jsp/"list".jsp
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) return "redirect:/seckill/list";

        Seckill seckill = seckillService.queryById(seckillId);
        if (seckill == null) return "redirect:/seckill/list";

        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody//表明返回数据会被封装成json
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execute",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long userPhone) {

        if(userPhone == null){
            return new SeckillResult<SeckillExecution>(true,"手机号码未注册");
        }
        SeckillExecution execution;
        try{
            execution = seckillService.seckillExecution(seckillId, userPhone, md5);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch(RepeatSeckillException e){
            execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEATE_SECKILL);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch(SeckillCloseException e){
            execution = new SeckillExecution(seckillId,SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            execution = new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date date = new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }
}
