package com.lemonxq_laplace.pregnantmonitor.Util;

import com.lemonxq_laplace.pregnantmonitor.Data.User;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/10
 */

public class UserManager {
    private static User mUser;
//
//    public static void initUser(){
//        Log.d("UserManager","INIT USER");
//        User user = UserManager.getCurrentUser();
//        List<Record> recordList = Database.findRecords(user);
//        if(!recordList.isEmpty()){// 本地数据库中有记录
//            user.getRecordList().addAll(recordList);
//            user.save();
//        }else{
//            // 服务器端加载记录
//
//        }
//    }

    public static User getCurrentUser(){
        if(mUser == null){
            mUser = new User();
        }
        return mUser;
    }

    public static void setCurrentUser(User user){
        mUser = user;
    }

    public static void clear(){
        mUser = null;
    }
}
