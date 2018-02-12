package com.lemonxq_laplace.pregnantmonitor.Data;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Lemon-XQ
 * @date: 2018/2/5
 */

public class User extends DataSupport{
    private int id;
    private String account;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private boolean isVisitor;
    private Date birthDate;
    private Date pregnantDate;
    private float weight;
    private float height;

    private List<Record> recordList = new ArrayList<Record>();// 一个用户对应多条检测记录

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public boolean isVisitor() {
        return isVisitor;
    }

    public void setVisitor(boolean visitor) {
        isVisitor = visitor;
    }
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public Date getPregnantDate() {
        return pregnantDate;
    }

    public void setPregnantDate(Date pregnantDate) {
        this.pregnantDate = pregnantDate;
    }
}
