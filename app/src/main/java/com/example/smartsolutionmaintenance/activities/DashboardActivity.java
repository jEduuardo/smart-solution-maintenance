package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.smartsolutionmaintenance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalAlertasAtivos, tvManutencoesPendentes;
    private TextView tvConsumoAguaMes, tvConsumoEnergiaMes;
    private TextView tvEquipamentosAtivos, tvManutencoesMes;
    private CardView cardConsumo, cardManutencao, cardAlertas, cardEquipamentos;
    private CardView cardInstituicoes, cardRegistros;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dashboard");
        }

        inicializarViews();
        configurarBotoes();
        configurarBottomNav();
        carregarDados();
    }

    private void inicializarViews() {
        tvManutencoesPendentes = findViewById(R.id.tvManutencoesPendentes);
        tvConsumoAguaMes = findViewById(R.id.tvConsumoAguaMes);
        tvConsumoEnergiaMes = findViewById(R.id.tvConsumoEnergiaMes);
        tvEquipamentosAtivos = findViewById(R.id.tvEquipamentosAtivos);
        tvManutencoesMes = findViewById(R.id.tvManutencoesMes);

        cardConsumo = findViewById(R.id.cardConsumo);
        cardManutencao = findViewById(R.id.cardManutencao);
        cardEquipamentos = findViewById(R.id.cardEquipamentos);
        cardInstituicoes = findViewById(R.id.cardInstituicoes);
        cardRegistros = findViewById(R.id.cardRegistros);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void configurarBotoes() {
        cardConsumo.setOnClickListener(v -> startActivity(new Intent(this, ConsumoActivity.class)));
        cardManutencao.setOnClickListener(v -> startActivity(new Intent(this, ManutencaoActivity.class)));
        cardEquipamentos.setOnClickListener(v -> startActivity(new Intent(this, EquipamentosActivity.class)));
        cardInstituicoes.setOnClickListener(v -> startActivity(new Intent(this, InstituicaoActivity.class)));
        cardRegistros.setOnClickListener(v -> startActivity(new Intent(this, RegistroAtividadeActivity.class)));
    }

    private void configurarBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_dashboard);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_consumo) {
                startActivity(new Intent(this, ConsumoActivity.class));
                return true;
            } else if (id == R.id.nav_manutencao) {
                startActivity(new Intent(this, ManutencaoActivity.class));
                return true;
            } else if (id == R.id.nav_mais) {
                startActivity(new Intent(this, EquipamentosActivity.class));
                return true;
            }
            return false;
        });
    }

    private void carregarDados() {
        // Alertas ativos
        db.collection("alertas")
                .whereEqualTo("status", "ativo")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int total = queryDocumentSnapshots.size();
                    tvTotalAlertasAtivos.setText(String.valueOf(total));
                });

        // Manutenções pendentes
        db.collection("manutencoes")
                .whereEqualTo("status", "pendente")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tvManutencoesPendentes.setText(String.valueOf(queryDocumentSnapshots.size()));
                });

        // Equipamentos ativos
        db.collection("equipamentos")
                .whereEqualTo("status", "ativo")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tvEquipamentosAtivos.setText(String.valueOf(queryDocumentSnapshots.size()));
                });

        // Consumo do mês atual
        carregarConsumoMes();
    }

    private void carregarConsumoMes() {
        // Simplificado: busca últimos registros de consumo
        db.collection("consumo")
                .whereEqualTo("tipo", "agua")
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        Double valor = doc.getDouble("valor");
                        if (valor != null) {
                            tvConsumoAguaMes.setText(String.format("%.1f m³", valor));
                        }
                    }
                });

        db.collection("consumo")
                .whereEqualTo("tipo", "energia")
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        Double valor = doc.getDouble("valor");
                        if (valor != null) {
                            tvConsumoEnergiaMes.setText(String.format("%.1f kWh", valor));
                        }
                    }
                });
    }
}