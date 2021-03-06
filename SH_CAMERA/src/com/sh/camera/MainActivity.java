/*  car eye 车辆管理平台
 * car-eye管理平台   www.car-eye.cn
 * car-eye开源网址:  https://github.com/Car-eye-team
 * Copyright
 */

package com.sh.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.sh.camera.service.MainService;
import com.sh.camera.util.Constants;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.sh.camera.service.ShCommService;


public class MainActivity extends Activity {
	public static MainActivity mainactivity;
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
			Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECORD_AUDIO,
			Manifest.permission.READ_PHONE_STATE};
	//public FloatWindowManager.MyListener listener;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Log.d("-------------------", "MainActivity onCreate" );
		Constants.setParam(this);
		int version = android.os.Build.VERSION.SDK_INT;
		Log.d("CMD", "version : " + version);
		Bugly.init(getApplicationContext(), "9c4b0e3ce3", false);
		Beta.checkUpgrade(false,false);
		setContentView(R.layout.activity_splash);
		/*listener=new FloatWindowManager.MyListener() {
			@Override
			public void getData(boolean res) {
				if (res)
				{
					getPermission();
				}
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				FloatWindowManager.getInstance().setOnListener(listener);
				//	getPermission();

			}
		}).start();*/
		mainactivity = this;
		getPermission();
		//启动通讯service\
		//FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this);

	}

	private void getPermission() {
		Log.e("TAG","OK");
		applypermission();
	}

	public void gotoService(){
		if(!MainService.isrun){
			startService(new Intent(MainActivity.this, MainService.class));
			Log.d("MainActivity" , "You got error here 0?");
		}else{
			Intent intent = new Intent(MainService.ACTION);
			intent.putExtra("type", MainService.FULLSCREEN);
			sendBroadcast(intent);
		}
		startService(new Intent(MainActivity.this, ShCommService.class));
		finish();
	}

	public void applypermission(){
		boolean needapplypermission=false;
		if(Build.VERSION.SDK_INT>=23){
			for(int i=0; i < PERMISSIONS_STORAGE.length ; i++){
				int chechpermission= ContextCompat.checkSelfPermission(getApplicationContext(),
						PERMISSIONS_STORAGE[i]);
				if(chechpermission!= PackageManager.PERMISSION_GRANTED){
					needapplypermission=true;
				}
			}
			if(needapplypermission){
				ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE,1);
			}else{
				gotoService();
			}
		}else {
			gotoService();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		for(int i=0;i<grantResults.length;i++){
			if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
				//Toast.makeText(MainActivity.this, permissions[i]+"已授权",Toast.LENGTH_SHORT).show();
				gotoService();
			}
			else {
				//Toast.makeText(MainActivity.this,permissions[i]+"拒绝授权,请再设置中开启权限",Toast.LENGTH_SHORT).show();

				finish();
			}
		}
		//if ready
		//send msg rto notify?
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*Boolean checkPer=	FloatWindowManager.getInstance().CheckPer(MainActivity.this);
		if (checkPer)
		{
			getPermission();
		}*/
	}
}



