package cn.lacia.wheel.tom.mouse.thread;

import cn.lacia.wheel.tom.mouse.TomMouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * @author 你是电脑
 * @create 2019/11/28 - 13:57
 */
public class ClientCallable implements Callable<Void> {
    private static final Logger logger = LoggerFactory.getLogger(ClientCallable.class);
    private Socket socket;

    public ClientCallable() {
    }

    public ClientCallable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Void call() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (socket == null) {
            logger.error("socket null error");
        }
        // 连接成功
        else {
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                logger.error("getInputStream error");
                e.printStackTrace();
            }
            if (inputStream == null) {
                logger.error("inputStream null error");
            }
            // 成功取得输入流
            else {
                //长度
                int size = 0;
                // 接收的数据包
                byte[] data = null;
                // 超时时间 3 s
                long timeOut = System.currentTimeMillis() + 3000;
                // 等待数据包
                while (size == 0) {
                    if (timeOut < System.currentTimeMillis()) {
                        logger.error("connect timeout");
                        break;
                    }
                    try {
                        size = inputStream.available();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(size>0){
                    data = new byte[size];
                    try {
                        int readSize = inputStream.read(data);
                        logger.debug("readSize : {} , data : \n{}", readSize, new String(data));
                        logger.debug("============================================================");
                    } catch (IOException e) {
                        logger.error("read data error");
                        e.printStackTrace();
                    }
                    try {
                        outputStream = socket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 读取请求信息成功
                    // TODO 解析请求首行 判断是否符合规则，资源路径是否存在
                    // inputStream 塞进 request , outputStream 塞进 response
                    // TODO 解析完整请求信息，分发到 Servlet .
                    // TODO 接收到 Servlet 响应，把 Response 对象解析成响应数据
                }

            }
            String testData = "HTTP/1.1 200 OK\nContext-type:text/html;charset=utf-8;\n\n<h1>testData</h1>";

            try {
                assert outputStream != null;
                outputStream.write(testData.getBytes());
                logger.debug("testData : \n{}", testData);
                logger.debug("============================================================");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 断开连接

        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                TomMouse.sockets.remove(socket);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
