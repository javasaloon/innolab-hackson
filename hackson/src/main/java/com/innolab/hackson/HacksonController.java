package com.innolab.hackson;

import cn.bubi.baas.account.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class HacksonController {

    //    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();
    boolean isDead = false;
    int event_id = 1;
    final static String[] bossName = {"boss1", "boss2", "boss3"};
    static Map<Long, List<Battle>> map = new HashMap<>();
    static List<Boss> bossList = new ArrayList<>(bossName.length);
    BlockchainManager blockchainManager = new BlockchainManager();

    static {
        final Long[] bossId = {1l, 2l, 3l};
        final int[] max_health = {10000, 10000, 10000};
        final double[] location_x = {-74.0066, -73.9966, -73.98660000000001};
        final double[] location_y = {40.7135, 40.71351, 40.7235};
        int[] cur_health = {10000, 10000, 10000};
        boolean[] cur_status = {true, true, true};
        int[] r = {500, 400, 666};

        for (int i = 0; i < bossName.length; i++) {
            Boss b = new Boss();
            b.setR(r[i]);
            b.setId(bossId[i]);
            b.setName(bossName[i]);
            b.setMax_health(max_health[i]);
            b.setCur_health(cur_health[i]);
            Point p = new Point(location_x[i], location_y[i]);
            b.setLocation(p);
            b.setLive(cur_status[i]);
            bossList.add(b);
        }

        for (int i = 0; i < bossName.length; i++) {
            if (!map.containsKey(bossList.get(i).boss_id)) {
                List<Battle> bl = new ArrayList<>();
                map.put(bossList.get(i).boss_id, bl);
            }
        }
    }

    @RequestMapping(path = "/bosses", method = RequestMethod.GET)
    public List<Boss> get_bossList(@RequestParam(value = "name", defaultValue = "World") String name) {
        return bossList;
    }

    @RequestMapping(path = "/players", method = RequestMethod.GET)
    public List<Player> get_userList(@RequestParam(value = "name", defaultValue = "World") String name) {
        final String[] userName = {"user1", "user2", "user3"};
        final Long[] userId = {1l, 2l, 3l};
        final Long[] groupId = {-1l, 1l, 1l};
        final int[] level = {1, 1, 2};
        final int[] power = {1000, 1200, 1500};
        final double[] original_location_x = {-74.0036, -74.01100000000001, -74.00460000000001};
        final double[] original_location_y = {40.7113, 40.711000000000006, 40.715500000000006};

        List<Player> userList = new ArrayList<>(userName.length);
        for (int i = 0; i < userName.length; i++) {
            Player user = new Player();
            user.setId(userId[i]);
            user.setName(userName[i]);
            user.setGroup_id(groupId[i]);
            user.setLevel(level[i]);
            user.setPower(power[i]);
            Point p = new Point(original_location_x[i], original_location_y[i]);
            user.setLocation(p);
            userList.add(user);
        }
        return userList;
    }

    @RequestMapping(path = "/battle", method = RequestMethod.POST)
    public Battle post_battle_List(@RequestParam(value = "boss_id") long boss_id, @RequestParam(value = "player_id") long player_id) {

        boolean flag = true;
        List<Battle> battleList = map.get(boss_id);
        Boss boss = (Boss) bossList.get(((int) boss_id) - 1).clone();
        Player user = (Player) get_userList("World").get(((int) player_id) - 1).clone();
        Battle battle = new Battle();
        battle.setTimestamp(System.currentTimeMillis());
        battle.setId(event_id);
        event_id++;
        int damage = user.getPower();
        double r2 = getRandom(80, 120) / 100.0;
        System.out.println(r2);
        damage = (int)((double)damage*r2);
        System.out.println(damage);
        user.setPower(damage);
        battle.setDamage(damage);
        battle.setUser(user);
        int cur_heal = boss.getCur_health() - damage;
//            System.out.println(cur_heal);
        boss.setCur_health(cur_heal);
        bossList.get(((int) boss_id) - 1).setCur_health(cur_heal);
        if(!isDead) {
            if (boss.getCur_health() <= 0) {
                boss.setLive(false);
                boss.setCur_health(0);
                isDead = true;

                blockchainManager.transferAsserts(boss_id, player_id, 10);

            }
            battle.setB(boss);
            battleList.add(battle);
        }else{
            event_id--;
        }
        return battle;
    }

    @RequestMapping(path = "/path", method = RequestMethod.POST)
    public Player post_path(@RequestParam(value = "player_id") long player_id,
                            @RequestParam(value = "x") double x,
                            @RequestParam(value = "y") double y) {
        Player user = get_userList("World").get(((int) player_id) - 1);
        Point p = new Point(x,y);
        user.setLocation(p);
        return user;
    }

    @RequestMapping(path = "/battles", method = RequestMethod.GET)
    public List<Battle> get_battle_List(@RequestParam(value = "boss_id") long boss_id, @RequestParam(value = "event_id") long event_id) {
        List<Battle> list = map.get(boss_id);
        int start_index = ((int) event_id) - 1;
        if(start_index >= list.size()){
            return new ArrayList<>();
        }
        if(start_index < 0) start_index = 0;
        return list.subList(start_index, list.size());
    }

    private static double getRandom(int min, int max) {
        return (Math.random() * (max - min) + min);
    }
}