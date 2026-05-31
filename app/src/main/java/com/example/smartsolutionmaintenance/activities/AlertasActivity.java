package com.example.smartsolutionmaintenance.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.adapters.AlertaAdapter;
import com.example.smartsolutionmaintenance.models.Alerta;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AlertasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlertaAdapter adapter;
    private List<Alerta> listaAlertas;
    private FirebaseFirestore db;
    private LinearLayout layoutVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewAlertas);
        layoutVazio  = findViewById(R.id.layoutVazio);

        listaAlertas = new ArrayList<>();
        adapter = new AlertaAdapter(listaAlertas, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        carregarAlertas();
    }

    private void carregarAlertas() {
        db.collection("alertas")
                .orderBy("dataHora", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    listaAlertas.clear();
                    for (QueryDocumentSnapshot doc : snap) {
                        Alerta a = doc.toObject(Alerta.class);
                        a.setId(doc.getId());
                        listaAlertas.add(a);
                    }
                    adapter.notifyDataSetChanged();
                    layoutVazio.setVisibility(listaAlertas.isEmpty() ? View.VISIBLE : View.GONE);
                    recyclerView.setVisibility(listaAlertas.isEmpty() ? View.GONE : View.VISIBLE);
                });
    }

    public void marcarComoResolvido(String alertaId) {
        db.collection("alertas").document(alertaId)
                .update("status", "resolvido")
                .addOnSuccessListener(v -> carregarAlertas());
    }
    public void voltarTela(View view) {
        finish();
    }
}
