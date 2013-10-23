package com.example.searchNearby.util;


import android.content.Context;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import org.apache.http.HttpRequest;
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

public class Tools {

    public static ArrayList<HashMap<String, Object>> getAdapterData(String[] temp) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < temp.length; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("itemTextView", temp[i]);
            data.add(item);
        }
        return data;
    }

    public static ArrayList<HashMap<String, Object>> getAdapterDataWithIndex(int index, String[][] temp) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < temp[index].length; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            if (temp[index][i] != null) {
                item.put("itemTextView", temp[index][i]);
                data.add(item);
            }
        }
        return data;
    }

    public static ArrayList<HashMap<String, Object>> getAdapterDataWithIndex(int firstIndex, int secondIndex, String[][][] temp) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int j = 0; j < temp[firstIndex][secondIndex].length; j++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            if (temp[firstIndex][secondIndex][j] != null) {
                item.put("itemTextView", temp[firstIndex][secondIndex][j]);
                data.add(item);
            }
        }
        return data;
    }

    public static ArrayList<HashMap<String, Object>> getAdapterAllDataWithIndex(int index, String[][][] temp) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < temp[index].length; i++) {
            for (int j = 0; j < temp[index][i].length; j++) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                if (temp[index][i][j] != null) {
                    item.put("itemTextView", temp[index][i][j]);
                    data.add(item);
                }

            }
        }
        return data;
    }

}
