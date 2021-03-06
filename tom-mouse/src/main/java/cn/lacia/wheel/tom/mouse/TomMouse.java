package cn.lacia.wheel.tom.mouse;

import cn.lacia.wheel.tom.mouse.thread.ClientCallable;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 你是电脑
 * @create 2019/11/27 - 18:35
 */
@Data
public class TomMouse {
    private static final Logger logger = LoggerFactory.getLogger(TomMouse.class);
    private ServerSocket server = null;

    /**
     * 暂存的客户端
     */
    public static List<Socket> sockets = new ArrayList<>();
    /**
     * 连接池
     */
    private ExecutorService pool;
    /**
     * 端口号
     */
    private int port = 8080;
    /**
     * 启动类，用于扫描 servlet 和 静态文件
     */
    private Class clazz;

    private List<String> classNames = new ArrayList<>();
    public void start(){
        // 开启Server
        logger.debug("server start");

        // 创建线程池
        pool = new ThreadPoolExecutor(1, 2, 1000,
                TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            logger.error("server start error");
            e.printStackTrace();

        }
        if (server == null) {
            logger.error("server null error");
            return;
        }
        // 扫描 class 和 静态文件
        if (clazz!=null){
            String replace = clazz.getCanonicalName().replace("." + clazz.getSimpleName(), "");
            logger.info("init scan package : {}",replace);
            doScanner(replace);
        }
        logger.info("classNames : {}",classNames);
        // 注册 ServletMapping
        regMapping();
        //准备完成
        Socket accept = null;
        while (true){
            if(server==null){
                logger.error("server runtime null error");
                break;
            }

            try {
                accept = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (accept == null) {
                logger.error("accept connect null error");
            }else {
                logger.debug("new user connect {} : {}", accept.getInetAddress(),accept.toString());
                // 执行线程
                FutureTask<Void> futureTask = new FutureTask<Void>(new ClientCallable(accept));
                pool.submit(futureTask);
                sockets.add(accept);
            }


        }

    }

    private void regMapping() {
        if (classNames.isEmpty()) {
            logger.error("classNames null error");
        }else {
            classNames.forEach(classNames->{
                Class<?> aClass = null;
                try {
                    aClass = Class.forName(classNames);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (aClass != null) {
                    // 添加了 WebServlet 注解且继承了 HttpServlet 才可以是一个有效的 Servlet
                    if (aClass.isAnnotationPresent(WebServlet.class)&& HttpServlet.class.isAssignableFrom(aClass)) {
                        // 取出相应元数据封装成 ServletHandler
                    }
                }
            });
        }
    }

    private void doScanner(String packageName) {
        URL resource = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        assert resource != null;
        File file = new File(resource.getFile());
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            if (listFile.isDirectory()) {
                doScanner(packageName + "." + listFile.getName());
            } else {
                classNames.add(packageName + "." + listFile.getName().replaceAll(".class", ""));
            }
        }
    }

}


