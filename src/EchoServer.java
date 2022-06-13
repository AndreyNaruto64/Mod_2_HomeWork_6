//import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Scanner;

public class EchoServer {

    public static void main(String[] args) throws IOException {

        MiniServ serv = new MiniServ();
        serv.start();
        serv.client();

        new Thread() {
            public void run() {
                while(true){
                    String text=null;
                    try{
                        text=serv.in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(text!=null){
                        try{
                            serv.sendMessage(text);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

        new Thread() {
            public void run(){
                try{
                    serv.write();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

}

class MiniServ {
    Socket socket = null;
    BufferedReader in=null;
    PrintWriter out=null;
    String input;

    ServerSocket serverSocket=null;
    BufferedReader console=null;

    void start() {
        try {
            serverSocket = new ServerSocket(8189);
        } catch (IOException e) {
            System.out.println("Не могу подключиться к порту: 8189");
            System.exit(1);
        }
        System.out.print("Сервер запущен, ожидает подключения клиента..."+"\n");
    }

    void client() throws IOException{
        try{
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
        }catch (IOException e) {
            System.out.println("Клиент не может подключиться");
            System.exit(1);
        }
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream(), true);
         System.out.println("Ожидание сообщения...");
    }

    void sendMessage(String sms) throws IOException {
        if(sms.equalsIgnoreCase("exit")) close();
        out.println("Сервер прислал сообщение: "+sms);
        System.out.println(sms);
    }
    void close() throws IOException{
        out.close();
        in.close();
        socket.close();
        serverSocket.close();
    }

    void write() throws IOException{
        while(true){
            console = new BufferedReader(new InputStreamReader(System.in));
            String text = console.readLine();
            sendMessage(text);
        }
    }

}