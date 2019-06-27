package com.ktp.project.entity;

/**
 * 位置实体
 *
 * @author djcken
 * @date 2018/6/20
 */
public class Location {

    private double x;//维度
    private double y;//经度
    private String time;//记录时间

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
