package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Atendimento {
    private Solicitacao solicitacao;
    private LocalDateTime dataConclusao;

    public Atendimento(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
        this.dataConclusao = LocalDateTime.now();
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Atendimento da solicitação " + solicitacao.getCodigo()
                + " (" + solicitacao.getSolicitante() + ")"
                + " | resp.: " + solicitacao.getResponsavel()
                + " | concluído em " + dataConclusao.format(f);
    }
}