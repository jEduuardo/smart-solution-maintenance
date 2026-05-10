package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartsolutionmaintenance.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private Button btnEntrar;
    private ProgressBar progressBar;
    private TextView tvEsqueceuSenha;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);
        tvEsqueceuSenha = findViewById(R.id.tvEsqueceuSenha);

        btnEntrar.setOnClickListener(v -> fazerLogin());

        tvEsqueceuSenha.setOnClickListener(v -> recuperarSenha());
    }

    private void fazerLogin() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Informe o e-mail");
            return;
        }
        if (TextUtils.isEmpty(senha)) {
            edtSenha.setError("Informe a senha");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnEntrar.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    btnEntrar.setEnabled(true);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Falha no login. Verifique suas credenciais.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void recuperarSenha() {
        String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Informe o e-mail para recuperação");
            return;
        }
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Erro ao enviar e-mail.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
