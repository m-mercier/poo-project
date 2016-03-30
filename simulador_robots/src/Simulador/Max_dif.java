package Simulador;

import java.util.*;

/**
 * @author Ana Guarino
 * @author Marta Mercier
 */
public class Max_dif extends Agente {

    //private int dif_hamming;  PODE NÃO SER NECESSÁRIO GUARDAR

    Max_dif (int identificacao, String c, String fg, int x, int y, int cv){
        super(identificacao, c, fg, x, y , 3, cv); //tipo = 3
    }

    /**
     *
     * @param objectos lista de objectos existentes no ambiente
     * @param comprimento comprimento do ambiente
     * @param largura largura do ambiente
     */
    @Override
    public void mover(ArrayList<Objecto> objectos, int comprimento, int largura) {
        int move = 0; //objecto ainda não se moveu
        int max_dif_hamming = 0;
        int obj_dif_hamming;
        Objecto obj_max = new Objecto();

        if(!this.campo_visao_list.isEmpty()){ // verfica se o campo de visão está vazio
            if(!this.memoria.isEmpty()){ // verifica se a memória está vazia
                //enquanto não se mover e ainda houver objs no campo de visão, procura um local para onde se mover
                while(move == 0 && !this.campo_visao_list.isEmpty() ){
                    Iterator <Objecto> it = campo_visao_list.iterator();
                    while(it.hasNext()){ // percorre os objectos do campo de visao
                        Objecto obj = it.next();
                         //verifica se nunca esteve nessa posição - se já tiver o obj na memória não vale a pena calcular a dif de hamming
                        if(!this.memoria.contains(obj)){ //se o objecto ainda não tiver sido aprendido, calcula a dif de hamming
                            obj_dif_hamming = this.calcular_dif_hamming(obj);
                            if ( obj_dif_hamming > max_dif_hamming){
                                max_dif_hamming = obj_dif_hamming; // é seleccionado o objecto com maior diferença de hamming
                                obj_max = obj;
                            }
                        }
                        else{
                            // o objecto existe na memória - agente não se pode mover para lá
                            it.remove(); //remove-o do campo de visão
                        }
                    }

                    // se o campo_visao não estiver vazio significa que calculou a dif de hamming de algum agente (ou seja, algum agente do campo de visao não estava na memoria)
                    if(!this.campo_visao_list.isEmpty()){
                        int pos_x = obj_max.posicao.getPos_x();
                        int pos_y = obj_max.posicao.getPos_y();

                        Coordenadas new_pos = new Coordenadas(pos_x, pos_y); // cria nova posição para adicionar ao array 
                        this.caminho.add(new_pos); // acrescenta ao caminho a posição para onde vai

                        this.posicao.setPos_x(pos_x);
                        this.posicao.setPos_y(pos_y);


                        this.n_obj_dif++;
                        this.memoria.add(obj_max); //adiciona o objecto para onde vai à memória

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

                // memória do agente está vazia - move-se aleatoriamente para um dos objectos do campo de visão
                // acontece no primeiro movimento
                this.move_aleatoriamente();
            }
        }
        else{

            //se não tiver nenhum objecto no campo de visão desloca-se para uma posição aleatória
            this.mover_aleatorio(objectos, comprimento, largura);   

        }

    }
            
    /**
     * Método que calcula a diferença de Hamming entre um objecto do campo de visão e os objectos na memória do agente
     * @param obj objecto em análise
     * @return diferença de hamming
     */
    public int calcular_dif_hamming(Objecto obj) {
        int dif_hamming = 0;
        int dif_hamming_final = 3;
            for (Objecto mem_obj : this.memoria){
                if (!obj.cor.equals(mem_obj.cor)){
                    dif_hamming++;
                }
                if (!obj.forma_geometrica.equals(mem_obj.cor)){
                    dif_hamming++;
                }
                if (!obj.getTipo().equals(mem_obj.getTipo())){
                    dif_hamming++;
                }
                if (dif_hamming < dif_hamming_final){
                    dif_hamming_final = dif_hamming; // é "guardada" a menor diferença de hamming
                }
            }
        return dif_hamming_final;
    }
          
    /**
     * Método que move o agente para uma posição aleatória do campo de visão
     */
    public void move_aleatoriamente(){
        if(!campo_visao_list.isEmpty()){	// próximo objecto é escolhido aleatoriamente da lista de objectos no campo de visão
            int i, flag = 0;
            int num_objs = this.campo_visao_list.size();
            Random gerador = new Random(); //cria novo gerador aleatório
            int n = gerador.nextInt(num_objs); //escolhe aleatoriamente um número entre 0 e num_objs-1 
            for (i=0; i<num_objs && flag==0; i++){
                //verifica se o objecto é o escolhido aleatoriamente
                if (this.campo_visao_list.indexOf(this.campo_visao_list.get(i)) == n){
                    int pos_x = this.campo_visao_list.get(i).posicao.getPos_x();
                    int pos_y = this.campo_visao_list.get(i).posicao.getPos_y();

                    Coordenadas new_pos = new Coordenadas(pos_x, pos_y); // cria nova posição para adicionar ao array 
                    this.caminho.add(new_pos); // acrescenta ao caminho a posição para onde vai

                    this.posicao.setPos_x(pos_x);
                    this.posicao.setPos_y(pos_y);


                    if(!this.memoria.contains(this.campo_visao_list.get(i))){ //se o objecto ainda não tiver sido aprendido
                        this.n_obj_dif++;
                        this.memoria.add(this.campo_visao_list.get(i)); //adiciona o objecto para onde vai à memória
                    }
                    flag = 1; // encontrou o objecto, não precisa de continuar à procura  
                }
            }     
	}
    }

}