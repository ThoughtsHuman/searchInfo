package com.example.searchNearby.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.example.searchNearby.Constants;
import com.example.searchNearby.R;
import com.example.searchNearby.util.MyLocation;
import com.example.searchNearby.util.Tools;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Han
 * Date: 13-10-20
 * Time: 下午9:33
 * To change this template use File | Settings | File Templates.
 */
public class ItemlActivity extends Activity implements MyLocation.MyLocationListener {
    private String city, coordinate, range, count, searchtype,q,jsonURL = "https://api.weibo.com/2/location/pois/search/by_geo.json?"+Constants.ACCESS_TOKEN;
    private ImageButton backButton, refresh, listOrMapButton;
    private TextView detailTitle, detailLimit;
    private LinearLayout listLayout, linearLayout;
    private ListView itemListView;
    private MapView mapView;
    private CommonAdapter adapter = null;
    private int mainSelected,secondSelected,thridSelected;
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    private MyLocation myLocation = new MyLocation();
    private Resources res;
    final String[] searchLimit = {"1000", "2000", "3000", "4000", "5000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemlview);
        res = getResources();
        myLocation.getMyLocation(ItemlActivity.this, this);

        final LayoutInflater inflater = getLayoutInflater();

        Intent intent = getIntent();
        mainSelected = intent.getIntExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, -1);
        secondSelected = intent.getIntExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED,-1);
        thridSelected = intent.getIntExtra(Constants.THRID_ACTIVITY_LISTVIEW_SELECTED,-1);

        backButton = (ImageButton) findViewById(R.id.detailBackButton);
        refresh = (ImageButton) findViewById(R.id.detailRefresh);
        detailTitle = (TextView) findViewById(R.id.detailTitle);
        detailLimit = (TextView) findViewById(R.id.detailLimit);
        linearLayout = (LinearLayout) findViewById(R.id.detailListOrMapView);
        listLayout = (LinearLayout) inflater.inflate(R.layout.item_listview, null).findViewById(R.id.listLayout);
        itemListView = (ListView) listLayout.findViewById(R.id.detail_listview);

        searchtype =disposeArgSearchtype();

        listOrMapButton = (ImageButton) findViewById(R.id.detailListOrMapButton);
        listOrMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout(view);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRefresh();
            }
        });
//        jsonURL = jsonURL+"&coordinate="+coordinate+"&searchtype="+searchtype;
        jsonURL = jsonURL + "&coordinate=116.322479,39.980781&q=" + searchtype;

        Log.d(Constants.TAG, "-------oncreat---------" + jsonURL);

        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute();
        linearLayout.addView(listLayout);

        detailLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLimitText();
            }
        });
        detailTitle.setText(Constants.FIRST_DATA[mainSelected]);

        backButton();

    }

    private void changeLayout(View view) {
        ImageButton button = (ImageButton) view;

        if (button.getResources().getResourceName(R.drawable.ic_action_list).equals(res.getResourceName(R.drawable.ic_action_list))) {
            button.setImageResource(R.drawable.ic_action_map);
        }

    }

    private String disposeArgSearchtype(){
       String str = "";
        if(thridSelected==-1&&secondSelected!=-1){
           str = Constants.SECOND_DATA[mainSelected][secondSelected];
        }
        if(thridSelected!=-1){
            str = Constants.THRID_DATA[mainSelected][secondSelected][thridSelected];
        }
        if(secondSelected == -1&&thridSelected==-1){
            str = Constants.FIRST_DATA[mainSelected];
        }
        return str;
    }

    private void startRefresh() {
        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute();
    }

    private void setLimitText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(searchLimit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                detailLimit.setText("范围:" + searchLimit[which] + "米");
                jsonURL = jsonURL + "&range=" + searchLimit[which];

                MyAsynTask myAsynTask = new MyAsynTask();
                myAsynTask.execute();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemlActivity.this,ThirdActivity.class);
                intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED,mainSelected);
                intent.putExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED,secondSelected);
                finish();
            }
        });
    }


    @Override
    public void getLocation(BDLocation bdLocation) {
        city = bdLocation.getCity();
        coordinate = bdLocation.getLatitude() + "," + bdLocation.getLongitude();
        Log.d(Constants.TAG, "--------------getLocation" + coordinate + "" + city);
    }


    private class MyAsynTask extends AsyncTask {
        ProgressDialog dialog = new ProgressDialog(ItemlActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("刷新中...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... objects) {

            try {
                Log.d(Constants.TAG, "-------doInbackground---------" + jsonURL);
                data = getJsonData(jsonURL);


            } catch (IOException e) {
                return Constants.SERVERDATA_ERROR;
            } catch (JSONException e) {
                return Constants.SERVERDATA_ERROR;
            }
            return 0;
        }


        @Override
        protected void onPostExecute(Object o) {
            if ((Integer) o == Constants.SERVERDATA_ERROR) {
                Toast.makeText(ItemlActivity.this, "服务器数据错误", 500);
            }
            if ((Integer) o == Constants.DATA_ERROR) {
                Toast.makeText(ItemlActivity.this, "数据错误", 500);
            }
            dialog.dismiss();
            adapter = new CommonAdapter(data);
            itemListView.setAdapter(adapter);
            super.onPostExecute(o);
        }
    }

    private ArrayList<HashMap<String, Object>> getJsonData(String url) throws IOException, JSONException {

        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        HttpGet request = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray array = jsonObject.optJSONArray("poilist");


        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObjectItem = (JSONObject) array.get(i);
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("poiTitle", jsonObjectItem.optString("name"));
            item.put("poiAddrs", jsonObjectItem.optString("address"));
            item.put("poiDistence", jsonObjectItem.optString("distance") + "米");
            data.add(item);
        }

        Log.d("tag", "------------bbbbbbbbbbbbbbb------------" + jsonStr);

        return data;
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
                convertView = layoutInflater.inflate(R.layout.item_listview_row, viewGroup, false);
            }

            Map<String, Object> itemData = (Map<String, Object>) getItem(position);
            TextView poiTitle = (TextView) convertView.findViewById(R.id.poiTitle);
            TextView poiAddrs = (TextView) convertView.findViewById(R.id.poiAddrs);
            TextView poiDistence = (TextView) convertView.findViewById(R.id.poiDistence);

            poiTitle.setText(itemData.get("poiTitle").toString());
            poiAddrs.setText(itemData.get("poiAddrs").toString());
            poiDistence.setText(itemData.get("poiDistence").toString());
            return convertView;
        }
    }
}
