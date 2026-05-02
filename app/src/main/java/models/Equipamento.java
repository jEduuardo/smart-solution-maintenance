package models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Equipamento {
    @DocumentId
    private String id;
    private String nome;
    private String codigoPatrimonio;
    private String modelo;
    private String fabricante;
    private String categoria;
    private String setorId;
    private String instituicaoId;
    private String status; // ATIVO, EM_MANUTENCAO, INATIVO, DESCARTADO
    private Timestamp dataAquisicao;
    private Timestamp proximaManutencao;
    private int intervaloManutencaoDias;
    private double valorAquisicao;
    private String observacoes;
    private String imagemUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static final String STATUS_ATIVO = "ATIVO";
    public static final String STATUS_EM_MANUTENCAO = "EM_MANUTENCAO";
    public static final String STATUS_INATIVO = "INATIVO";
    public static final String STATUS_DESCARTADO = "DESCARTADO";

    public Equipamento() {}

    public Equipamento(String nome, String codigoPatrimonio, String modelo,
                       String fabricante, String categoria, String setorId,
                       String instituicaoId) {
        this.nome = nome;
        this.codigoPatrimonio = codigoPatrimonio;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.categoria = categoria;
        this.setorId = setorId;
        this.instituicaoId = instituicaoId;
        this.status = STATUS_ATIVO;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigoPatrimonio() { return codigoPatrimonio; }
    public void setCodigoPatrimonio(String codigoPatrimonio) { this.codigoPatrimonio = codigoPatrimonio; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getSetorId() { return setorId; }
    public void setSetorId(String setorId) { this.setorId = setorId; }

    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getDataAquisicao() { return dataAquisicao; }
    public void setDataAquisicao(Timestamp dataAquisicao) { this.dataAquisicao = dataAquisicao; }

    public Timestamp getProximaManutencao() { return proximaManutencao; }
    public void setProximaManutencao(Timestamp proximaManutencao) { this.proximaManutencao = proximaManutencao; }

    public int getIntervaloManutencaoDias() { return intervaloManutencaoDias; }
    public void setIntervaloManutencaoDias(int intervaloManutencaoDias) { this.intervaloManutencaoDias = intervaloManutencaoDias; }

    public double getValorAquisicao() { return valorAquisicao; }
    public void setValorAquisicao(double valorAquisicao) { this.valorAquisicao = valorAquisicao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}