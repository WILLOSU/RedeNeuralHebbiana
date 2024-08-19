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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class HebbController {

    private HebbModel modelo;
    private HebbView view;
    private static final int TAMANHO = 10;
    private static boolean modoTreinamento = true;
    private static boolean modoTeste = false;
    private int[][] primeiraLetra = new int[TAMANHO][TAMANHO];
    private int[][] segundaLetra = new int[TAMANHO][TAMANHO];
    private int[][] padraoTeste = new int[TAMANHO][TAMANHO];

    public HebbController(HebbModel modelo, HebbView view) {
        this.modelo = modelo;
        this.view = view;
        inicializar();
        exibirMatrizesIniciais(); // Exibe as matrizes iniciais ao iniciar a aplicação
    }

    private void inicializar() {
        view.getPainelDesenho().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / 30;
                int y = e.getY() / 30;
                if (x >= TAMANHO || y >= TAMANHO) {
                    return;
                }

                // Determina se o usuário está desenhando a primeira ou segunda letra
                int[][] padraoAlvo;
                Color corDesenho;

                if (modoTeste) {
                    padraoAlvo = padraoTeste;
                    corDesenho = Color.BLACK;
                } else {
                    padraoAlvo = modoTreinamento ? primeiraLetra : segundaLetra;
                    corDesenho = modoTreinamento ? Color.RED : Color.BLUE;
                }

                padraoAlvo[x][y] = (padraoAlvo[x][y] == 0) ? 1 : 0;
                view.atualizarDesenho(padraoAlvo, corDesenho);
            }
        });

        view.getBotaoTreinar().addActionListener(e -> treinar());

        view.getBotaoTestar().addActionListener(e -> {
            int resultado = modelo.reconhecer(padraoTeste);

            String mensagemResultado;
            Color corResultado;

            if (resultado == 1) {
                mensagemResultado = "Sua letra testada foi reconhecida como 1º LETRA";
                corResultado = Color.RED;
            } else if (resultado == 2) {
                mensagemResultado = "Sua letra testada foi reconhecida como 2º LETRA";
                corResultado = Color.BLUE;
            } else {
                mensagemResultado = "Sua letra testada não foi reconhecida.";
                corResultado = Color.GRAY; // Cor para indicar não identificado
            }

            // Criar um BufferedImage a partir do padraoTeste
            int novoTamanho = TAMANHO * 30; // Ajuste o fator de escala conforme necessário
            BufferedImage image = new BufferedImage(novoTamanho, novoTamanho, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < novoTamanho; i++) {
                for (int j = 0; j < novoTamanho; j++) {
                    int x = i * TAMANHO / novoTamanho; // Mapeamento para a matriz original
                    int y = j * TAMANHO / novoTamanho;
                    int color = padraoTeste[x][y] == 1 ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    image.setRGB(i, j, color);
                }
            }

            // Criar um JLabel para exibir a imagem
            JLabel label = new JLabel(new ImageIcon(image));
            label.setPreferredSize(new Dimension(TAMANHO * 40, TAMANHO * 40)); // Ajustar o tamanho da imagem

            // Exibir o JLabel no JOptionPane com um painel personalizado
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.CENTER);
            panel.add(new JLabel(mensagemResultado), BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(view, panel, "Resultado do Teste", JOptionPane.INFORMATION_MESSAGE);
        }
        );

        view.getBotaoTrocarModo().addActionListener(e -> {
            if (modoTeste) {
                modoTeste = false;
                modoTreinamento = !modoTreinamento;
                limparDesenhoAtual();
                JOptionPane.showMessageDialog(view, modoTreinamento ? "Modo alterado para Primeira Letra" : "Modo alterado para Segunda Letra");
            } else {
                modoTeste = true;
                limparDesenhoAtual();
                JOptionPane.showMessageDialog(view, "Modo alterado para Teste");
            }
        }
        );
    }

    private void treinar() {
        modelo.inicializarPesos();
        modelo.treinar(primeiraLetra, true); // Treina com a primeira letra
        modelo.treinar(segundaLetra, false); // Treina com a segunda letra
        limparDesenhoAtual(); // Limpa o desenho do modo anterior
        JOptionPane.showMessageDialog(view, "Treinamento concluído.");
    }

    private void exibirMatrizesIniciais() {
        primeiraLetra[2][1] = 0;
        primeiraLetra[2][2] = 0;
        primeiraLetra[2][3] = 0;
        primeiraLetra[3][1] = 0;
        primeiraLetra[4][1] = 0;

        segundaLetra[1][1] = 0;
        segundaLetra[1][2] = 0;
        segundaLetra[1][3] = 0;
        segundaLetra[2][2] = 0;
        segundaLetra[3][2] = 0;

        view.atualizarDesenho(primeiraLetra, Color.RED);
        view.atualizarDesenho(segundaLetra, Color.BLUE);
    }

    private void limparDesenhoAtual() {
        int[][] padraoAtual;
        Color corDesenho;

        if (modoTeste) {
            padraoAtual = padraoTeste;
            corDesenho = Color.BLACK;
        } else {
            padraoAtual = modoTreinamento ? primeiraLetra : segundaLetra;
            corDesenho = modoTreinamento ? Color.RED : Color.BLUE;
        }

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                padraoAtual[i][j] = 0;
            }
        }
        view.atualizarDesenho(padraoAtual, corDesenho);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HebbModel modelo = new HebbModel();
            HebbView view = new HebbView();
            HebbController controller = new HebbController(modelo, view);
            view.setVisible(true);
        });
    }
}
