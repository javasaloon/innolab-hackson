package com.innolab.hackson;

public class Boss implements Cloneable{
    long boss_id;

    String boss_name;

    int max_health;

    int cur_health;

    Point location;

    boolean isLive;

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getMax_health() {
        return max_health;
    }

    public void setMax_health(int max_health) {
        this.max_health = max_health;
    }

    public int getCur_health() {
        return cur_health;
    }

    public void setCur_health(int cur_health) {
        this.cur_health = cur_health;
    }

    public long getId() {
        return boss_id;
    }

    public void setId(long id) {
        this.boss_id = id;
    }

    public String getName() {
        return boss_name;
    }

    public void setName(String boss_name) {
        this.boss_name = boss_name;
    }

    @Override
    public Object clone()
    {
        Boss boss = null;
        try{
            boss = (Boss)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return boss;
    }
}
