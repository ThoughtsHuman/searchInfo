package com.example.searchNearby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import com.example.searchNearby.view.MainActivity;


public class StartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    Log.e("TAg",e.getMessage());
                }
            }
        }).start();

    }
}
