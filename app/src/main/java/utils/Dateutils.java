package utils;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String FORMAT_DATETIME = "dd/MM/yyyy HH:mm";
    public static final String FORMAT_MONTH_YEAR = "MM/yyyy";

    private static final String[] MESES = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    public static String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "";
        return new SimpleDateFormat(FORMAT_DATE, new Locale("pt", "BR"))
                .format(timestamp.toDate());
    }

    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) return "";
        return new SimpleDateFormat(FORMAT_DATETIME, new Locale("pt", "BR"))
                .format(timestamp.toDate());
    }

    public static String getMesNome(int mes) {
        if (mes >= 1 && mes <= 12) return MESES[mes - 1];
        return "";
    }

    public static String getMesAno(int mes, int ano) {
        return getMesNome(mes) + "/" + ano;
    }

    public static String getTempoRelativo(Timestamp timestamp) {
        if (timestamp == null) return "";
        long diffMs = System.currentTimeMillis() - timestamp.toDate().getTime();
        long diffMin = diffMs / 60000;
        long diffHoras = diffMin / 60;
        long diffDias = diffHoras / 24;

        if (diffMin < 1) return "Agora";
        if (diffMin < 60) return diffMin + " min atrás";
        if (diffHoras < 24) return diffHoras + "h atrás";
        if (diffDias < 7) return diffDias + " dias atrás";
        return formatDate(timestamp);
    }

    public static int getMesAtual() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
    }

    public static int getAnoAtual() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    }

    public static Timestamp fromDate(Date date) {
        return new Timestamp(date);
    }

    public static long getDiasParaVencimento(Timestamp dataVencimento) {
        if (dataVencimento == null) return Long.MAX_VALUE;
        long diffMs = dataVencimento.toDate().getTime() - System.currentTimeMillis();
        return diffMs / (1000 * 60 * 60 * 24);
    }
}