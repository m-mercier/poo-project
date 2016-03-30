package Simulador;

import java.util.*;

/**
 *@author Ana Guarino
 * @author Marta Mercier
 */
public class Aleatorio extends Agente {

    // constructor da classe Aleatorio
    Aleatorio(int identificacao, String c, String fg, int x, int y, int cv){
        super(identificacao, c, fg, x, y , 1, cv); //tipo = 1
    }
        
    /**
     * Método que move o agente de forma aleatória
     * @param objectos lista dos objectos existente no ambiente
     * @param comprimento comprimento do ambiente
     * @param largura largura do ambiente
     */
    @Override
    public void mover(ArrayList<Objecto> objectos, int comprimento, int largura) {
        int move = 0; // agente ainda não se moveu
	if(!campo_visao_list.isEmpty()){	// próximo objecto é escolhido aleatoriamente da lista de objectos no campo de visão
            int i, flag = 0;
            int num_objs;
            //enquanto não se mover e ainda houver objs no campo de visão, procura um local para onde se mover
            while(move == 0 && !this.campo_visao_list.isEmpty() ){ 
                num_objs = this.campo_visao_list.size(); // nº de objectos no campo de visão do agente
                
                Random gerador = new Random(); //cria novo gerador aleatório
                int n = gerador.nextInt(num_objs); //escolhe aleatoriamente um número entre 0 e num_objs-1


                // ciclo para procurar o objecto escolhido aleatoriamente
                for (i=0; i<num_objs && flag==0; i++){

                    //verifica se o objecto é o escolhido aleatoriamente
                    if (this.campo_visao_list.indexOf(this.campo_visao_list.get(i)) == n){
                        int pos_x = this.campo_visao_list.get(i).posicao.getPos_x();
                        int pos_y = this.campo_visao_list.get(i).posicao.getPos_y();

                        //verifica se nunca esteve nessa posição
                        if(!this.memoria.contains(this.campo_visao_list.get(i))){ //se o objecto ainda não tiver sido aprendido, move-se para lá

                            Coordenadas new_pos = new Coordenadas(pos_x, pos_y); // cria nova posição para adicionar ao array 
                            this.caminho.add(new_pos); // acrescenta ao caminho a posição para onde vai

                            this.posicao.setPos_x(pos_x);
                            this.posicao.setPos_y(pos_y);
                            
                            this.n_obj_dif++;
                            this.memoria.add(this.campo_visao_list.get(i)); //adiciona o objecto para onde vai à memória
                            
                            // moveu-se, não precisa de continuar    
                            move = 1;
                        }
                        else{
                           // se já estiver estado nessa posição não se pode mover para lá
                            // remove objecto do campo de visão (já não é uma opção)
                            this.campo_visao_list.remove(i);
                        }
                        // encontrou o objecto, não precisa de continuar à procura
                        flag = 1; 
                        
                    }
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