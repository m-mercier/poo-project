package Simulador;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ana Guarino
 * @author Marta Mercier
 */
public class Ambiente {

    private int largura;
    private int comprimento;
    private int tempo_vida;
    private final ArrayList<Objecto> objectos; // Array com todos os objectos no ambiente
    private final ArrayList<Agente> agentes; // Array com todos os agentes no ambiente
     
    // constructor da classe Ambiente
    Ambiente(){ // quando a largura, comprimento e tempo de vida não são definidos em ficheiro, atribuimos estes valores por omissão
        largura = 10;
        comprimento = 10;
        tempo_vida = 5;
        objectos =  new ArrayList<>();
        agentes = new ArrayList<>();
    }
        
    // constructor da classe Ambiente
    Ambiente(int l, int c, int tv){
        largura = l;
        comprimento = c;
        tempo_vida = tv;
        objectos =  new ArrayList<>();
        agentes = new ArrayList<>();
    }

    /**
     * Método que altera a largura do ambiente
     * @param l largura
     */
    public void setLargura(int l) {
        largura = l;
    }
        
    /**
     * Método que altera o comprimento do ambiente
     * @param c comprimento
     */
    public void setComprimento(int c) {
        comprimento = c;
    }
        
    /**
     * Método que altera o tempo de vida de um agente
     * @param tv tempo de vida de um agente
     */
    public void setTV(int tv) {
	tempo_vida = tv;
    }
        
    /**
     * Método que devolve a lista de agentes
     * @return lista de agentes
     */
    public ArrayList<Agente> getListaAgentes (){
        return agentes;
    }
        
    /**
     * Método que executa toda a simulação
     */
    public void run(){
        try {
            // ler informações do ficheiro e configurar o ambiente
            ler_ficheiro_config();
        } catch (IOException ex) {
            Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
        }
        // correr o programa -> em ciclo -> nº de iterações = tempo de vida
        for (Agente a : agentes){
            int tv_aux = this.tempo_vida;
            while(tv_aux>0){
                // Agente adquire o seu campo de visão
                a.adquire_campo_visao(this.objectos, this.comprimento, this.largura);
               
                // Agente move-se
                a.mover(objectos, comprimento, largura);
                
                //diminui tempo de vida
                tv_aux--;
            }
            a.calcular_distancia();
            // escreve informações em ficheiro
            try {
                escrever_ficheiro_objectos("memoria.txt", 0);
            } catch (IOException ex) {
                Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                escrever_ficheiro_objectos("percepcoes.txt", 1);
            } catch (IOException ex) {
                Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                escrever_ficheiro_coordenadas("coordenadas.txt");
            } catch (IOException ex) {
                Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                escrever_ficheiro_info();
            } catch (IOException ex) {
                Logger.getLogger(Ambiente.class.getName()).log(Level.SEVERE, null, ex);
            }
               
        }
    }

    /**
     * Método que lista objectos - utilizada para debug
     * @param lista lista de objectos do ambiente
     */
    public void listar_objectos(ArrayList<Objecto> lista){
        if (!lista.isEmpty()){
            for (Objecto obj : lista){
                System.out.println("Objecto "+obj.id);
                System.out.println("-cor: "+obj.cor);
                System.out.println("-forma: "+obj.forma_geometrica);
                System.out.println("-posicao: ("+obj.posicao.getPos_x()+","+obj.posicao.getPos_y()+")");
                System.out.println("-tipo: "+obj.getTipo());
                System.out.println("------------------------");
            }
        }
        else{
            Interface.janela_popup("A lista está vazia! \n");
        }
    }

