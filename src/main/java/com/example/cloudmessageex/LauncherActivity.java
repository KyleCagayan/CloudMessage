package com.example.cloudmessageex;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.pax.market.android.app.sdk.AdvertisementDialog;


public class LauncherActivity extends FragmentActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();
//    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.launcher);

        showAd(this);
    }

    private void showAd(Context context) {
        int showResult = AdvertisementDialog.show(context, linkUrl -> { });
        Log.d(TAG, "showResult:" + showResult);
    }
}
