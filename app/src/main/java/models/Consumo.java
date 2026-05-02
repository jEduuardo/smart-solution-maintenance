package models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Consumo {
    @DocumentId
    private String id;
    private String tipo; // AGUA, ENERGIA
    private String instituicaoId;
    private String setorId;
    private double valor;
    private String unidade; // m³ para água, kWh para energia
    private double custoTotal;
    private int mes;
    private int ano;
    private Timestamp dataLeitura;
    private double valorMeta;
    private double valorAnterior;
    private boolean anomalia;
    private String observacoes;
    private String registradoPor;
    private Timestamp createdAt;

    public static final String TIPO_AGUA = "AGUA";
    public static final String TIPO_ENERGIA = "ENERGIA";

    public Consumo() {}

    public Consumo(String tipo, String instituicaoId, String setorId,
                   double valor, String unidade, double custoTotal,
                   int mes, int ano) {
        this.tipo = tipo;
        this.instituicaoId = instituicaoId;
        this.setorId = setorId;
        this.valor = valor;
        this.unidade = unidade;
        this.custoTotal = custoTotal;
        this.mes = mes;
        this.ano = ano;
        this.dataLeitura = Timestamp.now();
        this.createdAt = Timestamp.now();
        this.anomalia = false;
    }

    // Helper: verifica se é anomalia (>20% acima da meta)
    public void verificarAnomalia() {
        if (valorMeta > 0) {
            this.anomalia = valor > (valorMeta * 1.2);
        }
    }

    public double getVariacaoPercentual() {
        if (valorAnterior > 0) {
            return ((valor - valorAnterior) / valorAnterior) * 100;
        }
        return 0;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }

    public String getSetorId() { return setorId; }
    public void setSetorId(String setorId) { this.setorId = setorId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public double getCustoTotal() { return custoTotal; }
    public void setCustoTotal(double custoTotal) { this.custoTotal = custoTotal; }

    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public Timestamp getDataLeitura() { return dataLeitura; }
    public void setDataLeitura(Timestamp dataLeitura) { this.dataLeitura = dataLeitura; }

    public double getValorMeta() { return valorMeta; }
    public void setValorMeta(double valorMeta) { this.valorMeta = valorMeta; }

    public double getValorAnterior() { return valorAnterior; }
    public void setValorAnterior(double valorAnterior) { this.valorAnterior = valorAnterior; }

    public boolean isAnomalia() { return anomalia; }
    public void setAnomalia(boolean anomalia) { this.anomalia = anomalia; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String registradoPor) { this.registradoPor = registradoPor; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