    /**
     * Método que escreve ficheiro de memória ou de percepções
     * @param nome nome do ficheiro a criar
     * @param tipo tipo de ficheiro(tipo=0 ficheiro de memoria tipo!=0 ficheiro de percepções)
     * @throws IOException
     */
    public void escrever_ficheiro_objectos(String nome, int tipo) throws IOException
    {
        int i,tamanho_agentes=agentes.size();
        int j,tamanho;
            
        try {
            try (PrintWriter f = new PrintWriter(new FileWriter(new File(nome)))) {
                for(i=0;i<tamanho_agentes;i++)
                {
                    if(tipo==0)
                    {
                        tamanho=agentes.get(i).memoria.size();
                        f.print("A memória do ");
                        if(agentes.get(i).tipo==1)
                        {
                            f.println("Robot Aleatório é: ");
                        }
                        if(agentes.get(i).tipo==2)
                        {
                            f.println("Robot Mais perto é:");
                        }
                        if(agentes.get(i).tipo==3)
                        {
                            f.println("Robot Máxima diferença é:");
                        }
                        for(j=0;j<tamanho;j++)
                        {
                            f.println("");
                            f.println("ID: "+agentes.get(i).memoria.get(j).id);
                            f.println("Cor: "+agentes.get(i).memoria.get(j).cor);
                            f.println("Forma Geometrica: "+agentes.get(i).memoria.get(j).forma_geometrica);
                            f.println("Posição");
                            f.println("Coordenada x:"+agentes.get(i).memoria.get(j).posicao.getPos_x());
                            f.println("Coordenada y:"+agentes.get(i).memoria.get(j).posicao.getPos_y());
                            f.println("");
                        } 
                    }else
                    {
                        tamanho=agentes.get(i).percecoes.size();
                        f.print("As perceções do ");
                        if(agentes.get(i).tipo==1)
                        {
                            f.println("Robot Aleatório são: ");
                        }
                        if(agentes.get(i).tipo==2)
                        {
                            f.println("Robot Mais perto são:");
                        }
                        if(agentes.get(i).tipo==3)
                        {
                        f.println("Robot Máxima diferença são:");
                        }
                        for(j=0;j<tamanho;j++)
                        {
                            f.println("\nID: "+agentes.get(i).percecoes.get(j).id);
                            f.println("Cor: "+agentes.get(i).percecoes.get(j).cor);
                            f.println("Forma Geometrica: "+agentes.get(i).percecoes.get(j).forma_geometrica);
                            f.println("Posição");
                            f.println("Coordenada x:"+agentes.get(i).percecoes.get(j).posicao.getPos_x());
                            f.println("Coordenada y:"+agentes.get(i).percecoes.get(j).posicao.getPos_y()+"\n");

                        }

                    }               
                }
                f.close();
            }
        } catch (IOException e) {
            Interface.janela_popup("Erro");
        }
            
    }
        

    /**
     * Método que escreve ficheiro com o caminho do agente
     * @param nome nome do ficheiro
     * @throws IOException
     */
    public void escrever_ficheiro_coordenadas(String nome) throws IOException
    {
        int i,tamanho_agentes=agentes.size();
        int j,tamanho;
            
        try {
            try (PrintWriter f = new PrintWriter(new FileWriter(new File(nome)))) {
                for(i=0;i<tamanho_agentes;i++)
                {
                    tamanho=agentes.get(i).caminho.size();
                    f.print("O caminho do ");
                    if(agentes.get(i).tipo==1)
                    {
                        f.println("Robot Aleatório foi:\n");
                    }
                    if(agentes.get(i).tipo==2)
                    {
                        f.println("Robot Mais perto foi:\n");
                    }
                    if(agentes.get(i).tipo==3)
                    {
                        f.println("Robot Máxima diferença foi:\n");
                    }
                    for(j=0;j<tamanho;j++)
                    {
                        f.println((j+1)+"ªPosição-("+agentes.get(i).caminho.get(j).getPos_x()+","+agentes.get(i).caminho.get(j).getPos_y()+")\n");
                    }     
                }
                f.close();
            }
                
        } catch (IOException e) {
            Interface.janela_popup("Erro");
        }
            
    }

    /**
     * Método que escreve ficheiro de estatísticas
     * @throws IOException
     */
    public void escrever_ficheiro_info () throws IOException{
        try{
            try(PrintWriter f = new PrintWriter(new FileWriter(new File("info.txt")))){
                for(Agente A : agentes){
                    f.println("Robot "+A.id+":");
                    f.format("\tDistância Percorrida = %.2f", A.dist_percorrida);
                    f.println("");
                    f.println("\tNº de objectos diferentes aprendidos: "+A.n_obj_dif);
                    f.println("\tNº de objectos aprendidos: "+A.n_obj);
                }
                f.close();
            }
        } catch (IOException e) {
            Interface.janela_popup("Erro");
        }
    }

