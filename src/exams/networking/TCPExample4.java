package exams.networking;

import java.io.*;
import java.net.Socket;

public class TCPExample4 {
    public static final String ip = "194.149.135.49";
    public static final int port = 9357;

    public static void main(String[] args) {
        try (Socket socket = new Socket(ip, port)) {

            SendClient send = new SendClient(socket, "hello:193263");
            ReceiveClient receive = new ReceiveClient(socket);

            send.start();
            receive.start();
            receive.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class SendClient extends Thread {

    private final Socket socket;
    private final String message;

    SendClient(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(message);
            pw.flush();
            System.out.println("Sent: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReceiveClient extends Thread {

    private final Socket socket;

    ReceiveClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String string = br.readLine();

                if (string == null) {
                    continue;
                }

                System.out.println("Received: " + string);

                if (string.equals("193263:hello")) {
                    Send sReceive = new Send(socket, "193263:attach");
                    sReceive.start();
                }

                if (string.startsWith("193263:attach")) {
                    String[] lines = string.split(":");
                    File file = new File(lines[2]);

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    try (FileWriter fw = new FileWriter(file)) {
                        while (true) {
                            String input = br.readLine();

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
                    Send size = new Send(socket, "193263:size:" + file.length());
                    size.start();
                }
                if (string.startsWith("Server: I received your calculated file size!")) {
                    System.out.println("SUCCESS!");
                    System.exit(0);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
