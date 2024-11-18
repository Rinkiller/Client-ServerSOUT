import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("Server is runeble!!!");
            ServerSocket serverSocket = new ServerSocket(1400);
            Server server = new Server(serverSocket);
            
            server.runServer();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
       
    
    

}
