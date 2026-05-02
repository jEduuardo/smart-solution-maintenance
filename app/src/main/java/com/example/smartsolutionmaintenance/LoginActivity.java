package com.example.smartsolutionmaintenance;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.utils.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilSenha;
    private TextInputEditText etEmail, etSenha;
    private MaterialButton btnLogin;
    private CircularProgressIndicator progressBar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseHelper = FirebaseHelper.getInstance();

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.til_email);
        tilSenha = findViewById(R.id.til_senha);
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.tv_cadastrar).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.tv_esqueceu_senha).setOnClickListener(v -> recuperarSenha());
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> fazerLogin());
    }

    private void fazerLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String senha = etSenha.getText() != null ? etSenha.getText().toString().trim() : "";

        if (!validarCampos(email, senha)) return;

        setLoading(true);

        firebaseHelper.getAuth()
                .signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener(authResult -> {
                    setLoading(false);
                    // Atualizar último acesso
                    String uid = authResult.getUser().getUid();
                    firebaseHelper.getUsuariosRef()
                            .document(uid)
                            .update("ultimoAcesso", com.google.firebase.Timestamp.now());

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    tilSenha.setError("E-mail ou senha inválidos");
                });
    }

    private boolean validarCampos(String email, String senha) {
        boolean valido = true;
        tilEmail.setError(null);
        tilSenha.setError(null);

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Informe o e-mail");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("E-mail inválido");
            valido = false;
        }

        if (TextUtils.isEmpty(senha)) {
            tilSenha.setError("Informe a senha");
            valido = false;
        } else if (senha.length() < 6) {
            tilSenha.setError("Senha deve ter pelo menos 6 caracteres");
            valido = false;
        }

        return valido;
    }

    private void recuperarSenha() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Informe o e-mail para recuperar a senha");
            return;
        }

        firebaseHelper.getAuth()
                .sendPasswordResetEmail(email)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao enviar e-mail de recuperação", Toast.LENGTH_SHORT).show());
    }

    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setText(loading ? "" : getString(R.string.login));
    }
}
