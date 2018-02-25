package com.lemonxq_laplace.pregnantmonitor.Util;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

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

    /**
     * Toast封装
     * @param context
     * @param msg
     */
    public static void makeToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据生日推算年龄
     * @param birthDay
     * @return
     */
    public static int getAgeFromDate(Date birthDay){
        int age;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDay);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }

    /**
     * 根据怀孕日期推算怀孕天数、周数、月数
     * @param pregnantDate
     * @return
     */
    public static long getPregnantDays(Date pregnantDate){
        Date currentDate = new Date();
        return (currentDate.getTime()-pregnantDate.getTime())/(24*60*60*1000);
    }

    public static long getPregnantWeeks(Date pregnantDate){
        return getPregnantDays(pregnantDate)/7;
    }

    public static long getPregnantMonths(Date pregnantDate){
        return getPregnantDays(pregnantDate)/30;
    }

    /**
     * Date中提取年月日
     * @param date
     * @return
     */
    public static int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }
    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据日期构造Date
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date formDate(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        return calendar.getTime();
    }
    public static Date formDate(com.haibin.calendarview.Calendar cal){
        Calendar calendar = Calendar.getInstance();
        calendar.set(cal.getYear(),cal.getMonth()-1,cal.getDay());
        return calendar.getTime();
    }
    public static Date formDate(int year,int month,int day,int hour,int min){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,hour,min);
        return calendar.getTime();
    }

    /**
     * 获取诊断结果字符串
     * @param res
     * @return
     */
    public static String getResultStr(Boolean res){
        return res?"正常":"有妊娠期糖尿病风险";
    }

    /**
     * 将date转换为calendar
     * @param date
     * @return
     */
    public static com.haibin.calendarview.Calendar dateToCalendar(Date date){
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setDay(getDay(date));
        calendar.setMonth(getMonth(date)+1);
        calendar.setYear(getYear(date));
        calendar.setSchemeColor(Color.WHITE);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme("");
        return calendar;
    }

    /**
     * 判断sd卡是否存在
     * @return
     */
    public static boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
