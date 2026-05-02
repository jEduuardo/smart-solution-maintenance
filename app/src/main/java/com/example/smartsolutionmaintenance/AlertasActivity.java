package com.example.smartsolutionmaintenance;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.adapters.AlertaAdapter;
import com.smartsolution.maintenance.models.Alerta;
import com.smartsolution.maintenance.utils.AlertaManager;
import com.smartsolution.maintenance.utils.FirebaseHelper;
import java.util.ArrayList;
import java.util.List;

public class AlertasActivity extends AppCompatActivity implements AlertaAdapter.OnAlertaClickListener {

    private RecyclerView recyclerView;
    private AlertaAdapter adapter;
    private List<Alerta> alertaList;
    private List<Alerta> filteredList;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvVazio, tvContadorAlertas;
    private ChipGroup chipGroupFiltro;
    private Chip chipTodos, chipNovos, chipAlta;
    private FirebaseHelper firebaseHelper;
    private AlertaManager alertaManager;

    private String filtroAtivo = "TODOS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);

        firebaseHelper = FirebaseHelper.getInstance();
        alertaManager = AlertaManager.getInstance();
        alertaList = new ArrayList<>();
        filteredList = new ArrayList<>();

        setupToolbar();
        initViews();
        carregarAlertas();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Alertas Inteligentes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        tvVazio = findViewById(R.id.tv_vazio);
        tvContadorAlertas = findViewById(R.id.tv_contador_alertas);
        chipGroupFiltro = findViewById(R.id.chip_group_filtro);
        chipTodos = findViewById(R.id.chip_todos);
        chipNovos = findViewById(R.id.chip_novos);
        chipAlta = findViewById(R.id.chip_alta);

        adapter = new AlertaAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::carregarAlertas);

        chipGroupFiltro.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip_todos) filtroAtivo = "TODOS";
            else if (checkedId == R.id.chip_novos) filtroAtivo = "NOVOS";
            else if (checkedId == R.id.chip_alta) filtroAtivo = "ALTA";
            aplicarFiltros();
        });

        // Marcar todos como lidos
        findViewById(R.id.btn_marcar_todos_lidos).setOnClickListener(v -> marcarTodosLidos());
    }

    private void carregarAlertas() {
        swipeRefresh.setRefreshing(true);
        firebaseHelper.getAlertasRef()
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(snap -> {
                    alertaList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snap) {
                        Alerta a = doc.toObject(Alerta.class);
                        a.setId(doc.getId());
                        alertaList.add(a);
                    }
                    aplicarFiltros();
                    atualizarContador();
                    swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> swipeRefresh.setRefreshing(false));
    }

    private void aplicarFiltros() {
        filteredList.clear();
        for (Alerta a : alertaList) {
            boolean passa = true;
            switch (filtroAtivo) {
                case "NOVOS": passa = !a.isLido(); break;
                case "ALTA": passa = a.getPrioridade() != null && a.getPrioridade().equals("ALTA"); break;
            }
            if (passa) filteredList.add(a);
        }
        adapter.notifyDataSetChanged();
        tvVazio.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void atualizarContador() {
        long naoLidos = alertaList.stream().filter(a -> !a.isLido()).count();
        if (tvContadorAlertas != null) {
            tvContadorAlertas.setText(naoLidos > 0 ?
                    naoLidos + " alerta(s) não lido(s)" : "Todos os alertas lidos");
        }
    }

    private void marcarTodosLidos() {
        for (Alerta a : alertaList) {
            if (!a.isLido()) {
                alertaManager.marcarComoLido(a.getId());
                a.setLido(true);
            }
        }
        adapter.notifyDataSetChanged();
        atualizarContador();
    }

    @Override
    public void onAlertaClick(Alerta alerta) {
        alertaManager.marcarComoLido(alerta.getId());
        alerta.setLido(true);
        adapter.notifyDataSetChanged();
        atualizarContador();
    }

    @Override
    public void onAlertaResolvido(Alerta alerta) {
        alertaManager.marcarComoResolvido(alerta.getId());
        alertaList.remove(alerta);
        aplicarFiltros();
        atualizarContador();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAlertas();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}