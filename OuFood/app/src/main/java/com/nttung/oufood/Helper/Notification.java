package com.nttung.oufood.Helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Activity.HomeActivity;
import com.nttung.oufood.Class.Request;
import com.nttung.oufood.Class.User;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import io.paperdb.Paper;

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, HomeActivity.class);
        String idRequest = intent.getStringExtra("idRequest");
        intent1.putExtra("flag", "notification");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent1);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(Common.NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Common.CHANNEL_CART)
                .setContentTitle("Thông báo")
                .setContentText("Đơn hàng của bạn đã được giao thành công")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Đơn hàng của bạn đã được giao thành công"))
                .setSmallIcon(R.drawable.notifications_ic)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Common.CURRENTUSER != null) {
                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Request request = snapshot.getValue(Request.class);
                        if (request.getIdCurrentUser().equals(Common.CURRENTUSER.getIdUser())) {
                            if (request.getStatus().equals("0")) {
                                notificationManager.notify(Common.NOTIFICATION_ID, builder.build());
                                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).child("status").setValue("1");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Paper.init(context);
                String user = Paper.book().read(Common.USERNAME_KEY);
                String password = Paper.book().read(Common.PASSWORD_KEY);
                if (user != null && password != null) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(user, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Request request = snapshot.getValue(Request.class);

                                    if (request.getIdCurrentUser().equals(userUid)) {
                                        if (request.getStatus().equals("0")) {
                                            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User user = snapshot.getValue(User.class);
                                                    Common.CURRENTUSER = user;
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                            notificationManager.notify(Common.NOTIFICATION_ID, builder.build());
                                            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).child("status").setValue("1");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }
        }
    }

}