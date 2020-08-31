package Music_Advisor;

import Music_Advisor.api.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // parsing cli parameters
        if (args.length != 0) {
            for (int i = 0; i < args.length - 1; i++) {
                if (args[i].equals("-access")) {
                    Auth.setAuthServer(args[i + 1]);
                }
                if (args[i].equals("-resource")) {
                    Client.setApiServerPath(args[i + 1]);
                }
                if (args[i].equals("-page")) {
                    Printer.setPageLength(Integer.parseInt(args[i + 1]));
                }
            }
        }

        Menu.launch();
    }
}
