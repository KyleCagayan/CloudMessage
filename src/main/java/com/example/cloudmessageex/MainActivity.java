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
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import com.bumptech.glide.Glide;
import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.Notifications;
import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.market.android.app.sdk.dto.MediaMesageInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CloudMessagingDemo";
    private Helper helper = new Helper();

    TextView  tv_message, tv_title, tv_content, tv_data;
    BroadcastReceiver br;
    Handler handler = new Handler();
    private static MainActivity ins;

    ImageView imageView;

    public static MainActivity  getInstance(){
        return ins;
    }

    protected static class PushMessageReceiver extends BroadcastReceiver {

        private static final String TAG = "CloudMessagingEx";
        private Handler handler = null;
        private TextView tv_message;
        private boolean defaultConstructorFlag = false;



        public PushMessageReceiver() {
            Log.d(TAG, "Broadcast receiver default constructor method called");
            defaultConstructorFlag = true;
        }

        public PushMessageReceiver(android.os.Handler handler){ //, TextView tv_message
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Cloud message related broadcast received!");
            if (ACTION_NOTIFY_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) { // 4.1 Mixed Message
                Log.i(TAG, "### NOTIFY_DATA_MESSAGE_RECEIVED 4.1");
                String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
                String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
                String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
                Log.d(TAG, "Notification title: " + title + "\nNotitication content: " + content + "\nData: " + dataJson);
                Log.d(TAG, "NOTIFY_DATA_MESSAGE_RECEIVED End");

            } else if (ACTION_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) { // 4.2
                Log.d(TAG, "### DATA_MESSAGE_RECEIVED 4.2");
                String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
                String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
                String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);

                MainActivity.getInstance().updateOnData(title, content, dataJson);

                Log.d(TAG,"DATA_MESSAGE_RECEIVED Begin\nData:" + dataJson + "\nDATA_MESSAGE_RECEIVED End");
                Log.d(TAG, "NOTIFY_DATA_MESSAGE_RECEIVED End");

            } else if (ACTION_NOTIFICATION_MESSAGE_RECEIVED.equals(intent.getAction())) {  // 4.3
                //else if (ACTION_NOTIFICATION_MESSAGE_RECEIVED.equals(intent.getAction()))
                Log.d(TAG, "### NOTIFICATION_MESSAGE_RECEIVED Begin 4.3");
                String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
                String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);

                MainActivity.getInstance().updateOnNotif(title, content);

                Log.d(TAG, "Notification title:" + title + "\nNotification content:" + content);
                Log.d(TAG, "NOTIFICATION_MESSAGE_RECEIVED End");

            } else if (ACTION_NOTIFICATION_CLICK.equals(intent.getAction())) { // 4.4
                Log.d(TAG, "### ACTION_NOTIFICATION_CLICK Begin 4.4");
                int nid = intent.getIntExtra(EXTRA_MESSAGE_NID, 0);
                String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
                String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
                String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);

                MainActivity.getInstance().updateOnNotifClick(title,content);

                Log.d(TAG, "Notification id:" + nid + "Notification title:" + title + "\nNotitication content:" + content + "\nData:" + dataJson);
                Log.d(TAG, "ACTION_NOTIFICATION_CLICK End");

            } else if (ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "### ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED Begin 4.5");

//                MediaMesageInfo mediaMesageInfo = StoreSdk.getInstance().getMediaMessage(context);
//
//                MainActivity.getInstance().updateOnMedia(mediaMesageInfo);

                String mediaJson = intent.getStringExtra(EXTRA_MEIDA);

                Log.d(TAG, "ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED End");
            }
        }
    }

    public void clearAll(){
        MainActivity.this.runOnUiThread(() -> {
            tv_message.setText("");
            tv_title.setText("");
            tv_content.setText("");
            tv_data.setText("");
        });
    }

    public void updateOnNotif(final String title, final String content) {
        MainActivity.this.runOnUiThread(() -> {
            clearAll();
            tv_message.setText("Notification");
            tv_title.setText(title);
            tv_content.setText(content);
        });
    }

    public void updateOnNotifClick(final String title, final String content){
        updateOnNotif(title, content);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateOnData(final String title, final String content, final String dataJson){
        MainActivity.this.runOnUiThread(() -> {
            clearAll();
            tv_message.setText("dataMsg");
            tv_title.setText(title);
            tv_content.setText(content);
            tv_data.setText(dataJson);
        });
    }

    public void updateOnMixed(final String title, final String content, final String dataJson){
        MainActivity.this.runOnUiThread(() -> {
            clearAll();
            tv_message.setText("dataMsg");
            tv_title.setText(title);
            tv_content.setText(content);
        });
    }

    public void updateOnMedia(MediaMesageInfo mediaMesageInfo){
        MainActivity.this.runOnUiThread(() -> {
            clearAll();
            Glide.with(this).load(mediaMesageInfo.getImgUrl()).into(imageView);
        });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;
        tv_title=findViewById(R.id.tv_title);
        tv_content=findViewById(R.id.tv_content);
//        tv_message = findViewById(R.id.tv_message);
//        tv_data = findViewById(R.id.tv_data);
        br=new PushMessageReceiver(handler);

//        imageView = findViewById(R.id.imageView);

        initPaxStoreSdk();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.paxstore.mpush.NOTIFY_DATA_MESSAGE_RECEIVED");
        filter.addAction("com.paxstore.mpush.DATA_MESSAGE_RECEIVED");
        filter.addAction("com.paxstore.mpush.NOTIFICATION_MESSAGE_RECEIVED");
        filter.addAction("com.paxstore.mpush.NOTIFICATION_CLICK");
        filter.addAction("com.paxstore.mpush.ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED");

        filter.addAction("com.paxstore.mpush.EXTRA_MESSAGE_CONTENT");
        filter.addAction("com.paxstore.mpush.EXTRA_MESSAGE_DATA");
        filter.addAction("com.paxstore.mpush.EXTRA_MESSAGE_NID");
        filter.addAction("com.paxstore.mpush.EXTRA_MESSAGE_TITLE");

        registerReceiver(br, filter);

//        Notifications.I.init(getApplicationContext()).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_demo));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(br);
    }

    private void initPaxStoreSdk() {
        // initializes appkey, appsecret and SN, confirms appkey and appsecret are correct.
        StoreSdk.getInstance().init(getApplicationContext(), helper.getAppKey(), helper.getAppSecret(), new BaseApiService.Callback() {
            @Override
            public void initSuccess() {
                tv_message.setText("success");
            }

            @Override
            public void initFailed(RemoteException e) {
                tv_message.setText("failure");
            }
        });

        Notifications.I.init(getApplicationContext())
                .setSmallIcon(R.drawable.logo_demo_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_demo));
        // disable the Notifications we provided through below code.
        // Notifications.I.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }



}

