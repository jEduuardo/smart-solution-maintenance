package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.adapters.ManutencaoAdapter;
import com.example.smartsolutionmaintenance.models.Manutencao;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManutencaoAdapter adapter;
    private List<Manutencao> listaManutencao;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdicionar;
    private ChipGroup chipGroupStatus;
    private LinearLayout layoutVazio;
    private TextView tvTotalPendentes, tvTotalAndamento, tvTotalConcluidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao);
        db = FirebaseFirestore.getInstance();

        recyclerView       = findViewById(R.id.recyclerViewManutencao);
        fabAdicionar       = findViewById(R.id.fabAdicionar);
        chipGroupStatus    = findViewById(R.id.chipGroupStatus);
        layoutVazio        = findViewById(R.id.layoutVazio);
        tvTotalPendentes   = findViewById(R.id.tvTotalPendentes);
        tvTotalAndamento   = findViewById(R.id.tvTotalAndamento);
        tvTotalConcluidas  = findViewById(R.id.tvTotalConcluidas);

        listaManutencao = new ArrayList<>();
        adapter = new ManutencaoAdapter(listaManutencao, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdicionar.setOnClickListener(v ->
                startActivity(new Intent(this, NovaManutencaoActivity.class)));

        chipGroupStatus.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                if (chip != null) {
                    String txt = chip.getText().toString().toLowerCase();
                    if (txt.equals("todas")) carregarManutencoes(null);
                    else if (txt.equals("pendente")) carregarManutencoes("pendente");
                    else if (txt.contains("andamento")) carregarManutencoes("em_andamento");
                    else carregarManutencoes("concluido");
                }
            }
        });

        carregarContadores();
        carregarManutencoes(null);
    }

    private void carregarContadores() {
        String[] statuses = {"pendente", "em_andamento", "concluido"};
        TextView[] tvs = {tvTotalPendentes, tvTotalAndamento, tvTotalConcluidas};
        for (int i = 0; i < statuses.length; i++) {
            final int idx = i;
            db.collection("manutencoes").whereEqualTo("status", statuses[i]).get()
                    .addOnSuccessListener(snap -> tvs[idx].setText(String.valueOf(snap.size())));
        }
    }

    private void carregarManutencoes(String status) {
        com.google.firebase.firestore.Query query = db.collection("manutencoes")
                .orderBy("prioridade", com.google.firebase.firestore.Query.Direction.DESCENDING);
        if (status != null) query = query.whereEqualTo("status", status);

        query.get().addOnSuccessListener(snap -> {
            listaManutencao.clear();
            for (QueryDocumentSnapshot doc : snap) {
                Manutencao m = doc.toObject(Manutencao.class);
                m.setId(doc.getId());
                listaManutencao.add(m);
            }
            adapter.notifyDataSetChanged();
            layoutVazio.setVisibility(listaManutencao.isEmpty() ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(listaManutencao.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarContadores();
        carregarManutencoes(null);
    }
}
