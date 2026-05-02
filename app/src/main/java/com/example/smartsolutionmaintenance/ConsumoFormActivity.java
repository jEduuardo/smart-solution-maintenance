package com.example.smartsolutionmaintenance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.models.Consumo;
import com.smartsolution.maintenance.utils.DateUtils;
import com.smartsolution.maintenance.utils.AlertaManager;
import com.smartsolution.maintenance.utils.FirebaseHelper;

public class ConsumoFormActivity extends AppCompatActivity {

    private TextInputEditText etValor, etCustoTotal, etValorMeta, etValorAnterior, etObservacoes;
    private AutoCompleteTextView spinnerTipo, spinnerMes, spinnerAno, spinnerSetor;
    private SwitchMaterial switchAnomalia;
    private MaterialButton btnSalvar;
    private CircularProgressIndicator progressBar;

    private FirebaseHelper firebaseHelper;
    private String consumoId;
    private boolean isEditing;

    private static final String[] MESES_NOMES = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_form);

        firebaseHelper = FirebaseHelper.getInstance();
        consumoId = getIntent().getStringExtra("consumo_id");
        isEditing = consumoId != null;

        setupToolbar();
        initViews();

        if (isEditing) carregarConsumo();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditing ? "Editar Consumo" : "Registrar Consumo");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        etValor = findViewById(R.id.et_valor);
        etCustoTotal = findViewById(R.id.et_custo_total);
        etValorMeta = findViewById(R.id.et_valor_meta);
        etValorAnterior = findViewById(R.id.et_valor_anterior);
        etObservacoes = findViewById(R.id.et_observacoes);
        spinnerTipo = findViewById(R.id.spinner_tipo);
        spinnerMes = findViewById(R.id.spinner_mes);
        spinnerAno = findViewById(R.id.spinner_ano);
        spinnerSetor = findViewById(R.id.spinner_setor);
        switchAnomalia = findViewById(R.id.switch_anomalia);
        btnSalvar = findViewById(R.id.btn_salvar);
        progressBar = findViewById(R.id.progress_bar);

        String[] tipos = {"Água", "Energia"};
        spinnerTipo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tipos));
        spinnerMes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MESES_NOMES));
        spinnerMes.setText(MESES_NOMES[DateUtils.getMesAtual() - 1], false);

        String[] anos = {String.valueOf(DateUtils.getAnoAtual() - 1),
                String.valueOf(DateUtils.getAnoAtual()),
                String.valueOf(DateUtils.getAnoAtual() + 1)};
        spinnerAno.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, anos));
        spinnerAno.setText(String.valueOf(DateUtils.getAnoAtual()), false);

        btnSalvar.setOnClickListener(v -> salvar());
    }

    private void carregarConsumo() {
        setLoading(true);
        firebaseHelper.getConsumosRef().document(consumoId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Consumo c = doc.toObject(Consumo.class);
                        if (c != null) {
                            spinnerTipo.setText(c.getTipo().equals(Consumo.TIPO_AGUA) ? "Água" : "Energia", false);
                            spinnerMes.setText(MESES_NOMES[c.getMes() - 1], false);
                            spinnerAno.setText(String.valueOf(c.getAno()), false);
                            etValor.setText(String.valueOf(c.getValor()));
                            etCustoTotal.setText(String.valueOf(c.getCustoTotal()));
                            etValorMeta.setText(String.valueOf(c.getValorMeta()));
                            etValorAnterior.setText(String.valueOf(c.getValorAnterior()));
                            etObservacoes.setText(c.getObservacoes());
                            switchAnomalia.setChecked(c.isAnomalia());
                        }
                    }
                    setLoading(false);
                })
                .addOnFailureListener(e -> setLoading(false));
    }

    private void salvar() {
        String valorStr = getText(etValor);
        String tipoDisplay = spinnerTipo.getText().toString();
        String mesStr = spinnerMes.getText().toString();
        String anoStr = spinnerAno.getText().toString();

        if (TextUtils.isEmpty(valorStr)) {
            etValor.setError("Informe o valor de consumo");
            return;
        }

        setLoading(true);

        String tipo = tipoDisplay.equals("Água") ? Consumo.TIPO_AGUA : Consumo.TIPO_ENERGIA;
        String unidade = tipo.equals(Consumo.TIPO_AGUA) ? "m³" : "kWh";
        double valor = Double.parseDouble(valorStr);
        int mes = getMesNumero(mesStr);
        int ano = Integer.parseInt(anoStr);
        double custoTotal = toDouble(getText(etCustoTotal));
        double valorMeta = toDouble(getText(etValorMeta));
        double valorAnterior = toDouble(getText(etValorAnterior));

        Consumo consumo = new Consumo(tipo, "", "", valor, unidade, custoTotal, mes, ano);
        consumo.setValorMeta(valorMeta);
        consumo.setValorAnterior(valorAnterior);
        consumo.setObservacoes(getText(etObservacoes));
        consumo.verificarAnomalia();
        consumo.setRegistradoPor(firebaseHelper.getCurrentUserId());

        if (isEditing) {
            firebaseHelper.getConsumosRef().document(consumoId).set(consumo)
                    .addOnSuccessListener(v -> onSaved(consumo))
                    .addOnFailureListener(e -> setLoading(false));
        } else {
            firebaseHelper.getConsumosRef().add(consumo)
                    .addOnSuccessListener(ref -> {
                        consumo.setId(ref.getId());
                        onSaved(consumo);
                    })
                    .addOnFailureListener(e -> setLoading(false));
        }
    }

    private void onSaved(Consumo consumo) {
        // Verificar e gerar alerta de anomalia
        if (consumo.isAnomalia()) {
            AlertaManager.getInstance().gerarAlertaConsumo(consumo);
        }
        Toast.makeText(this, getString(R.string.sucesso_salvar), Toast.LENGTH_SHORT).show();
        finish();
    }

    private int getMesNumero(String nomeMes) {
        for (int i = 0; i < MESES_NOMES.length; i++) {
            if (MESES_NOMES[i].equals(nomeMes)) return i + 1;
        }
        return DateUtils.getMesAtual();
    }

    private double toDouble(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setLoading(boolean loading) {
        btnSalvar.setEnabled(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}