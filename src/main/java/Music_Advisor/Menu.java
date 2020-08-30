package Music_Advisor;

import Music_Advisor.api.*;

import java.io.IOException;
import java.util.Scanner;

class Menu {
    public static void launch() throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        String command = "";

        while(!command.equals("exit")) {
            command = in.nextLine();
            parseCommand(command);
        }

        System.out.println("---GOODBYE!---");
    }

    private static void parseCommand(String command) throws IOException, InterruptedException {
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
            Auth.init();

            if (Auth.isAuthorized()) {
                System.out.println("---SUCCESS---");
            } else {
                System.out.println("code not received");
                System.out.println("---FAILURE---");
            }
        }

        if(!Auth.isAuthorized()) {
            System.out.println("Please, provide access for main.java.Music_Advisor.application.");
            return;
        }

        if(command.equals("featured")) {
            Client.getFeatured();
        }

        if(command.equals("new")) {
            Client.getNewReleases();
        }

        if(command.equals("categories")) {
            Client.getCategories();
        }

        String[] playlists_command = command.split("\\s+");

        if(playlists_command[0].equals("playlists")) {
            Client.getCategoryPlaylists(command.replace("playlists ", " "));
        }
    }
}
