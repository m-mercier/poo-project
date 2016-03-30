package Simulador;

import java.util.*;

/**
 *
 * @author Ana Guarino
 * @author Marta Mercier
 */
public abstract class Agente extends Entidade {

    /**
     *  tipo de agente (1-Aleatorio 2-Mais_perto 3-Max_dif)
     */
    protected int tipo;

    /**
     *  número de quadriculas analisadas em toda a volta da posição do agente
     */
    protected int campo_visao;

    /**
     *  distância percorrida pelo agente durante a simulação
     */
    protected double dist_percorrida;

    /**
     *  número de objectos diferentes aprendidos
     */
    protected int n_obj_dif;

    /**
     *  números de objectos aprendidos
     */
    protected int n_obj;

    /**
     *  lista dos objectos em memória
     */
    protected ArrayList<Objecto> memoria;

    /**
     *  lista de coordenadas onde esteve o agente
     */
    protected ArrayList<Coordenadas> caminho;

    /**
     *  lista de objectos existentes no campo de visão
     */
    protected ArrayList<Objecto> campo_visao_list;

    /**
     *  lista de todos os campos de visão 
     */
    protected ArrayList<Objecto> percecoes;

    // constructor da classe Agente
    Agente(int identificacao, String c, String fg, int x, int y, int t, int cv){
        super(identificacao, c, fg, x, y);
        tipo = t; //posteriormente definido em cada tipo de Agente
        campo_visao = cv;
        dist_percorrida = 0; // inicialmente o robot ainda não se moveu
        n_obj_dif = 0; // inicialmente a zero - ainda não "captou" nenhum objecto
        n_obj = 0; // inicialmente a zero
        memoria = new ArrayList<>();
        caminho = new ArrayList<>();
        campo_visao_list = new ArrayList<>();
        percecoes = new ArrayList<>();
    }
        
    /**
     * Método para adquirir o campo de visão
     * @param objectos lista de objectos no ambiente
     * @param comprimento comprimento do ambiente
     * @param largura largura do ambiente
     */
    public void adquire_campo_visao(ArrayList<Objecto> objectos, int comprimento, int largura) {
            
        // posição em que o robot se encontra
        int pos_x_robot = this.posicao.getPos_x();
        int pos_y_robot = this.posicao.getPos_y();
            
        //limpa o campo de visão anterior
        this.campo_visao_list.clear();
            
        for (int x=-this.campo_visao; x<=this.campo_visao;x++){
            for (int y=-this.campo_visao; y<=this.campo_visao; y++){
                //posição que vai analisar
                int pos_x = this.posicao.getPos_x() + x;
                int pos_y = this.posicao.getPos_y() + y;
                    
                // não analisa a posição em que o robot está
                if(pos_x!=pos_x_robot || pos_y!=pos_y_robot)
                {
                    //verificar se a posição analisada está dentro do ambiente
                    if (pos_x < comprimento && pos_x>0 && pos_y>0 && pos_y < largura){
                        //verifica se a lista de objectos está vazia
                        if (!objectos.isEmpty()){
                            //percorrer lista de objectos para verificar se algum está na posição em análise
                            for (Objecto obj : objectos){
                                if(obj.posicao.getPos_x()== pos_x && obj.posicao.getPos_y() == pos_y){
                                    //objecto está na posição analisada -> insere na lista do campo de visão
                                    this.campo_visao_list.add(obj);
                                    this.percecoes.add(obj);
                                    this.n_obj++;  //incrementa o nº de objectos aprendidos
                                }
                            }
                        }
                        else{
                            Interface.janela_popup("Ainda não há objectos no Ambiente!\n");
                        }
                    }
                }
            }
        }
    }
        
    /**
     * Método para listar a memória - utilizada para debug
     */
    public void listar_memoria() {
        System.out.println("\n**MEMÓRIA**");
        if(!memoria.isEmpty()){ //verifica se a memória não está vazia
            for (Objecto obj : memoria){
                System.out.println("Objecto "+obj.id);
                System.out.println("-cor: "+obj.cor);
                System.out.println("-forma: "+obj.forma_geometrica);
                System.out.println("-posicao: ("+obj.posicao.getPos_x()+","+obj.posicao.getPos_y()+")");
                System.out.println("-tipo: "+obj.getTipo());
                System.out.println("------------------------");
            }
        }
        else
            System.out.println("Memória vazia!\n");
    }

    /**
     * Método que calcula a distância percorrida pelo agente
     */
    public void calcular_distancia() {
        Coordenadas pos_inicial = this.caminho.get(0);
        int pi_x = pos_inicial.getPos_x();
        int pi_y = pos_inicial.getPos_y();
        Coordenadas pos_final = this.caminho.get(this.caminho.size()-1);
        int pf_x = pos_final.getPos_x();
        int pf_y = pos_final.getPos_y();
                
        this.dist_percorrida = Math.sqrt(Math.pow((pi_x-pf_x),2)+Math.pow((pi_y-pf_y),2));
                
    }

