package Simulador;

/**
 *
 * @author Marta
 */
public class Objecto extends Entidade {
    
    private final String tipo;
        
    Objecto(){
        super(0, "inexistente", "inexistente", 0, 0);
        tipo = "inexistente";
    }
        
    Objecto(int identificacao, String c, String fg, int x, int y, String t){
        super(identificacao, c, fg, x, y);
        tipo = t;
    }
        
    /**
     * Método que devolve o tipo de objecto
     * @return tipo de objecto
     */
    public String getTipo(){
        return tipo;
    }
        

}