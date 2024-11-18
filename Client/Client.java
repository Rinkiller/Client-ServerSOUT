import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final String userName;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    public Client(Socket socket, String name) {
        this.socket = socket;
        this.userName = name;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
        
    }
    // Слушатель сообщений
    public void listenForMessage(){
        new Thread(
        new Runnable() {
            @Override
            public void run(){
                String message;
                while (socket.isConnected()) {
                    try{
                    message = bufferedReader.readLine();
                    System.out.println(message);
                    }catch (IOException e){
                        closeEverything(socket,bufferedWriter,bufferedReader);
                    }
                }
            }
        }
        ).start();
    }
    // Отправка сообщений
    public void sendMessage(){
        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bufferedWriter.write(userName + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch(IOException e){
            
            closeEverything(socket,bufferedWriter,bufferedReader);
        }

    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
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
    
}
