package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static final int PORT = 3443;
//    private ClientHandler client = new ClientHandler();

    public Server() {

        Socket clientSocket = null;
        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");
            while (true) {
                clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket, this);
//                clients.add(client);
//                System.out.println("Count clients on system = "+ clients.size());
                new Thread(client).start();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                clientSocket.close();
                System.out.println("Сервер остановлен");
                serverSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToAllClients(String msg) {
//        for (ClientHandler o : clients) {
//            o.sendMsg(msg);
//        }

    }

    public void removeClient(ClientHandler client) {
//        clients.remove(client);
//        System.out.println("Count clients on system = "+ clients.size());
    }

}