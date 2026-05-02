package models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Usuario {
    @DocumentId
    private String id;
    private String nome;
    private String email;
    private String cargo;
    private String perfil; // ADMIN, GESTOR, TECNICO, OPERADOR
    private String instituicaoId;
    private String telefone;
    private String avatarUrl;
    private boolean ativo;
    private Timestamp createdAt;
    private Timestamp ultimoAcesso;

    public static final String PERFIL_ADMIN = "ADMIN";
    public static final String PERFIL_GESTOR = "GESTOR";
    public static final String PERFIL_TECNICO = "TECNICO";
    public static final String PERFIL_OPERADOR = "OPERADOR";

    public Usuario() {}

    public Usuario(String nome, String email, String cargo, String perfil, String instituicaoId) {
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.perfil = perfil;
        this.instituicaoId = instituicaoId;
        this.ativo = true;
        this.createdAt = Timestamp.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(Timestamp ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }
}
