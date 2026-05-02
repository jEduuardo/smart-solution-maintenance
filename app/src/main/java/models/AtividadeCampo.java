package models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class AtividadeCampo {
    @DocumentId
    private String id;
    private String titulo;
    private String descricao;
    private String tipo; // INSPECAO, REPARO, INSTALACAO, LIMPEZA, OUTRO
    private String status; // PENDENTE, EM_ANDAMENTO, CONCLUIDA
    private String manutencaoId;
    private String equipamentoId;
    private String equipamentoNome;
    private String instituicaoId;
    private String setorId;
    private String tecnicoId;
    private String tecnicoNome;
    private Timestamp dataInicio;
    private Timestamp dataFim;
    private int duracaoMinutos;
    private String observacoes;
    private String resultado;
    private double custoMateriais;
    private Timestamp createdAt;

    public static final String TIPO_INSPECAO = "INSPECAO";
    public static final String TIPO_REPARO = "REPARO";
    public static final String TIPO_INSTALACAO = "INSTALACAO";
    public static final String TIPO_LIMPEZA = "LIMPEZA";
    public static final String TIPO_OUTRO = "OUTRO";

    public AtividadeCampo() {}

    public AtividadeCampo(String titulo, String descricao, String tipo,
                          String manutencaoId, String equipamentoId, String equipamentoNome,
                          String instituicaoId, String setorId,
                          String tecnicoId, String tecnicoNome) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.manutencaoId = manutencaoId;
        this.equipamentoId = equipamentoId;
        this.equipamentoNome = equipamentoNome;
        this.instituicaoId = instituicaoId;
        this.setorId = setorId;
        this.tecnicoId = tecnicoId;
        this.tecnicoNome = tecnicoNome;
        this.status = "PENDENTE";
        this.createdAt = Timestamp.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getManutencaoId() { return manutencaoId; }
    public void setManutencaoId(String manutencaoId) { this.manutencaoId = manutencaoId; }

    public String getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(String equipamentoId) { this.equipamentoId = equipamentoId; }

    public String getEquipamentoNome() { return equipamentoNome; }
    public void setEquipamentoNome(String equipamentoNome) { this.equipamentoNome = equipamentoNome; }

    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }

    public String getSetorId() { return setorId; }
    public void setSetorId(String setorId) { this.setorId = setorId; }

    public String getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(String tecnicoId) { this.tecnicoId = tecnicoId; }

    public String getTecnicoNome() { return tecnicoNome; }
    public void setTecnicoNome(String tecnicoNome) { this.tecnicoNome = tecnicoNome; }

    public Timestamp getDataInicio() { return dataInicio; }
    public void setDataInicio(Timestamp dataInicio) { this.dataInicio = dataInicio; }

    public Timestamp getDataFim() { return dataFim; }
    public void setDataFim(Timestamp dataFim) { this.dataFim = dataFim; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public double getCustoMateriais() { return custoMateriais; }
    public void setCustoMateriais(double custoMateriais) { this.custoMateriais = custoMateriais; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}