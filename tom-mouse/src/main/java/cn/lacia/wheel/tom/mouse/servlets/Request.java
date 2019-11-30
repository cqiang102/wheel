package cn.lacia.wheel.tom.mouse.servlets;


import cn.lacia.wheel.tom.mouse.utils.StringUtil;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author 你是电脑
 * @create 2019/11/28 - 16:14
 */

public class Request implements HttpServletRequest {
    private String method = null;
    private String url = null;
    private String version = null;
    private String body = null;
    private InputStream inputStream = null;
    private Map<String,String> headers = new HashMap<>();
    private Map<String,String> parms = new HashMap<>();
    private Socket socket = null;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", body='" + body + '\'' +
                ", inputStream=" + inputStream +
                ", headers=" + headers +
                ", parms=" + parms +
                '}';
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParms() {
        return parms;
    }

    public void setParms(Map<String, String> parms) {
        this.parms = parms;
    }

    /**
     * 返回用来保护 servlet 的认证方案名，如果 servlet 未受保护则返回 null。
     * @return
     */
    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        String cookie = headers.get("Cookie");
        String[] split = cookie.split("&");
        Cookie[] cookies = new Cookie[split.length];
        for (int i = 0; i < split.length; i++) {
            String[] temp = split[i].split("=");
            cookies[i] = new Cookie(temp[0],temp[1]);
        }
        return cookies;
    }

    /**
     * 返回构建 Date 对象的 long 值。如果不包含 header，则返回 -1。
     * @param name
     * @return
     */
    @Override
    public long getDateHeader(String name) {
        String date = headers.get(name);
        if (StringUtil.isNotNull(date)){
            return  new Date(date).getTime();
        }
        return -1;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new Enumeration<String>(){

            @Override
            public boolean hasMoreElements() {
                return headers.entrySet().iterator().hasNext();
            }

            @Override
            public String nextElement() {
                return headers.entrySet().iterator().next().getKey();
            }
        };
    }

    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    /**
     * 返回与客户端发出此请求时发送的URL关联的任何其他路径信息。
     * 额外的路径信息在 servlet 路径之后，但在查询字符串之前，并以“ /”字符开头。
     * 如果没有多余的路径信息，则此方法返回null。
     */
    @Override
    public String getPathInfo() {
        return null;
    }

    /**
     * getPathTranslated返回servlet名字之后，查询之前的路径信息
     * @return
     */
    @Override
    public String getPathTranslated() {
        return null;
    }

    /**
     * 返回当前页面所在的应用的名字。
     * @return
     */
    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        String s = null;
        try {
            s = url.split("\\?")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        String s = headers.get("Content-Length");
        if (s==null){
            s=headers.get("ContentLength");
        }
        return Integer.parseInt(s);
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        String s = headers.get("Content-Type");
        if (s==null){
            s=headers.get("ContentType");
        }
        return s;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return (ServletInputStream) this.inputStream;
    }

    @Override
    public String getParameter(String name) {
        String[] split = body.split("&");
        for (String s : split) {
            if(s.split("=")[0].equals(name)){
                return s.split("=")[1];
            }
        }
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] split = body.split("&");
        String[] parmVal = new String[split.length];
        int i = 0;
        for (String s : split) {
            if(s.split("=")[0].equals(name)){
                parmVal[i++]=s.split("=")[1];
            }
        }
        return parmVal;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return headers.get("Server");
    }

    @Override
    public int getServerPort() {
        return socket.getPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return socket.getLocalSocketAddress().toString();
    }

    @Override
    public String getLocalAddr() {
        return socket.getLocalAddress().toString();
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
