package com.lemonxq_laplace.pregnantmonitor.Util;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/11
 */

public class Consts {
    public static String URL = "http://39.108.183.228:10240/PregnantMonitor/";
    public static String URL_Register = URL + "RegisterServlet";
    public static String URL_Login = URL + "LoginServlet";

    // 服务器返回代码
    public static String ERRORCODE_NULL = "200";
    public static String ERRORCODE_PWD = "201";
    public static String ERRORCODE_ACCOUNTNOTEXIST = "202";
    public static String ERRORCODE_ACCOUNTEXIST = "203";
    public static String ERRORCODE_INSERT = "204";
    public static String SUCCESSCODE_LOGIN = "100";
    public static String SUCCESSCODE_REGISTER = "101";

    // 代码对应信息
    public static String ERRORMSG_NULL = "账号或密码不能为空";
    public static String ERRORMSG_PWD = "密码错误";
    public static String ERRORMSG_ACCOUNTNOTEXIST = "账号不存在，请注册";
    public static String ERRORMSG_ACCOUNTEXIST = "账号已存在，请登录";
    public static String ERRORMSG_INSERT = "插入信息失败";
    public static String SUCCESSMSG_LOGIN = "登录成功";
    public static String SUCCESSMSG_REGISTER = "注册成功";
}
