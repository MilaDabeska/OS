package aud5.tcp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient extends Thread {

    private String serverName;
    private int serverPort;

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;

        Scanner scanner = null;
        PrintWriter pw = null;

        try {
            socket = new Socket(serverName, serverPort);
            pw = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(System.in);

            while (true) { //se dodeka korisnikot prakja soodvetni poraki
                String line = scanner.nextLine();
                pw.println(line);
                pw.flush();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient("localhost", 5000);
        client.start();
    }
}
