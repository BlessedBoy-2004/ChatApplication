package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ChatClient implements Runnable {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientGUI gui;
    public ChatClient(ClientGUI gui) {
        this.gui = gui;
    }
    @Override
    public void run() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(gui.getUsername());
            String message;
            while ((message = in.readLine()) != null) {
                gui.appendMessage(EncryptionUtil.decrypt(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        try {
            out.println(EncryptionUtil.encrypt(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
