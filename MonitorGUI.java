import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonitorGUI extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel lblStatus;
    private JLabel lblUltimaAtualizacao;

    public MonitorGUI() {
        setTitle("SISTEMA DE GESTAO AEROPORTUARIA - SBGR (AUTO-REFRESH)");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel de Topo mais moderno
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(15, 30, 60));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titulo = new JLabel("PAINEL DE OPERAÇÕES - GUARULHOS", SwingConstants.LEFT);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        
        lblUltimaAtualizacao = new JLabel("Iniciando...", SwingConstants.RIGHT);
        lblUltimaAtualizacao.setForeground(new Color(0, 255, 127));
        
        painelTopo.add(titulo, BorderLayout.WEST);
        painelTopo.add(lblUltimaAtualizacao, BorderLayout.EAST);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela e Status
        String[] colunas = {"VOO", "MODELO", "ROTA", "STATUS", "ATRASO", "PAX AFETADOS"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        lblStatus = new JLabel("Sincronizando com API...", SwingConstants.CENTER);
        lblStatus.setPreferredSize(new Dimension(0, 40));
        add(lblStatus, BorderLayout.SOUTH);

        // TIMER: Atualiza a cada 60 segundos (60000 milissegundos)
        Timer timer = new Timer(60000, e -> atualizarTudo());
        timer.start();

        // Primeira rodada manual ao abrir
        atualizarTudo();
    }

    private void atualizarTudo() {
        lblStatus.setText("🔄 Buscando novos dados da API...");
        
        try {
            // 1. Chama o script Python de dentro do Java!
            Process p = Runtime.getRuntime().exec("python coleta_dados.py");
            p.waitFor(); // Espera o Python terminar de salvar o CSV

            // 2. Limpa a tabela atual
            modeloTabela.setRowCount(0);

            // 3. Lê o CSV novo e preenche
            int totalPax = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("dados_atrasos.csv"))) {
                String linha;
                br.readLine(); // Pula cabeçalho
                while ((linha = br.readLine()) != null) {
                    String[] d = linha.split(",");
                    VooAtrasado v = new VooAtrasado(d[0], d[1], d[2], d[3], d[4], d[5]);
                    modeloTabela.addRow(new Object[]{v.voo, v.modelo, v.origem + " -> " + v.destino, 
                                                     "🚨 " + v.status, v.atraso, v.getPax()});
                    totalPax += v.getPax();
                }
            }

            // 4. Atualiza Labels
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblUltimaAtualizacao.setText("Última Atualização: " + dtf.format(LocalDateTime.now()));
            lblStatus.setText("Monitorando: " + modeloTabela.getRowCount() + " voos críticos | Total PAX Afetados: " + totalPax);
            
        } catch (Exception ex) {
            lblStatus.setText("Erro na sincronização: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MonitorGUI().setVisible(true));
    }
}