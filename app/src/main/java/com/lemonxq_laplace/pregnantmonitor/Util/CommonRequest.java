package com.lemonxq_laplace.pregnantmonitor.Util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author: Lemon-XQ
 * @date: 2017/12/6
 */

public class CommonRequest {
    /**
     * 请求码，类似于接口号（可用于区分一类请求下的不同请求，如获取不同信息等）
     */
    private String requestCode;
    /**
     * 请求参数
     * （说明：这里只用一个简单map类封装请求参数，对于请求报文需要上传一个数组的复杂情况需要自己再加一个ArrayList类型的成员变量来实现）
     */
    private HashMap<String, String> requestParam;

    public CommonRequest() {
        requestCode = "";
        requestParam = new HashMap<>();
    }

    /**
     * 设置请求代码，即接口号
     */
    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * 为请求报文设置参数
     * @param paramKey 参数名
     * @param paramValue 参数值
     */
    public void addRequestParam(String paramKey, String paramValue) {
        requestParam.put(paramKey, paramValue);
    }

    /**
     * 将请求报文体组装成json形式的字符串，以便进行网络发送
     * @return 请求报文的json字符串
     */
    public String getJsonStr() {
        JSONObject object = new JSONObject();
        JSONObject param = new JSONObject(requestParam);
        try {
            // "requestCode"、"requestParam"是和服务器约定好的请求体字段名称
            object.put("requestCode", requestCode);
            object.put("requestParam", param);
        } catch (JSONException e) {
            Log.e("Request","请求报文组装异常：" + e.getMessage());
        }
        // 打印原始请求报文
        Log.d("Request",object.toString());
        return object.toString();
    }
}
