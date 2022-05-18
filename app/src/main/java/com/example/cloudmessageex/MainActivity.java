package com.example.cloudmessageex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.Notifications;
import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.market.android.app.sdk.dto.MediaMesageInfo;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CloudMessagingEx";
    private Helper helper = new Helper();

    TextView tv_message, tv_title, tv_content, tv_data;
    BroadcastReceiver br;
    Handler handler = new Handler();
    private static MainActivity ins;

    ImageView imageView;

    public static MainActivity getInstance() {
        return ins;
    }

    public void onNotif(Intent intent) {
        Log.d(TAG, "notification received");
        tv_title.setText(intent.getStringExtra("title"));
        tv_content.setText(intent.getStringExtra("content"));
    }

    public void onNotifClick(final String title, final String content) {
//        onNotif(title, content);
        Intent intent = new Intent(this, MainActivity.class);
    }

    public void onData(final String title, final String content, final String dataJson) {
        MainActivity.this.runOnUiThread(() -> {
            clearAll();
            tv_message.setText("dataMsg");
            tv_title.setText(title);
            tv_content.setText(content);
            tv_data.setText(dataJson);
        });
    }

    public void onMixed(Bundle bundle) {
//        MainActivity.this.runOnUiThread(() -> {
//            clearAll();
//            tv_message.setText("dataMsg");
//            tv_title.setText(title);
//            tv_content.setText(content);
//        });


    }

    public void onMedia(String template, String imgUrl) {
        Intent toMediaMsgIntent = new Intent(this, MediaMessage1.class);
        toMediaMsgIntent.putExtra("template", template);
        toMediaMsgIntent.putExtra("imgUrl", imgUrl);
        startActivity(toMediaMsgIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent called!");
        switch (intent.getStringExtra("message")) {
            case "4.1":
                //todo mixed message
                onMixed(intent.getExtras());
                break;
            case "4.2":
                // todo data message
                break;
            case "4.4":
                // todo notification clicked
            case "4.3":
                // todo notification received
                onNotif(intent);
                break;
            case "4.5":
                // todo media message
                String template = intent.getStringExtra("template");
                String imgUrl = intent.getStringExtra("imgurl");
                onMedia(template, imgUrl);
                break;
        }


    }

    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }

    public void clearAll() {
        MainActivity.this.runOnUiThread(() -> {
            tv_message.setText("");
            tv_title.setText("");
            tv_content.setText("");
            tv_data.setText("");
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
//        tv_message = findViewById(R.id.tv_message);
//        tv_data = findViewById(R.id.tv_data);
        br = new PushMessageReceiver(handler);
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
                Log.i(TAG, "initSuccess");
            }

            @Override
            public void initFailed(RemoteException e) {
                Log.i(TAG, "initFail");
            }
        });


        // todo fix notification icons
//        Notifications.I.init(getApplicationContext())
//                .setSmallIcon(R.drawable.logo_demo_white)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_demo));
        // disable the Notifications we provided through below code.
        // Notifications.I.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }


}

