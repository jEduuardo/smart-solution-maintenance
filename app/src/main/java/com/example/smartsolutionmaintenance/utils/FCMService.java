package com.example.smartsolutionmaintenance.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.smartsolutionmaintenance.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "apae_alertas";
    private static final String CHANNEL_NAME = "Alertas Smart Solution";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String titulo = "Smart Solution Maintenance";
        String mensagem = "Novo alerta recebido";

        if (remoteMessage.getNotification() != null) {
            titulo = remoteMessage.getNotification().getTitle();
            mensagem = remoteMessage.getNotification().getBody();
        } else if (remoteMessage.getData().size() > 0) {
            titulo = remoteMessage.getData().getOrDefault("titulo", titulo);
            mensagem = remoteMessage.getData().getOrDefault("mensagem", mensagem);
        }

        exibirNotificacao(titulo, mensagem);
    }

    private void exibirNotificacao(String titulo, String mensagem) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Aqui você pode salvar o token no Firestore vinculado ao usuário
    }
}
