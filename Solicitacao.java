package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Solicitacao {
    private int codigo;
    private String solicitante;
    private String descricao;
    private String categoria;
    private int prioridade;
    private LocalDateTime dataAbertura;
    private String status;        // AGUARDANDO, EM_ATENDIMENTO, CONCLUIDA, CANCELADA
    private String responsavel;

    public Solicitacao(int codigo, String solicitante, String descricao,
                       String categoria, int prioridade) {
        this.codigo = codigo;
        this.solicitante = solicitante;
        this.descricao = descricao;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.dataAbertura = LocalDateTime.now();
        this.status = "AGUARDANDO";
        this.responsavel = null;
    }

    public int getCodigo() { return codigo; }
    public String getSolicitante() { return solicitante; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public int getPrioridade() { return prioridade; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public String getStatus() { return status; }
    public String getResponsavel() { return responsavel; }

    public void setStatus(String status) { this.status = status; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "[" + codigo + "] " + solicitante
                + " | " + categoria
                + " | prioridade " + prioridade
                + " | " + status
                + " | aberta em " + dataAbertura.format(f)
                + " | resp.: " + (responsavel == null ? "-" : responsavel)
                + "\n     " + descricao;
    }
}