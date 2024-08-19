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
public class HebbModel {

    private double[][] pesosPrimeiraLetra;
    private double[][] pesosSegundaLetra;
    private static final int TAMANHO = 10; // Tamanho da matriz
    private double eta = 0.1; // Taxa de aprendizado ajustável

    public HebbModel() {
        this.pesosPrimeiraLetra = new double[TAMANHO][TAMANHO];
        this.pesosSegundaLetra = new double[TAMANHO][TAMANHO];
        inicializarPesos();
    }

    // Inicializa os pesos
    public void inicializarPesos() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                pesosPrimeiraLetra[i][j] = 0;
                pesosSegundaLetra[i][j] = 0;
            }
        }
    }

    // Método para treinar a rede com um padrão
    public void treinar(int[][] padrao, boolean primeiraLetra) {
        double[][] pesosAlvo = primeiraLetra ? pesosPrimeiraLetra : pesosSegundaLetra;
        int valor = primeiraLetra ? 1 : -1;

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                pesosAlvo[i][j] += eta * padrao[i][j] * valor;
            }
        }
    }

    // Método para reconhecer um padrão
    public int reconhecer(int[][] padrao) {
        double somaPrimeira = 0;
        double somaSegunda = 0;

        // Calcula a soma para a primeira e segunda letra
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                somaPrimeira += pesosPrimeiraLetra[i][j] * padrao[i][j];
                somaSegunda += pesosSegundaLetra[i][j] * padrao[i][j];
            }
        }

        // Decide qual padrão é mais próximo
        if (somaPrimeira > somaSegunda) {
            return 1; // Reconhecido como a Primeira Letra
        } else if (somaSegunda > somaPrimeira) {
            return 2; // Reconhecido como a Segunda Letra
        } else {
            return 0; // Nenhum padrão reconhecido claramente
        }
    }
}
