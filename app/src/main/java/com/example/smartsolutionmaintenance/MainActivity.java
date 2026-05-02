package com.example.smartsolutionmaintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.models.Alerta;
import com.smartsolution.maintenance.utils.FirebaseHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Toolbar toolbar;
    private FirebaseHelper firebaseHelper;

    // Dashboard views
    private TextView tvTotalEquipamentos, tvManutencoesAbertas, tvAlertasAtivos;
    private TextView tvConsumoAgua, tvConsumoEnergia;
    private TextView tvNomeUsuario;
    private LinearProgressIndicator progressAgua, progressEnergia;
    private MaterialCardView cardInstituicoes, cardEquipamentos, cardManutencoes;
    private MaterialCardView cardConsumo, cardAlertas, cardAtividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = FirebaseHelper.getInstance();

        initViews();
        setupBottomNav();
        carregarDashboard();
        observarAlertas();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottom_nav);
        tvTotalEquipamentos = findViewById(R.id.tv_total_equipamentos);
        tvManutencoesAbertas = findViewById(R.id.tv_manutencoes_abertas);
        tvAlertasAtivos = findViewById(R.id.tv_alertas_ativos);
        tvConsumoAgua = findViewById(R.id.tv_consumo_agua);
        tvConsumoEnergia = findViewById(R.id.tv_consumo_energia);
        tvNomeUsuario = findViewById(R.id.tv_nome_usuario);
        progressAgua = findViewById(R.id.progress_agua);
        progressEnergia = findViewById(R.id.progress_energia);

        // Cards de menu rápido
        cardInstituicoes = findViewById(R.id.card_instituicoes);
        cardEquipamentos = findViewById(R.id.card_equipamentos);
        cardManutencoes = findViewById(R.id.card_manutencoes);
        cardConsumo = findViewById(R.id.card_consumo);
        cardAlertas = findViewById(R.id.card_alertas);
        cardAtividades = findViewById(R.id.card_atividades);

        setupCardListeners();
        carregarNomeUsuario();
    }

    private void setupCardListeners() {
        cardInstituicoes.setOnClickListener(v ->
                startActivity(new Intent(this, InstituicaoListActivity.class)));
        cardEquipamentos.setOnClickListener(v ->
                startActivity(new Intent(this, EquipamentoListActivity.class)));
        cardManutencoes.setOnClickListener(v ->
                startActivity(new Intent(this, ManutencaoListActivity.class)));
        cardConsumo.setOnClickListener(v ->
                startActivity(new Intent(this, ConsumoListActivity.class)));
        cardAlertas.setOnClickListener(v ->
                startActivity(new Intent(this, AlertasActivity.class)));
        cardAtividades.setOnClickListener(v ->
                startActivity(new Intent(this, AtividadeCampoFormActivity.class)));
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                // já está no dashboard
                return true;
            } else if (id == R.id.nav_equipamentos) {
                startActivity(new Intent(this, EquipamentoListActivity.class));
            } else if (id == R.id.nav_manutencoes) {
                startActivity(new Intent(this, ManutencaoListActivity.class));
            } else if (id == R.id.nav_consumo) {
                startActivity(new Intent(this, ConsumoListActivity.class));
            } else if (id == R.id.nav_alertas) {
                startActivity(new Intent(this, AlertasActivity.class));
            }
            return true;
        });
    }

    private void carregarNomeUsuario() {
        String uid = firebaseHelper.getCurrentUserId();
        if (uid == null) return;

        firebaseHelper.getUsuariosRef().document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String nome = doc.getString("nome");
                        if (nome != null && tvNomeUsuario != null) {
                            tvNomeUsuario.setText("Olá, " + nome.split(" ")[0] + "!");
                        }
                    }
                });
    }

    private void carregarDashboard() {
        // Total de equipamentos
        firebaseHelper.getEquipamentosRef()
                .whereEqualTo("status", "ATIVO")
                .get()
                .addOnSuccessListener(snap -> {
                    if (tvTotalEquipamentos != null)
                        tvTotalEquipamentos.setText(String.valueOf(snap.size()));
                });

        // Manutenções abertas (pendente + em andamento)
        firebaseHelper.getManutencoesRef()
                .whereIn("status", java.util.Arrays.asList("PENDENTE", "EM_ANDAMENTO"))
                .get()
                .addOnSuccessListener(snap -> {
                    if (tvManutencoesAbertas != null)
                        tvManutencoesAbertas.setText(String.valueOf(snap.size()));
                });

        // Alertas não lidos
        firebaseHelper.getAlertasRef()
                .whereEqualTo("lido", false)
                .get()
                .addOnSuccessListener(snap -> {
                    if (tvAlertasAtivos != null)
                        tvAlertasAtivos.setText(String.valueOf(snap.size()));
                });

        // Consumo do mês atual
        int mes = com.smartsolution.maintenance.utils.DateUtils.getMesAtual();
        int ano = com.smartsolution.maintenance.utils.DateUtils.getAnoAtual();

        firebaseHelper.getConsumosRef()
                .whereEqualTo("tipo", "AGUA")
                .whereEqualTo("mes", mes)
                .whereEqualTo("ano", ano)
                .get()
                .addOnSuccessListener(snap -> {
                    double total = 0;
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snap) {
                        Double v = doc.getDouble("valor");
                        if (v != null) total += v;
                    }
                    if (tvConsumoAgua != null)
                        tvConsumoAgua.setText(String.format("%.0f m³", total));
                });

        firebaseHelper.getConsumosRef()
                .whereEqualTo("tipo", "ENERGIA")
                .whereEqualTo("mes", mes)
                .whereEqualTo("ano", ano)
                .get()
                .addOnSuccessListener(snap -> {
                    double total = 0;
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snap) {
                        Double v = doc.getDouble("valor");
                        if (v != null) total += v;
                    }
                    if (tvConsumoEnergia != null)
                        tvConsumoEnergia.setText(String.format("%.0f kWh", total));
                });
    }

    private void observarAlertas() {
        // Badge no botão de alertas do bottom nav
        firebaseHelper.getAlertasRef()
                .whereEqualTo("lido", false)
                .addSnapshotListener((snap, e) -> {
                    if (snap != null) {
                        int count = snap.size();
                        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_alertas);
                        if (count > 0) {
                            badge.setVisible(true);
                            badge.setNumber(count);
                        } else {
                            badge.setVisible(false);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            firebaseHelper.getAuth().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
            return true;
        }
        if (item.getItemId() == R.id.action_perfil) {
            // Abrir perfil
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}