package com.example.smartsolutionmaintenance.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartsolutionmaintenance.R;
import com.example.smartsolutionmaintenance.models.Alerta;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AlertaAdapter extends RecyclerView.Adapter<AlertaAdapter.ViewHolder> {

    private List<Alerta> lista;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public AlertaAdapter(List<Alerta> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alerta a = lista.get(position);

        holder.tvTitulo.setText(a.getTitulo() != null ? a.getTitulo() : "Alerta");
        holder.tvDescricao.setText(a.getDescricao() != null ? a.getDescricao() : "");
        holder.tvSetor.setText(a.getSetor() != null ? a.getSetor() : "Setor não informado");

        if (a.getDataHora() != null) {
            holder.tvData.setText(sdf.format(a.getDataHora().toDate()));
        }

        // Status
        boolean ativo = "ativo".equals(a.getStatus());
        holder.tvStatus.setText(ativo ? "Ativo" : "Resolvido");
        holder.tvStatus.setTextColor(ativo
                ? context.getResources().getColor(R.color.vermelho)
                : context.getResources().getColor(R.color.verde));

        holder.btnResolver.setVisibility(ativo ? View.VISIBLE : View.GONE);
        holder.btnResolver.setOnClickListener(v -> {

        });

        // Prioridade cor
        String prioridade = a.getPrioridade() != null ? a.getPrioridade() : "media";
        int cor;
        switch (prioridade) {
            case "alta": cor = context.getResources().getColor(R.color.vermelho_light); break;
            case "baixa": cor = context.getResources().getColor(R.color.verde_light); break;
            default: cor = context.getResources().getColor(R.color.amarelo_light);
        }
        holder.itemView.setBackgroundColor(cor);
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescricao, tvSetor, tvData, tvStatus;
        Button btnResolver;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvSetor = itemView.findViewById(R.id.tvSetor);
            tvData = itemView.findViewById(R.id.tvData);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
