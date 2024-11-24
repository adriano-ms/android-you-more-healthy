package com.edu.fateczl.youmorehealthy.view;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.TaskType;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewUtils {

    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Notification channel";

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("pt", "BR"));
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", new Locale("pt", "BR"));

    public static void showToast(Context context, int msgId){
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    public static void showErrorDialog(Context context, int msgId){
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_error_title)
                .setMessage(msgId)
                .setNeutralButton(R.string.bt_ok, (d, i) -> d.dismiss())
                .show();
    }

    public static void changeFragment(FragmentManager fragmentManager, int viewIdToReplace, Fragment fragment, boolean addToBackStack){
        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(viewIdToReplace, fragment);
        if(addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showNotification(Context context, int id, int icon, String title, String content, Intent intent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        notificationManager.notify(id, buildNotification(context, icon, title, content, pendingIntent));
    }

    private static Notification buildNotification(Context context, int icon, String title, String content, PendingIntent pendingIntent){
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
    }

    public static void clickAnimation(View v, Runnable endAction){
        ViewPropertyAnimator animator = v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100);
        if(endAction != null)
            animator.withEndAction(endAction);
    }

    public static int chooseTaskIcon(TaskType type){
        switch (type) {
            case ROUTINE:
                return R.drawable.baseline_event_repeat_24;
            case SPORT:
                return R.drawable.baseline_sports_handball_24;
            case MEDICINE:
                return R.drawable.baseline_medication_24;
            case MEDIC:
                return R.drawable.baseline_medical_services_24;
            default:
                return R.drawable.baseline_event_24;
        }
    }
}
