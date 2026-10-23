package Data;

import java.util.ArrayList;
import java.util.List;

import Models.Cenario;
import Models.Projeto;

public class Base {

    private static Base instance;
    private boolean projetosBaseCriados;
    private boolean cenariosBaseCriados;

    private List<Projeto> projetos;
    private List<Cenario> cenarios;

    private Base() {
        projetos = new ArrayList<>();
        cenarios = new ArrayList<>();
        projetosBaseCriados = false;
        cenariosBaseCriados = false;
    }

    // --- Singleton

    public static Base getInstance() {
        if (instance == null) {
            instance = new Base();
        }

        return instance;
    }

    // --- Base inicial

    public void preencherBase(){
        System.out.println("\n> Iniciando base de dados!\n");
        criarProjetosBase();
        criarCenariosBase();
    }

    // --- Projetos

    public List<Projeto> getProjetos() {
        return this.projetos;
    }

    public void adicionarProjeto(Projeto projeto) {
        this.projetos.add(projeto);
    }

    public void criarProjetosBase(){

        if(!this.projetosBaseCriados){
            Projeto p1 = new Projeto("P1", "Modernização dos laboratórios", 18000, 90);
            this.adicionarProjeto(p1);
    
            Projeto p2 = new Projeto("P2","Plataforma de ensino", 12000, 65);
            this.adicionarProjeto(p2);
    
            Projeto p3 = new Projeto("P3", "Rede sem fio institucional", 10000, 55);
            this.adicionarProjeto(p3);

            Projeto p4 = new Projeto("P4", "Servidores de pesquisa", 22000, 110);
            this.adicionarProjeto(p4);

            Projeto p5 = new Projeto("P5", "Sistema de segurança", 8000, 40);
            this.adicionarProjeto(p5);

            Projeto p6 = new Projeto("P6", "Capacitação docente", 6000, 35);
            this.adicionarProjeto(p6);

            Projeto p7 = new Projeto("P7", "Laboratório de IA", 20000, 105);
            this.adicionarProjeto(p7);

            Projeto p8 = new Projeto("P8", "Sistema de gestão acadêmica", 14000, 75);
            this.adicionarProjeto(p8);
    
            this.projetosBaseCriados = true;
            
            for(Projeto projeto: this.projetos){
                System.out.println("Projeto "+projeto.getCodigo()+" criado!");
            }

            System.out.println("> Projetos base criados com sucesso!\n");
        }else{
            System.out.println("> Projetos base já foram criados!\n");
        }

    }

    // --- Cenarios

    public List<Cenario> getCenarios(){
        return this.cenarios;
    }

    public void adicionarCenario(Cenario cenario){
        this.cenarios.add(cenario);
    }

    public void criarCenariosBase(){

        if(!cenariosBaseCriados){
            Cenario cenario_a = new Cenario("Cenario A", 50000, null);
            this.adicionarCenario(cenario_a);
            
            Projeto projeto_p7 = null;
            for(Projeto projeto : projetos){
                if(projeto.getCodigo().compareTo("P7") == 0){
                    projeto_p7 = projeto;
                }
            }
    
            if(projeto_p7 == null){
                System.out.println("Projeto P7 não encontrado, Cenario B não será criado.\n");
            }else{
                Cenario cenario_b = new Cenario("Cenario B", 30000, null);
                cenario_b.adicionarObrigatorio(projeto_p7);
                this.adicionarCenario(cenario_b);
            }

            this.cenariosBaseCriados = true;
    
            for(Cenario cenario: this.cenarios){
                System.out.println("Cenario "+cenario.getNome()+" criado!");
            }
    
            System.out.println("> Cenarios base criados com sucesso!\n");
        }else{
            System.out.println("> Cenarios base já foram criados!\n");
        }


    }

}
