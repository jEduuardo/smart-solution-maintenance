package com.example.smartsolutionmaintenance.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartsolutionmaintenance.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class NovaManutencaoActivity extends AppCompatActivity {

    private TextInputEditText edtTitulo, edtDescricao, edtEquipamento,
            edtResponsavel, edtPrazo, edtCusto;
    private AutoCompleteTextView spinnerPrioridade, spinnerTipo, spinnerStatus;
    private Button btnSalvar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_manutencao);
        db = FirebaseFirestore.getInstance();

        edtTitulo       = findViewById(R.id.edtTitulo);
        edtDescricao    = findViewById(R.id.edtDescricao);
        edtEquipamento  = findViewById(R.id.edtEquipamento);
        edtResponsavel  = findViewById(R.id.edtResponsavel);
        edtPrazo        = findViewById(R.id.edtPrazo);
        edtCusto        = findViewById(R.id.edtCusto);
        spinnerPrioridade = findViewById(R.id.spinnerPrioridade);
        spinnerTipo       = findViewById(R.id.spinnerTipo);
        spinnerStatus     = findViewById(R.id.spinnerStatus);
        btnSalvar         = findViewById(R.id.btnSalvar);

        spinnerPrioridade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{"Alta","Média","Baixa"}));
        spinnerTipo.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"Preventiva","Corretiva","Preditiva","Emergencial"}));
        spinnerStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"Pendente","Em Andamento","Concluído"}));

        btnSalvar.setOnClickListener(v -> salvarManutencao());
    }

    private void salvarManutencao() {
        String titulo     = edtTitulo.getText().toString().trim();
        String prioridade = spinnerPrioridade.getText().toString().trim();
        if (titulo.isEmpty() || prioridade.isEmpty()) {
            Toast.makeText(this, "Título e prioridade são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        String statusDisplay   = spinnerStatus.getText().toString().trim();
        String statusCodigo    = statusDisplay.equals("Em Andamento") ? "em_andamento"
                : statusDisplay.equals("Concluído") ? "concluido" : "pendente";
        String prioridadeCodigo = prioridade.equals("Alta") ? "alta"
                : prioridade.equals("Baixa") ? "baixa" : "media";

        Map<String, Object> dados = new HashMap<>();
        dados.put("titulo",       titulo);
        dados.put("descricao",    edtDescricao.getText().toString().trim());
        dados.put("equipamento",  edtEquipamento.getText().toString().trim());
        dados.put("responsavel",  edtResponsavel.getText().toString().trim());
        dados.put("prazo",        edtPrazo.getText().toString().trim());
        dados.put("prioridade",   prioridadeCodigo);
        dados.put("tipo",         spinnerTipo.getText().toString().trim());
        dados.put("status",       statusCodigo);
        dados.put("dataCriacao",  Timestamp.now());

        String custoStr = edtCusto.getText().toString().trim();
        if (!custoStr.isEmpty()) {
            try { dados.put("custo", Double.parseDouble(custoStr)); }
            catch (NumberFormatException ignored) {}
        }

        btnSalvar.setEnabled(false);
        db.collection("manutencoes").add(dados)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Manutenção registrada!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnSalvar.setEnabled(true);
                    Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
