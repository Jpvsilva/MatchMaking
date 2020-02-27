package client;

import generalresources.Hero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.TreeMap;

public class Menu {

    private static PrintWriter out;
    private static BufferedReader in;
    private static boolean searchOK;

    static void menuHUB(MENU menuDestiny, PrintWriter out2, BufferedReader in2, boolean searchOK) throws IOException {
        out = out2;
        in = in2;
        searchOK = searchOK;
        MENU menu = menuDestiny;
        while (menu != MENU.LOGOUT) {
            switch (menu) {
                case FIRST_MENU:
                    menu = mainMenu();
                    break;
                case LOGIN:
                    menu = loginMenu();
                    break;
                case REGISTER:
                    menu = registerMenu();
                    break;
                case PAGE:
                    menu = pageMenu();
                    break;
                case PAGE2:
                    menu = page2Menu();
                    break;
                case SEARCH_TEAM:
                    menu = findTeamMenu();
                    break;
                case CHOOSE_HERO:
                    menu = chooseHeroMenu();
                    break;
                case MATCH:
                    menu = matchMenu();
                    break;
                case CANCEL:
                    menu = cancelMenu();
                    break;
                case RANK:
                    menu = rankMenu();
                    break;
                case POINTS:
                    menu = pointsMenu();
                    break;
            }
        }
        out.println("LOGOUT");
        out.flush();
    }

    /**
     * LOGIN/REGISTER SCREEN
     * */
    private static MENU mainMenu() throws IOException {
        String option = null;

        System.out.println(" L - Login");
        System.out.println(" R - Register");
        System.out.println(" E - Exit");
        option = readChoice();
        switch (option) {
            case "L":
                return MENU.LOGIN;
            case "R":
                return MENU.REGISTER;
            case "E":
                return MENU.LOGOUT;
            default:
                return MENU.FIRST_MENU;
        }
    }

    /**
     * LOGIN
     * */
    private static MENU loginMenu() throws IOException {
        String option = null;
        String username, password, serverAnswer;
        boolean loginOk = false;
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("---------------------------------------------------------------");
        System.out.println("LOGIN");
        System.out.println("---------------------------------------------------------------");
        while (!loginOk) {
            System.out.println("Username: ");
            username = sin.readLine();
            System.out.println("Password: ");
            password = sin.readLine();
            out.println("LOGIN" + "|" + username + "|" + password);
            out.flush();
            serverAnswer = in.readLine();
            if (serverAnswer.equals("SUCCESS")) {
                System.out.println("Welcome: "+username);
                loginOk = true;
            } else {
                System.out.println("Username doesn't exist or wrong password!");
                System.out.println("---------------------------------------------------------------");
                System.out.println("L - Try Again");
                System.out.println("M - Main Menu | E - Exit");
                System.out.println("---------------------------------------------------------------");
                option = readChoice();
                switch (option) {
                    case "M":
                        return MENU.FIRST_MENU;
                    case "E":
                        return MENU.LOGOUT;
                    default:
                        return MENU.LOGIN;
                }
            }
        }
        return MENU.PAGE;
    }

    /**
     * REGISTER
     * */
    private static MENU registerMenu() throws IOException {

        String option;
        String username, password, serverAnswer;
        boolean registerSuccess = false;
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("---------------------------------------------------------------");
        System.out.println("REGISTER");
        System.out.println("---------------------------------------------------------------");
        while (!registerSuccess) {
            System.out.println("Username: ");
            username = sin.readLine();
            System.out.println("Password: ");
            password = sin.readLine();
            out.println("REG" + "|" + username + "|" + password);
            out.flush();
            serverAnswer = in.readLine();
            if (serverAnswer.equals("SUCCESS")) {
                System.out.println("Successfully registered!");
                registerSuccess = true;
            } else {
                System.out.println("Registration Error.");
                System.out.println("---------------------------------------------------------------");
                System.out.println("T - Try Again");
                System.out.println("M - Main Menu | E - Exit");
                System.out.println("---------------------------------------------------------------");
                option = readChoice();
                switch (option) {
                    case "M":
                        return MENU.FIRST_MENU;
                    case "E":
                        return MENU.LOGOUT;
                    default:
                        return MENU.REGISTER;
                }
            }
        }
        return MENU.PAGE;
    }

