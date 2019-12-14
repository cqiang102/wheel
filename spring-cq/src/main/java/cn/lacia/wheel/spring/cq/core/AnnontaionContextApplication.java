package cn.lacia.wheel.spring.cq.core;

import cn.lacia.wheel.spring.cq.annotation.LaciaApplication;

import java.lang.annotation.Annotation;

/**
 * @author 你是电脑
 * @create 2019/11/28 - 13:44
 */
public class AnnontaionContextApplication {

    private Class clazz;

    public void setScanClass(Class clazz){
        this.clazz = clazz;
    }
    /**
     * 根据 Class 上的注解获得包扫描路径
     * @param clazz
     */
    private void getScannerUrl(Class clazz) {
        Annotation annotation = clazz.getAnnotation(LaciaApplication.class);
        System.out.println(annotation);
    }

    public void run() {

        // 扫描包
        getScannerUrl(this.clazz);
        // 实例化
        // 依赖注入
        // 注册 HandlerMapping
    }
}
