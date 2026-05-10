package com.example.smartsolutionmaintenance.models;

import com.google.firebase.Timestamp;

public class Manutencao {
    private String id, titulo, descricao, equipamento, responsavel;
    private String prazo, prioridade, tipo, status;
    private double custo;
    private Timestamp dataCriacao, dataConclusao;

    public Manutencao() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getEquipamento() { return equipamento; }
    public void setEquipamento(String equipamento) { this.equipamento = equipamento; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getPrazo() { return prazo; }
    public void setPrazo(String prazo) { this.prazo = prazo; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getCusto() { return custo; }
    public void setCusto(double custo) { this.custo = custo; }
    public Timestamp getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Timestamp dataCriacao) { this.dataCriacao = dataCriacao; }
    public Timestamp getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(Timestamp dataConclusao) { this.dataConclusao = dataConclusao; }
}