    /**
     * FIRST PAGE
     * */
    private static MENU pageMenu() throws IOException {
        System.out.println("---------------------------------------------------------------");
        System.out.println("S - Search for other players");
        System.out.println("P - Check Points");
        System.out.println("R - Check Rank");
        System.out.println("M - Main Menu | E - Exit");
        System.out.println("---------------------------------------------------------------");
        String choice = readChoice();
        switch (choice) {
            case "M":
                return MENU.FIRST_MENU;
            case "E":
                return MENU.LOGOUT;
            case "P":
                return MENU.POINTS;
            case "R":
                return MENU.RANK;
            case "S":
                return MENU.SEARCH_TEAM;
            default:
                return MENU.PAGE;
        }
    }

    /**
     * PAGE AFTER SEARCH ENABLED
     * */
    private static MENU page2Menu() throws IOException {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Searching still active.");
        System.out.println("C - Cancel searching for other players");
        System.out.println("P - Check Points");
        System.out.println("R - Check Rank");
        System.out.println("B - Begin Match");
        System.out.println("M - Main Menu | E - Exit");
        System.out.println("---------------------------------------------------------------");
        String choice = readChoice();
        switch (choice) {
            case "M":
                return MENU.FIRST_MENU;
            case "E":
                return MENU.LOGOUT;
            case "C":
                return MENU.CANCEL;
            case "P":
                return MENU.POINTS;
            case "R":
                return MENU.RANK;
            case "B":
                return MENU.CHOOSE_HERO;
            default:
                return MENU.PAGE;
        }
    }

    /**
     * GET RANK
     * */
    private static MENU rankMenu() throws IOException {
        out.println("RANK");
        out.flush();
        String response = in.readLine();
        System.out.println("Rank: " + response);
        return MENU.PAGE;
    }

    /**
     * GET POINTS
     * */
    private static MENU pointsMenu() throws IOException {
        out.println("POINTS");
        out.flush();
        String response = in.readLine();
        System.out.println("Points:" + response);
        return MENU.PAGE;
    }

    /**
     * CANCEL SEARCH
     * */
    private static MENU cancelMenu() {
        out.println("CANCEL");
        out.flush();
        searchOK = false;
        System.out.println("You cancelled team search");

        return MENU.PAGE;
    }

    /**
     * SEARCH ANOTHER PLAYERS
     * */
    private static MENU findTeamMenu() throws IOException {
        if(searchOK){
            return MENU.PAGE2;
        }
        out.println("SEARCH");
        out.flush();

        String response;
        response = in.readLine();

        if(response.equals("SEARCH_OK")){
            searchOK = true;
            System.out.println("Waiting for other players...");
            return MENU.PAGE2;
        }
        else{
            searchOK = false;
            return MENU.SEARCH_TEAM;
        }
    }

