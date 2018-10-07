package com.example.ericjeffrey.helloworld.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.ericjeffrey.helloworld.R;
import com.example.ericjeffrey.helloworld.tools.PermissionHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationTestActivity extends AppCompatActivity {

    private boolean permissionLocation;
    private boolean permissionStorage;

    private TextView locationRes;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        permissionLocation = PermissionHelper.requestPermission(this, PermissionHelper.REQUEST_CODE_LOCATION);
        if (!permissionLocation)
            return;
        permissionStorage = PermissionHelper.requestPermission(this, PermissionHelper.REQUEST_CODE_STORAGE);
        if (!permissionStorage)
            return;

        startLocate();
    }

    private void startLocate() {
        locationRes = findViewById(R.id.locate_result);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocationTestActivity.this, MapTestActivity.class));
            }
        });

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation == null || amapLocation.getErrorCode() != 0){
                    Toast.makeText(LocationTestActivity.this, "请打开定位开关", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder builder = new StringBuilder();
                String tmp;
                tmp = String.valueOf(amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                builder.append("位置类型：").append(tmp).append("\n");
                tmp = String.valueOf(amapLocation.getLatitude());//获取纬度
                builder.append("纬度：").append(tmp).append("\n");
                tmp = String.valueOf(amapLocation.getLongitude());//获取经度
                builder.append("经度：").append(tmp).append("\n");
                tmp = String.valueOf(amapLocation.getAccuracy());//获取精度信息
                builder.append("精度：").append(tmp).append("\n");
                tmp = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                builder.append("地址：").append(tmp).append("\n");
                tmp = amapLocation.getCountry();//国家信息
                builder.append("国家：").append(tmp).append("\n");
                tmp = amapLocation.getProvince();//省信息
                builder.append("省：").append(tmp).append("\n");
                tmp = amapLocation.getCity();//城市信息
                builder.append("城市：").append(tmp).append("\n");
                tmp = amapLocation.getDistrict();//城区信息
                builder.append("地区：").append(tmp).append("\n");
                tmp = amapLocation.getStreet();//街道信息
                builder.append("街道：").append(tmp).append("\n");
                tmp = amapLocation.getStreetNum();//街道门牌号信息
                builder.append("门牌号：").append(tmp).append("\n");
                tmp = amapLocation.getCityCode();//城市编码
                builder.append("城市编码：").append(tmp).append("\n");
                tmp = amapLocation.getAdCode();//地区编码
                builder.append("地区编码：").append(tmp).append("\n");
                tmp = amapLocation.getAoiName();//获取当前定位点的AOI信息
                builder.append("定位的AIO：").append(tmp).append("\n");
                tmp = amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                builder.append("室内建筑物ID：").append(tmp).append("\n");
                tmp = amapLocation.getFloor();//获取当前室内定位的楼层
                builder.append("楼层：").append(tmp).append("\n");
                tmp = String.valueOf(amapLocation.getGpsAccuracyStatus());//获取GPS的当前状态
                builder.append("GPS状态：").append(tmp).append("\n");
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = new Date(amapLocation.getTime());
                tmp = df.format(date);
                builder.append("日期：").append(tmp).append("\n");
                locationRes.setText(builder.append("").toString());
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setInterval(60000);

        //设置定位模式为AMapLocationMode.High_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int res = PermissionHelper.isPermissionGranted(this, requestCode, permissions, grantResults);
        switch (res) {
            case PermissionHelper.DENIED_LOCATION:
                Toast.makeText(this, "请授予地理位置权限", Toast.LENGTH_SHORT).show();
                PermissionHelper.requestPermission(this, PermissionHelper.REQUEST_CODE_LOCATION);
                break;
            case PermissionHelper.DENIED_STORAGE:
                Toast.makeText(this, "请授予存储权限", Toast.LENGTH_SHORT).show();
                PermissionHelper.requestPermission(this, PermissionHelper.REQUEST_CODE_STORAGE);
                break;
            case PermissionHelper.FOREVER_DENIED_LOCATION:
                Toast.makeText(this, "没权限没法玩老铁，设置->权限->打开 谢谢:-)", Toast.LENGTH_SHORT).show();
                break;
            case PermissionHelper.FOREVER_DENIED_STORAGE:
                Toast.makeText(this, "没权限没法玩老铁，设置->权限->打开 谢谢:-)", Toast.LENGTH_SHORT).show();
                break;
            case PermissionHelper.GRANTED_LOCATION:
                permissionLocation = true;
                if (!permissionStorage)
                    permissionStorage = PermissionHelper.requestPermission(this, PermissionHelper.REQUEST_CODE_STORAGE);
                break;
            case PermissionHelper.GRANTED_STORAGE:
                permissionStorage = true;
                if (!permissionLocation)
                    permissionLocation = PermissionHelper.requestPermission(this, PermissionHelper.REQUEST_CODE_LOCATION);
                break;
            default:
                break;
        }
        if (permissionStorage && permissionLocation)
            startLocate();
    }
}
