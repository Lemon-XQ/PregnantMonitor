package com.lemonxq_laplace.pregnantmonitor.Util;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/11
 */

public class Consts {
    private static String URL = "http://服务器IP:端口/PregnantMonitor/";
    public static String URL_Register = URL + "RegisterServlet";
    public static String URL_Login = URL + "LoginServlet";
    public static String URL_Analyse = URL + "AnalyseServlet";
    public static String URL_ModifyPwd = URL + "ModifyPwdServlet";
    public static String URL_UploadImg = URL + "UploadImgServlet";
    public static String URL_DownloadImg = URL + "DownloadImgServlet";
    public static String URL_GetInfo = URL + "GetInfoServlet";
    public static String URL_SetInfo = URL + "SetInfoServlet";

    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int RESIZE_REQUEST_CODE = 2;
    public static final String IMAGE_FILE_NAME="tmp_avatar.jpg";

    // 服务器代码
    public static String ERRORCODE_NULL = "200";
    public static String ERRORCODE_PWD = "201";
    public static String ERRORCODE_ACCOUNTNOTEXIST = "202";
    public static String ERRORCODE_ACCOUNTEXIST = "203";
    public static String ERRORCODE_INSERT = "204";
    public static String ERRORCODE_FORMAT = "205";
    public static String ERRORCODE_AGE_INVALID = "206";
    public static String ERRORCODE_HEIGHT_INVALID = "207";
    public static String ERRORCODE_WEIGHT_FORMAT = "208";
    public static String ERRORCODE_OGTT_FORMAT = "209";

    public static String SUCCESSCODE_LOGIN = "100";
    public static String SUCCESSCODE_REGISTER = "101";
    public static String SUCCESSCODE_GDMANALYSE = "102";// GDM分析完成
    public static String SUCCESSCODE_MODIFYPWD = "103";
    public static String SUCCESSCODE_UPLOADIMG = "104";
    public static String SUCCESSCODE_DOWNLOADIMG = "105";
    public static String SUCCESSCODE_GETINFO = "106";
    public static String SUCCESSCODE_SETINFO = "107";

    public static String REQUESTCODE_NICKNAME = "1";
    public static String REQUESTCODE_HEIGHT = "2";
    public static String REQUESTCODE_BIRTHDATE = "3";
    public static String REQUESTCODE_PREGNANTDATE = "4";
    public static String REQUESTCODE_RECORD = "5";

    // 代码对应信息
    public static String ERRORMSG_NULL = "不能为空";
    public static String ERRORMSG_PWD = "密码错误";
    public static String ERRORMSG_ACCOUNTNOTEXIST = "账号不存在，请注册";
    public static String ERRORMSG_ACCOUNTEXIST = "账号已存在，请登录";
    public static String ERRORMSG_INSERT = "插入信息失败";

    public static String SUCCESSMSG_LOGIN = "登录成功";
    public static String SUCCESSMSG_REGISTER = "注册成功";
    public static String SUCCESSMSG_DIAB = "血糖水平不正常，注意少食多餐@_@";
    public static String SUCCESSMSG_NOTDIAB = "您的身体的血糖水平很健康=w=";

    // 客户端提示信息
    public static String ERROR_FORMAT = "输入数据格式错误";
    public static String AGE_INVALID = "请输入合法年龄值（1-100）";
    public static String HEIGHT_INVALID = "请输入合法身高（1.0-2.0）";
    public static String WEIGHT_INVALID = "请输入合法体重（>0）";
    public static String OGTT_INVALID = "请输入合法空腹血糖值（>0）";
}
