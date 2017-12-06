package com.lemonxq_laplace.pregnantmonitor.Util;

import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author: Lemon-XQ
 * @date: 2017/11/11
 */

public class HttpUtil {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    /**
     * GET请求（不带参数）
     * @param address
     * @param callback
     */
    public static void sendRequest(String address, okhttp3.Callback callback){
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * GET请求（参数为字符串map）
     * @param address
     * @param params
     * @param callback
     */
    public static void sendRequest(String address, LinkedHashMap<String,String>params, okhttp3.Callback callback){
        // 执行GET请求，将请求结果回调到okhttp3.Callback中
        address = attachHttpGetParams(address,params);
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * POST请求（参数为字符串map）
     * @param address
     * @param params
     * @param callback
     */
    public static void sendPost(String address,LinkedHashMap<String,String> params, okhttp3.Callback callback){
        FormBody.Builder builder = new FormBody.Builder();
        // builder填充参数，构造请求体
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        for (int i=0;i<params.size();i++){
            builder.add(keys.next(),values.next());
            System.out.println(i);
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * POST请求（参数为JSON格式）
     * @param address
     * @param json
     * @param callback
     */
    public static void sendPost(String address, String json, okhttp3.Callback callback) {
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 为HttpGet 的 url 添加多个name value 参数。
     * @param url
     * @param params
     * @return
     */
    private static String attachHttpGetParams(String url, LinkedHashMap<String,String> params){

        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i=0;i<params.size();i++ ) {
            stringBuffer.append(keys.next()+"="+values.next());
            if (i!=params.size()-1) {
                stringBuffer.append("&");
            }
        }
        System.out.println("url:"+ url + stringBuffer.toString());
        return url + stringBuffer.toString();
    }

}
