package com.example.searchNearby.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.searchNearby.Constants;
import com.example.searchNearby.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchActivity extends Activity {
    private String coordinate, range, count, q, jsonURL = Constants.JSON_URL;
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    private ImageView backImageView;
    private ImageButton searchButton;
    private CommonAdapter adapter = null;
    private ListView itemListView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);
        backImageView = (ImageView) findViewById(R.id.search_backImageView);
        itemListView = (ListView) findViewById(R.id.search_listView);
        searchButton = (ImageButton) findViewById(R.id.search_ImageView);
        editText = (EditText) findViewById(R.id.search_editText);

        jsonURL = jsonURL + "&coordinate=116.322479,39.980781";

        search();

        gotoDetail();

        backView();
    }

    private void gotoDetail(){
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               TextView textView = (TextView) view.findViewById(R.id.poiTitle);
//                textView.getText();
                Log.d("Tag",""+textView.getText()+"----------------------------");
            }
        });
    }

    private void addData(){
        String str = editText.getText().toString();
        str = str.trim().replaceAll(" ", "");
        Log.d("tag",str+"------------------------------");
        if (!str.equals("")) {
            jsonURL = jsonURL + "&q=" + editText.getText().toString().trim();
            MyAsynTask myAsynTask = new MyAsynTask();
            myAsynTask.execute();
        } else {
            Toast.makeText(this, "请输入要搜索的内容", Toast.LENGTH_LONG).show();
        }
    }

    private void search() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addData();
            }
        });
    }

    private void backView() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private class MyAsynTask extends AsyncTask<Object, Object, Integer> {
        ProgressDialog dialog = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("刷新中...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Object... objects) {

            try {
                data = getJsonData(jsonURL);
            } catch (IOException e) {
                return Constants.SERVERDATA_ERROR;
            } catch (JSONException e) {
                return Constants.DATA_ERROR;
            }
            return Constants.SUCCESS;
        }


        @Override
        protected void onPostExecute(Integer o) {

            if (o == Constants.SUCCESS) {

                adapter = new CommonAdapter(data);
                itemListView.setAdapter(adapter);
            } else {
                if (o == Constants.SERVERDATA_ERROR) {
                    Toast.makeText(SearchActivity.this, "服务器数据错误", 500).show();
                }
                if (o == Constants.DATA_ERROR) {
                    Toast.makeText(SearchActivity.this, "数据错误", 500).show();
                }
            }
            dialog.dismiss();
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

        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObjectItem = (JSONObject) array.get(i);
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("poiTitle", jsonObjectItem.optString("name"));
                item.put("poiAddrs", jsonObjectItem.optString("address"));
                item.put("poiDistence", jsonObjectItem.optString("distance") + "米");
                data.add(item);
            }
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