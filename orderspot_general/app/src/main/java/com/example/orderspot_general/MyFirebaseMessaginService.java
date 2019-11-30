package com.example.orderspot_general;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.estimote.sdk.internal.utils.L;
import com.example.orderspot_general.Main.MainActivity;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessaginService extends FirebaseMessagingService{
    private static final String TAG = "FCM";

    //푸시 알림 설정
    private String tittle = "";
    private String body = "";

    public MyFirebaseMessaginService(){

    }
    //새로운 토큰을 확인했을때 호출되는 메소드.
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        //토큰 정보를 출력합니다.
        Log.e(TAG, "onNewToken 호출됨: " + token);
        //서버로 보내고싶을때
        sendRegistrationServer(token);
    }

    // 새로운 메시지를 받았을 때 호출되는 메소드.
    // 이부분은 메시지를 받았을때 처리르 하는 함수
    // 그리고 메시지를 받았을떄 sendNotification을 호출
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 일단 받은 데이터 중, 내용만 가지고와 출력하는 메소드.
        String from = remoteMessage.getFrom();
        Log.d(TAG,"From" + from);

        //푸쉬 알림 메시지 분기
        //putDate를 사용했을 때 data 가져오기
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG,"Message data payload: " + remoteMessage.getData());

            tittle = remoteMessage.getNotification().getTitle();
            body =remoteMessage.getNotification().getBody();
            Log.e("확인용",tittle+"\n"+body);
            if(/*데이처 필요 긴 러닝 잡*/true){
                scheduleJob();
            }else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        //Notification을 사용했을때 data 가져오기
        if(remoteMessage.getNotification() != null) {
            Log.d(TAG,"Message Notification Body: "+ remoteMessage.getNotification().getTitle());
            Log.d(TAG,"Message Notification Body: "+ remoteMessage.getNotification().getBody());
            sendNotification();

        }

    }

    public void scheduleJob() {
        // start dispatch_job
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder().setService(MyJobService.class)
                .setTag("my-job-tag").build();
        dispatcher.schedule(myJob);

    }

    private void handleNow() {
        Log.d(TAG,"short lived task is done");
    }

    private void sendRegistrationServer(String token) {
        // TODO: Implement this method to send token to your app server
    }

    // 푸시메시지의 세부 설정과 안드로이드에 푸시 알림을 보내는 함수.
    private  void sendNotification() {
        Intent intent = new Intent(this, ConfirmActivity.class);   // 알림바 클릭시 넘어감

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /*Request code*/,
                intent,PendingIntent.FLAG_ONE_SHOT);
        String channelId = "채널 ID";
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.im) // 아이콘 설정
                .setContentTitle(tittle)    //제목설정
                .setContentText(body)   //내용 설정
                .setAutoCancel(true)    //사용자가 탭을 클릭하면 제거
                .setVibrate(new long[] { 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

        //알림 표시
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Since android Oreo notification channel is needed.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable tittle",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /*정의해야하는 각 알림의 고유한 int값 */,notificationBuilder.build());
    }

}
