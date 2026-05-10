package com.example.smartsolutionmaintenance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.smartsolutionmaintenance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        // Inicia o Dashboard como tela principal
      if (savedInstanceState == null) {
          startActivity(new Intent(this, DashboardActivity.class));
      }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_consumo) {
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



}
