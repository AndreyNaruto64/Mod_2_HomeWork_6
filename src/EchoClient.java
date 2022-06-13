import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) throws IOException{
        FirstClient firstClient = new FirstClient();

     new Thread(){
         public void run() {
             try{
                 firstClient.readMessage();
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
     }.start();

     new Thread() {
         public void run(){
             try{
                 firstClient.sendMessage();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }.start();



    }

}

class FirstClient{
    Socket socket = null;
    BufferedReader in = null;
    PrintWriter out= null;
    BufferedReader console = null;

    public FirstClient() throws IOException{
        socket = new Socket("127.0.0.1",8189);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        console = new BufferedReader(new InputStreamReader(System.in));
    }
    String clientSMS, serverSMS;

    void sendMessage() throws IOException{
        while(true){
            if((clientSMS=console.readLine()) !=null){
                out.println("Клиент прислал сообщение: "+clientSMS);
                if (clientSMS.equalsIgnoreCase("exit")){
                    break;
                }
            }
        }
    }
    void readMessage() throws IOException{
        while(true){
            if((serverSMS=in.readLine()) != null){
                System.out.println(serverSMS);
            }
        }
    }
    void close() throws IOException{
        out.close();
        in.close();
        console.close();
        socket.close();
    }
}