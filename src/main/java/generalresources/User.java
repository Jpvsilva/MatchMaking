package generalresources;

import java.io.Serializable;

public class User implements Serializable,Comparable<User> {
    private final String username;
    private final String password;
    private int rank;
    private int hero;
    private int points;
    private boolean online;
    private boolean inQueue;
    private boolean inGame;
    private int teamNo;
    private int matchNo;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.rank = 0;
        this.hero = 0;
    }

    public User(String username, String password, int r) {
        this.username = username;
        this.password = password;
        this.rank = r;
        this.hero = 0;
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized int getRank() {
        return rank;
    }

    public synchronized void setRank() {
        this.rank += 0.1;
    }

    public synchronized int getPoints(){return points;}

    public synchronized void setPoints(int add){this.points += add;}

    public synchronized int getHero() {
        return hero;
    }

    public synchronized void setHero(int h) {
        this.hero = h;
    }

    public synchronized boolean getOnlineState() {return online;}

    public synchronized void setOnlineState(){
        this.online = true;
    }

    public synchronized boolean isInQueue() {return inQueue;}

    public synchronized void setInQueue(boolean inQueue){
        this.inQueue = inQueue;
    }

    public synchronized void setOfflineState(){
        this.online = false;
    }

    public synchronized boolean isInGame() {
        return inGame;
    }

    public synchronized void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public synchronized int getTeamNo() {
        return teamNo;
    }

    public synchronized void setTeamNo(int teamNo) {
        this.teamNo = teamNo;
    }

    public synchronized int getMatchNo() {
        return matchNo;
    }

    public synchronized void setMatchNo(int matchNo) {
        this.matchNo = matchNo;
    }

    @Override
    public int compareTo(User o) {
        if (o.getUsername().equals(username)) {
            return -1;
        }
        return 0;
    }
}
