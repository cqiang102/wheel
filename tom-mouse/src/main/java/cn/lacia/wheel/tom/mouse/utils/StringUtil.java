package cn.lacia.wheel.tom.mouse.utils;

/**
 * @author 你是电脑
 * @create 2019/11/28 - 19:24
 */
public class StringUtil {
    public static boolean isNull(String str){
        if (str == null) {
            return true;
        }
        return "".equals(str.trim());
    }
    public static boolean isNotNull(String str){
        return !(isNull(str));
    }
}
