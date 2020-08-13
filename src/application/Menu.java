package application;

import java.util.Scanner;

public class Menu {
    public static void launch() {
        Scanner in = new Scanner(System.in);
        String command = "";

        while(!command.equals("exit")) {
            command = in.nextLine();
            parseCommand(command);
        }

        System.out.println("---GOODBYE!---");
    }

    private static void parseCommand(String command) {
        if(command.equals("help")) {
            System.out.print("Available commands:\n" +
                    "\t- auth (authorize app)" +
                    "\t- featured (list of featured playlists)\n" +
                    "\t- new (list of new albums)\n" +
                    "\t- categories (list of available categories)\n" +
                    "\t- playlists <CATEGORY NAME> (list of playlists from chosen category)\n" +
                    "\t- exit\n");
        }

        if(command.equals("auth")) {
            System.out.println(api.Auth.sendRequest());
            System.out.println("---SUCCESS---");
        }

        if(!api.Auth.getStatus()) {
            System.out.println("Please, provide access for application.");
            return;
        }

        if(command.equals("featured")) {
            // TODO api integration
            System.out.println("---FEATURED---\n" +
                    "Mellow Morning\n" +
                    "Wake Up and Smell the Coffee\n" +
                    "Monday Motivation\n" +
                    "Songs to Sing in the Shower");
        }

        if(command.equals("new")) {
            // TODO api integration
            System.out.println("---NEW RELEASES---\n" +
                    "Mountains [Sia, Diplo, Labrinth]\n" +
                    "Runaway [Lil Peep]\n" +
                    "The Greatest Show [Panic! At The Disco]\n" +
                    "All Out Life [Slipknot]");
        }

        if(command.equals("categories")) {
            // TODO api integration
            System.out.println("---CATEGORIES---\n" +
                    "Top Lists\n" +
                    "Pop\n" +
                    "Mood\n" +
                    "Latin");
        }

        String[] playlists_command = command.split("\\s+");

        if(playlists_command[0].equals("playlists")) {
            // TODO api integration
            System.out.println("---" + playlists_command[1].toUpperCase() + " PLAYLISTS---\n" +
                    "Walk Like A Badass  \n" +
                    "Rage Beats  \n" +
                    "Arab Mood Booster  \n" +
                    "Sunday Stroll");

        }
    }
}
