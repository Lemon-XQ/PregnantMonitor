package com.lemonxq_laplace.pregnantmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 官网：http://www.93sec.cc
 *
 *         郑传伟编写： 微博：http://weibo.com/93sec.cc
 *
 * @version V1.0正式版
 *
 * @process
 *
 * @Note
 *
 * @dateTime 2015-10-17下午5:02:26
 *
 */
public class TwoFragment extends Fragment implements IHttpGetListener_robot,
        OnClickListener {
    private HttpData_robot httpDataRobot;

    private List<ListData_robot> lists;

    private ListView lv;

    private String str;
    private EditText gettext;
    private Button btn_send;

    private TextAdapter_robot adapter;

    private double currentTime;
    private double oldTime = 0;

    String welcome_array[];

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main_robot,null);
        lv = (ListView) view.findViewById(R.id.lv);
        gettext = (EditText) view.findViewById(R.id.sendText);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        lists = new ArrayList<ListData_robot>();
        btn_send.setOnClickListener(this);
        adapter = new TextAdapter_robot(lists, getActivity());
        lv.setAdapter(adapter);
        ListData_robot listDataRobot;
        listDataRobot = new ListData_robot(getRandomWelcomTips(), ListData_robot.RECEIVER,
                getTime());
        lists.add(listDataRobot);
        return view;

    }

    public void initView() {
        lv = (ListView) getView().findViewById(R.id.lv);
        gettext = (EditText) getView().findViewById(R.id.sendText);
        btn_send = (Button) getView().findViewById(R.id.btn_send);
        lists = new ArrayList<ListData_robot>();
        btn_send.setOnClickListener(this);
        adapter = new TextAdapter_robot(lists, getActivity());
        lv.setAdapter(adapter);
        ListData_robot listDataRobot;
        listDataRobot = new ListData_robot(getRandomWelcomTips(), ListData_robot.RECEIVER,
                getTime());
        lists.add(listDataRobot);
    };
    // 这是实现接口中的方法
    public void getDataUrl(String data) {
        // TODO Auto-generated method stub
        // Log.d("1507", data);
        pareText(data);

    }


    private String getRandomWelcomTips() {
        String welcome = null;
        welcome_array = this.getResources().getStringArray(
                R.array.welcome_trips);
        int index = (int) ((Math.random()) * (welcome_array.length - 1));
        welcome = welcome_array[index];
        return welcome;

    }

    // 解析方法
    public void pareText(String str) {
        try {
            JSONObject jb = new JSONObject(str);
            Log.d("1507", jb.getString("code"));
            Log.d("1507", jb.getString("text"));
            ListData_robot listDataRobot;
            listDataRobot = new ListData_robot(jb.getString("text"), ListData_robot.RECEIVER,
                    getTime());
            lists.add(listDataRobot);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 点击发送
    public void onClick(View v) {

        getTime();
        // TODO Auto-generated method stub

        str = gettext.getText().toString();
        gettext.setText("");
        String dropk = str.replace(" ", "");
        String droph = dropk.replace("\n", "");
        ListData_robot listDataRobot;
        listDataRobot = new ListData_robot(str, ListData_robot.SEND, getTime());
        lists.add(listDataRobot);
        if (lists.size() > 30) {
            for (int i = 0; i < lists.size(); i++) {
                lists.remove(i);
            }
        }
        // 更新界面
        adapter.notifyDataSetChanged();
        httpDataRobot = (HttpData_robot) new HttpData_robot(
                "http://www.tuling123.com/openapi/api?key=b44db554abaa4ffca9df330b3706a879&info="
                        + droph, this).execute();
    }

    // 得到当前时间
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curData = new Date();
        String str = format.format(curData);
        if (currentTime - oldTime >= 5 * 60 * 1000) {
            oldTime = currentTime;
            return str;
        } else {
            return "";
        }
    }
}

/**
 * A simple {@link Fragment} subclass.
 */
