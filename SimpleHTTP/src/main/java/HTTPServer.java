import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {

    private static final int port = 5510;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                System.out.println("browser connection");
                RequestHandler requestHandler = new RequestHandler(socket);
                Thread thread = new Thread(requestHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

