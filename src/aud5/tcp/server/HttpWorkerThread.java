package aud5.tcp.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpWorkerThread extends Thread {

    private Socket socket;

    public HttpWorkerThread(Socket socket) {
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
            StringBuilder sb = new StringBuilder();

            while (!(line = br.readLine()).isEmpty()) {
                sb.append(line).append("\n");
                System.out.println(line);
            }

            RequestProcessor request = RequestProcessor.of(sb.toString());
            pw.write("HTTP/1.1 200 OK\n\n");

            if (request.getCommand().equals("GET") && request.getUri().equals("/time")) {
                pw.printf("<html><body><h1>%s</h1></body></html>",
                        LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
            } else {
                pw.printf("<html><body><h1>Hello World</h1></body></html>");
            }

            pw.flush();

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
