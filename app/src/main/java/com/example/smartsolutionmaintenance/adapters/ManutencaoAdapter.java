package com.example.smartsolutionmaintenance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.models.Manutencao;
import java.util.List;

public class ManutencaoAdapter extends RecyclerView.Adapter<ManutencaoAdapter.ViewHolder> {

    private List<Manutencao> lista;
    private Context context;

    public ManutencaoAdapter(List<Manutencao> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manutencao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Manutencao m = lista.get(position);

        holder.tvTitulo.setText(m.getTitulo() != null ? m.getTitulo() : "Sem título");
        holder.tvEquipamento.setText(m.getEquipamento() != null ? m.getEquipamento() : "Equipamento não informado");
        holder.tvResponsavel.setText(m.getResponsavel() != null ? "Resp.: " + m.getResponsavel() : "Responsável não definido");
        holder.tvTipo.setText(m.getTipo() != null ? m.getTipo() : "");

        // Prioridade
        String prioridade = m.getPrioridade() != null ? m.getPrioridade() : "media";
        int prioridadeCor;
        String prioridadeText;
        switch (prioridade) {
            case "alta":
                prioridadeCor = context.getResources().getColor(R.color.prioridade_alta);
                prioridadeText = "ALTA";
                break;
            case "baixa":
                prioridadeCor = context.getResources().getColor(R.color.prioridade_baixa);
                prioridadeText = "BAIXA";
                break;
            default:
                prioridadeCor = context.getResources().getColor(R.color.prioridade_media);
                prioridadeText = "MÉDIA";
        }
        holder.tvPrioridade.setText(prioridadeText);
        holder.tvPrioridade.setTextColor(prioridadeCor);
        holder.viewPrioridade.setBackgroundColor(prioridadeCor);

        // Status
        String status = m.getStatus() != null ? m.getStatus() : "pendente";
        int statusCor;
        String statusText;
        switch (status) {
            case "em_andamento":
                statusCor = context.getResources().getColor(R.color.amarelo);
                statusText = "Em Andamento";
                break;
            case "concluido":
                statusCor = context.getResources().getColor(R.color.verde);
                statusText = "Concluído";
                break;
            default:
                statusCor = context.getResources().getColor(R.color.vermelho);
                statusText = "Pendente";
        }
        holder.tvStatus.setText(statusText);
        holder.tvStatus.setTextColor(context.getResources().getColor(R.color.branco));
        holder.tvStatus.setBackgroundColor(statusCor);

        if (m.getPrazo() != null && !m.getPrazo().isEmpty()) {
            holder.tvPrazo.setVisibility(View.VISIBLE);
            holder.tvPrazo.setText("Prazo: " + m.getPrazo());
        } else {
            holder.tvPrazo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvEquipamento, tvResponsavel, tvPrioridade;
        TextView tvStatus, tvTipo, tvPrazo;
        View viewPrioridade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvEquipamento = itemView.findViewById(R.id.tvEquipamento);
            tvResponsavel = itemView.findViewById(R.id.tvResponsavel);
            tvPrioridade = itemView.findViewById(R.id.tvPrioridade);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvPrazo = itemView.findViewById(R.id.tvPrazo);
            viewPrioridade = itemView.findViewById(R.id.viewPrioridade);
        }
    }
}
