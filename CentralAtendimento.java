package service;

import estruturas.LinkedQueue;
import estruturas.LinkedStack;
import estruturas.Node;
import estruturas.SLinkedList;
import estruturas.DLinkedList;
import estruturas.DNode;
import model.Atendimento;
import model.Operacao;
import model.Solicitacao;

/**
 * Camada de serviço: concentra as regras do sistema e integra as estruturas.
 * Não faz Scanner nem menu (isso fica na classe Menu).
 *
 * Fila         -> solicitações aguardando atendimento
 * Pilha        -> histórico recente de operações / desfazer
 * ListaSimples -> cadastro geral de solicitações
 * ListaDupla   -> histórico navegável de atendimentos concluídos
 */
public class CentralAtendimento {

    private LinkedQueue<Solicitacao> fila = new LinkedQueue<>();
    private LinkedStack<Operacao> historico = new LinkedStack<>();
    private SLinkedList<Solicitacao> cadastro = new SLinkedList<>();
    private DLinkedList<Atendimento> atendimentos = new DLinkedList<>();

    private int proximoCodigo = 101;

    // ---------- ATENDIMENTO ----------

    public Solicitacao cadastrar(String solicitante, String descricao,
                                 String categoria, int prioridade) {
        Solicitacao s = new Solicitacao(proximoCodigo++, solicitante,
                descricao, categoria, prioridade);
        s.setStatus("AGUARDANDO");

        cadastro.addLast(s);                                 // Lista Simples
        fila.enqueue(s);                                     // Fila
        historico.push(new Operacao("CADASTRO", s));         // Pilha
        return s;
    }

    public Solicitacao consultarProxima() {
        if (fila.isEmpty()) return null;
        return fila.peekFirst();
    }

    public Solicitacao atenderProxima(String responsavel) {
        if (fila.isEmpty()) return null;

        Solicitacao s = fila.dequeue();                      // Fila
        s.setResponsavel(responsavel);
        s.setStatus("CONCLUIDA");

        atendimentos.addLast(new Atendimento(s));            // Lista Dupla
        historico.push(new Operacao("ATENDIMENTO", s));      // Pilha
        return s;
    }

    public void exibirFila() {
        fila.display();
    }

    public int quantidadeAguardando() {
        return fila.getSize();
    }

    // ---------- OPERAÇÕES (Pilha) ----------

    public Operacao ultimaOperacao() {
        if (historico.isEmpty()) return null;
        return historico.top();
    }

    public void exibirHistoricoRecente() {
        historico.printStack();   // do topo para a base
    }

    /** Desfaz a última operação. Retorna a operação desfeita ou null. */
    public Operacao desfazerUltima() {
        if (historico.isEmpty()) return null;

        Operacao op = historico.pop();
        Solicitacao s = op.getSolicitacao();

        if (op.getTipo().equals("CADASTRO")) {
            // remove do meio/fim da fila e do cadastro geral
            reconstruirFila(s, null);
            cadastro.remove(s);
        } else if (op.getTipo().equals("ATENDIMENTO")) {
            // tira do histórico de concluídos e devolve à FRENTE da fila
            atendimentos.removeNode(localizarAtendimento(s.getCodigo()));
            s.setStatus("AGUARDANDO");
            s.setResponsavel(null);
            reconstruirFila(null, s);
        }
        return op;
    }

    /**
     * A fila só permite remover do início, então para remover um elemento
     * qualquer (ou inserir na frente) esvaziamos a fila numa auxiliar
     * e reenfileiramos na ordem correta.
     */
    private void reconstruirFila(Solicitacao remover, Solicitacao inserirNaFrente) {
        LinkedQueue<Solicitacao> aux = new LinkedQueue<>();

        if (inserirNaFrente != null) aux.enqueue(inserirNaFrente);

        while (!fila.isEmpty()) {
            Solicitacao atual = fila.dequeue();
            if (atual != remover) aux.enqueue(atual);
        }
        while (!aux.isEmpty()) {
            fila.enqueue(aux.dequeue());
        }
    }

