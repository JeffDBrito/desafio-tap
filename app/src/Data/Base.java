package Data;

import java.util.ArrayList;
import java.util.List;

import Models.Projeto;

public class Base {

    private static Base instance;
    private boolean projetosbasecriados;

    private List<Projeto> projetos;

    private Base() {
        projetos = new ArrayList<>();
        projetosbasecriados = false;
    }

    public static Base getInstance() {
        if (instance == null) {
            instance = new Base();
        }

        return instance;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void adicionarProjeto(Projeto projeto) {
        projetos.add(projeto);
    }

    public void criarProjetosBase(){

        if(!this.projetosbasecriados){
            Projeto p1 = new Projeto("P1", "Modernização dos laboratórios", 18000, 90);
            this.adicionarProjeto(p1);
    
            Projeto p2 = new Projeto("P2","Plataforma de ensino", 12000, 65);
            this.adicionarProjeto(p2);
    
            Projeto p3 = new Projeto("P3", "Rede sem fio institucional", 10000, 55);
            this.adicionarProjeto(p3);
    
            this.projetosbasecriados = true;
            System.out.println("Projetos base criados com sucesso!");
        }else{
            System.out.println("Projetos base já foram criados!");
        }

    }

}
