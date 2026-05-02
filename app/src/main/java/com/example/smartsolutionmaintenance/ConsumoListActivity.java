package com.example.smartsolutionmaintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.adapters.ConsumoAdapter;
import com.smartsolution.maintenance.models.Consumo;
import com.smartsolution.maintenance.utils.DateUtils;
import com.smartsolution.maintenance.utils.FirebaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ConsumoListActivity extends AppCompatActivity implements ConsumoAdapter.OnConsumoClickListener {

    private RecyclerView recyclerView;
    private ConsumoAdapter adapter;
    private List<Consumo> consumoList;
    private List<Consumo> filteredList;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvVazio;
    private BarChart barChart;
    private TabLayout tabTipo;
    private AutoCompleteTextView spinnerAno;
    private FirebaseHelper firebaseHelper;

    private String filtroTipo = "AGUA";
    private int filtroAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_list);

        firebaseHelper = FirebaseHelper.getInstance();
        consumoList = new ArrayList<>();
        filteredList = new ArrayList<>();
        filtroAno = DateUtils.getAnoAtual();

        setupToolbar();
        initViews();
        carregarConsumos();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Monitoramento de Consumo");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        tvVazio = findViewById(R.id.tv_vazio);
        barChart = findViewById(R.id.bar_chart);
        tabTipo = findViewById(R.id.tab_tipo);
        spinnerAno = findViewById(R.id.spinner_ano);

        FloatingActionButton fab = findViewById(R.id.fab_adicionar);
        fab.setOnClickListener(v -> startActivity(new Intent(this, ConsumoFormActivity.class)));

        adapter = new ConsumoAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::carregarConsumos);

        // Tab seleção tipo
        tabTipo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                filtroTipo = tab.getPosition() == 0 ? "AGUA" : "ENERGIA";
                aplicarFiltros();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Seleção ano
        String[] anos = {String.valueOf(filtroAno - 1), String.valueOf(filtroAno), String.valueOf(filtroAno + 1)};
        spinnerAno.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, anos));
        spinnerAno.setText(String.valueOf(filtroAno), false);
        spinnerAno.setOnItemClickListener((parent, view, position, id) -> {
            filtroAno = Integer.parseInt(anos[position]);
            carregarConsumos();
        });

        setupChart();
    }

    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(false);

        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(meses));
        barChart.getXAxis().setGranularity(1f);
    }

    private void carregarConsumos() {
        swipeRefresh.setRefreshing(true);
        firebaseHelper.getConsumosRef()
                .whereEqualTo("ano", filtroAno)
                .orderBy("mes")
                .get()
                .addOnSuccessListener(snap -> {
                    consumoList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snap) {
                        Consumo c = doc.toObject(Consumo.class);
                        c.setId(doc.getId());
                        consumoList.add(c);
                    }
                    aplicarFiltros();
                    swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> swipeRefresh.setRefreshing(false));
    }

    private void aplicarFiltros() {
        filteredList.clear();
        List<BarEntry> chartEntries = new ArrayList<>();

        for (Consumo c : consumoList) {
            if (c.getTipo().equals(filtroTipo)) {
                filteredList.add(c);
                chartEntries.add(new BarEntry(c.getMes() - 1, (float) c.getValor()));
            }
        }

        adapter.notifyDataSetChanged();
        tvVazio.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);

        // Atualizar gráfico
        if (!chartEntries.isEmpty()) {
            int cor = filtroTipo.equals("AGUA") ?
                    getResources().getColor(R.color.chart_agua, null) :
                    getResources().getColor(R.color.chart_energia, null);

            BarDataSet dataSet = new BarDataSet(chartEntries,
                    filtroTipo.equals("AGUA") ? "Água (m³)" : "Energia (kWh)");
            dataSet.setColor(cor);

            barChart.setData(new BarData(dataSet));
            barChart.invalidate();
            barChart.setVisibility(View.VISIBLE);
        } else {
            barChart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConsumoClick(Consumo consumo) {
        Intent intent = new Intent(this, ConsumoFormActivity.class);
        intent.putExtra("consumo_id", consumo.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarConsumos();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
