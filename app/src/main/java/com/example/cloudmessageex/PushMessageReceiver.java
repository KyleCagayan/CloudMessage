package com.example.cloudmessageex;

import static com.pax.market.android.app.sdk.PushConstants.ACTION_DATA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFICATION_CLICK;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFICATION_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFY_DATA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MEIDA;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_CONTENT;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_DATA;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_NID;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_TITLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.market.android.app.sdk.dto.MediaMesageInfo;

public class PushMessageReceiver extends BroadcastReceiver {

    private static final String TAG = "CloudMessagingEx";
    private Handler handler = null;
    private TextView tv_message;
    private boolean defaultConstructorFlag = false;

    public PushMessageReceiver() {
        Log.d(TAG, "Broadcast receiver default constructor method called");
        defaultConstructorFlag = true;
    }

    public PushMessageReceiver(android.os.Handler handler) { //, TextView tv_message
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent2open = new Intent(context, MainActivity.class);
        intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        Log.i(TAG, "Cloud message related broadcast received!");
        if (ACTION_NOTIFY_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) { // 4.1 Mixed Message
            Log.i(TAG, "### NOTIFY_DATA_MESSAGE_RECEIVED 4.1");
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);

            intent2open.putExtra("message", "4.1");
            intent2open.putExtra("content", content);

            Log.d(TAG, "Notification title: " + title + "\nNotitication content: " + content + "\nData: " + dataJson);
            Log.d(TAG, "NOTIFY_DATA_MESSAGE_RECEIVED End");

        } else if (ACTION_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) { // 4.2
            Log.d(TAG, "### DATA_MESSAGE_RECEIVED 4.2");
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);

            intent2open.putExtra("message", "4.2");
            intent2open.putExtra("title", title);
            intent2open.putExtra("content", content);
            intent2open.putExtra("dataJson", dataJson);

            Log.d(TAG, "DATA_MESSAGE_RECEIVED Begin\nData:" + dataJson + "\nDATA_MESSAGE_RECEIVED End");
            Log.d(TAG, "NOTIFY_DATA_MESSAGE_RECEIVED End");

        } else if (ACTION_NOTIFICATION_MESSAGE_RECEIVED.equals(intent.getAction())) {  // 4.3
            //else if (ACTION_NOTIFICATION_MESSAGE_RECEIVED.equals(intent.getAction()))
            Log.d(TAG, "### NOTIFICATION_MESSAGE_RECEIVED Begin 4.3");
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);


            intent2open.putExtra("message", "4.3");
            intent2open.putExtra("title", title);
            intent2open.putExtra("content", content);
//            MainActivity.getInstance().updateOnNotif(title, content);

            Log.d(TAG, "Notification title:" + title + "\nNotification content:" + content);
            Log.d(TAG, "NOTIFICATION_MESSAGE_RECEIVED End");

        } else if (ACTION_NOTIFICATION_CLICK.equals(intent.getAction())) { // 4.4
            Log.d(TAG, "### ACTION_NOTIFICATION_CLICK Begin 4.4");
            int nid = intent.getIntExtra(EXTRA_MESSAGE_NID, 0);
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);

            intent2open.putExtra("message", "4.4");
            intent2open.putExtra("title", title);
            intent2open.putExtra("content", content);
            intent2open.putExtra("dataJson", dataJson);

//            MainActivity.getInstance().updateOnNotifClick(title,content);

            Log.d(TAG, "Notification id:" + nid + "Notification title:" + title + "\nNotitication content:" + content + "\nData:" + dataJson);
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK End");

        } else if (ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "### ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED Begin 4.5");

            MediaMesageInfo mediaMesageInfo = StoreSdk.getInstance().getMediaMessage(context);

            System.out.println(mediaMesageInfo);
            System.out.println("imgUrl: " + mediaMesageInfo.getImgUrl());
            System.out.println("linkUrl: " + mediaMesageInfo.getLinkUrl());

            intent2open.putExtra("message", "4.5");
            intent2open.putExtra("template", String.valueOf(mediaMesageInfo.getTemplate()));
            intent2open.putExtra("imgUrl", mediaMesageInfo.getImgUrl());
            intent2open.putExtra("title", mediaMesageInfo.getTitle());

            Log.d(TAG, "ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED End");
        }

        context.startActivity(intent2open);
    } // onReceive End
}