package com.lemonxq_laplace.pregnantmonitor.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/9
 */

public class SharedPreferencesUtil {

    private Context context;
    private String FILE_NAME = "UserData";

    public SharedPreferencesUtil(Context context) {
        this.context=context;
    }

    public SharedPreferencesUtil(Context context,String fileName) {
        this.context=context;
        this.FILE_NAME = fileName;
    }

    /**
     * 保存数据，需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void setParam(String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if ("String".equals(type)) {
            editor.putString(key, object.toString());
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.apply();
    }

    /**
     * 得到保存数据
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return pref.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return pref.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return pref.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return pref.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return pref.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除保存数据
     *
     * @param key
     * @return
     */
    // Delete
    public void remove( String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
