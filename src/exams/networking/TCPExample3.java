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
            socket = new Socket(this.address, this.port);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {

                String inp;
                pw.println("hello:193263");
                pw.flush();
                System.out.println("Sent: hello:193263");

                inp = br.readLine();
                System.out.println("Received: " + inp);

                if (inp.equals("193263:hello")) {
                    pw.println("193263:receive");
                    pw.flush();

                    System.out.println("Sent: 193263:receive");

                    inp = br.readLine();
                    System.out.println("Received: " + inp);
                }
                if (inp.startsWith("193263:send")) {
                    String[] strs = inp.split(":");

                    File f = new File(strs[2]);
                    f.createNewFile();
                    FileWriter fileWriter = new FileWriter(f);

                    while (true) {
                        String input = br.readLine();

                        if (input.equals("193263:over")) {
                            System.out.println("Sent: 193263:size:" + f.length());
                            pw.println("193263:size:" + f.length());
                            pw.flush();

                            System.out.println(br.readLine());
                            break;
                        }
                        fileWriter.write(input);
                        fileWriter.write("\n");
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
