package com.example.smartsolutionmaintenance.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.utils.AlertaManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class NovoRegistroConsumoActivity extends AppCompatActivity {

    private TextInputEditText edtValor, edtObservacao, edtLeitura;
    private AutoCompleteTextView spinnerTipo, spinnerSetor;
    private Button btnSalvar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_consumo);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Novo Registro de Consumo");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtValor = findViewById(R.id.edtValor);
        edtObservacao = findViewById(R.id.edtObservacao);
        edtLeitura = findViewById(R.id.edtLeitura);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerSetor = findViewById(R.id.spinnerSetor);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Configurar dropdowns
        String[] tipos = {"agua", "energia"};
        String[] tiposDisplay = {"Água", "Energia"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, tiposDisplay);
        spinnerTipo.setAdapter(tipoAdapter);

        String[] setores = {"Administração", "Sala de Aula", "Cozinha", "Banheiros",
                "Área Externa", "Fisioterapia", "Terapia Ocupacional", "Outros"};
        ArrayAdapter<String> setorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, setores);
        spinnerSetor.setAdapter(setorAdapter);

        btnSalvar.setOnClickListener(v -> salvarConsumo());
    }

    private void salvarConsumo() {
        String valorStr = edtValor.getText().toString().trim();
        String tipo = spinnerTipo.getText().toString().trim();
        String setor = spinnerSetor.getText().toString().trim();
        String observacao = edtObservacao.getText().toString().trim();
        String leituraStr = edtLeitura.getText().toString().trim();

        if (valorStr.isEmpty() || tipo.isEmpty() || setor.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converter tipo display para código
        String tipoCodigo = tipo.equals("Água") ? "agua" : "energia";

        Map<String, Object> dados = new HashMap<>();
        dados.put("tipo", tipoCodigo);
        dados.put("valor", valor);
        dados.put("setor", setor);
        dados.put("observacao", observacao);
        dados.put("data", Timestamp.now());
        if (!leituraStr.isEmpty()) {
            try {
                dados.put("leitura", Double.parseDouble(leituraStr));
            } catch (NumberFormatException ignored) {}
        }

        btnSalvar.setEnabled(false);

        db.collection("consumo").add(dados)
                .addOnSuccessListener(ref -> {
                    // Verificar anomalias
                    AlertaManager.verificarAnomaliaConsumo(db, tipoCodigo, valor, setor);
                    Toast.makeText(this, "Consumo registrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnSalvar.setEnabled(true);
                    Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
