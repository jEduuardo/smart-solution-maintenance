package com.example.smartsolutionmaintenance.models;

import com.google.firebase.Timestamp;

public class Instituicao {
    private String id, nome, endereco, responsavel, telefone, setores;
    private Timestamp dataCadastro;

    public Instituicao() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getSetores() { return setores; }
    public void setSetores(String setores) { this.setores = setores; }
    public Timestamp getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Timestamp dataCadastro) { this.dataCadastro = dataCadastro; }
}
