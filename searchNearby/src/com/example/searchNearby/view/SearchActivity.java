package com.example.searchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.example.searchNearby.R;

/**
 * Created with IntelliJ IDEA.
 * User: Han
 * Date: 13-10-18
 * Time: 下午3:20
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends Activity {
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);
        backImageView = (ImageView) findViewById(R.id.search_backImageView);

        initTitle();
    }

    private void initTitle() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}