package com.lemonxq_laplace.pregnantmonitor.Util;

import android.util.Log;

import com.lemonxq_laplace.pregnantmonitor.Data.Record;
import com.lemonxq_laplace.pregnantmonitor.Data.User;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/11
 */

public class Database {

    /**
     * 通过用户名查找用户
     * @param name
     * @return
     */
    public static User findUserByName(String name){DataSupport.delete(User.class,1);
        return DataSupport.where("account=?",name)
                                .findFirst(User.class);
    }

    /**
     * 通过用户名查找用户怀孕日期
     * @param name
     * @return
     */
    public static Date findPregnantDateByUsername(String name){
        return DataSupport.select("pregnantDate")
                          .where("account=?",name)
                          .findFirst(User.class).getPregnantDate();
    }

    /**
     * 通过用户名查找用户生日
     * @param name
     * @return
     */
    public static Date findBirthDateByUsername(String name){
        return DataSupport.select("birthDate")
                .where("account=?",name)
                .findFirst(User.class).getPregnantDate();
    }

    /**
     * 查找用户所有检测记录
     * @param user
     * @return
     */
    public static List<Record> findRecords(User user){
        return DataSupport.where("user_id=?",String.valueOf(user.getId()))
                          .find(Record.class);
    }

    /**
     * 查找用户在某一天的检测记录（只记录当天最后一条检测记录）
     * @param dateStr
     * @param user
     * @return
     */
    public static Record findRecord(String dateStr,User user){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(Long.parseLong(dateStr));
        calendar.setTime(date);

        // 找当天0点及23:59分时的date，beginDate-1000是因为不能精确到毫秒，有一定误差
        String beginDate = String.valueOf(Util.formDate(calendar.get(Calendar.YEAR),
                                         calendar.get(Calendar.MONTH),
                                         calendar.get(Calendar.DATE),0,0,0).getTime()-1000);
        String endDate = String.valueOf(Util.formDate(calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DATE),
                                       23,59,59).getTime());
        String beginDateStr = Util.formDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),0,0,0).toString();
        String endDateStr = Util.formDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                23,59,59).toString();
        Log.d("DATABASE",date.getTime()+"-"+beginDate+"-"+endDate);
        return DataSupport.where("date>=? and date<=? and user_id=?",
                                    beginDate,endDate,String.valueOf(user.getId()))
                          .findFirst(Record.class);
    }
}
