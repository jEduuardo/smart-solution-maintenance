package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.smartsolutionmaintenance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalAlertasAtivos, tvManutencoesPendentes;
    private TextView tvConsumoAguaMes, tvConsumoEnergiaMes;
    private TextView tvEquipamentosAtivos;
    private CardView cardConsumo, cardManutencao, cardAlertas, cardEquipamentos;
    private CardView cardInstituicoes, cardRegistros;
    private ImageButton btnSair;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();

        inicializarViews();
        configurarBotoes();
        configurarBottomNav();
        carregarDados();
    }

    private void inicializarViews() {
        tvTotalAlertasAtivos    = findViewById(R.id.tvTotalAlertasAtivos);
        tvManutencoesPendentes  = findViewById(R.id.tvManutencoesPendentes);
        tvConsumoAguaMes        = findViewById(R.id.tvConsumoAguaMes);
        tvConsumoEnergiaMes     = findViewById(R.id.tvConsumoEnergiaMes);
        tvEquipamentosAtivos    = findViewById(R.id.tvEquipamentosAtivos);
        cardConsumo             = findViewById(R.id.cardConsumo);
        cardManutencao          = findViewById(R.id.cardManutencao);
        cardAlertas             = findViewById(R.id.cardAlertas);
        cardEquipamentos        = findViewById(R.id.cardEquipamentos);
        cardInstituicoes        = findViewById(R.id.cardInstituicoes);
        cardRegistros           = findViewById(R.id.cardRegistros);
        btnSair                 = findViewById(R.id.btnSair);
        bottomNav               = findViewById(R.id.bottomNav);
    }

    private void configurarBotoes() {
        cardConsumo.setOnClickListener(v -> startActivity(new Intent(this, ConsumoActivity.class)));
        cardManutencao.setOnClickListener(v -> startActivity(new Intent(this, ManutencaoActivity.class)));
        cardAlertas.setOnClickListener(v -> startActivity(new Intent(this, AlertasActivity.class)));
        cardEquipamentos.setOnClickListener(v -> startActivity(new Intent(this, EquipamentosActivity.class)));
        cardRegistros.setOnClickListener(v -> startActivity(new Intent(this, RegistroAtividadeActivity.class)));

        btnSair.setOnClickListener(v -> confirmarSaida());
    }

    private void confirmarSaida() {
        new AlertDialog.Builder(this)
                .setTitle("Sair do sistema")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sair", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
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
            } else if (id == R.id.nav_alertas) {
                startActivity(new Intent(this, AlertasActivity.class));
                return true;
            } else if (id == R.id.nav_mais) {
                startActivity(new Intent(this, EquipamentosActivity.class));
                return true;
            }
            return false;
        });
    }

    private void carregarDados() {
        db.collection("alertas").whereEqualTo("status", "ativo").get()
                .addOnSuccessListener(snap -> tvTotalAlertasAtivos.setText(String.valueOf(snap.size())));

        db.collection("manutencoes").whereEqualTo("status", "pendente").get()
                .addOnSuccessListener(snap -> tvManutencoesPendentes.setText(String.valueOf(snap.size())));

        db.collection("equipamentos").whereEqualTo("status", "ativo").get()
                .addOnSuccessListener(snap -> tvEquipamentosAtivos.setText(String.valueOf(snap.size())));

        db.collection("consumo").whereEqualTo("tipo", "agua")
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener(snap -> {
                    if (!snap.isEmpty()) {
                        Double v = ((QueryDocumentSnapshot) snap.getDocuments().get(0)).getDouble("valor");
                        if (v != null) tvConsumoAguaMes.setText(String.format("%.1f m³", v));
                    }
                });

        db.collection("consumo").whereEqualTo("tipo", "energia")
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener(snap -> {
                    if (!snap.isEmpty()) {
                        Double v = ((QueryDocumentSnapshot) snap.getDocuments().get(0)).getDouble("valor");
                        if (v != null) tvConsumoEnergiaMes.setText(String.format("%.1f kWh", v));
                    }
                });
    }


}
