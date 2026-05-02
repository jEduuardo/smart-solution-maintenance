package utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.smartsolution.maintenance.models.Alerta;
import com.smartsolution.maintenance.models.Consumo;
import com.smartsolution.maintenance.models.Equipamento;
import com.smartsolution.maintenance.models.Manutencao;

public class AlertaManager {

    private static AlertaManager instance;
    private final FirebaseHelper firebaseHelper;

    private AlertaManager() {
        firebaseHelper = FirebaseHelper.getInstance();
    }

    public static AlertaManager getInstance() {
        if (instance == null) {
            instance = new AlertaManager();
        }
        return instance;
    }

    // Gera alerta de anomalia de consumo
    public void gerarAlertaConsumo(Consumo consumo) {
        if (!consumo.isAnomalia()) return;

        String tipoStr = consumo.getTipo().equals(Consumo.TIPO_AGUA) ? "Água" : "Energia";
        double variacao = consumo.getVariacaoPercentual();

        String titulo = "Anomalia de Consumo de " + tipoStr + " Detectada";
        String mensagem = String.format(
                "Consumo de %s em %s/%d está %.1f%% acima do esperado. Valor: %.2f %s.",
                tipoStr,
                DateUtils.getMesNome(consumo.getMes()),
                consumo.getAno(),
                variacao,
                consumo.getValor(),
                consumo.getUnidade()
        );

        Alerta alerta = new Alerta(
                Alerta.TIPO_ANOMALIA_CONSUMO,
                titulo,
                mensagem,
                variacao > 50 ? Alerta.TIPO_ANOMALIA_CONSUMO : Manutencao.PRIORIDADE_MEDIA,
                consumo.getId(),
                "CONSUMO",
                consumo.getInstituicaoId()
        );
        alerta.setPrioridade(variacao > 50 ? "ALTA" : "MEDIA");

        salvarAlerta(alerta);
    }

    // Gera alerta de manutenção próxima
    public void gerarAlertaManutencaoProxima(Equipamento equipamento) {
        long diasRestantes = DateUtils.getDiasParaVencimento(equipamento.getProximaManutencao());
        if (diasRestantes > 7) return;

        String titulo = "Manutenção Preventiva Próxima";
        String mensagem = String.format(
                "O equipamento '%s' precisa de manutenção preventiva em %d dias.",
                equipamento.getNome(),
                diasRestantes
        );

        Alerta alerta = new Alerta(
                Alerta.TIPO_MANUTENCAO_PROXIMA,
                titulo,
                mensagem,
                diasRestantes <= 1 ? "ALTA" : diasRestantes <= 3 ? "MEDIA" : "BAIXA",
                equipamento.getId(),
                "EQUIPAMENTO",
                equipamento.getInstituicaoId()
        );

        salvarAlerta(alerta);
    }

    // Gera alerta de equipamento em estado crítico
    public void gerarAlertaEquipamentoCritico(Equipamento equipamento, String motivo) {
        String titulo = "Equipamento em Estado Crítico";
        String mensagem = String.format(
                "O equipamento '%s' requer atenção imediata. Motivo: %s",
                equipamento.getNome(),
                motivo
        );

        Alerta alerta = new Alerta(
                Alerta.TIPO_EQUIPAMENTO_CRITICO,
                titulo,
                mensagem,
                "ALTA",
                equipamento.getId(),
                "EQUIPAMENTO",
                equipamento.getInstituicaoId()
        );

        salvarAlerta(alerta);
    }

    private void salvarAlerta(Alerta alerta) {
        firebaseHelper.getAlertasRef()
                .add(alerta)
                .addOnSuccessListener(ref -> alerta.setId(ref.getId()))
                .addOnFailureListener(e -> {
                    // Log error silently
                });
    }

    public void marcarComoLido(String alertaId) {
        firebaseHelper.getAlertasRef()
                .document(alertaId)
                .update(
                        "lido", true,
                        "status", Alerta.STATUS_LIDO,
                        "leituraAt", com.google.firebase.Timestamp.now()
                );
    }

    public void marcarComoResolvido(String alertaId) {
        firebaseHelper.getAlertasRef()
                .document(alertaId)
                .update("status", Alerta.STATUS_RESOLVIDO);
    }
}