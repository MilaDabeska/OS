package labs;

import java.io.*;
import java.net.Socket;

public class TCPClient extends Thread {
    String address;
    int port;

    public TCPClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            socket = new Socket(address, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                String line = br.readLine();
                pw.write(line);
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient("194.149.135.49", 9753);
        client.start();
    }
}