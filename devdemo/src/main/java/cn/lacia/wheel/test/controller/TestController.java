package cn.lacia.wheel.test.controller;

import cn.lacia.wheel.spring.cq.annotation.LaciaController;
import cn.lacia.wheel.test.service.TestService;

import javax.annotation.Resource;

/**
 * @author 你是电脑
 * @create 2019/12/15 - 0:14
 */
@LaciaController
public class TestController {

    @Resource
    private TestService testService1;

    public String test(String test){
        return testService1.test(test)+this.getClass().getSimpleName();
    }
}