    // ---------- CADASTRO GERAL (Lista Simples) ----------

    public void exibirTodas() {
        if (cadastro.isEmpty()) {
            System.out.println("Nenhuma solicitação cadastrada.");
            return;
        }
        Node<Solicitacao> atual = cadastro.head();
        while (atual != null) {
            System.out.println(atual.getElement());
            atual = atual.getNext();
        }
    }

    public Solicitacao buscarPorCodigo(int codigo) {
        Node<Solicitacao> atual = cadastro.head();
        while (atual != null) {
            if (atual.getElement().getCodigo() == codigo) return atual.getElement();
            atual = atual.getNext();
        }
        return null;
    }

    public boolean removerSolicitacao(int codigo) {
        Solicitacao s = buscarPorCodigo(codigo);
        if (s == null) return false;

        // se ainda está aguardando, precisa sair da fila também
        if (s.getStatus().equals("AGUARDANDO")) {
            reconstruirFila(s, null);
        }
        s.setStatus("CANCELADA");
        historico.push(new Operacao("CANCELAMENTO", s));
        return cadastro.remove(s);
    }

    public void exibirPorStatus(String status) {
        boolean achou = false;
        Node<Solicitacao> atual = cadastro.head();
        while (atual != null) {
            if (atual.getElement().getStatus().equalsIgnoreCase(status)) {
                System.out.println(atual.getElement());
                achou = true;
            }
            atual = atual.getNext();
        }
        if (!achou) System.out.println("Nenhuma solicitação com esse status.");
    }

    public void exibirPorCategoria(String categoria) {
        boolean achou = false;
        Node<Solicitacao> atual = cadastro.head();
        while (atual != null) {
            if (atual.getElement().getCategoria().equalsIgnoreCase(categoria)) {
                System.out.println(atual.getElement());
                achou = true;
            }
            atual = atual.getNext();
        }
        if (!achou) System.out.println("Nenhuma solicitação nessa categoria.");
    }

    // ---------- HISTÓRICO DE ATENDIMENTOS (Lista Dupla) ----------

    /** Percorre do início para o fim usando next. */
    public void exibirAtendimentosInicioFim() {
        if (atendimentos.isEmpty()) {
            System.out.println("Histórico vazio.");
            return;
        }
        DNode<Atendimento> atual = atendimentos.getHead();
        while (atual != null) {
            System.out.println(atual.getElement());
            atual = atual.getNext();
        }
    }

    /** Percorre do fim para o início usando prev. */
    public void exibirAtendimentosFimInicio() {
        if (atendimentos.isEmpty()) {
            System.out.println("Histórico vazio.");
            return;
        }
        DNode<Atendimento> atual = atendimentos.getTail();
        while (atual != null) {
            System.out.println(atual.getElement());
            atual = atual.getPrev();
        }
    }

    public Atendimento buscarAtendimento(int codigo) {
        DNode<Atendimento> no = localizarAtendimento(codigo);
        return no == null ? null : no.getElement();
    }

    private DNode<Atendimento> localizarAtendimento(int codigo) {
        DNode<Atendimento> atual = atendimentos.getHead();
        while (atual != null) {
            if (atual.getElement().getSolicitacao().getCodigo() == codigo) return atual;
            atual = atual.getNext();
        }
        return null;
    }

    public DLinkedList<Atendimento> getAtendimentos() {
        return atendimentos;   // o Menu usa para navegar (anterior/próximo)
    }

    // ---------- RELATÓRIO ----------

    public String resumo() {
        return "Aguardando: " + fila.getSize()
             + "\nOperações no histórico: " + historico.size()
             + "\nTotal cadastradas: " + cadastro.size()
             + "\nAtendimentos concluídos: " + atendimentos.size();
    }
}