package com.example.smartsolutionmaintenance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.models.Equipamento;
import java.util.List;

public class EquipamentoAdapter extends RecyclerView.Adapter<EquipamentoAdapter.ViewHolder> {

    private List<Equipamento> lista;

    public EquipamentoAdapter(List<Equipamento> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Equipamento e = lista.get(position);
        Context ctx = holder.itemView.getContext();

        holder.tvNome.setText(e.getNome() != null ? e.getNome() : "Sem nome");
        holder.tvCategoria.setText(e.getCategoria() != null ? e.getCategoria() : "Sem categoria");
        holder.tvSetor.setText(e.getSetor() != null ? e.getSetor() : "Setor não informado");
        holder.tvModelo.setText(e.getModelo() != null ? "Modelo: " + e.getModelo() : "");

        // Status
        String status = e.getStatus() != null ? e.getStatus() : "ativo";
        String statusDisplay;
        int statusCor;
        switch (status) {
            case "inativo":
                statusDisplay = "Inativo";
                statusCor = ctx.getResources().getColor(R.color.cinza);
                break;
            case "em_manutencao":
                statusDisplay = "Em Manutenção";
                statusCor = ctx.getResources().getColor(R.color.amarelo);
                break;
            case "descartado":
                statusDisplay = "Descartado";
                statusCor = ctx.getResources().getColor(R.color.vermelho);
                break;
            default:
                statusDisplay = "Ativo";
                statusCor = ctx.getResources().getColor(R.color.verde);
        }
        holder.tvStatus.setText(statusDisplay);
        holder.tvStatus.setTextColor(statusCor);

        if (e.getUltimaManutencao() != null && !e.getUltimaManutencao().isEmpty()) {
            holder.tvUltimaManutencao.setVisibility(View.VISIBLE);
            holder.tvUltimaManutencao.setText("Última manutenção: " + e.getUltimaManutencao());
        } else {
            holder.tvUltimaManutencao.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCategoria, tvSetor, tvStatus, tvModelo, tvUltimaManutencao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvSetor = itemView.findViewById(R.id.tvSetor);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvUltimaManutencao = itemView.findViewById(R.id.tvUltimaManutencao);
        }
    }
}
