package com.example.searchNearby.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.searchNearby.Constants;
import com.example.searchNearby.R;
import com.example.searchNearby.util.MyLocation;
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
    private String city, coordinate, range, count, searchtype, q, jsonURL = Constants.JSON_URL;
    private ImageButton backButton, refresh, listOrMapButton;
    private TextView detailTitle, detailLimit;
    private ListView itemListView;
    private MapView mapView;
    private View mapPopWindow;
    private MyLocationOverlay myLocationOverlay;
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    private MapController mapController;
    private CommonAdapter adapter = null;
    private int mainSelected, secondSelected, thridSelected;
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    private MyLocation myLocation = new MyLocation();

    final String[] searchLimit = {"1000", "2000", "3000", "4000", "5000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemlview);

        myLocation.getMyLocation(ItemlActivity.this, this);

        Intent intent = getIntent();
        mainSelected = intent.getIntExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, -1);
        secondSelected = intent.getIntExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED, -1);
        thridSelected = intent.getIntExtra(Constants.THRID_ACTIVITY_LISTVIEW_SELECTED, -1);

        backButton = (ImageButton) findViewById(R.id.detailBackButton);
        refresh = (ImageButton) findViewById(R.id.detailRefresh);
        detailTitle = (TextView) findViewById(R.id.detailTitle);
        detailLimit = (TextView) findViewById(R.id.detailLimit);
        itemListView = (ListView) findViewById(R.id.detail_listview);
        listOrMapButton = (ImageButton) findViewById(R.id.detailListOrMapButton);

        mapView = (MapView) findViewById(R.id.detailMapView);
        mapPopWindow = getLayoutInflater().inflate(R.layout.map_pop_window, null);
        mapPopWindow.setVisibility(View.GONE);
        mapView.addView(mapPopWindow);
        mapController = mapView.getController();


        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        myLocationOverlay = new MyLocationOverlay(marker,mapView);

        initMapView();

        searchtype = disposeArgSearchtype();
        jsonURL = jsonURL + "&coordinate=108.95000,34.26667&searchtype=" + searchtype;

        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute();

        listOrMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(view);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRefresh();
            }
        });

        detailLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLimitText();
            }
        });

        backButton();

    }

    private void initOvreLayout(){
        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);

        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);

        for (int i = 0; i < data.size(); i++) {
            Log.d("TAG",data.get(i).get("latitude")+","+data.get(i).get("longitude")+"size:"+data.size());
            GeoPoint point = new GeoPoint((int)((Double)data.get(i).get("longitude") * 1E6),(int)((Double)data.get(i).get("latitude") * 1E6));
            OverlayItem overlayItem = new OverlayItem(point, data.get(i).get("poiTitle").toString(),data.get(i).get("poiAddrs").toString());
            itemItemizedOverlay.addItem(overlayItem);
        }
//        mapController.zoomToSpan();

        mapView.getOverlays().add(itemItemizedOverlay);
        mapView.refresh();
    }

    private void initMapView(){
        //设置地图隐藏
        mapView.setVisibility(View.GONE);
        mapView.setBuiltInZoomControls(true);
        //控制缩放等级
        mapController.setZoom(14);
        //设置中心点
        GeoPoint point = new GeoPoint((int) (34.26667 * 1E6), (int) (108.95000 * 1E6));
        mapController.setCenter(point);
//        myLocationOverlay = new MyLocationOverlay(getResources().getDrawable(R.drawable.ic_loc_normal), mapView);
//        mapView.getOverlays().add(myLocationOverlay);


    }

    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
            GeoPoint point = item.getPoint();
            String title = item.getTitle();
            String content = item.getSnippet();

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.contentTextView);
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);

            mapController.animateTo(point);

            return super.onTap(i);
        }

        //点击overLayout以外的事件
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);

            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    private void changeView(View view) {
        ImageButton button = (ImageButton) view;
        if (mapView.getVisibility() == View.GONE) {
            button.setImageResource(R.drawable.ic_action_map);
            mapView.setVisibility(View.VISIBLE);
            itemListView.setVisibility(View.GONE);
            initOvreLayout();
        } else {
            button.setImageResource(R.drawable.ic_action_list);
            mapView.setVisibility(View.GONE);
            itemListView.setVisibility(View.VISIBLE);
        }

    }

    private String disposeArgSearchtype() {
        String str = "";
        if (thridSelected == -1 && secondSelected != -1) {
            str = Constants.SECOND_DATA[mainSelected][secondSelected];
        }
        if (thridSelected != -1) {
            str = Constants.THRID_DATA[mainSelected][secondSelected][thridSelected];
        }
        if (secondSelected == -1 && thridSelected == -1) {
            str = Constants.FIRST_DATA[mainSelected];
        }
        detailTitle.setText(str);
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
                Intent intent = null;
                if (thridSelected != -1) {
                    intent = new Intent(ItemlActivity.this, ThirdActivity.class);
                    intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, mainSelected);
                    intent.putExtra(Constants.SECOND_ACTIVITY_LISTVIEW_SELECTED, secondSelected);
                }
                if (secondSelected == -1) {
                    intent = new Intent(ItemlActivity.this, MainActivity.class);
                }
                if (secondSelected != -1 && thridSelected == -1) {
                    intent = new Intent(ItemlActivity.this, SecondActivity.class);
                    intent.putExtra(Constants.MAIN_ACTIVITY_LISTVIEW_SELECTED, mainSelected);

                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void getLocation(BDLocation bdLocation) {
        city = bdLocation.getCity();
//        GeoPoint loc = new GeoPoint((int)(bdLocation.getLatitude()*1E6),(int)(bdLocation.getLongitude()*1E6));
//        myLocationOverlay.setMyLocation(loc);
//        mapView.getOverlays().add(myLocationOverlay);
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
                data = getJsonData(jsonURL);
            } catch (IOException e) {
                return Constants.SERVERDATA_ERROR;
            } catch (JSONException e) {
                return Constants.SERVERDATA_ERROR;
            }
            return Constants.SUCCESS;
        }


        @Override
        protected void onPostExecute(Object o) {
            if ((Integer) o == Constants.SUCCESS) {
                dialog.dismiss();
                adapter = new CommonAdapter(data);
                itemListView.setAdapter(adapter);
            } else {
                if ((Integer) o == Constants.SERVERDATA_ERROR) {
                    Toast.makeText(ItemlActivity.this, "服务器数据错误", 500).show();
                }
                if ((Integer) o == Constants.DATA_ERROR) {
                    Toast.makeText(ItemlActivity.this, "数据错误", 500).show();
                }
            }
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
            item.put("latitude", jsonObjectItem.optDouble("x"));
            item.put("longitude", jsonObjectItem.optDouble("y"));
            data.add(item);
        }

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
