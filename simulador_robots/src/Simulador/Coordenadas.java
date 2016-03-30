package Simulador;

/**
 * @author Ana Guarino
 * @author Marta Mercier
 */
public class Coordenadas {

    private int pos_x;
    private int pos_y;
        
    //Contructor da classe Coordenadas
    Coordenadas(int x, int y){
        pos_x = x;
        pos_y = y;
    }
       
    /**
     *  Método que retorna a coordenada x
     * @return coordenada x
     */
    public int getPos_x() {
        return this.pos_x;
    }

    /**
     * Método que altera a coordenada x
     * @param x coordenada x
     */
    public void setPos_x(int x) {
        pos_x = x;
    }
    
    /**
     * Método que retorna a coordenada y
     * @return coordenada y
     */
    public int getPos_y() {
        return this.pos_y;
    }

    /**
     * Método que altera a coordenada y
     * @param y coordenada y
     */
    public void setPos_y(int y) {
	pos_y = y;
    }
}