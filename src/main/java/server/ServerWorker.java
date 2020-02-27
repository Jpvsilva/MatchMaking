package server;

import exceptions.UserExistsException;
import generalresources.User;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;


public class ServerWorker implements Runnable {

    private final int clientNumber;
    private final SSLSocket socket;
    private PrintWriter out;
    private String clientName;

    final TeamsManager tm;

    final PlayerManager playerManager;
    final ArrayBlockingQueue<User> onlinePlayers;
    ArrayList<User> team1, team2;

    public ServerWorker(SSLSocket socket, int clientNumber, PlayerManager pm, TeamsManager teamsManager, ArrayBlockingQueue<User> onlineGamers) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        this.playerManager = pm;
        this.onlinePlayers = onlineGamers;
        this.tm = teamsManager;
    }

    @Override
    public void run() {

        System.out.println("ServerLogin Started for Client: "+clientNumber);
        // InputStream and OutputStream Stuff
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if(inputLine.equals("LOGOUT")){
                    System.out.println("Goodbye Client "+clientNumber);
                    break;
                }
                else{
                    answer(inputLine);
                }
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answer(String inputLine) {
        String[] line = inputLine.split("\\|");
        String user = null;
        String pass = null;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(line[0]);
            switch(line[0]){
                case("LOGIN"):
                    user = line[1];
                    pass = line[2];

                    System.out.println("Server read the username: " + user);
                    System.out.println("Server read the password: "+ pass);
                    boolean r = login(user,pass);
                    if(r){
                        clientName = user;
                        playerManager.Users.get(clientName).setOnlineState();
                        out.println("SUCCESS");
                    }else{
                        out.println("UNSUCCESSFUL");
                    }
                    out.flush();
                    playerManager.Users.get(clientName).setOnlineState();
                    System.out.println("Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("REG"):
                    user = line[1];
                    pass = line[2];

                    System.out.println("Server read the username: " + user);
                    System.out.println("Server read the password: "+ pass);
                    try {
                        register(user,pass);
                        clientName = user;
                        playerManager.Users.get(clientName).setOnlineState();
                        out.println("SUCCESS");
                    } catch (UserExistsException ex) {
                        out.println("UNSUCCESSFUL");
                    }
                    out.flush();
                    System.out.println("REGISTER Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("SEARCH"):
                    if(playerManager.Users.get(clientName).isInQueue()){
                        out.println("SEARCH_ENABLED");
                    }else{
                        onlinePlayers.add(playerManager.Users.get(clientName));
                        out.println("SEARCH_OK");
                    }
                    out.flush();
                    System.out.println("SEARCH Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("CANCEL"):
                    onlinePlayers.remove(playerManager.Users.get(clientName));
                    playerManager.Users.get(clientName).setInQueue(false);
                    System.out.println("CANCEL Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("HERO"):
                    if(playerManager.Users.get(clientName).isInQueue()){
                        out.println("HERO_NOK");
                    }else{
                        ArrayList<User> players = tm.gamesTakingPlace.get(playerManager.Users.get(clientName).getMatchNo());
                        ArrayList<String> team1String = new ArrayList<>();
                        team1String.add(players.get(0).getUsername());
                        team1String.add(players.get(1).getUsername());
                        team1 = new ArrayList<>();
                        team1.add(players.get(0));
                        team1.add(players.get(1));

                        ArrayList<String> team2String = new ArrayList<>();
                        team2String.add(players.get(2).getUsername());
                        team2String.add(players.get(3).getUsername());
                        team2 = new ArrayList<>();
                        team2.add(players.get(2));
                        team2.add(players.get(3));

                        out.println("HERO_OK|"+ team1String.toString() + "|" + team2String.toString());
                        playerManager.Users.get(clientName).setInQueue(false);
                    }
                    out.flush();
                    System.out.println("HERO Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("HERO_CHOOSE"):
                    String hero = line[1];
                    if(!playerManager.Users.get(clientName).isInQueue()){
                        System.out.println("HERO_CHOOSE Received From Client " + clientNumber + " CHOSEN HERO: " + hero);
                        int matchNo = playerManager.Users.get(clientName).getMatchNo();
                        boolean chosen = tm.setHero(hero,clientName,matchNo);
                        if(chosen){
                            out.println("UNSUCCESSFUL");
                        }else{
                            out.println("HERO_SUCCESS");
                        }
                    }else{
                        out.println("UNSUCCESSFUL");
                    }
                    out.flush();
                    break;
                case("MATCH"):
                    playerManager.Users.get(clientName).setInQueue(false);
                    int matchNo = playerManager.Users.get(clientName).getMatchNo();
                    tm.waitingForOtherPlayers(matchNo);
                    out.println("MATCH_SUCCESS");
                    out.flush();
                    break;
                case("RETRY"):
                    System.out.println("RETRY Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("LOGOUT"):
                    if(onlinePlayers.contains(playerManager.Users.get(clientName))){
                        onlinePlayers.remove(playerManager.Users.get(clientName));
                    }
                    playerManager.Users.get(clientName).setInQueue(false);
                    System.out.println("LOGOUT Received From Client " + clientNumber + ": " + inputLine);
                    break;
                case("RANK"):
                    int rank = playerManager.Users.get(clientName).getRank();
                    out.println(rank);
                    out.flush();
                    break;
                case("POINTS"):
                    int points = playerManager.Users.get(clientName).getPoints();
                    out.println(points);
                    out.flush();
                    break;
                default:
                    out.println("INVALID");
                    out.flush();
                    System.out.println("Received From Client " + clientNumber + ": " + inputLine);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean login(String user, String pass){
        boolean result;
        if (!this.playerManager.Users.containsKey(user)) {
            result = false;
        } else {
            result = this.playerManager.Users.get(user).getPassword().equals(pass);
            User u = this.playerManager.Users.get(user);
            this.playerManager.Users.put(user, u);
        }
        return result;
    }

    public void register(String user, String pass) throws UserExistsException {
        if (this.playerManager.Users.containsKey(user)) {
            throw new UserExistsException("User " + user + " already registered!");
        }
        else{
            User u = new User(user, pass);
            this.playerManager.Users.put(user, u);
        }
    }
}