    private static String readHero() throws IOException {
        boolean option_Ok = false;
        String option = null;
        System.out.println("Select a Hero:");
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        while (!option_Ok) {
            try {
                option = sin.readLine();
                if(option.matches("-?\\d+(\\.\\d+)?")){
                    int heroValue = Integer.parseInt(option);
                    if(heroValue > 0 && heroValue < 30){
                        option_Ok = true;
                    }else{
                        option_Ok = false;
                        System.out.println("Invalid choice. Please select another:");
                    }
                }else{
                    System.out.println("It must be a number. Please select another:");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please select another:");
            }
        }
        return option;
    }

    /**
     * CHOOSE HERO
     * */
    private static MENU chooseHeroMenu() throws IOException {
        out.println("HERO");
        out.flush();

        String response;
        response = in.readLine();
        String[] line = response.split("\\|");
        if(line[0].equals("HERO_OK")){
            System.out.println("Team 1: " + line[1] + " Team 2: " + line[2]);
            System.out.println("Pick up a Hero to fight:");
            System.out.println(" 1 - HERO: Doomfist  RANK: 1 |  2 - HERO: Genji      RANK: 3 |  3 - HERO: McCree    RANK: 5");
            System.out.println(" 4 - HERO: Pharah    RANK: 1 |  5 - HERO: Reaper     RANK: 3 |  6 - HERO: Soldier76 RANK: 2");
            System.out.println(" 7 - HERO: Sombra    RANK: 3 |  8 - HERO: Traver     RANK: 5 |  9 - HERO: Bastion   RANK: 1");
            System.out.println("10 - HERO: Hanzo     RANK: 3 | 11 - HERO: Junkrat    RANK: 1 | 12 - HERO: Mei       RANK: 3");
            System.out.println("13 - HERO: Torbjorn  RANK: 5 | 14 - HERO: Widowmaker RANK: 1 | 15 - HERO: DVa       RANK: 3");
            System.out.println("16 - HERO: Orisa     RANK: 2 | 17 - HERO: Reinhardt  RANK: 3 | 18 - HERO: Roadhog   RANK: 5");
            System.out.println("19 - HERO: Winston   RANK: 1 | 20 - HERO: Zarya      RANK: 3 | 21 - HERO: Ana       RANK: 3");
            System.out.println("22 - HERO: Lucio     RANK: 1 | 23 - HERO: Mercy      RANK: 3 | 24 - HERO: Moira     RANK: 5");
            System.out.println("25 - HERO: Symmetra  RANK: 1 | 26 - HERO: Zenyatta   RANK: 3 | 27 - HERO: Athena    RANK: 2");
            System.out.println("28 - HERO: Emily     RANK: 3 | 29 - HERO: Liao       RANK: 3 | 30 - HERO: Pachimari RANK: 1");
            String hero = readHero();
            out.println("HERO_CHOOSE|" + hero);
            out.flush();
            response = in.readLine();
            System.out.println(response);
            if(response.equals("HERO_SUCCESS")){
                return MENU.MATCH;
            }else{
                System.out.println("Hero already chose. Please select another: ");
                return MENU.CHOOSE_HERO;
            }
        }else{
            return MENU.SEARCH_TEAM;
        }
    }

    /**
     * PLAY MATCH
     * */
    private static MENU matchMenu() throws IOException {
        out.println("MATCH");
        out.flush();
        System.out.println(" Match Menu");
        System.out.println(" The match is about to begin. Waiting for other players...");
        String response = in.readLine();
        if(response.equals("MATCH_SUCCESS")){
            System.out.println("All players ready.");
            System.out.println("Beginning Game.");
            return MENU.LOGOUT;
        }else{
            return MENU.PAGE2;
        }
    }

    /**
     * Main function that deals with client input
     * Sends info to the corresponding function
     * In case of an invalid option says invalid option asking for another instruction
     * */
    private static String readChoice() throws IOException {
        boolean option_Ok = false;
        String option = null;
        System.out.println("Select an option:");
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        while (!option_Ok) {
            try {
                option = sin.readLine();
                option_Ok = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please select another:");
            }
        }
        return option;
    }

    enum MENU {
        FIRST_MENU, // first menu to appear
        LOGIN, // ask user and password
        REGISTER, // ask user and password
        LOGOUT, // logout from app
        PAGE, // menu after register or login
        PAGE2, // menu after searching players active
        SEARCH_TEAM, // called function to trigger search players on
        CHOOSE_HERO, // menu called to choose hero
        MATCH, // menu to start new match after both heroes and teams are in place
        CANCEL, // function to cancel player searching
        RANK, // function to get player rank
        POINTS // function to get player points
    }
}

