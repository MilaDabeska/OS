package labs;

public class UDPClient extends Thread{
    String serverName;
    int port;

    public UDPClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;

    }

    @Override
    public void run() {
        String name = "helllo:index\n";
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient("194.149.135.49",9753);
        client.start();
    }
}
