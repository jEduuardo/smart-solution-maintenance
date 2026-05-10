package com.example.smartsolutionmaintenance.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Nova Manutenção");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtEquipamento = findViewById(R.id.edtEquipamento);
        edtResponsavel = findViewById(R.id.edtResponsavel);
        edtPrazo = findViewById(R.id.edtPrazo);
        edtCusto = findViewById(R.id.edtCusto);
        spinnerPrioridade = findViewById(R.id.spinnerPrioridade);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSalvar = findViewById(R.id.btnSalvar);

        String[] prioridades = {"alta", "media", "baixa"};
        String[] priorDisplay = {"Alta", "Média", "Baixa"};
        spinnerPrioridade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, priorDisplay));

        String[] tipos = {"Preventiva", "Corretiva", "Preditiva", "Emergencial"};
        spinnerTipo.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, tipos));

        String[] statuses = {"pendente", "em_andamento", "concluido"};
        String[] statusDisplay = {"Pendente", "Em Andamento", "Concluído"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, statusDisplay));

        btnSalvar.setOnClickListener(v -> salvarManutencao());
    }

    private void salvarManutencao() {
        String titulo = edtTitulo.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();
        String equipamento = edtEquipamento.getText().toString().trim();
        String responsavel = edtResponsavel.getText().toString().trim();
        String prazo = edtPrazo.getText().toString().trim();
        String prioridade = spinnerPrioridade.getText().toString().trim();
        String tipo = spinnerTipo.getText().toString().trim();
        String status = spinnerStatus.getText().toString().trim();

        if (titulo.isEmpty() || prioridade.isEmpty()) {
            Toast.makeText(this, "Título e prioridade são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converter status display para código
        String statusCodigo = "pendente";
        if (status.equals("Em Andamento")) statusCodigo = "em_andamento";
        else if (status.equals("Concluído")) statusCodigo = "concluido";

        String prioridadeCodigo = "media";
        if (prioridade.equals("Alta")) prioridadeCodigo = "alta";
        else if (prioridade.equals("Baixa")) prioridadeCodigo = "baixa";

        Map<String, Object> dados = new HashMap<>();
        dados.put("titulo", titulo);
        dados.put("descricao", descricao);
        dados.put("equipamento", equipamento);
        dados.put("responsavel", responsavel);
        dados.put("prazo", prazo);
        dados.put("prioridade", prioridadeCodigo);
        dados.put("tipo", tipo);
        dados.put("status", statusCodigo);
        dados.put("dataCriacao", Timestamp.now());

        String custoStr = edtCusto.getText().toString().trim();
        if (!custoStr.isEmpty()) {
            try {
                dados.put("custo", Double.parseDouble(custoStr));
            } catch (NumberFormatException ignored) {}
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
