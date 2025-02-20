package server;
import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private static Set<ClientHandler> clients = new HashSet<>();
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.username = in.readLine();
        clients.add(this);
        broadcast(username + " has joined the chat.", null);
    }
    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/private")) {
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String recipient = parts[1];
                        String privateMessage = parts[2];
                        sendPrivateMessage(recipient, privateMessage);
                    }
                } else {
                    broadcast(username + ": " + message, this);
                }
            }
        } catch (IOException e) {
            System.out.println(username + " has left the chat.");
        } finally {
            clients.remove(this);
            broadcast(username + " has left the chat.", null);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void sendPrivateMessage(String recipient, String message) {
        for (ClientHandler client : clients) {
            if (client.username.equals(recipient)) {
                client.sendMessage("[Private from " + username + "]: " + message);
                this.sendMessage("[Private to " + recipient + "]: " + message);
                return;
            }
        }
        this.sendMessage("User " + recipient + " not found.");
    }
    public void sendMessage(String message) {
        out.println(message);
    }
    public static void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }
}