    /**
     * Método que lê o ficheiro de configurações e configura o ambiente com os dados em ficheiro
     * @throws IOException
     */
    public void ler_ficheiro_config() throws IOException{
	try{
            String tipo, cor, forma;
            int i, id = 0, pos_x, pos_y, campo_visao;
            try(BufferedReader f = new BufferedReader(new FileReader(new File("config.txt")))){
                // lê linhas iniciais do ficheiro
                for(i=0;i<2;i++){
                    f.readLine();
                }

                // lê configurações do ambiente
                String linha = f.readLine();
                setLargura(Integer.parseInt(linha.substring(9)));

		linha = f.readLine();
                setComprimento(Integer.parseInt(linha.substring(13)));
                       
		linha = f.readLine();
                setTV(Integer.parseInt(linha.substring(15)));

		// lê título intermédio do ficheiro
		for(i=0;i<3;i++){
                    f.readLine();
                }
		// lê o ficheiro restante até ao fim - configurações de agentes e objectos
		while((linha = f.readLine()) != null) {
                    if(linha.equals("Agente")){

                    	// lê todas as características do agente
                        linha = f.readLine();
                        tipo = linha.substring(6);

			linha = f.readLine();
			cor = linha.substring(5);

			linha = f.readLine();
			forma = linha.substring(18);

			linha = f.readLine();
                        pos_x = Integer.parseInt(linha.substring(11));
					
                        linha = f.readLine();
                        pos_y = Integer.parseInt(linha.substring(11));
                                        
                                        
			linha = f.readLine();
                        campo_visao = Integer.parseInt(linha.substring(16));
                                       
			// cria agente
			switch(tipo){

                            case "Aleatório":
                            {
				Aleatorio agente = new Aleatorio(id++, cor, forma, pos_x, pos_y, campo_visao);
                                                        
                                // adiciona o agente ao array de agentes
                                agentes.add(agente);
                                                        
                                // adiciona a primeira posição do agente ao caminho
                                if(verifica_coord(pos_x, pos_y)){
                                    Coordenadas pos = new Coordenadas (pos_x, pos_y);
                                    agente.caminho.add(pos);
                                }
                                else{
                                    Interface.janela_popup(String.format("Coordenadas do Agente %d inválidas! Corrija o ficheiro, por favor.\n", agente.id));
                                    System.exit(0);
                                }
                                break;
                            }
                            case "MaxDif":
                            {
                                Max_dif agente = new Max_dif(id++, cor, forma, pos_x, pos_y, campo_visao);
                                                        
                                // adiciona o agente ao array de agentes
                                agentes.add(agente);
                                                        
                                // adiciona a primeira posição do agente ao caminho
                                if(verifica_coord(pos_x, pos_y)){
                                    Coordenadas pos = new Coordenadas (pos_x, pos_y);
                                    agente.caminho.add(pos);
                                }
                                else{
                                    Interface.janela_popup(String.format("Coordenadas do Agente %d inválidas! Corrija o ficheiro, por favor.\n", agente.id));
                                    System.exit(0);
                                }
                                break;
                            }

                            case "MaisPerto":
                            {
                                Mais_perto agente = new Mais_perto(id++, cor, forma, pos_x, pos_y, campo_visao);

                                // adiciona o agente ao array de agentes
                                agentes.add(agente);

                                // adiciona a primeira posição do agente ao caminho
                                if(verifica_coord(pos_x, pos_y)){
                                    Coordenadas pos = new Coordenadas (pos_x, pos_y);
                                    agente.caminho.add(pos);
                                }
                                else{
                                    Interface.janela_popup(String.format("Coordenadas do Agente %d inválidas! Corrija o ficheiro, por favor.\n", agente.id));
                                    System.exit(0);
                                }
                                                        
                                break;
                            }

			}

                        // lê linha de intervalo
                        f.readLine();
                    }

                    else if (linha.equals("Objecto")){

                        // lê todas as características do objecto
                        linha = f.readLine();
                        tipo = linha.substring(6);

                        linha = f.readLine();
                        cor = linha.substring(5);

                        linha = f.readLine();
                        forma = linha.substring(18);

                        linha = f.readLine();
                        pos_x = Integer.parseInt(linha.substring(11));

                        linha = f.readLine();
                        pos_y = Integer.parseInt(linha.substring(11));

                        // cria objecto
                        Objecto obj = new Objecto(id++, cor, forma, pos_x, pos_y, tipo);

                        // adiciona o objecto ao array de objectos
                        objectos.add(obj);

                        // lê linha de intervalo
                        f.readLine();
                    }
		}
                f.close();
			
            } catch (FileNotFoundException e){
		Interface.janela_popup("Ficheiro inexistente!");
            }
        } catch (IOException e){
            Interface.janela_popup("Erro ao ler uma linha do ficheiro.");
        }
    }

    /**
     * Método que verifica se as coordenadas estão dentro do ambiente
     * @param pos_x coordenada x
     * @param pos_y coordenada y
     * @return true/false caso as coordenadas estejam dentro ou fora do ambiente, respectivamente
     */
    public boolean verifica_coord(int pos_x, int pos_y){
        return !(pos_x < 0 || pos_x >= comprimento || pos_y < 0 || pos_y >= largura);
    }
        
}