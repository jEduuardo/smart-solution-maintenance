package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.adapters.ConsumoAdapter;
import com.example.smartsolutionmaintenance.models.Consumo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ConsumoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConsumoAdapter adapter;
    private List<Consumo> listaConsumo;
    private FirebaseFirestore db;
    private Spinner spinnerTipo;
    private FloatingActionButton fabAdicionar;
    private TextView tvMediaMensal, tvUltimoRegistro, tvVariacao;
    private LinearLayout layoutVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);
        db = FirebaseFirestore.getInstance();

        recyclerView     = findViewById(R.id.recyclerViewConsumo);
        spinnerTipo      = findViewById(R.id.spinnerTipo);
        fabAdicionar     = findViewById(R.id.fabAdicionar);
        tvMediaMensal    = findViewById(R.id.tvMediaMensal);
        tvUltimoRegistro = findViewById(R.id.tvUltimoRegistro);
        tvVariacao       = findViewById(R.id.tvVariacao);
        layoutVazio      = findViewById(R.id.layoutVazio);

        listaConsumo = new ArrayList<>();
        adapter = new ConsumoAdapter(listaConsumo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        configurarSpinner();
        fabAdicionar.setOnClickListener(v ->
                startActivity(new Intent(this, NovoRegistroConsumoActivity.class)));

        carregarConsumo("agua");
    }

    private void configurarSpinner() {
        String[] tiposDisplay = {"Água", "Energia"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposDisplay);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterSpinner);

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carregarConsumo(position == 0 ? "agua" : "energia");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void carregarConsumo(String tipo) {
        db.collection("consumo")
                .whereEqualTo("tipo", tipo)
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnSuccessListener(snap -> {
                    listaConsumo.clear();
                    double soma = 0;
                    for (QueryDocumentSnapshot doc : snap) {
                        Consumo c = doc.toObject(Consumo.class);
                        c.setId(doc.getId());
                        listaConsumo.add(c);
                        if (c.getValor() > 0) soma += c.getValor();
                    }
                    adapter.notifyDataSetChanged();
                    if (listaConsumo.isEmpty()) {
                        layoutVazio.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        layoutVazio.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        String unidade = tipo.equals("agua") ? "m³" : "kWh";
                        double media = soma / listaConsumo.size();
                        tvMediaMensal.setText(String.format("Média: %.1f %s", media, unidade));
                        tvUltimoRegistro.setText(String.format("Último: %.1f %s",
                                listaConsumo.get(0).getValor(), unidade));
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarConsumo(spinnerTipo.getSelectedItemPosition() == 0 ? "agua" : "energia");
    }
}
