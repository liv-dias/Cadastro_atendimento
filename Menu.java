package ui;

import java.util.Scanner;

import estruturas.DNode;
import model.Atendimento;
import model.Operacao;
import model.Solicitacao;
import service.CentralAtendimento;

/**
 * Camada de interface: só lê dados do usuário e imprime resultados.
 * Todas as regras e estruturas ficam em CentralAtendimento.
 */
public class Menu {

    private final Scanner sc = new Scanner(System.in);
    private final CentralAtendimento central = new CentralAtendimento();

    public void executar() {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInt("Escolha uma opção: ");
            System.out.println();

            switch (opcao) {
                case 1  -> cadastrar();
                case 2  -> consultarProxima();
                case 3  -> atenderProxima();
                case 4  -> central.exibirFila();
                case 5  -> System.out.println("Aguardando: " + central.quantidadeAguardando());
                case 6  -> consultarUltimaOperacao();
                case 7  -> central.exibirHistoricoRecente();
                case 8  -> desfazer();
                case 9  -> central.exibirTodas();
                case 10 -> pesquisarPorCodigo();
                case 11 -> remover();
                case 12 -> porStatus();
                case 13 -> porCategoria();
                case 14 -> central.exibirAtendimentosInicioFim();
                case 15 -> central.exibirAtendimentosFimInicio();
                case 16 -> navegarHistorico();
                case 17 -> pesquisarAtendimento();
                case 18 -> System.out.println(central.resumo());
                case 0  -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void exibirMenu() {
        System.out.println("\n=================================================");
        System.out.println("    CENTRAL DE ATENDIMENTO E SERVIÇOS");
        System.out.println("=================================================");
        System.out.println("ATENDIMENTO");
        System.out.println(" 1 - Cadastrar nova solicitação");
        System.out.println(" 2 - Consultar próxima solicitação");
        System.out.println(" 3 - Atender próxima solicitação");
        System.out.println(" 4 - Exibir fila de espera");
        System.out.println(" 5 - Quantidade de solicitações aguardando");
        System.out.println("OPERAÇÕES");
        System.out.println(" 6 - Consultar última operação");
        System.out.println(" 7 - Exibir histórico recente");
        System.out.println(" 8 - Desfazer última operação");
        System.out.println("CADASTRO GERAL");
        System.out.println(" 9 - Exibir todas as solicitações");
        System.out.println("10 - Pesquisar solicitação por código");
        System.out.println("11 - Remover solicitação");
        System.out.println("12 - Exibir solicitações por status");
        System.out.println("13 - Exibir solicitações por categoria");
        System.out.println("HISTÓRICO DE ATENDIMENTOS");
        System.out.println("14 - Exibir do primeiro ao último");
        System.out.println("15 - Exibir do último ao primeiro");
        System.out.println("16 - Navegar pelo histórico");
        System.out.println("17 - Pesquisar atendimento");
        System.out.println("RELATÓRIOS");
        System.out.println("18 - Exibir resumo do sistema");
        System.out.println(" 0 - Encerrar");
    }

    // ---------- AÇÕES ----------

    private void cadastrar() {
        String nome = lerTexto("Solicitante: ");
        String descricao = lerTexto("Descrição do problema: ");
        String categoria = lerTexto("Categoria: ");
        int prioridade = lerIntFaixa("Prioridade (1 a 5): ", 1, 5);

        Solicitacao s = central.cadastrar(nome, descricao, categoria, prioridade);
        System.out.println("Solicitação cadastrada! Código: " + s.getCodigo());
    }

    private void consultarProxima() {
        Solicitacao s = central.consultarProxima();
        if (s == null) System.out.println("Não há solicitações aguardando.");
        else System.out.println("Próxima: " + s);
    }

    private void atenderProxima() {
        if (central.consultarProxima() == null) {
            System.out.println("Não há solicitações aguardando.");
            return;
        }
        String responsavel = lerTexto("Responsável pelo atendimento: ");
        Solicitacao s = central.atenderProxima(responsavel);
        System.out.println("Atendida: " + s);
    }

    private void consultarUltimaOperacao() {
        Operacao op = central.ultimaOperacao();
        if (op == null) System.out.println("Nenhuma operação registrada.");
        else System.out.println("Última operação: " + op);
    }

    private void desfazer() {
        Operacao op = central.desfazerUltima();
        if (op == null) System.out.println("Nada para desfazer.");
        else System.out.println("Desfeita: " + op);
    }

    private void pesquisarPorCodigo() {
        int codigo = lerInt("Código: ");
        Solicitacao s = central.buscarPorCodigo(codigo);
        if (s == null) System.out.println("Solicitação não encontrada.");
        else System.out.println(s);
    }

    private void remover() {
        int codigo = lerInt("Código a remover: ");
        if (central.removerSolicitacao(codigo)) System.out.println("Solicitação removida.");
        else System.out.println("Solicitação não encontrada.");
    }

    private void porStatus() {
        System.out.println("Status: AGUARDANDO, EM_ATENDIMENTO, CONCLUIDA, CANCELADA");
        String status = lerTexto("Status: ").toUpperCase();
        central.exibirPorStatus(status);
    }

    private void porCategoria() {
        central.exibirPorCategoria(lerTexto("Categoria: "));
    }

    private void pesquisarAtendimento() {
        int codigo = lerInt("Código do atendimento: ");
        Atendimento a = central.buscarAtendimento(codigo);
        if (a == null) System.out.println("Atendimento não encontrado.");
        else System.out.println(a);
    }

    /** Navegação [A]nterior / [P]róximo / [S]air usando os dois ponteiros da lista dupla. */
    private void navegarHistorico() {
        DNode<Atendimento> atual = central.getAtendimentos().getHead();
        if (atual == null) {
            System.out.println("Histórico vazio.");
            return;
        }

        String op;
        do {
            System.out.println("\nAtendimento atual: " + atual.getElement());
            System.out.println("[A] Anterior  [P] Próximo  [S] Sair");
            op = lerTexto("> ").toUpperCase();

            switch (op) {
                case "A" -> {
                    if (atual.getPrev() == null) System.out.println("Este é o primeiro registro.");
                    else atual = atual.getPrev();
                }
                case "P" -> {
                    if (atual.getNext() == null) System.out.println("Este é o último registro.");
                    else atual = atual.getNext();
                }
                case "S" -> System.out.println("Saindo da navegação.");
                default -> System.out.println("Opção inválida.");
            }
        } while (!op.equals("S"));
    }

    // ---------- LEITURA COM VALIDAÇÃO ----------

    private String lerTexto(String msg) {
        String texto;
        do {
            System.out.print(msg);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) System.out.println("O campo não pode ficar vazio.");
        } while (texto.isEmpty());
        return texto;
    }

    private int lerInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private int lerIntFaixa(String msg, int min, int max) {
        int valor;
        do {
            valor = lerInt(msg);
            if (valor < min || valor > max)
                System.out.println("Valor deve estar entre " + min + " e " + max + ".");
        } while (valor < min || valor > max);
        return valor;
    }
}