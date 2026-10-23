package Models;

public class Projeto {

    private String codigo;
    private String nome;
    private float custo;
    private int beneficio;

    public Projeto(String codigo, String nome, float custo, int beneficio){
        this.codigo = codigo;
        this.nome = nome;
        this.custo = custo;
        this.beneficio = beneficio;
    }

    public String toString(){
        return this.codigo+" | "+this.nome+" | "+this.custo+" | "+this.beneficio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public int getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(int beneficio) {
        this.beneficio = beneficio;
    }
}
