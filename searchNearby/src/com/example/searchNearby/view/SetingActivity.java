package com.example.searchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.searchNearby.R;

/**
 * Created with IntelliJ IDEA.
 * User: Han
 * Date: 13-10-18
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class SetingActivity extends Activity {
    private TextView titleTextView;
    private ImageView backImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        backImageView = (ImageView) findViewById(R.id.titleLeftImageView);

        initTitle();
    }
    private void initTitle(){
        titleTextView.setText("设置");
        backImageView.setImageResource(R.drawable.ic_nav_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
