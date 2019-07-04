package com.xf.carwasher.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.squareup.picasso.Picasso;
import com.xf.carwasher.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FinalActivity implements AMap.OnMyLocationChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @ViewInject(id = R.id.map)
    private MapView mMapView;
    private AMap aMap = null;

    @ViewInject(id = R.id.dialog_shop_picture)
    private ImageView dialogShopPicture;

    @ViewInject(id=R.id.scan_btn)
    private RelativeLayout scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initMap();
        setLocation();
        setMarker();
     //   Picasso.with(getApplicationContext()).load("https://lanhu.oss-cn-beijing.aliyuncs.com/xd91b35749-a0f6-4178-8f40-573402d8ed59").into(dialogShopPicture);
        Log.e(TAG,"MainActivity");
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"scan");
                Intent intent = new Intent(MainActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initMap(){

        //   Toast.makeText(MainActivity.this, SHA.sHA1(this),Toast.LENGTH_LONG).show();
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
    }

    public void setLocation(){
        MyLocationStyle myLocationStyle;
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
     //   aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Map<String,Object> map = (Map<String,Object>)marker.getObject();
                Log.e(TAG,JSONObject.toJSONString(map));
                return false;
            }
        });
    }

    public void setMarker(){
        LatLng latLng = new LatLng(39.906901,116.397972);
        final Marker marker = aMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),R.mipmap.point_icon)))
                .title("北京")
                .snippet("DefaultMarker"));

        Map<String,Object> map = new HashMap<>(4);
        map.put("shopId",1024);
        map.put("shopName","中石化陆家嘴加油站");
        map.put("status",1);
        marker.setObject(map);
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.e(TAG,"--> "+JSONObject.toJSONString(location));
        Toast.makeText(MainActivity.this, location.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
