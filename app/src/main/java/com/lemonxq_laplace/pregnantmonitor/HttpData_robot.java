package com.lemonxq_laplace.pregnantmonitor;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author 官网：http://www.93sec.cc
 *         <p>
 *         郑传伟编写：     微博：http://weibo.com/93sec.cc
 * @version V1.0正式版    异步任务解析数据类
 * @process
 * @Note
 * @dateTime 2015-10-18下午2:25:37
 */
public class HttpData_robot extends AsyncTask<String, Void, String> {
    private HttpGet mHttpGet;
    private HttpResponse mHttpResponse;
    private HttpClient mHttpClient;
    private String url;
    private InputStream is;
    private HttpEntity mHttpEntity;

    private IHttpGetListener_robot listener;

    public HttpData_robot(String url, IHttpGetListener_robot listener) {
        super();
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        try {
            mHttpClient = new DefaultHttpClient();
            mHttpGet = new HttpGet(url);
            mHttpResponse = mHttpClient.execute(mHttpGet);
            mHttpEntity = mHttpResponse.getEntity();
            is = mHttpEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);

            }
            return sb.toString();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    //接口回调返回得到的数据
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        listener.getDataUrl(result);
        super.onPostExecute(result);
    }

}
