package cn.lacia.wheel.spring.cq;

import cn.lacia.wheel.spring.cq.core.AnnontaionContextApplication;

/**
 * @author 你是电脑
 * @create 2019/11/27 - 18:38
 */
public class LaciaSpringApplication {
    public static void run(Class clazz,String[] args){

        // 开启 TomCat 通过 往 TomCat 注册一个 Servlet 从servlet 开启 AnnontaionContextApplication，运行 Spring 项目
        // 暂时先直接启动
        AnnontaionContextApplication annontaionContextApplication = new AnnontaionContextApplication();
        annontaionContextApplication.setScanClass(clazz);

        annontaionContextApplication.run();

    }



}
