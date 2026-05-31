package com.example.smartsolutionmaintenance.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.adapters.InstituicaoAdapter;
import com.example.smartsolutionmaintenance.models.Instituicao;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstituicaoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InstituicaoAdapter adapter;
    private List<Instituicao> listaInstituicoes;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdicionar;
    private LinearLayout layoutVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao);
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewInstituicoes);
        fabAdicionar = findViewById(R.id.fabAdicionar);
        layoutVazio  = findViewById(R.id.layoutVazio);

        listaInstituicoes = new ArrayList<>();
        adapter = new InstituicaoAdapter(listaInstituicoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdicionar.setOnClickListener(v -> mostrarDialogNovaInstituicao());
        carregarInstituicoes();
    }

    private void mostrarDialogNovaInstituicao() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_nova_instituicao, null);
        TextInputEditText edtNome        = dialogView.findViewById(R.id.edtNomeInstituicao);
        TextInputEditText edtEndereco    = dialogView.findViewById(R.id.edtEndereco);
        TextInputEditText edtResponsavel = dialogView.findViewById(R.id.edtResponsavel);
        TextInputEditText edtTelefone    = dialogView.findViewById(R.id.edtTelefone);
        TextInputEditText edtSetores     = dialogView.findViewById(R.id.edtSetores);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Nova Instituição")
                .setView(dialogView)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String nome = edtNome.getText().toString().trim();
                    if (nome.isEmpty()) {
                        Toast.makeText(this, "Nome é obrigatório", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, Object> dados = new HashMap<>();
                    dados.put("nome", nome);
                    dados.put("endereco", edtEndereco.getText().toString().trim());
                    dados.put("responsavel", edtResponsavel.getText().toString().trim());
                    dados.put("telefone", edtTelefone.getText().toString().trim());
                    dados.put("setores", edtSetores.getText().toString().trim());
                    dados.put("dataCadastro", Timestamp.now());
                    db.collection("instituicoes").add(dados)
                            .addOnSuccessListener(ref -> {
                                Toast.makeText(this, "Instituição cadastrada!", Toast.LENGTH_SHORT).show();
                                carregarInstituicoes();
                            });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void carregarInstituicoes() {
        db.collection("instituicoes").orderBy("nome").get()
                .addOnSuccessListener(snap -> {
                    listaInstituicoes.clear();
                    for (QueryDocumentSnapshot doc : snap) {
                        Instituicao inst = doc.toObject(Instituicao.class);
                        inst.setId(doc.getId());
                        listaInstituicoes.add(inst);
                    }
                    adapter.notifyDataSetChanged();
                    layoutVazio.setVisibility(listaInstituicoes.isEmpty() ? View.VISIBLE : View.GONE);
                    recyclerView.setVisibility(listaInstituicoes.isEmpty() ? View.GONE : View.VISIBLE);
                });
    }
}
