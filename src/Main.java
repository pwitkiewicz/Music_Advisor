import java.util.Scanner;

public class Main {
    private static void menu(String command) {
        if(command.equals("help")) {
            System.out.print("Available commands:\n" +
                    "\t- featured (list of featured playlists)\n" +
                    "\t- new (list of new albums)\n" +
                    "\t- categories (list of available categories)\n" +
                    "\t- playlists <CATEGORY NAME> (list of playlists from chosen category)\n" +
                    "\t- exit\n");
        }

        if(command.equals("featured")) {
            // TODO api integration
            System.out.println("listing featured playlists...");
        }

        if(command.equals("new")) {
            // TODO api integration
            System.out.println("listing new playlists...");
        }

        if(command.equals("categories")) {
            // TODO api integration
            System.out.println("listing categories...");
        }

        String[] playlists_command = command.split("\\s+");

        if(playlists_command[0].equals("playlists")) {
            // TODO api integration
            System.out.println("listing playlists from category...");

        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String command = new String();
        System.out.println("Type command (help for info):");

        while(!command.equals("exit")) {
            System.out.print("> ");

            command = in.nextLine();
            menu(command);
        }
    }
}
