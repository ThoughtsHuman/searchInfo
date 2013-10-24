package com.example.searchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.searchNearby.Constants;
import com.example.searchNearby.R;
import com.example.searchNearby.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Han
 * Date: 13-10-17
 * Time: 下午9:04
 * To change this template use File | Settings | File Templates.
 */
public class SecondActivity extends Activity{
    private ListView itemListView;
    private int mainSelected,secondSelected;
    private TextView titleTextview;
    private ImageButton titleLeftImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.secondactivity);
        Intent data = getIntent();
        itemListView = (ListView) findViewById(R.id.itemListView);
        titleTextview = (TextView) findViewById(R.id.titleTextView);
        titleLeftImageButton = (ImageButton) findViewById(R.id.titleLeftImageView);

        mainSelected = data.getIntExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,-1);
        initTitle_clickEvent();

        itemListView.setAdapter(new CommonAdapter(Tools.getAdapterDataWithIndex(mainSelected, Constants.SECOND_DATA)));
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(SecondActivity.this,ItemlActivity.class);
                intent.putExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED,position);
                intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainSelected);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initTitle_clickEvent() {
        titleTextview.setText(Constants.FIRST_DATA[mainSelected]);
        titleLeftImageButton.setImageResource(R.drawable.ic_nav_back);

        titleLeftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    private class CommonAdapter extends BaseAdapter {
        private ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String, Object>>();
        private CommonAdapter(ArrayList<HashMap<String,Object>> data) {
            this.data = data;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            secondSelected = position;
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.listviewitem, viewGroup, false);
            }
            Map<String, Object> itemData = (Map<String, Object>) getItem(position);
            TextView itemTextView = (TextView) convertView.findViewById(R.id.itemTextView);
            ImageView itemImageView = (ImageView) convertView.findViewById(R.id.nextItemIamgeView);

            itemTextView.setText(itemData.get("itemTextView").toString());
            itemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                    intent.putExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED,position);
                    intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainSelected);
                    startActivity(intent);
                    finish();
                }
            });
            return convertView;
        }
    }

}
