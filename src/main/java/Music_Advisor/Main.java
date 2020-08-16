package Music_Advisor;

import Music_Advisor.api.Auth;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // parsing cli parameters
        if (args.length != 0) {
            for (int i = 0; i < args.length - 1; i++) {
                if (args[i].equals("-access")) {
                    Auth.setAuthServer(args[i + 1]);
                }
            }
        }

        Menu.launch();
    }
}
