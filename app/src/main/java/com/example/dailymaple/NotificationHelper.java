package com.example.dailymaple;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

// Constant Import
import static com.example.dailymaple.Constants.COIN_NAME;
import static com.example.dailymaple.Constants.COIN_NOTIFICATION_CODE;
import static com.example.dailymaple.Constants.NOTIFICATION_CHANNEL_ID;
import static com.example.dailymaple.Constants.URSUS_NAME;
import static com.example.dailymaple.Constants.URSUS_NOTIFICATION_CODE;

public class NotificationHelper {
    private Context mContext;

    // initialize
    NotificationHelper(Context context) {
        mContext = context;
    }

    public void createNotification(String workName) {
        // Notification 클릭시 실행
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); // 대기열에 이미 있다면 MainActivity가 아닌 앱 활성화
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notificatoin을 이루는 공통 부분 정의
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.maplestory_mushroom_icon) // Set Image
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true); // Remove Notification

        if (workName.equals(URSUS_NAME)) { // Ursus Event Notification
            // Notification 클릭 시 동작할 Intent 입력, 중복 방지를 위해 FLAG_CANCEL_CURRENT로 설정, CODE를 다르게하면 개별 생성
            // Code가 같으면 같은 알림으로 인식하여 갱신작업 진행
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, URSUS_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            // Notification 제목, 컨텐츠 설정
            notificationBuilder.setContentTitle("아 맞다 우르스!!").setContentText("우르스 골든타임이 1시간 남았어요!")
                    .setContentIntent(pendingIntent);

            if (notificationManager != null) {
                notificationManager.notify(URSUS_NOTIFICATION_CODE, notificationBuilder.build());
            }
        } else if (workName.equals(COIN_NAME)) { // Coin Event Notification
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, COIN_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            notificationBuilder.setContentTitle("아 맞다 코인!!").setContentText("코인은 다 캐셨나요?")
                    .setContentIntent(pendingIntent);

            if (notificationManager != null) {
                notificationManager.notify(COIN_NOTIFICATION_CODE, notificationBuilder.build());
            }
        }
    }

    public static void createNotificationChannel(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // NotificationChannel 초기화
                NotificationChannel notificationChannel = new NotificationChannel("Notification Test", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel
                notificationChannel.setDescription("푸시알림");
                notificationChannel.enableLights(true); // 화면활성화 설정
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500}); // 진동패턴 설정
                notificationChannel.enableVibration(true); // 진동 설정
                notificationManager.createNotificationChannel(notificationChannel); // channel 생성
            }
        } catch (NullPointerException nullException) {
            // notificationManager null 오류 raise
            Toast.makeText(context, "푸시 알림 채널 생성에 실패했습니다. 앱을 재실행하거나 재설치해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }
}
