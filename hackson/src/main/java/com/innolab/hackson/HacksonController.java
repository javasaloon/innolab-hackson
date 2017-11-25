package com.innolab.hackson;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/game")
public class HacksonController {

//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();
    final static String[] bossName = {"boss1", "boss2", "boss3"};
    static Map<Long, List<Battle>> map = new HashMap<>();
    static List<Boss> bossList = new ArrayList<>(bossName.length);
    static{
        final Long[] bossId = {1l, 2l, 3l};
        final int[] max_health = {10000, 10000, 10000};
        final double[] location_x = {101.11, 101.12, 101.13};
        final double[] location_y = {101.11, 101.12, 101.13};
        int[] cur_health = {21, 10000, 10000};
        boolean[] cur_status = {true, true, true};

        for(int i = 0; i < bossName.length; i++){
            Boss b = new Boss();
            b.setId(bossId[i]);
            b.setName(bossName[i]);
            b.setMax_health(max_health[i]);
            b.setCur_health(cur_health[i]);
            Point p = new Point(location_x[i], location_y[i]);
            b.setLocation(p);
            b.setLive(cur_status[i]);
            bossList.add(b);
        }

        for(int i = 0; i < bossName.length; i++){
            if(!map.containsKey(bossList.get(i).boss_id)){
                List<Battle> bl = new ArrayList<>();
                map.put(bossList.get(i).boss_id, bl);
            }
        }
    }

    @RequestMapping(path="/bosses", method = RequestMethod.GET)
    public List<Boss> get_bossList(@RequestParam(value="name", defaultValue="World") String name) {
        return bossList;
    }

    @RequestMapping(path="/players", method = RequestMethod.GET)
    public List<Player> get_userList(@RequestParam(value="name", defaultValue="World") String name) {
        final String[] userName = {"user1", "user2", "user3", "user4", "user5"};
        final Long[] userId = {1l, 2l, 3l, 4l, 5l};
        final Long[] groupId = {-1l, -1l, 1l, 1l, 1l};
        final int[] level = {1, 1, 2, 2, 3};
        final int[] power = {10, 100, 200, 200, 300};
        final double[] original_location_x = {101.21, 101.22, 101.23, 101.24, 101.25};
        final double[] original_location_y = {101.21, 101.22, 101.23, 101.24, 101.25};

        List<Player> userList = new ArrayList<>(userName.length);
        for(int i = 0; i < userName.length; i++){
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

    @RequestMapping(path="/battle", method = RequestMethod.POST)
    public void post_battle_List(@RequestParam(value="boss_id") long boss_id,@RequestParam(value="player_id") long player_id) {
        int event_id = 1;
        List<Battle> battleList = map.get(boss_id);
        Boss boss = (Boss) bossList.get(((int)boss_id)-1).clone();
        Player user = get_userList("World").get(((int)player_id)-1);
        while(true){
            Boss boss_cur = (Boss) boss.clone();
            Battle battle = new Battle();
            battle.setTimestamp(System.currentTimeMillis());
            battle.setId(event_id);
            event_id++;
            int damage = user.getPower();
            battle.setDamage(damage);
            battle.setUser(user);
            int cur_heal = boss.getCur_health()-damage;
//            System.out.println(cur_heal);
            boss_cur.setCur_health(cur_heal);
            boss.setCur_health(cur_heal);
            if(boss_cur.getCur_health() <= 0){
                boss_cur.setLive(false);
                battle.setB(boss_cur);
                battleList.add(battle);
                break;
            }else {
                battle.setB(boss_cur);
                battleList.add(battle);
            }
        }
    }

    @RequestMapping(path="/battles", method = RequestMethod.GET)
    public List<Battle> get_battle_List(@RequestParam(value="boss_id") long boss_id,@RequestParam(value="event_id") long event_id) {
        List<Battle> list = map.get(boss_id);
        int start_index = ((int)event_id)-1;
        return list.subList(start_index, list.size());
    }

}