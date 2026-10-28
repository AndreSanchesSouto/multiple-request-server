package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {


    private static final List<String> listClients = new ArrayList<>();

    static void main() {
        try {
            ServerSocket serverSocket = new ServerSocket(55000);
                while (true) {
                    // Liga o servidor na porta 55000 e fica esperando um cliente se conectar
                    System.out.println("Aguardando conexão...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Conectado: " + clientSocket.getRemoteSocketAddress());

                    listClients.add(
                            clientSocket.getRemoteSocketAddress().toString().replace("/", "")
                    );

                    Thread.startVirtualThread(() -> {
                        try {
                            // Conecta ao canal de entrada para conseguir ler o que o cliente mandou
                            DataInputStream input = new DataInputStream(clientSocket.getInputStream());

                            // Pega o número inteiro enviado pelo cliente e guarda na variável
                            int valor = input.readInt();

                            Thread.sleep(5000);

                            // Instancia o canal de saída para preparar a resposta do cliente
                            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

                            // Define a resposta com base no valor recebido e já envia para o cliente
                            output.writeUTF(valor > 0 ? "Maior que zero" : "Menor que zero");

                            // Fecha a conexão com esse cliente específico, pois deu tudo certo
                            clientSocket.close();
                            System.out.println(listClients);

                        } catch (InterruptedException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
