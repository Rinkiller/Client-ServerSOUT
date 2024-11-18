import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class App {

    public static void main(String[] args)  {
        Scanner scan = new Scanner(System.in);
        System.out.println("Imput you'r name: ");
        String name = scan.nextLine();
        try {
            Socket socet = new Socket("localhost", 1400);
            Client client = new Client(socet, name);
            InetAddress inetAddress = socet.getInetAddress();
            System.out.println("inetAdress" + inetAddress);
            String remoteIp = inetAddress.getHostAddress();
            System.out.println("Remote IP: " + remoteIp);
            System.out.println("Lochal port: "+ socet.getLocalPort());
            client.listenForMessage();
            client.sendMessage();


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}