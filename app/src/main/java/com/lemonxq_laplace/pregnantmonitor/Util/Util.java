package com.lemonxq_laplace.pregnantmonitor.Util;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/12
 * @description: 功能类，封装一些功能函数
 */

public class Util {

    /**
     * @description 字符串处理，防止SQL注入
     * @param input
     * @return
     */
    public static String StringHandle(String input){
        String output;
        // 将包含有 单引号(')，分号(;) 和 注释符号(--)的语句替换掉
        output = input.trim().replaceAll(".*([';]+|(--)+).*", " ");
        return output;
    }
}
