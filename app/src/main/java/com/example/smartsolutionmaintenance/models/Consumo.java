package com.example.smartsolutionmaintenance.models;

import com.google.firebase.Timestamp;

public class Consumo {
    private String id, tipo, setor, observacao;
    private double valor, leitura;
    private Timestamp data;

    public Consumo() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public double getLeitura() { return leitura; }
    public void setLeitura(double leitura) { this.leitura = leitura; }
    public Timestamp getData() { return data; }
    public void setData(Timestamp data) { this.data = data; }
}
