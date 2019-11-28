package cn.lacia.wheel.test;

import cn.lacia.wheel.spring.cq.LaciaSpringApplication;
import cn.lacia.wheel.spring.cq.annotation.LaciaApplication;

/**
 * @author 你是电脑
 * @create 2019/11/28 - 13:42
 */
@LaciaApplication
public class TestApplication {
    public static void main(String[] args) {
        LaciaSpringApplication.run(TestApplication.class,args);
    }
}
