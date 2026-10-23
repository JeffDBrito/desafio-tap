package Models;

import java.util.ArrayList;
import java.util.List;

public class Cenario {
    
    private String nome;
    private float orcamento;
    private List<Projeto> obrigatorios;

    public Cenario(String nome, float orcamento, List<Projeto> obrigatorios) {
        this.nome = nome;
        this.orcamento = orcamento;
        if(obrigatorios != null){
            this.obrigatorios = obrigatorios;
        }else{
            this.obrigatorios = new ArrayList<>();
        }
    }

    public String toString(){
        String obrigatorios_str = this.obrigatorios.toString();
        if(this.obrigatorios.size() == 0 ){
            obrigatorios_str = "Nenhum projeto obrigatorio";
        }
        return this.nome + " | "+this.orcamento+" | "+obrigatorios_str;
    }

    // --- Nome

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    // --- Orcamento

    public float getOrcamento() {
        return this.orcamento;
    }
    
    public void setOrcamento(float orcamento) {
        this.orcamento = orcamento;
    }

    // --- Projetos obrigatorios

    public List<Projeto> getObrigatorios() {
        return this.obrigatorios;
    }

    public void setObrigatorios(List<Projeto> obrigatorios) {
        this.obrigatorios = obrigatorios;
    }

    public void adicionarObrigatorio(Projeto projeto){
        this.obrigatorios.add(projeto);
    }

}
