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

public class NovoEquipamentoActivity extends AppCompatActivity {

    private TextInputEditText edtNome, edtNumeroSerie, edtModelo,
            edtFabricante, edtSetor, edtDataAquisicao, edtUltimaManutencao, edtObservacoes;
    private AutoCompleteTextView spinnerStatus, spinnerCategoria;
    private Button btnSalvar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_equipamento);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Novo Equipamento");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtNome = findViewById(R.id.edtNome);
        edtNumeroSerie = findViewById(R.id.edtNumeroSerie);
        edtModelo = findViewById(R.id.edtModelo);
        edtFabricante = findViewById(R.id.edtFabricante);
        edtSetor = findViewById(R.id.edtSetor);
        edtDataAquisicao = findViewById(R.id.edtDataAquisicao);
        edtUltimaManutencao = findViewById(R.id.edtUltimaManutencao);
        edtObservacoes = findViewById(R.id.edtObservacoes);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSalvar = findViewById(R.id.btnSalvar);

        String[] statuses = {"Ativo", "Inativo", "Em Manutenção", "Descartado"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, statuses));

        String[] categorias = {"Ar Condicionado", "Computador", "Impressora", "Bomba d'água",
                "Gerador", "Elevador", "Câmera de Segurança", "Cadeira de Rodas",
                "Equipamento Terapêutico", "Eletrodoméstico", "Outro"};
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categorias));

        btnSalvar.setOnClickListener(v -> salvarEquipamento());
    }

    private void salvarEquipamento() {
        String nome = edtNome.getText().toString().trim();
        if (nome.isEmpty()) {
            Toast.makeText(this, "Nome é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        String status = spinnerStatus.getText().toString().trim();
        String statusCodigo = "ativo";
        if (status.equals("Inativo")) statusCodigo = "inativo";
        else if (status.equals("Em Manutenção")) statusCodigo = "em_manutencao";
        else if (status.equals("Descartado")) statusCodigo = "descartado";

        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("numeroSerie", edtNumeroSerie.getText().toString().trim());
        dados.put("modelo", edtModelo.getText().toString().trim());
        dados.put("fabricante", edtFabricante.getText().toString().trim());
        dados.put("setor", edtSetor.getText().toString().trim());
        dados.put("dataAquisicao", edtDataAquisicao.getText().toString().trim());
        dados.put("ultimaManutencao", edtUltimaManutencao.getText().toString().trim());
        dados.put("observacoes", edtObservacoes.getText().toString().trim());
        dados.put("categoria", spinnerCategoria.getText().toString().trim());
        dados.put("status", statusCodigo);
        dados.put("dataCadastro", Timestamp.now());

        btnSalvar.setEnabled(false);

        db.collection("equipamentos").add(dados)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Equipamento cadastrado!", Toast.LENGTH_SHORT).show();
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
