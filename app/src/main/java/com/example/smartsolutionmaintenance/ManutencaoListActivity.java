package com.example.smartsolutionmaintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.adapters.ManutencaoAdapter;
import com.smartsolution.maintenance.models.Manutencao;
import com.smartsolution.maintenance.utils.FirebaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoListActivity extends AppCompatActivity implements ManutencaoAdapter.OnManutencaoClickListener {

    private RecyclerView recyclerView;
    private ManutencaoAdapter adapter;
    private List<Manutencao> manutencaoList;
    private List<Manutencao> filteredList;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvVazio;
    private Spinner spinnerFiltroStatus, spinnerFiltroPrioridade;
    private FirebaseHelper firebaseHelper;

    private String filtroStatus = "TODOS";
    private String filtroPrioridade = "TODOS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao_list);

        firebaseHelper = FirebaseHelper.getInstance();
        manutencaoList = new ArrayList<>();
        filteredList = new ArrayList<>();

        setupToolbar();
        initViews();
        carregarManutencoes();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manutenções");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        tvVazio = findViewById(R.id.tv_vazio);
        spinnerFiltroStatus = findViewById(R.id.spinner_filtro_status);
        spinnerFiltroPrioridade = findViewById(R.id.spinner_filtro_prioridade);

        FloatingActionButton fabAdicionar = findViewById(R.id.fab_adicionar);
        fabAdicionar.setOnClickListener(v ->
                startActivity(new Intent(this, ManutencaoFormActivity.class)));

        adapter = new ManutencaoAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::carregarManutencoes);

        setupSpinners();
    }

    private void setupSpinners() {
        String[] statusOptions = {"Todos", "Pendente", "Em Andamento", "Concluída", "Cancelada"};
        String[] statusValues = {"TODOS", "PENDENTE", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"};
        String[] prioridadeOptions = {"Todas", "Alta", "Média", "Baixa"};
        String[] prioridadeValues = {"TODOS", "ALTA", "MEDIA", "BAIXA"};

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroStatus.setAdapter(statusAdapter);

        spinnerFiltroStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                filtroStatus = statusValues[pos];
                aplicarFiltros();
            }
            @Override
            public void onNothingSelected(AdapterView<?> p) {}
        });

        ArrayAdapter<String> prioridadeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, prioridadeOptions);
        prioridadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroPrioridade.setAdapter(prioridadeAdapter);

        spinnerFiltroPrioridade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                filtroPrioridade = prioridadeValues[pos];
                aplicarFiltros();
            }
            @Override
            public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void carregarManutencoes() {
        swipeRefresh.setRefreshing(true);
        firebaseHelper.getManutencoesRef()
                .orderBy("dataSolicitacao", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    manutencaoList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snap) {
                        Manutencao m = doc.toObject(Manutencao.class);
                        m.setId(doc.getId());
                        manutencaoList.add(m);
                    }
                    aplicarFiltros();
                    swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> swipeRefresh.setRefreshing(false));
    }

    private void aplicarFiltros() {
        filteredList.clear();
        for (Manutencao m : manutencaoList) {
            boolean passaStatus = filtroStatus.equals("TODOS") || m.getStatus().equals(filtroStatus);
            boolean passaPrioridade = filtroPrioridade.equals("TODOS") || m.getPrioridade().equals(filtroPrioridade);
            if (passaStatus && passaPrioridade) {
                filteredList.add(m);
            }
        }
        adapter.notifyDataSetChanged();
        tvVazio.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onManutencaoClick(Manutencao manutencao) {
        Intent intent = new Intent(this, ManutencaoFormActivity.class);
        intent.putExtra("manutencao_id", manutencao.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarManutencoes();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
