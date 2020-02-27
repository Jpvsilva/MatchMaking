package server;

import generalresources.User;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TeamsManager implements Runnable{

    final int MATCH_PLAYERS = 4;
    final PlayerManager pm;
    final ArrayBlockingQueue<User> onlinePlayers;
    final ArrayList<ArrayList<User>> gamesTakingPlace;
    private ReentrantLock lock;

    public TeamsManager(PlayerManager pm, ArrayBlockingQueue<User> playersInQueue) {
        this.pm = pm;
        this.onlinePlayers = playersInQueue;
        this.gamesTakingPlace = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void run() {
        pm.initDB();
        User playerWantsToPlay = null;
        while(true){
            /*Get first player who wants to play*/
            try {
                playerWantsToPlay = onlinePlayers.take();
                System.out.println("PLAYER: " + playerWantsToPlay.getUsername() + " WANTS TO PLAY");
                createMatch(playerWantsToPlay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateMatchesState();
            }
        }

    private synchronized void updateMatchesState() {
        if(!gamesTakingPlace.isEmpty()){
            int index = 0;
            for(ArrayList<User> match : gamesTakingPlace){
                if(match.size() == MATCH_PLAYERS){
                    lock.lock();
                    System.out.println("Matches " + gamesTakingPlace.size());
                    for(User aux : match){
                        /*Creates team*/
                        aux.setMatchNo(gamesTakingPlace.indexOf(match));
                        aux.setTeamNo(index);
                        aux.setInQueue(false);
                    }
                    lock.unlock();
                }
                index++;
            }
        }
    }

    private synchronized void createMatch(User u){
        if(!u.isInQueue()){
            if(gamesTakingPlace.isEmpty()){
                System.out.println("PLAYER: " + u.getUsername() + " CREATED MATCH NO. 0");
                u.setInQueue(true);
                ArrayList<User> match1 = new ArrayList<>();
                match1.add(u);
                gamesTakingPlace.add(match1);
            }else{
                int index = 0;
                boolean placed = false;
                for(ArrayList<User> match : gamesTakingPlace){
                    int rank = getMatchRank(match);
                    int userRank = u.getRank();
                    System.out.println("MATCH RANK: " + rank + " USER RANK: " + userRank);
                    if(userRank > 0){
                        /*If player rank has a difference of 1 or is the same then it can be added*/
                        if((userRank > rank+1 || userRank == rank) && match.size() < 4){
                            lock.lock();
                            u.setInQueue(true);
                            match.add(u);
                            placed = true;
                            lock.unlock();
                            System.out.println("PLAYER: " + u.getUsername() + " INSERTED IN MATCH NO. " + index + " WHICH HAS " + match.size() + " PLAYERS ALREADY");
                        }
                    }
                    index++;
                }
                System.out.println("INDEX: " + index +" Matches Created: " + gamesTakingPlace.size());
                /*If there are no available matches*/
                if(!placed){
                    u.setInQueue(true);
                    ArrayList<User> match1 = new ArrayList<>();
                    match1.add(u);
                    lock.lock();
                    gamesTakingPlace.add(match1);
                    lock.unlock();
                    System.out.println("PLAYER: " + u.getUsername() + " CREATED MATCH NO. " + (index+1));
                }
            }
            System.out.println("Matches Created: " + gamesTakingPlace.size());
        }
    }

    private synchronized int getMatchRank(ArrayList<User> match){
        int r = 0;
        if(match == null){
            return r;
        }else{
            int rankTotal = 0;
            for(User aux : match){
                rankTotal += aux.getRank();
            }
            r = rankTotal/match.size();
        }
        return r;
    }

    public synchronized boolean setHero(String hero, String clientName, int matchNo) {
        boolean chosen = false;
        lock.lock();

        ArrayList<User> match = gamesTakingPlace.get(matchNo);
        for(User aux : match){
            if(aux.getHero() == Integer.parseInt(hero) && !aux.getUsername().equals(clientName)){
                lock.unlock();
                chosen = true;
                break;
            }
        }
        if(!chosen){
            for(User aux : match){
                if(aux.getUsername().equals(clientName)){
                    aux.setHero(Integer.parseInt(hero));
                    lock.unlock();
                    chosen = false;
                    break;
                }
            }
        }
        return chosen;
    }

    public synchronized void waitingForOtherPlayers(int matchNo) throws InterruptedException {
        while(countingPlayers(matchNo) != MATCH_PLAYERS){
            wait();
        }
        notifyAll();
    }
    public synchronized int countingPlayers(int matchNo) {
        int sum = 0;
        ArrayList<User> match = gamesTakingPlace.get(matchNo);
        for (User aux : match) {
            if (aux.getHero() != 0) {
                sum++;
            }
        }
        return sum;
    }
}
