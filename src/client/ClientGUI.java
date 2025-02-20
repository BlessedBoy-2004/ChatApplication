package client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class ClientGUI extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private ChatClient chatClient;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Client");
        chatArea = new TextArea();
        chatArea.setEditable(false);
        inputField = new TextField();
        inputField.setPromptText("Type your message here...");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());
        VBox layout = new VBox(10, chatArea, inputField, sendButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
        chatClient = new ChatClient(this);
        new Thread(chatClient).start();
    }
    public void appendMessage(String message) {
        chatArea.appendText(message + "\n");
    }
    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            chatClient.sendMessage(message);
            inputField.clear();
        }
    }
}