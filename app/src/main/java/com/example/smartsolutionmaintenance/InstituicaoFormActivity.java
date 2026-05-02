package com.example.smartsolutionmaintenance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.models.Instituicao;
import com.smartsolution.maintenance.utils.FirebaseHelper;

public class InstituicaoFormActivity extends AppCompatActivity {

    private TextInputLayout tilNome, tilCnpj, tilEndereco, tilCidade, tilEstado,
            tilTelefone, tilEmail, tilResponsavel, tilDescricao;
    private TextInputEditText etNome, etCnpj, etEndereco, etCidade, etEstado,
            etTelefone, etEmail, etResponsavel, etDescricao;
    private MaterialButton btnSalvar, btnExcluir;
    private CircularProgressIndicator progressBar;

    private FirebaseHelper firebaseHelper;
    private String instituicaoId;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao_form);

        firebaseHelper = FirebaseHelper.getInstance();
        instituicaoId = getIntent().getStringExtra("instituicao_id");
        isEditing = instituicaoId != null;

        setupToolbar();
        initViews();

        if (isEditing) {
            carregarInstituicao();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditing ? "Editar Instituição" : "Nova Instituição");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        tilNome = findViewById(R.id.til_nome);
        tilCnpj = findViewById(R.id.til_cnpj);
        tilEndereco = findViewById(R.id.til_endereco);
        tilCidade = findViewById(R.id.til_cidade);
        tilEstado = findViewById(R.id.til_estado);
        tilTelefone = findViewById(R.id.til_telefone);
        tilEmail = findViewById(R.id.til_email);
        tilResponsavel = findViewById(R.id.til_responsavel);
        tilDescricao = findViewById(R.id.til_descricao);

        etNome = findViewById(R.id.et_nome);
        etCnpj = findViewById(R.id.et_cnpj);
        etEndereco = findViewById(R.id.et_endereco);
        etCidade = findViewById(R.id.et_cidade);
        etEstado = findViewById(R.id.et_estado);
        etTelefone = findViewById(R.id.et_telefone);
        etEmail = findViewById(R.id.et_email);
        etResponsavel = findViewById(R.id.et_responsavel);
        etDescricao = findViewById(R.id.et_descricao);

        btnSalvar = findViewById(R.id.btn_salvar);
        btnExcluir = findViewById(R.id.btn_excluir);
        progressBar = findViewById(R.id.progress_bar);

        btnSalvar.setOnClickListener(v -> salvar());
        btnExcluir.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        btnExcluir.setOnClickListener(v -> confirmarExclusao());
    }

    private void carregarInstituicao() {
        setLoading(true);
        firebaseHelper.getInstituicoesRef().document(instituicaoId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Instituicao inst = doc.toObject(Instituicao.class);
                        if (inst != null) {
                            etNome.setText(inst.getNome());
                            etCnpj.setText(inst.getCnpj());
                            etEndereco.setText(inst.getEndereco());
                            etCidade.setText(inst.getCidade());
                            etEstado.setText(inst.getEstado());
                            etTelefone.setText(inst.getTelefone());
                            etEmail.setText(inst.getEmail());
                            etResponsavel.setText(inst.getResponsavel());
                            etDescricao.setText(inst.getDescricao());
                        }
                    }
                    setLoading(false);
                })
                .addOnFailureListener(e -> setLoading(false));
    }

    private void salvar() {
        String nome = getText(etNome);
        String cnpj = getText(etCnpj);
        String endereco = getText(etEndereco);
        String cidade = getText(etCidade);
        String estado = getText(etEstado);
        String telefone = getText(etTelefone);
        String email = getText(etEmail);
        String responsavel = getText(etResponsavel);
        String descricao = getText(etDescricao);

        if (!validar(nome, responsavel)) return;

        setLoading(true);

        Instituicao inst = new Instituicao(nome, cnpj, endereco, cidade, estado, telefone, email, responsavel, descricao);
        inst.setUpdatedAt(Timestamp.now());
        inst.setCreatedBy(firebaseHelper.getCurrentUserId());

        if (isEditing) {
            firebaseHelper.getInstituicoesRef().document(instituicaoId)
                    .set(inst)
                    .addOnSuccessListener(v -> {
                        Toast.makeText(this, getString(R.string.sucesso_salvar), Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        setLoading(false);
                        Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_SHORT).show();
                    });
        } else {
            firebaseHelper.getInstituicoesRef().add(inst)
                    .addOnSuccessListener(ref -> {
                        Toast.makeText(this, getString(R.string.sucesso_salvar), Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        setLoading(false);
                        Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Instituição")
                .setMessage(getString(R.string.confirmar_exclusao))
                .setPositiveButton("Excluir", (d, w) -> excluir())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluir() {
        setLoading(true);
        firebaseHelper.getInstituicoesRef().document(instituicaoId)
                .update("ativo", false)
                .addOnSuccessListener(v -> {
                    Toast.makeText(this, getString(R.string.sucesso_excluir), Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> setLoading(false));
    }

    private boolean validar(String nome, String responsavel) {
        tilNome.setError(null);
        tilResponsavel.setError(null);
        boolean ok = true;
        if (TextUtils.isEmpty(nome)) { tilNome.setError("Informe o nome"); ok = false; }
        if (TextUtils.isEmpty(responsavel)) { tilResponsavel.setError("Informe o responsável"); ok = false; }
        return ok;
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setLoading(boolean loading) {
        btnSalvar.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}