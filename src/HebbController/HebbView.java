/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HebbController;

/**
 *
 * @author willi
 */
import javax.swing.*;
import java.awt.*;

public class HebbView extends JFrame {
    private int[][] padraoAtual; // Armazena o padrão atual que será desenhado
    private Color corAtual; // Armazena a cor atual do desenho
    private JPanel painelDesenho;
    private JButton botaoTreinar;
    private JButton botaoTestar;
    private JButton botaoTrocarModo;

    public HebbView() {
        setTitle("       Rede Neural de Hebb");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        painelDesenho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharGrade(g);
                desenharPadrao(g);
            }
        };
        painelDesenho.setPreferredSize(new Dimension(300, 300));
        painelDesenho.setBackground(new Color(255, 255, 255)); // Azul clarinho
        add(painelDesenho, BorderLayout.CENTER);

       // Configurar painel de botões
        JPanel painelBotoes = new JPanel();
        botaoTreinar = new JButton("Treinar");
        botaoTestar = new JButton("Testar");
        botaoTrocarModo = new JButton("Trocar de Modo");

        // Aplicar fonte Verdana para os botões
        Font fonte = new Font("Verdana", Font.PLAIN, 14);
        botaoTreinar.setFont(fonte);
        botaoTestar.setFont(fonte);
        botaoTrocarModo.setFont(fonte);

        painelBotoes.add(botaoTreinar);
        painelBotoes.add(botaoTestar);
        painelBotoes.add(botaoTrocarModo);
        add(painelBotoes, BorderLayout.SOUTH);

        // Configurar a aparência inicial
        padraoAtual = new int[10][10]; // Inicializa o padrão com uma matriz vazia
        corAtual = Color.BLACK; // Inicializa a cor padrão
    }

    private void desenharGrade(Graphics g) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.drawRect(i * 30, j * 30, 30, 30);
            }
        }
    }

    private void desenharPadrao(Graphics g) {
        if (padraoAtual == null || corAtual == null) return;
        
        g.setColor(corAtual);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (padraoAtual[i][j] == 1) {
                    g.fillRect(i * 30, j * 30, 30, 30);
                }
            }
        }
    }

    public void atualizarDesenho(int[][] padrao, Color cor) {
        padraoAtual = padrao; // Atualiza o padrão atual
        corAtual = cor; // Atualiza a cor atual
        repaint(); // Solicita que o painel seja repintado
    }

    public JPanel getPainelDesenho() {
        return painelDesenho;
    }

    public JButton getBotaoTreinar() {
        return botaoTreinar;
    }

    public JButton getBotaoTestar() {
        return botaoTestar;
    }

    public JButton getBotaoTrocarModo() {
        return botaoTrocarModo;
    }
}
