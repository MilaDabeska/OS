package exams.networking;

import java.net.*;
import java.io.*;

public class Client {
    public static final String IP = "194.149.135.49";
    public static final int port = 9357;

    public static void main(String[] args) {
        try (Socket socket = new Socket(IP, port)) {

            ClientSend2 cs = new ClientSend2(socket, "hello:193263");
            ClientReceive2 cr = new ClientReceive2(socket);

            cs.start();
            cr.start();

            cr.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ClientSend2 extends Thread {
    private final Socket socket;
    private final String message;

    public ClientSend2(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(message);
            out.flush();
            System.out.println("Sent: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientReceive2 extends Thread {

    private final Socket socket;

    public ClientReceive2(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String str = in.readLine();

                if (str == null)
                    continue;

                System.out.println("Received: " + str);

                if (str.equals("193263:hello")) {
                    ClientSend2 csReceive = new ClientSend2(socket, " 193263:receive");
                    csReceive.start();
                }

                if (str.startsWith("193263:send")) {
                    String[] strs = str.split(":");

                    File f = new File(strs[2]);

                    if (!f.exists()) {
                        f.createNewFile();
                    }

                    try (FileWriter fw = new FileWriter(f)) {
                        while (true) {
                            String input = in.readLine();

                            if (input == null) {
                                continue;
                            }

                            if (input.equals("193263:over")) {
                                break;
                            }

                            fw.write(input);
                            fw.write("\n");

                            fw.flush();
                        }
                    }

                    ClientSend2 csSize = new ClientSend2(socket, "193263:size:" + f.length());
                    csSize.start();
                }

                if (str.startsWith("Server: I received your calculated file size!")) {
                    System.out.println("SUCCESS!");
                    System.exit(0);
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
