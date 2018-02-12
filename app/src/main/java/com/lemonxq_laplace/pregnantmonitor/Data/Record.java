package com.lemonxq_laplace.pregnantmonitor.Data;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/5
 */

public class Record extends DataSupport{
    private int id;
    private User user;// 一条记录对应一个用户
    private Date date;
    private int age;
    private float weight;
    private float height;
    private float ogtt;
    private boolean isHealthy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getOgtt() {
        return ogtt;
    }

    public void setOgtt(float ogtt) {
        this.ogtt = ogtt;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthy(boolean healthy) {
        isHealthy = healthy;
    }
}
