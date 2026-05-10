package com.example.smartsolutionmaintenance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.models.Instituicao;
import java.util.List;

public class InstituicaoAdapter extends RecyclerView.Adapter<InstituicaoAdapter.ViewHolder> {

    private List<Instituicao> lista;

    public InstituicaoAdapter(List<Instituicao> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instituicao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instituicao inst = lista.get(position);
        holder.tvNome.setText(inst.getNome() != null ? inst.getNome() : "Sem nome");
        holder.tvEndereco.setText(inst.getEndereco() != null ? inst.getEndereco() : "Endereço não informado");
        holder.tvResponsavel.setText(inst.getResponsavel() != null ? "Resp.: " + inst.getResponsavel() : "");
        holder.tvTelefone.setText(inst.getTelefone() != null ? inst.getTelefone() : "");
        if (inst.getSetores() != null && !inst.getSetores().isEmpty()) {
            holder.tvSetores.setVisibility(View.VISIBLE);
            holder.tvSetores.setText("Setores: " + inst.getSetores());
        } else {
            holder.tvSetores.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvEndereco, tvResponsavel, tvTelefone, tvSetores;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvEndereco = itemView.findViewById(R.id.tvEndereco);
            tvResponsavel = itemView.findViewById(R.id.tvResponsavel);
            tvTelefone = itemView.findViewById(R.id.tvTelefone);
            tvSetores = itemView.findViewById(R.id.tvSetores);
        }
    }
}
