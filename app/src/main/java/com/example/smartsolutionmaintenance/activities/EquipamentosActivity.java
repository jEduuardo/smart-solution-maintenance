package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.adapters.EquipamentoAdapter;
import com.example.smartsolutionmaintenance.models.Equipamento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class EquipamentosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EquipamentoAdapter adapter;
    private List<Equipamento> listaEquipamentos;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdicionar;
    private LinearLayout layoutVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipamentos);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Equipamentos e Ativos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewEquipamentos);
        fabAdicionar = findViewById(R.id.fabAdicionar);
        layoutVazio = findViewById(R.id.layoutVazio);

        listaEquipamentos = new ArrayList<>();
        adapter = new EquipamentoAdapter(listaEquipamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdicionar.setOnClickListener(v ->
                startActivity(new Intent(this, NovoEquipamentoActivity.class)));

        carregarEquipamentos();
    }

    private void carregarEquipamentos() {
        db.collection("equipamentos")
                .orderBy("nome")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaEquipamentos.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Equipamento e = doc.toObject(Equipamento.class);
                        e.setId(doc.getId());
                        listaEquipamentos.add(e);
                    }
                    adapter.notifyDataSetChanged();

                    if (listaEquipamentos.isEmpty()) {
                        layoutVazio.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        layoutVazio.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarEquipamentos();
    }
}
