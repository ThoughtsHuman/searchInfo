package com.example.searchNearby.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.example.searchNearby.Constants;
import com.example.searchNearby.R;
import com.example.searchNearby.util.MyLocation;
import com.example.searchNearby.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements MyLocation.MyLocationListener{

    private ListView mainListView;
    private long exitTime = 0;
    private TextView titleTextview,locationTextView;
    private ImageButton titleLeftImageButton, titleRightImageButton;
    private ImageButton locationImageButton;
    MyLocation myLocation = new MyLocation();
    private String addrsStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        mainListView = (ListView) findViewById(R.id.mainListView);
        locationImageButton = (ImageButton) findViewById(R.id.loactionImageView);
        locationTextView = (TextView) findViewById(R.id.locatedTextView);
        titleLeftImageButton = (ImageButton) findViewById(R.id.titleLeftImageView);
        titleRightImageButton = (ImageButton) findViewById(R.id.titleRightImageView);
        titleTextview = (TextView) findViewById(R.id.titleTextView);

          myLocation.getMyLocation(MainActivity.this,this);

        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocation();
            }
        });

//          初始化界面标题图标
        initTitle_clickEvent();

//        设置ListView中的数据并添加item事件监听
        mainListView.setAdapter(new CommonAdapter(Tools.getAdapterData(Constants.FIRST_DATA)));
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                gotoDetail(position);
                Log.d("TAG","--------detailActivity-----------"+position);
            }
        });

    }


    private void gotoDetail(int position){
        Intent intent = new Intent(MainActivity.this, ItemlActivity.class);
        Log.d("AAAAAAAAAAAAAAAA",""+position);
        intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, position);
        startActivity(intent);
    }

    private void startLocation() {
        myLocation.getMyLocation(MainActivity.this,this);
    }

    private void initTitle_clickEvent() {
        titleTextview.setText("搜周边");
        titleLeftImageButton.setImageResource(R.drawable.abs__ic_search);
        titleRightImageButton.setImageResource(R.drawable.ic_action_setting);

        titleLeftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        titleRightImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getLocation(BDLocation bdLocation) {
        addrsStr = bdLocation.getAddrStr();

        locationTextView.setText(addrsStr);
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
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
                Log.d("TAG","--------itemActivity-----------"+position);


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
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, position);
                    startActivity(intent);

                }
            });
            return convertView;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
