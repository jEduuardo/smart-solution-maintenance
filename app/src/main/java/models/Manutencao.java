package models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Manutencao {
    @DocumentId
    private String id;
    private String equipamentoId;
    private String equipamentoNome;
    private String instituicaoId;
    private String setorId;
    private String tipo; // PREVENTIVA, CORRETIVA, PREDITIVA
    private String prioridade; // ALTA, MEDIA, BAIXA
    private String status; // PENDENTE, EM_ANDAMENTO, CONCLUIDA, CANCELADA
    private String descricao;
    private String tecnicoResponsavel;
    private double custo;
    private Timestamp dataSolicitacao;
    private Timestamp dataAgendada;
    private Timestamp dataConclusao;
    private String observacoes;
    private String solicitanteId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static final String TIPO_PREVENTIVA = "PREVENTIVA";
    public static final String TIPO_CORRETIVA = "CORRETIVA";
    public static final String TIPO_PREDITIVA = "PREDITIVA";

    public static final String PRIORIDADE_ALTA = "ALTA";
    public static final String PRIORIDADE_MEDIA = "MEDIA";
    public static final String PRIORIDADE_BAIXA = "BAIXA";

    public static final String STATUS_PENDENTE = "PENDENTE";
    public static final String STATUS_EM_ANDAMENTO = "EM_ANDAMENTO";
    public static final String STATUS_CONCLUIDA = "CONCLUIDA";
    public static final String STATUS_CANCELADA = "CANCELADA";

    public Manutencao() {}

    public Manutencao(String equipamentoId, String equipamentoNome, String instituicaoId,
                      String setorId, String tipo, String prioridade,
                      String descricao, String tecnicoResponsavel) {
        this.equipamentoId = equipamentoId;
        this.equipamentoNome = equipamentoNome;
        this.instituicaoId = instituicaoId;
        this.setorId = setorId;
        this.tipo = tipo;
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.tecnicoResponsavel = tecnicoResponsavel;
        this.status = STATUS_PENDENTE;
        this.dataSolicitacao = Timestamp.now();
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(String equipamentoId) { this.equipamentoId = equipamentoId; }

    public String getEquipamentoNome() { return equipamentoNome; }
    public void setEquipamentoNome(String equipamentoNome) { this.equipamentoNome = equipamentoNome; }

    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }

    public String getSetorId() { return setorId; }
    public void setSetorId(String setorId) { this.setorId = setorId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTecnicoResponsavel() { return tecnicoResponsavel; }
    public void setTecnicoResponsavel(String tecnicoResponsavel) { this.tecnicoResponsavel = tecnicoResponsavel; }

    public double getCusto() { return custo; }
    public void setCusto(double custo) { this.custo = custo; }

    public Timestamp getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(Timestamp dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public Timestamp getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(Timestamp dataAgendada) { this.dataAgendada = dataAgendada; }

    public Timestamp getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(Timestamp dataConclusao) { this.dataConclusao = dataConclusao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(String solicitanteId) { this.solicitanteId = solicitanteId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
