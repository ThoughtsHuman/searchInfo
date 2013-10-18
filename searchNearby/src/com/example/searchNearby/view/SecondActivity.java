package com.example.searchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.searchNearby.EntityConstant;
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
    private int mainListSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemactivity);
        Intent data = getIntent();
        mainListSelected = data.getIntExtra(EntityConstant.MAIN_ACTIVITY_LISTVIEW_SELECTED,0);
        itemListView = (ListView) findViewById(R.id.itemListView);
        itemListView.setAdapter(new CommonAdapter(Tools.getAdapterDataWithIndex(mainListSelected,EntityConstant.SECOND_DATA)));
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     Intent  intent= new Intent(SecondActivity.this,ThirdActivity.class);
                        intent.putExtra("secondIndex",i);
                        intent.putExtra(EntityConstant.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainListSelected);
                    startActivity(intent);
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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.listviewitem, viewGroup, false);
            }
            final  int selected = position;
            Map<String, Object> itemData = (Map<String, Object>) getItem(position);
            TextView itemTextView = (TextView) convertView.findViewById(R.id.itemTextView);
            ImageView itemImageView = (ImageView) convertView.findViewById(R.id.nextItemIamgeView);

            itemTextView.setText(itemData.get("itemTextView").toString());
            itemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                    intent.putExtra("secondIndex",selected);
                    intent.putExtra(EntityConstant.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainListSelected);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}
