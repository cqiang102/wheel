package cn.lacia.wheel.spring.cq;

/**
 * @author 你是电脑
 * @create 2019/11/27 - 18:38
 */
public class LaciaSpringApplication {
    public void run(Class clazz,String args){
        //取得包扫描路径
        getScannerUrl(clazz);
    }

    /**
     * 根据 Class 上的注解获得包扫描路径
     * @param clazz
     */
    private void getScannerUrl(Class clazz) {

    }

}
