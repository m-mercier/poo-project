package Simulador;

/**
 * @author Ana Guarino
 * @author Marta Mercier
 */
public class Entidade {

    /**
     * id de identificação de cada entidade
     */
    protected int id;

    /**
     * Cor da entidade
     */
    protected String cor;

    /**
     * Forma geométrica da entidade
     */
    protected String forma_geometrica;

    /**
     * coordenadas da entidade
     */
    protected Coordenadas posicao;
        
    //Constructor da classe Entidade
    Entidade(int identificacao, String c, String fg, int x, int y){
        id = identificacao;
        cor = c;
        forma_geometrica = fg;
        posicao = new Coordenadas(x, y);
    }
}