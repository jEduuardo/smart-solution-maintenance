package com.example.smartsolutionmaintenance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.models.Consumo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConsumoAdapter extends RecyclerView.Adapter<ConsumoAdapter.ViewHolder> {

    private List<Consumo> lista;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public ConsumoAdapter(List<Consumo> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consumo c = lista.get(position);
        boolean isAgua = "agua".equals(c.getTipo());

        String unidade = isAgua ? "m³" : "kWh";
        holder.tvValor.setText(String.format("%.2f %s", c.getValor(), unidade));
        holder.tvSetor.setText(c.getSetor() != null ? c.getSetor() : "Setor não informado");
        holder.tvTipo.setText(isAgua ? "Água" : "Energia");
        holder.ivIcone.setImageResource(isAgua ? R.drawable.ic_water : R.drawable.ic_energy);

        if (c.getData() != null) {
            holder.tvData.setText(sdf.format(c.getData().toDate()));
        }

        if (c.getObservacao() != null && !c.getObservacao().isEmpty()) {
            holder.tvObservacao.setVisibility(View.VISIBLE);
            holder.tvObservacao.setText(c.getObservacao());
        } else {
            holder.tvObservacao.setVisibility(View.GONE);
        }

        // Cor de fundo baseada no tipo
        int bgColor = isAgua
                ? holder.itemView.getContext().getResources().getColor(R.color.azul_light)
                : holder.itemView.getContext().getResources().getColor(R.color.amarelo_light);
        holder.containerTipo.setBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvValor, tvSetor, tvTipo, tvData, tvObservacao;
        ImageView ivIcone;
        View containerTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvSetor = itemView.findViewById(R.id.tvSetor);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvData = itemView.findViewById(R.id.tvData);
            tvObservacao = itemView.findViewById(R.id.tvObservacao);
            ivIcone = itemView.findViewById(R.id.ivIcone);
            containerTipo = itemView.findViewById(R.id.containerTipo);
        }
    }
}
