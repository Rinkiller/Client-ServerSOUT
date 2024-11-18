

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable{
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private final ArrayList<ClientManager> clients = new ArrayList<>();
    private String name;

    public ClientManager(Socket socket) { 
        this.socket = socket;
        try{
            

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            System.out.println(name + " подключился к чату");
            broadcastMessage("Server: " + name + " подключился к чату.");

        } catch(IOException e){
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try{
                messageFromClient = bufferedReader.readLine();
                System.out.println(name + " -> "+messageFromClient);
                if (messageFromClient == null){
                    closeEverything( socket,  bufferedWriter,  bufferedReader);
                    break; 
                }
                if (messageFromClient.charAt(0)=='@'){
                    String namePost =  messageFromClient.split(" ")[0].substring(1);
                    int len = namePost.length() + 1;
                    for (ClientManager client: clients){
                        if (client.name.equals(namePost)){
                            client.bufferedWriter.write(name + " -> " + messageFromClient.substring(len));
                            client.bufferedWriter.newLine();
                            client.bufferedWriter.flush();
                        }
                    }

                }
                broadcastMessage(messageFromClient);
            }catch(IOException e){
                closeEverything( socket,  bufferedWriter,  bufferedReader);
                break;
            }
        }
    }
    private void broadcastMessage(String message) {
        try{
            for (ClientManager client: clients){
                if (!client.name.equals(name)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }
        }catch (IOException e){
            closeEverything( socket,  bufferedWriter,  bufferedReader);
        }
    }


    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        removeClient();
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private void removeClient(){
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadcastMessage("Server: " + name + " покинул чат.");
        
    }
    
}
