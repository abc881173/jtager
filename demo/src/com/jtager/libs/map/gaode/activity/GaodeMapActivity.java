package com.jtager.libs.map.gaode.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.hehp.app.R;
import com.jtager.libs.utils.JLogUtil;

/**
 * 
 * @author Helping
 *
 */
@SuppressWarnings("deprecation")
public class GaodeMapActivity extends Activity implements OnClickListener, LocationSource {
	
	private AMap aMap;
	private MapView mMapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private MyAMapLocationListener mMapLocationListener;
	private AMapLocation mAMapLocation;
	private Marker locationMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置设备为竖屏模式
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_gaode);
		initView();
		mMapView.onCreate(savedInstanceState);// 此方法必须重写
		// 初始化地图
		initMap();
	}

	/**
	 * 初始化
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			mMapLocationListener = new MyAMapLocationListener();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setTiltGesturesEnabled(false);// 设置地图是否可以倾斜
		uiSettings.setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
		uiSettings.setZoomControlsEnabled(false);
	}

	private void initView() {
		findViewById(R.id.map_custom_loc).setOnClickListener(this);
		mMapView = (MapView) findViewById(R.id.knd_map_gaode);
	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		if (vId == R.id.map_custom_loc) {
			if (aMap != null && mAMapLocation != null) {
				aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(mAMapLocation.getLatitude(),
						mAMapLocation.getLongitude())));
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10,
					mMapLocationListener);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(mMapLocationListener);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	private class MyAMapLocationListener implements AMapLocationListener {
		/**
		 * 此方法已经废弃
		 */
		@Override
		public void onLocationChanged(Location location) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(AMapLocation aLocation) {
			if (mListener != null && aLocation != null) {
				mAMapLocation = aLocation;
				// 显示系统小蓝点
				mListener.onLocationChanged(aLocation);
				// show Marker
				LatLng latLng = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
				addMarker("[当前位置]", latLng, aLocation.getAddress());

				Double geoLat = aLocation.getLatitude();
				Double geoLng = aLocation.getLongitude();
				String cityCode = "";
				String desc = "";
				Bundle locBundle = aLocation.getExtras();
				if (locBundle != null) {
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
				String str = ("定位成功:(" + geoLng + "," + geoLat + ")" + "\n精    度    :" + aLocation.getAccuracy() + "米"
						+ "\n定位方式:" + aLocation.getProvider() + "\n定位时间:" + cityCode + "\n位置描述:" + desc + "\n省:"
						+ aLocation.getProvince() + "\n市:" + aLocation.getCity() + "\n区(县):" + aLocation.getDistrict()
						+ "\n区域编码:" + aLocation.getAdCode());
				JLogUtil.println("--" + str);

				// 定位成功后停止定位
				stopLocation();
			}
		}
	}

	/**
	 * 往地图上添加marker
	 * 
	 * @param latLng
	 */
	private void addMarker(String title, LatLng latLng, String address) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title(title);
		markerOptions.snippet(address);
		Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp));
		aMap.clear();

		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
		locationMarker = aMap.addMarker(markerOptions);
		drawCircleOnMap(latLng);
		locationMarker.showInfoWindow();
	}

	/**
	 * 画半径值
	 * 
	 * @param Color_00000
	 *            设置半透明色
	 * @param locationLatLng
	 *            封装好的经纬度对象
	 */
	private void drawCircleOnMap(LatLng locationLatLng) {
		Circle circle = aMap.addCircle(new CircleOptions()
		// 圆圈半径
				.center(locationLatLng).radius(600).strokeColor(Color.GREEN)// 圆圈颜色
				// 圆圈宽度
				.fillColor(Color.BLUE).strokeWidth(1));
		circle.setFillColor(Color.argb(15, 0, 0, 180));
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(mMapLocationListener);
			mAMapLocationManager.destory();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		aMap = null;
	}
}
