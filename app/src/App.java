import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Algorithms.BranchAndBound;
import Data.Base;
import Models.Cenario;
import Models.Projeto;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<List<String>> INCOMPATIBILIDADES_PADRAO =
            new ArrayList<List<String>>() {{ add(Arrays.asList("P4", "P7")); }};

    public static void main(String[] args) {
        Base base = Base.getInstance();
        base.preencherBase();

        boolean executando = true;
        while (executando) {
            System.out.println("\n========== SELEÇÃO DE PROJETOS ==========");
            System.out.println("1 - Listar projetos");
            System.out.println("2 - Adicionar projeto");
            System.out.println("3 - Alterar projeto");
            System.out.println("4 - Remover projeto");
            System.out.println("5 - Executar Cenário A (R$ 50.000)");
            System.out.println("6 - Executar Cenário B (R$ 30.000, P7 obrigatório)");
            System.out.println("7 - Criar/executar cenário personalizado");
            System.out.println("0 - Sair");
            int opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1: listarProjetos(base); break;
                case 2: adicionarProjeto(base); break;
                case 3: alterarProjeto(base); break;
                case 4: removerProjeto(base); break;
                case 5: executar(base, buscarCenario(base, "Cenario A")); break;
                case 6: executar(base, buscarCenario(base, "Cenario B")); break;
                case 7: executarPersonalizado(base); break;
                case 0: executando = false; break;
                default: System.out.println("Opção inválida."); break;
            }
        }
        System.out.println("Programa encerrado.");
    }

    private static void listarProjetos(Base base) {
        System.out.println("\nCódigo | Nome | Custo | Benefício");
        for (Projeto p : base.getProjetos()) System.out.println(p);
    }

    private static void adicionarProjeto(Base base) {
        String codigo = lerTexto("Código (ex.: P9): ");
        if (buscarProjeto(base, codigo) != null) {
            System.out.println("Já existe um projeto com esse código."); return;
        }
        String nome = lerTexto("Nome: ");
        float custo = lerFloat("Custo em R$: ");
        int beneficio = lerInt("Benefício (inteiro): ");
        if (custo < 0 || beneficio < 0) {
            System.out.println("Custo e benefício não podem ser negativos."); return;
        }
        base.adicionarProjeto(new Projeto(codigo, nome, custo, beneficio));
        System.out.println("Projeto adicionado.");
    }

    private static void alterarProjeto(Base base) {
        String codigo = lerTexto("Código do projeto a alterar: ");
        Projeto p = buscarProjeto(base, codigo);
        if (p == null) { System.out.println("Projeto não encontrado."); return; }
        p.setNome(lerTexto("Novo nome [" + p.getNome() + "]: "));
        float custo = lerFloat("Novo custo em R$: ");
        int beneficio = lerInt("Novo benefício: ");
        if (custo < 0 || beneficio < 0) {
            System.out.println("Custo e benefício não podem ser negativos; alterações numéricas não aplicadas.");
            return;
        }
        p.setCusto(custo);
        p.setBeneficio(beneficio);
        System.out.println("Projeto atualizado.");
    }

    private static void removerProjeto(Base base) {
        String codigo = lerTexto("Código do projeto a remover: ");
        Projeto p = buscarProjeto(base, codigo);
        if (p == null) { System.out.println("Projeto não encontrado."); return; }
        base.getProjetos().remove(p);
        for (Cenario c : base.getCenarios()) c.getObrigatorios().remove(p);
        System.out.println("Projeto removido.");
    }

    private static void executarPersonalizado(Base base) {
        if (base.getProjetos().isEmpty()) {
            System.out.println("Cadastre pelo menos um projeto."); return;
        }
        String nome = lerTexto("Nome do cenário: ");
        float orcamento = lerFloat("Orçamento em R$: ");
        if (orcamento < 0) { System.out.println("Orçamento não pode ser negativo."); return; }

        List<Projeto> obrigatorios = new ArrayList<>();
        String codigos = lerTexto("Códigos obrigatórios separados por vírgula (Enter para nenhum): ");
        if (!codigos.trim().isEmpty()) {
            for (String codigo : codigos.split(",")) {
                Projeto p = buscarProjeto(base, codigo.trim());
                if (p == null) {
                    System.out.println("Projeto obrigatório não encontrado: " + codigo.trim()); return;
                }
                if (!obrigatorios.contains(p)) obrigatorios.add(p);
            }
        }

        List<List<String>> incompatibilidades = new ArrayList<>();
        System.out.println("Informe pares incompatíveis (um par por linha, formato P4,P7).");
        System.out.println("Pressione Enter em uma linha vazia para terminar.");
        while (true) {
            String linha = lerTexto("Par: ");
            if (linha.trim().isEmpty()) break;
            String[] partes = linha.split(",");
            if (partes.length != 2 || buscarProjeto(base, partes[0].trim()) == null
                    || buscarProjeto(base, partes[1].trim()) == null) {
                System.out.println("Par inválido. Use dois códigos existentes separados por vírgula."); continue;
            }
            incompatibilidades.add(Arrays.asList(partes[0].trim(), partes[1].trim()));
        }
        executar(base, new Cenario(nome, orcamento, obrigatorios), incompatibilidades);
    }

    private static void executar(Base base, Cenario cenario) {
        executar(base, cenario, INCOMPATIBILIDADES_PADRAO);
    }

    private static void executar(Base base, Cenario cenario, List<List<String>> incompatibilidades) {
        if (cenario == null) {
            System.out.println("Cenário não encontrado."); return;
        }
        System.out.println("\n--- " + cenario.getNome() + " ---");
        System.out.printf("Orçamento: R$ %.2f%n", cenario.getOrcamento());
        System.out.print("Obrigatórios: ");
        if (cenario.getObrigatorios().isEmpty()) System.out.println("nenhum");
        else System.out.println(cenario.getObrigatorios());

        System.out.println("Incompatibilidades: " + incompatibilidades);
        BranchAndBound algoritmo = new BranchAndBound(base.getProjetos(), cenario, incompatibilidades);
        BranchAndBound.Resultado r = algoritmo.resolver();
        if (!r.viavel) {
            System.out.println("Não existe solução viável que respeite o orçamento e as restrições.");
        } else {
            System.out.println("Projetos selecionados:");
            for (Projeto p : r.selecionados) System.out.println("  " + p);
            System.out.printf("Custo total: R$ %.2f%n", r.custoTotal);
            System.out.println("Benefício total: " + r.beneficioTotal);
        }
        System.out.println("\nEstatísticas Branch-and-Bound:");
        System.out.println("Nós explorados: " + r.nosExplorados);
        System.out.println("Podas por limite superior: " + r.podasPorLimite);
        System.out.println("Podas por restrição/orçamento: " + r.podasPorRestricao);
    }

    private static Cenario buscarCenario(Base base, String nome) {
        for (Cenario c : base.getCenarios()) if (c.getNome().equalsIgnoreCase(nome)) return c;
        return null;
    }

    private static Projeto buscarProjeto(Base base, String codigo) {
        for (Projeto p : base.getProjetos()) if (p.getCodigo().equalsIgnoreCase(codigo)) return p;
        return null;
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine().trim();
    }

    private static int lerInt(String mensagem) {
        while (true) {
            try { return Integer.parseInt(lerTexto(mensagem)); }
            catch (NumberFormatException e) { System.out.println("Informe um número inteiro válido."); }
        }
    }

    private static float lerFloat(String mensagem) {
        while (true) {
            try { return Float.parseFloat(lerTexto(mensagem).replace(",", ".")); }
            catch (NumberFormatException e) { System.out.println("Informe um número válido."); }
        }
    }
}
