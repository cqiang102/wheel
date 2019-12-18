package cn.lacia.wheel.spring.cq.core;

import cn.lacia.wheel.spring.cq.annotation.LaciaApplication;
import cn.lacia.wheel.spring.cq.annotation.LaciaController;
import cn.lacia.wheel.spring.cq.annotation.LaciaService;
import cn.lacia.wheel.tom.mouse.utils.StringUtil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 你是电脑
 * @create 2019/11/28 - 13:44
 */
public class AnnontaionContextApplication {

    private String baseScanUrl = "";
    private Class<?> clazz;
    private List<String> classNames = new LinkedList<>();
    private Map<String, Object> instance = new HashMap<>();

    /**
     * 根据 Class 上的注解获得包扫描路径
     *
     * @param clazz
     */
    public void setScanClass(Class<?> clazz) {
        this.clazz = clazz;
        String value = clazz.getAnnotation(LaciaApplication.class).value();
        if (StringUtil.isNull(value)) {
            value = clazz.getName().replace(".".concat(clazz.getSimpleName()), "");
        }
        this.baseScanUrl = value;
    }

    private void doScanner(String packageName) {
        String tempString = packageName.replaceAll("\\.", "/");
        URL url = clazz.getClassLoader().getResource(tempString);

        assert url != null;
        File file = new File(url.getFile());
        if (file.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(f -> {
                if (f.isDirectory()) {
                    doScanner(packageName + "." + f.getName());
                } else {
                    classNames.add(packageName + "." + f.getName().replace(".class", ""));
                }
            });
        }
    }

    public void run() {
        // 扫描包
        doScanner(baseScanUrl);
        // 实例化
        doInstance();
        // 依赖注入
        doDependencyInjection();
        // 注册 HandlerMapping
    }

    private void doDependencyInjection() {
        instance.forEach((key, val) -> {
            System.out.println(key.concat(" : ").concat(val.toString()));
            Arrays.asList(val.getClass().getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
//                System.out.println(field.getName());
                // 根据名称注入
                Object o = instance.get(field.getName());

                // 根据类型注入
                if (o == null) {
                    o = instance.get(field.getType().getTypeName());
                }
                try {
                    field.set(val, o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void doInstance() {
        classNames.forEach(className -> {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (aClass != null) {
                // Service 实例化
                if (aClass.isAnnotationPresent(LaciaService.class)) {
                    String beanName = aClass.getAnnotation(LaciaService.class).value();
                    if (StringUtil.isNull(beanName)) {
                        beanName = headLowercase(aClass.getSimpleName());
                    }
                    Object bean = null;
                    try {
                        bean = aClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    instance.put(beanName, bean);
                    // 如果有接口，把接口的类型也作为 一个 Key
                    for (Class<?> anInterface : aClass.getInterfaces()) {
                        instance.put(anInterface.getTypeName(), bean);
                    }
                }
                // Controller 实例化
                if (aClass.isAnnotationPresent(LaciaController.class)) {
                    String beanName = headLowercase(aClass.getSimpleName());
                    Object bean = null;
                    try {
                        bean = aClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    instance.put(beanName, bean);
                }

            }
        });
    }

    /**
     * 把首字母变成小写
     *
     * @param simpleName
     * @return
     */
    private String headLowercase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        if (chars[0] > 'A' - 1 && chars[0] < 'Z' - 1) {
            chars[0] = (char) (chars[0] + 32);
        }
        return String.valueOf(chars);
    }
}
