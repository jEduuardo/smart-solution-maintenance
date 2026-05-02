package models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Alerta {
    @DocumentId
    private String id;
    private String tipo; // ANOMALIA_CONSUMO, MANUTENCAO_PROXIMA, EQUIPAMENTO_CRITICO, CONSUMO_ELEVADO
    private String titulo;
    private String mensagem;
    private String prioridade; // ALTA, MEDIA, BAIXA
    private String status; // NOVO, LIDO, RESOLVIDO
    private String referenciaId; // ID do consumo/manutenção/equipamento que gerou o alerta
    private String referenciaTipo; // CONSUMO, MANUTENCAO, EQUIPAMENTO
    private String instituicaoId;
    private boolean lido;
    private Timestamp createdAt;
    private Timestamp leituraAt;

    public static final String TIPO_ANOMALIA_CONSUMO = "ANOMALIA_CONSUMO";
    public static final String TIPO_MANUTENCAO_PROXIMA = "MANUTENCAO_PROXIMA";
    public static final String TIPO_EQUIPAMENTO_CRITICO = "EQUIPAMENTO_CRITICO";
    public static final String TIPO_CONSUMO_ELEVADO = "CONSUMO_ELEVADO";

    public static final String STATUS_NOVO = "NOVO";
    public static final String STATUS_LIDO = "LIDO";
    public static final String STATUS_RESOLVIDO = "RESOLVIDO";

    public Alerta() {}

    public Alerta(String tipo, String titulo, String mensagem, String prioridade,
                  String referenciaId, String referenciaTipo, String instituicaoId) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.prioridade = prioridade;
        this.referenciaId = referenciaId;
        this.referenciaTipo = referenciaTipo;
        this.instituicaoId = instituicaoId;
        this.status = STATUS_NOVO;
        this.lido = false;
        this.createdAt = Timestamp.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReferenciaId() { return referenciaId; }
    public void setReferenciaId(String referenciaId) { this.referenciaId = referenciaId; }

    public String getReferenciaTipo() { return referenciaTipo; }
    public void setReferenciaTipo(String referenciaTipo) { this.referenciaTipo = referenciaTipo; }

    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getLeituraAt() { return leituraAt; }
    public void setLeituraAt(Timestamp leituraAt) { this.leituraAt = leituraAt; }
}