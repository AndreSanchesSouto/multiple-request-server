package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.function.Consumer;

public class Client {

    private Socket serverSocket;
    private DataOutputStream outToServer;
    private DataInputStream inFromServer;
    private String serverResponse;
    public Client() throws IOException {
        this.serverSocket = new Socket("localhost", 55000);
    }

    public void run(Consumer<String> onResponseReceived) {
        Thread.startVirtualThread(() -> {
            try {
                outToServer = new DataOutputStream(serverSocket.getOutputStream());
                final int randomInt = new Random().nextInt();
                outToServer.writeInt(randomInt);

                inFromServer = new DataInputStream(serverSocket.getInputStream());

                String response = inFromServer.readUTF();

                this.serverResponse = response;
                if (onResponseReceived != null) {
                    onResponseReceived.accept(response);
                }
                System.out.println(response);

                serverSocket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getAddrAndPort() {
        return String.format("%s:%s",serverSocket.getLocalAddress(), this.serverSocket.getLocalPort()).replace("/", "");
    }

    @Override
    public String toString() {
        return "Client{" +
                "serverSocket=" + serverSocket +
                ", outToServer=" + outToServer +
                ", inFromServer=" + inFromServer +
                ", serverResponse='" + serverResponse + '\'' +
                '}';
    }
}
