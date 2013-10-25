package com.example.searchNearby.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.searchNearby.R;
import com.example.searchNearby.util.MyLocation;

/**
 * Created with IntelliJ IDEA.
 * User: Han
 * Date: 13-10-25
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
public class DetailActivity extends Activity implements MyLocation.MyLocationListener {
    private MyLocationOverlay myLocationOverlay;
    private MapView mapView;
    private RadioGroup radioGroup;
//    private TextView distanceInfo,
    private GeoPoint fromPoint,toPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detailview);
    }

    @Override
    public void getLocation(BDLocation bdLocation) {

    }
}
