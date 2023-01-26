package exams.networking;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


// isprakjanje file

public class TCPExample5 {
    private static final String IP = "194.149.135.49";
    private static final int Port = 9753;

    public static void main(String[] args) {
        try (Socket socket = new Socket(IP, Port)) {
            ClientReceiver cr = new ClientReceiver(socket);
            ClientSender cs = new ClientSender(socket, "hello:193263");

            cr.start();
            cs.start();

            cr.join();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ClientSender extends Thread {
    private Socket socket;
    private String message;

    ClientSender(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br.write(message);
            br.flush();

            System.out.println("Sent: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class ClientReceiver extends Thread {
    private Socket socket;

    ClientReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {

                String line = br.readLine();
                if (line == null) {
                    continue;
                }

                System.out.println("Received: " + line);

                if (line.equals("193263:hello")) {
                    ClientSender cs = new ClientSender(socket, "193263:attach:filename.txt");
                    cs.start();

                    FileReader fw = new FileReader("PATH TO FILE");
                    BufferedReader fbr = new BufferedReader(fw);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String fLine = fbr.readLine();
                    int lenght = 0;
                    while (fLine != null) {
                        lenght += line.length();
                        bw.write(line);
                        bw.flush();
                        fLine = fbr.readLine();
                    }

                    ClientSender cs2 = new ClientSender(socket, "193263:over");
                    cs2.start();

                    ClientSender cs3 = new ClientSender(socket, "193263:fileSize:" + lenght);
                    cs3.start();
                }

                if (line.equals("Server: I received your calculated file size!")) {
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
