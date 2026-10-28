package panel;

import client.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Panel {

    private static List<String> clientHistory = new LinkedList<>();

    static void main() {
        JFrame frame = new JFrame("Exemplo de JPanel");
        frame.setSize(400, 300); // Aumentado um pouco para acomodar melhor a tabela e o painel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Garante que o programa fecha ao sair

        // 1. Configura as colunas e cria o modelo dinâmico da tabela (começa com 0 linhas)
        String[] column = {"Cliente", "Status / Resposta"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel("Adicionar cliente"));
        JButton clientAdd = new JButton("+");
        panel.add(clientAdd);

        // Configurando a ação do botão
        clientAdd.addActionListener(event -> {
            try {
                Client newClient = new Client();
                System.out.println(newClient);
                clientHistory.add(newClient.getAddrAndPort());

                // 1. Adiciona a linha na tabela imediatamente mostrando o socket conectado
                int linhaAtual = model.getRowCount(); // Pega o índice de onde este cliente vai ficar
                model.addRow(new Object[]{ newClient.getAddrAndPort(), "Aguardando..." });

                // 2. Executa o run passando a ação que vai rodar DAQUI A 5 SEGUNDOS
                newClient.run(resultado -> {
                    // Este bloco executa ASSÍNCRONAMENTE quando o servidor responder!
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        // Atualiza a segunda coluna da tabela com a resposta ("Maior que zero", etc)
                        // (Para isso funcionar, adicione uma segunda coluna no seu DefaultTableModel)
                        model.setValueAt(resultado, linhaAtual, 1);
                    });
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Organizando os componentes no JFrame de forma correta usando BorderLayout
        frame.add(scrollPane, BorderLayout.CENTER); // Tabela fica no centro ocupando o maior espaço
        frame.add(panel, BorderLayout.SOUTH);       // Painel com o botão fica na parte de baixo

        frame.setVisible(true);
    }
}