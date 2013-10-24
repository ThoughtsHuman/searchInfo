package com.example.searchNearby.util;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.searchNearby.Constants;

public class MyLocation {

    private MyLocationListener myLocationListener;

    public void getMyLocation(Context context, MyLocationListener listener) {
        Toast.makeText(context,"开始定位",300);
        this.myLocationListener = listener;


        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);

        LocationClient locationClient = new LocationClient(context);
        locationClient.setAK(Constants.KEY);
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                myLocationListener.getLocation(bdLocation);
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi");
            }
        });

        locationClient.start();
        locationClient.requestLocation();


    }



    public static interface MyLocationListener{
        public void getLocation(BDLocation bdLocation);
    }

}
