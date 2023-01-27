package exams.networking;

import java.io.*;
import java.net.Socket;

public class TCPExample3 extends Thread {
    private final String address;
    private final int port;


    public TCPExample3(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        PrintWriter pw = null;
        Socket socket = null;
        BufferedReader br = null;


        try {
            socket = new Socket(address, port);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String string;

                pw.println("hello:193263");
                pw.flush();
                System.out.println("Sent: hello:193263");

                string = br.readLine();
                System.out.println("Received: " + string);

                if (string.equals("193263:hello")) {
                    pw.println("193263:receive");
                    pw.flush();

                    System.out.println("Sent: 193263:receive");

                    string = br.readLine();
                    System.out.println("Received: " + string);
                }
                if (string.startsWith("193263:send")) {
                    String[] lines = string.split(":");

                    File file = new File(lines[2]);
                    file.createNewFile();
                    FileWriter fileWriter = new FileWriter(file);

                    while (true) {
                        String input = br.readLine();

                        if (input.equals("193263:over")) {
                            System.out.println("Sent: 193263:size:" + file.length());
                            pw.println("193263:size:" + file.length());
                            pw.flush();

                            System.out.println(br.readLine());
                            break;
                        }
                        fileWriter.write(input + "\n");
                        fileWriter.flush();
                    }
                }

                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String address = "194.149.135.49";
        int port = 9357;
        TCPExample3 client = new TCPExample3(address, port);
        client.start();
    }
}
