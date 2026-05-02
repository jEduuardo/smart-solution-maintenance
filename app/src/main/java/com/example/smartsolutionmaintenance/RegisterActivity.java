package com.example.smartsolutionmaintenance;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.models.Usuario;
import com.smartsolution.maintenance.utils.FirebaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilNome, tilEmail, tilSenha, tilConfirmSenha, tilCargo;
    private TextInputEditText etNome, etEmail, etSenha, etConfirmSenha, etCargo;
    private MaterialButton btnCadastrar;
    private CircularProgressIndicator progressBar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseHelper = FirebaseHelper.getInstance();
        initViews();
        setupListeners();
    }

    private void initViews() {
        tilNome = findViewById(R.id.til_nome);
        tilEmail = findViewById(R.id.til_email);
        tilSenha = findViewById(R.id.til_senha);
        tilConfirmSenha = findViewById(R.id.til_confirm_senha);
        tilCargo = findViewById(R.id.til_cargo);

        etNome = findViewById(R.id.et_nome);
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        etConfirmSenha = findViewById(R.id.et_confirm_senha);
        etCargo = findViewById(R.id.et_cargo);

        btnCadastrar = findViewById(R.id.btn_cadastrar);
        progressBar = findViewById(R.id.progress_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Criar Conta");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.tv_ja_tem_conta).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnCadastrar.setOnClickListener(v -> cadastrar());
    }

    private void cadastrar() {
        String nome = getText(etNome);
        String email = getText(etEmail);
        String senha = getText(etSenha);
        String confirmSenha = getText(etConfirmSenha);
        String cargo = getText(etCargo);

        if (!validar(nome, email, senha, confirmSenha)) return;

        setLoading(true);

        firebaseHelper.getAuth()
                .createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    Usuario usuario = new Usuario(nome, email, cargo, Usuario.PERFIL_OPERADOR, "");
                    usuario.setId(uid);

                    firebaseHelper.getUsuariosRef()
                            .document(uid)
                            .set(usuario)
                            .addOnSuccessListener(unused -> {
                                setLoading(false);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finishAffinity();
                            })
                            .addOnFailureListener(e -> {
                                setLoading(false);
                                tilEmail.setError("Erro ao salvar dados do usuário.");
                            });
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    String msg = e.getMessage() != null && e.getMessage().contains("email")
                            ? "E-mail já cadastrado"
                            : "Erro ao criar conta. Tente novamente.";
                    tilEmail.setError(msg);
                });
    }

    private boolean validar(String nome, String email, String senha, String confirmSenha) {
        clearErrors();
        boolean ok = true;

        if (TextUtils.isEmpty(nome)) { tilNome.setError("Informe seu nome"); ok = false; }
        if (TextUtils.isEmpty(email)) { tilEmail.setError("Informe o e-mail"); ok = false; }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("E-mail inválido"); ok = false;
        }
        if (TextUtils.isEmpty(senha)) { tilSenha.setError("Informe a senha"); ok = false; }
        else if (senha.length() < 6) { tilSenha.setError("Mínimo 6 caracteres"); ok = false; }
        if (!senha.equals(confirmSenha)) { tilConfirmSenha.setError("Senhas não conferem"); ok = false; }

        return ok;
    }

    private void clearErrors() {
        tilNome.setError(null);
        tilEmail.setError(null);
        tilSenha.setError(null);
        tilConfirmSenha.setError(null);
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setLoading(boolean loading) {
        btnCadastrar.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
