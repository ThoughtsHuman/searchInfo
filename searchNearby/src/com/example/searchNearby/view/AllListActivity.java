package com.example.searchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.searchNearby.Constants;
import com.example.searchNearby.R;
import com.example.searchNearby.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Han
 * Date: 13-10-18
 * Time: 下午1:22
 * To change this template use File | Settings | File Templates.
 */
public class AllListActivity extends Activity {
    private ListView listView;
    private   ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.secondactivity);
        listView = (ListView) findViewById(R.id.itemListView);
        Intent index = getIntent();
       int mainSelected = index.getIntExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,-1);
        data = Tools.getAdapterAllDataWithIndex(mainSelected, Constants.THRID_DATA);
        listView.setAdapter(new CommonAdapter(data));
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
//            final  int selected = position;
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.listviewitem_noimage, viewGroup, false);
            }

            Map<String, Object> itemData = (Map<String, Object>) getItem(position);
            TextView itemTextView = (TextView) convertView.findViewById(R.id.itemTextView);
//            ImageView itemImageView = (ImageView) convertView.findViewById(R.id.nextItemIamgeView);

            itemTextView.setText(itemData.get("itemTextView").toString());

            return convertView;
        }
    }
}
