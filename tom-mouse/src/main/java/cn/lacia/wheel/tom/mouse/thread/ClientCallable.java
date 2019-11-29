package cn.lacia.wheel.tom.mouse.thread;
import java.util.HashMap;

import cn.lacia.wheel.tom.mouse.TomMouse;
import cn.lacia.wheel.tom.mouse.servlets.Request;
import cn.lacia.wheel.tom.mouse.utils.StringUtil;
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
                        break;
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
                    String datas = new String(data);
                    if (StringUtil.isNotNull(datas)){
                    // TODO 解析请求首行 判断是否符合规则
                    logger.info("start parsing the first line");
                    String[] firstLine = firstLinePars(datas.split("\r\n")[0]);
                        if (firstLine!=null) {
                            //解析成功
                            // inputStream 塞进 request , outputStream 塞进 response
                            // TODO 解析完整请求信息
                            Request request = parsingRequest(firstLine,datas,inputStream);
                            logger.info("request : \n{}",request);
                            //  TODO 分发到 Servlet .
                            //   distribution(req,res);
                            // TODO 接收到 Servlet 响应，把 Response 对象解析成响应数据
                        }else{
                            logger.error("请求首行不合法");
                        }


                    }

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
        close(inputStream,outputStream);
        return null;
    }

    private Request parsingRequest(String[] firstLine, String datas, InputStream inputStream) {
        Request request = new Request();
        request.setMethod(firstLine[0]);
        request.setUrl(firstLine[1]);
        request.setVersion(firstLine[2]);
        request.setInputStream(inputStream);

        // 解析路径参数
        HashMap<String, String> parmMap = null;
        String[] urlParms = firstLine[1].split("\\?");
        if(urlParms.length>1){
            parmMap = new HashMap<>();
            String[] parm = urlParms[1].split("&");
            for (String kv : parm) {
                String[] split = kv.split("=");
                parmMap.put(split[0],split[1]);
            }
        }

        // 解析 body
        String[] headerBody = datas.split("\r\n\r\n");
        if (headerBody.length>1){
            request.setBody(headerBody[1]);
        }
        // 解析header
        String[] split = headerBody[0].split("\r\n");
        HashMap<String, String> headerMap = null;
        if(split.length>1){
            headerMap = new HashMap<>();
            for (int i = 1; i < split.length; i++) {
                String[] temp = split[i].split(":");
                headerMap.put(temp[0], temp[1]);
            }

        }


        request.setHeaders(headerMap);
        request.setParms(parmMap);

        return request;
    }

    private void close(InputStream inputStream,OutputStream outputStream) {
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
    }

    private String[] firstLinePars(String firstLine) {
        if (StringUtil.isNotNull(firstLine)){
            String[] split = firstLine.split(" ");
            // 请求首行必须包含 method , resourceUrl , version 所以 length == 3
            if (split.length==3){
                logger.debug("method : {} | url : {} | version : {} ",split[0],split[1],split[2]);
                return split;
            }
        }
        return null;
    }
}