    /**
     * Método para listar o caminho do agente - utilizada para debug
     */
    public void listar_caminho(){
        System.out.println("\n**CAMINHO**");
        if (!this.caminho.isEmpty()){
            for (Coordenadas pos : this.caminho){
                System.out.println("("+pos.getPos_x()+","+pos.getPos_y()+")");
            }
        }
        else{
            System.out.println("O Robot ainda não saiu da posição inicial\n");
        }
    }

    /**
     * Método que permite o movimento dos agentes
     * @param objectos lista dos objectos existentes no ambiente
     * @param comprimento comprimento do ambiente
     * @param largura largura do ambiente
     */
    public abstract void mover(ArrayList<Objecto> objectos, int comprimento, int largura);
        
    /**
     * Método que move um agente para uma posição escolhida aleatoriamente
     * @param objectos lista dos objectos existentes no ambiente
     * @param comprimento comprimento do ambiente
     * @param largura largura do ambiente
     */
    public void mover_aleatorio(ArrayList<Objecto> objectos, int comprimento, int largura){
        /* posição em que o agente se encontra*/
        int pos_x_robot = this.posicao.getPos_x();
        int pos_y_robot = this.posicao.getPos_y();
        // intervalos de valores (de posições) dentro do campo de visão do agente
        int range_x_inf = pos_x_robot - campo_visao; 
        // posição não pode ser fora do ambiente
        if(range_x_inf < 0){
            for(int i=1; i<=campo_visao; i++){
                range_x_inf = pos_x_robot - (campo_visao - i);
                if (range_x_inf >= 0){
                    break;
                }
            }
        }
            
        int range_x_sup = pos_x_robot + campo_visao;
            
        if(range_x_sup >= comprimento){
            for(int i=1; i<=campo_visao; i++){
                range_x_sup = pos_x_robot + (campo_visao + i);
                if (range_x_sup < comprimento){
                    break;
                }
            }
        }

        int range_y_inf = pos_y_robot - campo_visao;
            
        if(range_y_inf < 0){
            for(int i=1; i<=campo_visao; i++){
                range_y_inf = pos_y_robot - (campo_visao - i);
                if (range_y_inf >= 0){
                    break;
                }
            }
        }
        int range_y_sup = pos_y_robot + campo_visao;
        if(range_y_sup >= largura){
            for(int i=1; i<=campo_visao; i++){
                range_y_sup = pos_y_robot + (campo_visao + i);
                if (range_y_sup < largura){
                    break;
                }
            }
        }
        // escolhe nova posição aleatória
        Random gerador = new Random(); //cria novo gerador aleatório
        int x = gerador.nextInt(range_x_sup) + range_x_inf; //escolhe aleatoriamente um número entre range_x_inf e range_x_sup
        int y = gerador.nextInt(range_y_sup) + range_y_inf; //escolhe aleatoriamente um número entre range_y_inf e range_y_sup

        // não se pode mover para a posição onde está
        if(x != pos_x_robot || y != pos_y_robot){
		
            //verifica se nunca esteve nessa posição
            if(!verifica_pos(x ,y)){ 

                //se nunca tiver estado nessa posição, move-se para lá
                Coordenadas new_pos = new Coordenadas(x, y); // cria nova posição com as posições escolhidas
                this.caminho.add(new_pos); // acrescenta ao caminho a posição para onde vai
                this.posicao.setPos_x(x);
                this.posicao.setPos_y(y);

                int num_objs = objectos.size();
                int flag = 0;
                // aprende o objecto nessa posição, se existir
                for (int i = 0; i < num_objs && flag == 0; i++){
                    if (objectos.get(i).posicao.getPos_x() == x && objectos.get(i).posicao.getPos_y() == y){ // se o objecto estiver nessa posição
                        this.n_obj_dif++;
                        this.memoria.add(objectos.get(i)); //adiciona o objecto para onde vai à memória

                        flag = 1; //encontrou o objecto

                    }
                }
                // se não existir um objecto nessa posição não tem nada a aprender
           }
        }
    }
 
    /**
     * Método que verifica se o agente já esteve naquela posição
     * @param pos_x coordenada x
     * @param pos_y coordenada y
     * @return true/false em caso de ser uma posição valida ou não, respectivamente
     */
    public boolean verifica_pos(int pos_x, int pos_y){ 
        int passos = this.caminho.size();
        for(int i = 0; i < passos; i++){
            // se o objecto já estiver estado naquela posição - devolve TRUE
            return this.caminho.get(i).getPos_x()==pos_x && this.caminho.get(i).getPos_y()==pos_y;
        }
        return false;
    }

}