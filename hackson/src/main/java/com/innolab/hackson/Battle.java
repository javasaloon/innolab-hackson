package com.innolab.hackson;

public class Battle {

    boolean is_reborn;

    public boolean isIs_reborn() {
        return is_reborn;
    }

    public void setIs_reborn(boolean is_reborn) {
        this.is_reborn = is_reborn;
    }

    int battle_id;

    long timestamp;

    Player user;

    Boss b;

    int damage;

    public int getId() {
        return battle_id;
    }

    public void setId(int id) {
        this.battle_id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Player getUser() {
        return user;
    }

    public void setUser(Player user) {
        this.user = user;
    }

    public Boss getB() {
        return b;
    }

    public void setB(Boss b) {
        this.b = b;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
