package aud5.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {

    private int port;

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("TCPServer is starting...");
        ServerSocket serverSocket = null; //postojano da sluzi za novi konekcii i kje bide postojano aktiven


        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Socket server failed to start");
            return;
        }
        System.out.println("TCPServer server is started");
        System.out.println("Waiting for connections...");
        while (true) { //precekuvanje i obrabotuvanje na konekcii
            Socket socket = null;
            try {
                socket = serverSocket.accept(); //sme dobile nova konekcija od klient
//                new WorkerThread(socket).start();
                new HttpWorkerThread(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TCPServer server = new TCPServer(5000);
        server.start();
    }
}
