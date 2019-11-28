package cn.lacia.wheel.tom.mouse;

import cn.lacia.wheel.tom.mouse.thread.ClientCallable;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Socket> sockets = new ArrayList<>();
    private ExecutorService pool;
    private int port = 8080;
    public void start(){
        // 开启Server
        logger.debug("server start");
        System.out.println("????????");
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

}


