package com.lemonxq_laplace.pregnantmonitor.Util;

import com.lemonxq_laplace.pregnantmonitor.Data.User;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/10
 */

public class UserManager {
    private static User mUser;

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
