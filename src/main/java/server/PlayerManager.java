package server;

import generalresources.User;

import java.util.TreeMap;

public class PlayerManager {

    final TreeMap<String, User> Users;

    public PlayerManager(TreeMap<String, User> users) {
        this.Users = users;
    }

    /**
     * Mimics a database behavior
     * */
    public void initDB() {

        User u1 = new User("aaa","aaa",1);
        User u2 = new User("bbb","bbb",1);
        User u3 = new User("ccc","ccc",1);
        User u4 = new User("ddd","ddd",1);
        User u5 = new User("eee","eee",1);
        User u6 = new User("fff","fff",1);
        User u7 = new User("ggg","ggg",1);
        User u8 = new User("hhh","hhh",1);
        User u9 = new User("iii","iii",1);
        User u10 = new User("jjj","jjj",1);
        User u11 = new User("lll","lll",5);
        User u12 = new User("mmm","mmm",5);
        User u13 = new User("nnn","nnn",5);
        User u14 = new User("ooo","ooo",5);
        User u15 = new User("ppp","ppp",5);
        User u16 = new User("qqq","qqq",5);
        User u17 = new User("rrr","rrr",5);
        User u18 = new User("sss","sss",5);
        User u19 = new User("ttt","ttt",5);
        User u20 = new User("uuu","uuu",5);
        Users.put("aaa",u1);
        Users.put("bbb",u2);
        Users.put("ccc",u3);
        Users.put("ddd",u4);
        Users.put("eee",u5);
        Users.put("fff",u6);
        Users.put("ggg",u7);
        Users.put("hhh",u8);
        Users.put("iii",u9);
        Users.put("jjj",u10);
        Users.put("lll",u11);
        Users.put("mmm",u12);
        Users.put("nnn",u13);
        Users.put("ooo",u14);
        Users.put("ppp",u15);
        Users.put("qqq",u16);
        Users.put("rrr",u17);
        Users.put("sss",u18);
        Users.put("ttt",u19);
        Users.put("uuu",u20);
    }

}
