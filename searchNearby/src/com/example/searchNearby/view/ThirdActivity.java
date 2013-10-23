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
 * Time: 下午10:25
 * To change this template use File | Settings | File Templates.
 */
public class ThirdActivity extends Activity {
    private ListView itemListView;
    private int mainSelected, secondSelected;
    private TextView titleTextview;
    private ImageButton titleLeftImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.thirdactivity);

        itemListView = (ListView) findViewById(R.id.itemListView);
        titleTextview = (TextView) findViewById(R.id.titleTextView);
        titleLeftImageView = (ImageButton) findViewById(R.id.titleLeftImageView);
        Intent data = getIntent();
        mainSelected = data.getIntExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, -1);
        secondSelected = data.getIntExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED, -1);

        Log.d("TAG",mainSelected+","+secondSelected+"*************************************");

        initTitle_clickEvent();


        itemListView.setAdapter(new CommonAdapter(Tools.getAdapterDataWithIndex(mainSelected, secondSelected, Constants.THRID_DATA)));
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                gotoDetail(position);
            }
        });
    }


    private void gotoDetail(int position){
        Intent intent = new Intent(this,ItemlActivity.class);
        intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainSelected);
        intent.putExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED,secondSelected);
        intent.putExtra(Constants.THRID_ACTIVITY_LISTVIEW_SELECTED,position);
        startActivity(intent);
        finish();
    }

    private void initTitle_clickEvent() {
        titleTextview.setText(Constants.SECOND_DATA[mainSelected][secondSelected]);
        titleLeftImageView.setImageResource(R.drawable.ic_nav_back);

        titleLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainSelected);
                startActivity(intent);
                finish();
            }
        });
    }

    private class CommonAdapter extends BaseAdapter {
        private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        private CommonAdapter(ArrayList<HashMap<String, Object>> data) {
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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.listviewitem, viewGroup, false);
            }

            Map<String, Object> itemData = (Map<String, Object>) getItem(position);
            TextView itemTextView = (TextView) convertView.findViewById(R.id.itemTextView);
            itemTextView.setText(itemData.get("itemTextView").toString());
            return convertView;
        }
    }
}
