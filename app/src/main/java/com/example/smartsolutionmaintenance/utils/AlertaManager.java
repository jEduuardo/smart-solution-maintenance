package com.example.smartsolutionmaintenance.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class AlertaManager {

    // Limites de consumo para alertas (podem ser ajustados)
    private static final double LIMITE_AGUA_M3 = 50.0;
    private static final double LIMITE_ENERGIA_KWH = 500.0;
    private static final double VARIACAO_ANOMALIA_PERCENT = 30.0; // 30% acima da média

    public static void verificarAnomaliaConsumo(FirebaseFirestore db, String tipo, double valorAtual, String setor) {
        // Buscar média dos últimos 5 registros para comparar
        db.collection("consumo")
                .whereEqualTo("tipo", tipo)
                .whereEqualTo("setor", setor)
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) return;

                    double soma = 0;
                    int count = 0;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Double v = doc.getDouble("valor");
                        if (v != null) {
                            soma += v;
                            count++;
                        }
                    }

                    if (count == 0) return;
                    double media = soma / count;
                    double limite = tipo.equals("agua") ? LIMITE_AGUA_M3 : LIMITE_ENERGIA_KWH;

                    boolean anomalia = false;
                    String motivo = "";

                    if (valorAtual > limite) {
                        anomalia = true;
                        motivo = String.format("Consumo acima do limite: %.1f (limite: %.1f)", valorAtual, limite);
                    } else if (media > 0 && valorAtual > media * (1 + VARIACAO_ANOMALIA_PERCENT / 100)) {
                        anomalia = true;
                        motivo = String.format("Variação acima de %.0f%% da média (%.1f vs %.1f)", VARIACAO_ANOMALIA_PERCENT, valorAtual, media);
                    }

                    if (anomalia) {
                        criarAlerta(db, tipo, setor, motivo, valorAtual);
                    }
                });
    }

    private static void criarAlerta(FirebaseFirestore db, String tipo, String setor, String motivo, double valor) {
        String tipoDisplay = tipo.equals("agua") ? "Água" : "Energia";
        String unidade = tipo.equals("agua") ? "m³" : "kWh";

        Map<String, Object> alerta = new HashMap<>();
        alerta.put("titulo", "Anomalia no consumo de " + tipoDisplay);
        alerta.put("descricao", motivo + String.format(" (%.2f %s)", valor, unidade));
        alerta.put("tipo", "consumo");
        alerta.put("status", "ativo");
        alerta.put("setor", setor);
        alerta.put("prioridade", "alta");
        alerta.put("dataHora", Timestamp.now());

        db.collection("alertas").add(alerta);
    }
}
