package Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import Models.Cenario;
import Models.Projeto;

public class BranchAndBound {
    public static class Resultado {
        public boolean viavel;
        public List<Projeto> selecionados = new ArrayList<>();
        public double custoTotal;
        public int beneficioTotal;
        public long nosExplorados;
        public long podasPorLimite;
        public long podasPorRestricao;
    }

    private List<Projeto> projetos;
    private List<Projeto> obrigatorios;
    private List<List<String>> incompatibilidades;
    private double orcamento;
    private Resultado resultado;
    private List<Projeto> atuais;
    private List<Projeto> ordenados;

    public BranchAndBound(List<Projeto> projetos, Cenario cenario,
                          List<List<String>> incompatibilidades) {
        this.projetos = new ArrayList<>(projetos);
        this.obrigatorios = new ArrayList<>(cenario.getObrigatorios());
        this.orcamento = cenario.getOrcamento();
        this.incompatibilidades = incompatibilidades == null
                ? new ArrayList<>() : incompatibilidades;
    }

    public Resultado resolver() {
        resultado = new Resultado();
        atuais = new ArrayList<>();
        ordenados = new ArrayList<>(projetos);
        // Ordem por benefício/custo melhora a qualidade da primeira solução.
        ordenados.sort(new Comparator<Projeto>() {
            public int compare(Projeto a, Projeto b) {
                double ra = a.getCusto() <= 0 ? Double.POSITIVE_INFINITY : a.getBeneficio() / a.getCusto();
                double rb = b.getCusto() <= 0 ? Double.POSITIVE_INFINITY : b.getBeneficio() / b.getCusto();
                return Double.compare(rb, ra);
            }
        });

        for (Projeto p : obrigatorios) {
            if (!projetos.contains(p)) {
                resultado.viavel = false;
                return resultado;
            }
            if (!atuais.contains(p)) atuais.add(p);
        }
        if (!restricoesValidas(atuais) || custo(atuais) > orcamento) {
            resultado.viavel = false;
            resultado.podasPorRestricao++;
            return resultado;
        }

        // Os obrigatórios já estão selecionados. A recursão decide os demais.
        List<Projeto> opcionais = new ArrayList<>();
        for (Projeto p : ordenados) if (!obrigatorios.contains(p)) opcionais.add(p);
        explorar(opcionais, 0, custo(atuais), beneficio(atuais));
        return resultado;
    }

    private void explorar(List<Projeto> opcionais, int indice, double custoAtual, int beneficioAtual) {
        resultado.nosExplorados++;

        double limite = limiteSuperior(opcionais, indice, custoAtual, beneficioAtual);
        if (resultado.viavel && limite <= resultado.beneficioTotal) {
            resultado.podasPorLimite++;
            return;
        }

        if (indice >= opcionais.size()) {
            if (!resultado.viavel || beneficioAtual > resultado.beneficioTotal) {
                resultado.viavel = true;
                resultado.selecionados = new ArrayList<>(atuais);
                resultado.custoTotal = custoAtual;
                resultado.beneficioTotal = beneficioAtual;
            }
            return;
        }

        Projeto p = opcionais.get(indice);

        // Ramo incluir: só segue se orçamento e incompatibilidades permitirem.
        atuais.add(p);
        if (custoAtual + p.getCusto() <= orcamento && restricoesValidas(atuais)) {
            explorar(opcionais, indice + 1, custoAtual + p.getCusto(),
                     beneficioAtual + p.getBeneficio());
        } else {
            resultado.podasPorRestricao++;
        }
        atuais.remove(atuais.size() - 1);

        // Ramo excluir.
        explorar(opcionais, indice + 1, custoAtual, beneficioAtual);
    }

    private double limiteSuperior(List<Projeto> opcionais, int indice, double custoAtual, int beneficioAtual) {
        double capacidade = orcamento - custoAtual;
        double limite = beneficioAtual;
        if (capacidade < 0) return Double.NEGATIVE_INFINITY;

        List<Projeto> restantes = new ArrayList<>(opcionais.subList(indice, opcionais.size()));
        restantes.sort((a, b) -> {
            double ra = a.getCusto() <= 0 ? Double.POSITIVE_INFINITY : (double)a.getBeneficio()/a.getCusto();
            double rb = b.getCusto() <= 0 ? Double.POSITIVE_INFINITY : (double)b.getBeneficio()/b.getCusto();
            return Double.compare(rb, ra);
        });
        for (Projeto p : restantes) {
            if (p.getBeneficio() <= 0) continue;
            if (p.getCusto() <= capacidade) {
                capacidade -= p.getCusto();
                limite += p.getBeneficio();
            } else {
                if (p.getCusto() > 0) limite += ((double)p.getBeneficio() * capacidade / p.getCusto());
                break;
            }
        }
        return limite;
    }

    private boolean restricoesValidas(List<Projeto> selecao) {
        for (List<String> par : incompatibilidades) {
            if (par.size() >= 2 && contemCodigo(selecao, par.get(0)) && contemCodigo(selecao, par.get(1))) return false;
        }
        return true;
    }

    private boolean contemCodigo(List<Projeto> lista, String codigo) {
        for (Projeto p : lista) if (p.getCodigo().equalsIgnoreCase(codigo)) return true;
        return false;
    }

    private double custo(List<Projeto> lista) {
        double total = 0;
        for (Projeto p : lista) total += p.getCusto();
        return total;
    }

    private int beneficio(List<Projeto> lista) {
        int total = 0;
        for (Projeto p : lista) total += p.getBeneficio();
        return total;
    }
}
