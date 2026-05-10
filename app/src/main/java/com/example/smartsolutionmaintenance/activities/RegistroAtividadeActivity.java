package com.example.smartsolutionmaintenance.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.adapters.AtividadeAdapter;
import com.example.smartsolutionmaintenance.models.Atividade;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroAtividadeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;
    private List<Atividade> listaAtividades;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdicionar;
    private LinearLayout layoutVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_atividade);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registro de Atividades");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewAtividades);
        fabAdicionar = findViewById(R.id.fabAdicionar);
        layoutVazio = findViewById(R.id.layoutVazio);

        listaAtividades = new ArrayList<>();
        adapter = new AtividadeAdapter(listaAtividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdicionar.setOnClickListener(v -> mostrarDialogNovaAtividade());

        carregarAtividades();
    }

    private void mostrarDialogNovaAtividade() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_nova_atividade, null);
        TextInputEditText edtDescricao = dialogView.findViewById(R.id.edtDescricaoAtividade);
        TextInputEditText edtLocal = dialogView.findViewById(R.id.edtLocal);
        AutoCompleteTextView spinnerTipo = dialogView.findViewById(R.id.spinnerTipoAtividade);

        String[] tipos = {"Manutenção", "Inspeção", "Limpeza", "Reparo", "Instalação", "Outros"};
        spinnerTipo.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, tipos));

        new MaterialAlertDialogBuilder(this)
                .setTitle("Registrar Atividade")
                .setView(dialogView)
                .setPositiveButton("Registrar", (dialog, which) -> {
                    String descricao = edtDescricao.getText().toString().trim();
                    if (descricao.isEmpty()) {
                        Toast.makeText(this, "Descreva a atividade", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                            ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "desconhecido";
                    String userEmail = FirebaseAuth.getInstance().getCurrentUser() != null
                            ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : "desconhecido";

                    Map<String, Object> dados = new HashMap<>();
                    dados.put("descricao", descricao);
                    dados.put("local", edtLocal.getText().toString().trim());
                    dados.put("tipo", spinnerTipo.getText().toString().trim());
                    dados.put("userId", userId);
                    dados.put("userEmail", userEmail);
                    dados.put("dataHora", Timestamp.now());

                    db.collection("atividades").add(dados)
                            .addOnSuccessListener(ref -> {
                                Toast.makeText(this, "Atividade registrada!", Toast.LENGTH_SHORT).show();
                                carregarAtividades();
                            });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void carregarAtividades() {
        db.collection("atividades")
                .orderBy("dataHora", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaAtividades.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Atividade a = doc.toObject(Atividade.class);
                        a.setId(doc.getId());
                        listaAtividades.add(a);
                    }
                    adapter.notifyDataSetChanged();
                    layoutVazio.setVisibility(listaAtividades.isEmpty() ? View.VISIBLE : View.GONE);
                    recyclerView.setVisibility(listaAtividades.isEmpty() ? View.GONE : View.VISIBLE);
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
