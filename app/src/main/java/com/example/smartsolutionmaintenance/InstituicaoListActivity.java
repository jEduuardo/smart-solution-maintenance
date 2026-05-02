package com.example.smartsolutionmaintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartsolution.maintenance.R;
import com.smartsolution.maintenance.adapters.InstituicaoAdapter;
import com.smartsolution.maintenance.models.Instituicao;
import com.smartsolution.maintenance.utils.FirebaseHelper;
import java.util.ArrayList;
import java.util.List;

public class InstituicaoListActivity extends AppCompatActivity implements InstituicaoAdapter.OnInstituicaoClickListener {

    private RecyclerView recyclerView;
    private InstituicaoAdapter adapter;
    private List<Instituicao> instituicaoList;
    private List<Instituicao> filteredList;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvVazio;
    private FloatingActionButton fabAdicionar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_generic);

        firebaseHelper = FirebaseHelper.getInstance();
        instituicaoList = new ArrayList<>();
        filteredList = new ArrayList<>();

        setupToolbar();
        initViews();
        carregarInstituicoes();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Instituições");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        tvVazio = findViewById(R.id.tv_vazio);
        fabAdicionar = findViewById(R.id.fab_adicionar);

        adapter = new InstituicaoAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdicionar.setOnClickListener(v ->
                startActivity(new Intent(this, InstituicaoFormActivity.class)));

        swipeRefresh.setOnRefreshListener(this::carregarInstituicoes);

        // SearchView
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return true;
            }
        });
    }

    private void carregarInstituicoes() {
        swipeRefresh.setRefreshing(true);
        firebaseHelper.getInstituicoesRef()
                .whereEqualTo("ativo", true)
                .orderBy("nome")
                .get()
                .addOnSuccessListener(snap -> {
                    instituicaoList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snap) {
                        Instituicao inst = doc.toObject(Instituicao.class);
                        inst.setId(doc.getId());
                        instituicaoList.add(inst);
                    }
                    filteredList.clear();
                    filteredList.addAll(instituicaoList);
                    adapter.notifyDataSetChanged();
                    tvVazio.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
                    swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> swipeRefresh.setRefreshing(false));
    }

    private void filtrar(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(instituicaoList);
        } else {
            String lower = query.toLowerCase();
            for (Instituicao inst : instituicaoList) {
                if (inst.getNome().toLowerCase().contains(lower) ||
                        (inst.getCidade() != null && inst.getCidade().toLowerCase().contains(lower))) {
                    filteredList.add(inst);
                }
            }
        }
        adapter.notifyDataSetChanged();
        tvVazio.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onInstituicaoClick(Instituicao instituicao) {
        Intent intent = new Intent(this, InstituicaoFormActivity.class);
        intent.putExtra("instituicao_id", instituicao.getId());
        startActivity(intent);
    }

    @Override
    public void onInstituicaoSetoresClick(Instituicao instituicao) {
        Intent intent = new Intent(this, SetorFormActivity.class);
        intent.putExtra("instituicao_id", instituicao.getId());
        intent.putExtra("instituicao_nome", instituicao.getNome());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarInstituicoes();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
