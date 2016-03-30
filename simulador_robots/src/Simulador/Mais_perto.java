package Simulador;

import java.util.*;

/**
 * @author Ana Guarino
 * @author Marta Mercier
 */
public class Mais_perto extends Agente {
        
    // Constructor da classe Mais_perto
    Mais_perto(int identificacao, String c, String fg, int x, int y, int cv){
        super(identificacao, c, fg, x, y , 2, cv); //tipo = 2
    }

    /**
     * Método que calcula a distância entre o agente e um objecto
     * @param atual_x coordenada x do agente
     * @param atual_y coordenada y do agente
     * @param pos_x_obj coordenada x do objecto
     * @param pos_y_obj coordenada y do objecto
     * @return distancia entre agente e objecto
     */
    public double calcular_dist(int atual_x,int atual_y,int pos_x_obj,int pos_y_obj) {
        // calcula a distância a cada um dos objectos que se encontra no campo de visão
        double distancia = Math.sqrt(Math.pow(atual_x-pos_x_obj,2)+Math.pow(atual_y-pos_y_obj,2));
        return distancia;
    }

    /**
     * Método que move o agente 
     * @param objectos lista de objectos existentes no ambiente
     * @param comprimento comprimento do ambiente
     * @param largura largura do ambiente
     */
    @Override
    public void mover(ArrayList<Objecto> objectos, int comprimento, int largura) {
        double dist = (double) campo_visao+1; //variável que vai conter a distância mínima
        double dist_aux;
        int pos_x_obj,pos_y_obj; //posição do objecto a ser analisado
        int move = 0;
        Objecto obj_mais_perto = new Objecto();
        
        int atual_x,atual_y; //posição atual do robot
        atual_x=this.posicao.getPos_x();
        atual_y=this.posicao.getPos_y();
        
        if(!campo_visao_list.isEmpty()){ //caso o campo de visão não esteja vazio, o objecto escolhido é o mais próximo
            //enquanto não se mover e ainda houver objs no campo de visão, procura um local para onde se mover
            while(move == 0 && !this.campo_visao_list.isEmpty() ){
                Iterator <Objecto> it = campo_visao_list.iterator();
                while(it.hasNext()) // percorre os objectos do campo de visao
                {   
                    Objecto obj = it.next();
                    // verifica se o objecto já está na memória do agente
                    if(!this.memoria.contains(obj)){ //se o objecto ainda não tiver sido aprendido

                        //código do calculo da distância aos objectos do campo de visão
                        pos_x_obj = obj.posicao.getPos_x();
                        pos_y_obj = obj.posicao.getPos_y();
                        dist_aux = this.calcular_dist(atual_x,atual_y,pos_x_obj,pos_y_obj);

                        if(dist_aux<dist)
                        {
                            /*pos_x = pos_x_obj; //guarda as posições do objecto mais perto
                            pos_y = pos_y_obj;*/  
                            dist = dist_aux;   //actualiza a distância mínima
                            obj_mais_perto = obj; //actualiza objecto mais perto
                        }

                    }
                    else
                    {
                        it.remove();
                    }
                }
                // se o campo_visao não estiver vazio significa que calculou a distância de algum agente (ou seja, algum agente do campo de visao não estava na memoria)
                if(!this.campo_visao_list.isEmpty()){

                    int pos_x = obj_mais_perto.posicao.getPos_x();
                    int pos_y = obj_mais_perto.posicao.getPos_y();

                    Coordenadas new_pos = new Coordenadas(pos_x, pos_y); // cria nova posição para adicionar ao array 

                    this.caminho.add(new_pos); // acrescenta ao caminho a posição para onde vai

                    //move o robot de posição
                    this.posicao.setPos_x(pos_x);
                    this.posicao.setPos_y(pos_y);

                    this.n_obj_dif++;
                    this.memoria.add(obj_mais_perto); //adiciona o objecto para onde vai à memória

                    // o agente moveu-se
                    move = 1;
                }
            }
            // agente não se moveu e já não há objectos no campo de visão - move-se aleatoriamente
            if (move == 0){ // se o objecto não se tiver movido
                this.mover_aleatorio(objectos, comprimento, largura);
            } 
        }
        else{
            
            //se não tiver nenhum objecto no campo de visão desloca-se para uma posição aleatória
            this.mover_aleatorio(objectos, comprimento, largura);
        }
    }
}