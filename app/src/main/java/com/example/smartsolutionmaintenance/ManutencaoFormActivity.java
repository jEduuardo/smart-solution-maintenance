package com.example.smartsolutionmaintenance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.smartsolution.maintenance.models.Manutencao;
import com.smartsolution.maintenance.utils.FirebaseHelper;
import java.util.Calendar;
import java.util.Date;

public class ManutencaoFormActivity extends AppCompatActivity {

    private TextInputLayout tilEquipamento, tilTipo, tilPrioridade, tilStatus,
            tilTecnico, tilDescricao, tilCusto, tilDataAgendada, tilObservacoes;
    private TextInputEditText etEquipamento, etTecnico, etDescricao, etCusto,
            etDataAgendada, etObservacoes;
    private AutoCompleteTextView spinnerTipo, spinnerPrioridade, spinnerStatus;
    private MaterialButton btnSalvar, btnExcluir, btnRegistrarAtividade;
    private CircularProgressIndicator progressBar;

    private FirebaseHelper firebaseHelper;
    private String manutencaoId;
    private String equipamentoId;
    private boolean isEditing = false;
    private Date dataAgendadaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao_form);

        firebaseHelper = FirebaseHelper.getInstance();
        manutencaoId = getIntent().getStringExtra("manutencao_id");
        equipamentoId = getIntent().getStringExtra("equipamento_id");
        isEditing = manutencaoId != null;

        setupToolbar();
        initViews();

        if (isEditing) {
            carregarManutencao();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditing ? "Editar Manutenção" : "Nova Manutenção");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        etEquipamento = findViewById(R.id.et_equipamento);
        etTecnico = findViewById(R.id.et_tecnico);
        etDescricao = findViewById(R.id.et_descricao);
        etCusto = findViewById(R.id.et_custo);
        etDataAgendada = findViewById(R.id.et_data_agendada);
        etObservacoes = findViewById(R.id.et_observacoes);

        spinnerTipo = findViewById(R.id.spinner_tipo);
        spinnerPrioridade = findViewById(R.id.spinner_prioridade);
        spinnerStatus = findViewById(R.id.spinner_status);

        btnSalvar = findViewById(R.id.btn_salvar);
        btnExcluir = findViewById(R.id.btn_excluir);
        btnRegistrarAtividade = findViewById(R.id.btn_registrar_atividade);
        progressBar = findViewById(R.id.progress_bar);

        // Spinners
        String[] tipos = {"Preventiva", "Corretiva", "Preditiva"};
        String[] prioridades = {"Alta", "Média", "Baixa"};
        String[] statuses = {"Pendente", "Em Andamento", "Concluída", "Cancelada"};

        spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tipos));
        spinnerPrioridade.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prioridades));
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses));

        // Data picker
        etDataAgendada.setOnClickListener(v -> abrirDatePicker());

        btnSalvar.setOnClickListener(v -> salvar());
        btnExcluir.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        btnExcluir.setOnClickListener(v -> confirmarExclusao());

        btnRegistrarAtividade.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        btnRegistrarAtividade.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, AtividadeCampoFormActivity.class);
            intent.putExtra("manutencao_id", manutencaoId);
            startActivity(intent);
        });
    }

    private void abrirDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            dataAgendadaSelected = selected.getTime();
            etDataAgendada.setText(String.format("%02d/%02d/%d", day, month + 1, year));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void carregarManutencao() {
        setLoading(true);
        firebaseHelper.getManutencoesRef().document(manutencaoId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Manutencao m = doc.toObject(Manutencao.class);
                        if (m != null) {
                            etEquipamento.setText(m.getEquipamentoNome());
                            etTecnico.setText(m.getTecnicoResponsavel());
                            etDescricao.setText(m.getDescricao());
                            etCusto.setText(String.valueOf(m.getCusto()));
                            etObservacoes.setText(m.getObservacoes());

                            spinnerTipo.setText(getTipoDisplay(m.getTipo()), false);
                            spinnerPrioridade.setText(getPrioridadeDisplay(m.getPrioridade()), false);
                            spinnerStatus.setText(getStatusDisplay(m.getStatus()), false);
                        }
                    }
                    setLoading(false);
                })
                .addOnFailureListener(e -> setLoading(false));
    }

    private void salvar() {
        String equipamento = getText(etEquipamento);
        String tecnico = getText(etTecnico);
        String descricao = getText(etDescricao);
        String tipo = getTipoValue(spinnerTipo.getText().toString());
        String prioridade = getPrioridadeValue(spinnerPrioridade.getText().toString());
        String status = getStatusValue(spinnerStatus.getText().toString());

        if (TextUtils.isEmpty(descricao)) {
            Toast.makeText(this, "Informe a descrição da manutenção", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        Manutencao m = new Manutencao(equipamentoId, equipamento,
                "", "", tipo, prioridade, descricao, tecnico);
        m.setStatus(status);

        String custoStr = getText(etCusto);
        if (!TextUtils.isEmpty(custoStr)) {
            try { m.setCusto(Double.parseDouble(custoStr)); } catch (Exception ignored) {}
        }

        if (dataAgendadaSelected != null) {
            m.setDataAgendada(new Timestamp(dataAgendadaSelected));
        }

        m.setObservacoes(getText(etObservacoes));
        m.setSolicitanteId(firebaseHelper.getCurrentUserId());
        m.setUpdatedAt(Timestamp.now());

        if (isEditing) {
            firebaseHelper.getManutencoesRef().document(manutencaoId).set(m)
                    .addOnSuccessListener(v -> { Toast.makeText(this, getString(R.string.sucesso_salvar), Toast.LENGTH_SHORT).show(); finish(); })
                    .addOnFailureListener(e -> setLoading(false));
        } else {
            firebaseHelper.getManutencoesRef().add(m)
                    .addOnSuccessListener(ref -> { Toast.makeText(this, getString(R.string.sucesso_salvar), Toast.LENGTH_SHORT).show(); finish(); })
                    .addOnFailureListener(e -> setLoading(false));
        }
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Manutenção")
                .setMessage(getString(R.string.confirmar_exclusao))
                .setPositiveButton("Excluir", (d, w) -> {
                    firebaseHelper.getManutencoesRef().document(manutencaoId)
                            .update("status", Manutencao.STATUS_CANCELADA)
                            .addOnSuccessListener(v -> { Toast.makeText(this, getString(R.string.sucesso_excluir), Toast.LENGTH_SHORT).show(); finish(); });
                })
                .setNegativeButton("Cancelar", null).show();
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private String getTipoValue(String display) {
        switch (display) {
            case "Corretiva": return "CORRETIVA";
            case "Preditiva": return "PREDITIVA";
            default: return "PREVENTIVA";
        }
    }

    private String getTipoDisplay(String value) {
        switch (value) {
            case "CORRETIVA": return "Corretiva";
            case "PREDITIVA": return "Preditiva";
            default: return "Preventiva";
        }
    }

    private String getPrioridadeValue(String display) {
        switch (display) {
            case "Média": return "MEDIA";
            case "Baixa": return "BAIXA";
            default: return "ALTA";
        }
    }

    private String getPrioridadeDisplay(String value) {
        switch (value) {
            case "MEDIA": return "Média";
            case "BAIXA": return "Baixa";
            default: return "Alta";
        }
    }

    private String getStatusValue(String display) {
        switch (display) {
            case "Em Andamento": return "EM_ANDAMENTO";
            case "Concluída": return "CONCLUIDA";
            case "Cancelada": return "CANCELADA";
            default: return "PENDENTE";
        }
    }

    private String getStatusDisplay(String value) {
        switch (value) {
            case "EM_ANDAMENTO": return "Em Andamento";
            case "CONCLUIDA": return "Concluída";
            case "CANCELADA": return "Cancelada";
            default: return "Pendente";
        }
    }

    private void setLoading(boolean loading) {
        btnSalvar.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}