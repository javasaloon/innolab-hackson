package com.innolab.hackson;

public class Player {
    long user_id;

    long group_id;

    String user_name;

    int level;

    int power;

    Point location;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public long getId() {
        return user_id;
    }

    public void setId(long id) {
        this.user_id = id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return user_name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
    public void setName(String name) {
        this.user_name = name;
    }

    public int getLevel() {
        return level;
    }

}
