package com.lemonxq_laplace.pregnantmonitor;

/**
 * @author 官网：http://www.93sec.cc
 *         <p>
 *         郑传伟编写：     微博：http://weibo.com/93sec.cc
 * @version V1.0正式版    数据封装类
 * @process
 * @Note
 * @dateTime 2015-10-18下午2:27:00
 */
public class ListData_robot {
    private String content;//数据内容

    //为了使得到数据与发送数据进行区分
    public static final int SEND = 1;
    public static final int RECEIVER = 2;
    private int flag;
    //	显示时间
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ListData_robot(String content, int flag, String time) {
        setContent(content);
        setFlag(flag);
        setTime(time);

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
