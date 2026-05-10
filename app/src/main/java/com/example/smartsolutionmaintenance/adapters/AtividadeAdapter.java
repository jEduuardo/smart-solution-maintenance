package com.example.smartsolutionmaintenance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.models.Atividade;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {

    private List<Atividade> lista;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public AtividadeAdapter(List<Atividade> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_atividade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Atividade a = lista.get(position);
        holder.tvDescricao.setText(a.getDescricao() != null ? a.getDescricao() : "");
        holder.tvLocal.setText(a.getLocal() != null ? a.getLocal() : "Local não informado");
        holder.tvTipo.setText(a.getTipo() != null ? a.getTipo() : "");
        holder.tvUsuario.setText(a.getUserEmail() != null ? a.getUserEmail() : "");
        if (a.getDataHora() != null) {
            holder.tvData.setText(sdf.format(a.getDataHora().toDate()));
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescricao, tvLocal, tvTipo, tvUsuario, tvData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvLocal = itemView.findViewById(R.id.tvLocal);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvData = itemView.findViewById(R.id.tvData);
        }
    }
}
