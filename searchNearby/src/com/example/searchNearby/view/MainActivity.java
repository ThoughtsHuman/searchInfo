package com.example.searchNearby.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.example.searchNearby.EntityConstant;
import com.example.searchNearby.R;
import com.example.searchNearby.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private ListView mainListView;
    private long exitTime = 0;
    private TextView titleTextview,locatedTextView;
    private ImageView titleLeftImageView,titleRightImageView,locationImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        mainListView = (ListView) findViewById(R.id.mainListView);
        locationImageView = (ImageView) findViewById(R.id.loactionImageView);
        locatedTextView = (TextView) findViewById(R.id.locatedTextView);
        titleLeftImageView = (ImageView) findViewById(R.id.titleLeftImageView);
        titleRightImageView = (ImageView) findViewById(R.id.titleRightImageView);
        titleTextview = (TextView) findViewById(R.id.titleTextView);


//          初始化界面标题图标
        initTitle_clickEvent();

//        设置ListView中的数据并添加item事件监听
        mainListView.setAdapter(new CommonAdapter(Tools.getAdapterData(EntityConstant.FIRST_DATA)));
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this,AllListActivity.class);
                intent.putExtra(EntityConstant.MAIN_ACTIVITY_LISTVIEW_SELECTED,position);
                startActivity(intent);
            }
        });

//     异步用于处理定位
        MainAsyncTask mainAsyncTask = new MainAsyncTask();
        mainAsyncTask.execute(0);
    }

    private void initTitle_clickEvent(){
        titleTextview.setText("搜周边");
        titleLeftImageView.setImageResource(R.drawable.abs__ic_search);
        titleRightImageView.setImageResource(R.drawable.ic_action_setting);

        titleLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        titleRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SetingActivity.class);
                startActivity(intent);
            }
        });
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
            Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


//    异步——----定位代码
    private class MainAsyncTask extends AsyncTask<Integer,Integer,Integer>{

//    可以修改样式
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            dialog.setTitle("请稍等...");
            dialog.setMessage("定位中...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        //定位功能
        @Override
        protected Integer doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dialog.dismiss();
        }

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
            final  int selected = position;
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
                    Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                    intent.putExtra(EntityConstant.MAIN_ACTIVITY_LISTVIEW_SELECTED,selected);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}
