package aud5.tcp.server;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {

    private Socket socket = null;

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            System.out.println("Connected:" + socket.getInetAddress() + ":" + socket.getPort() + "\n");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line = null;
            while (!(line = br.readLine()).isEmpty()) {
//                System.out.println("New message from:" + socket.getInetAddress() + ":" +
//                        socket.getPort() + ": " + line);
                System.out.println(line);
                pw.write(line);
                pw.flush();
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (pw != null) pw.close();
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
