package com.swun.hl.studentcard;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * ��Application����
 * 
 * @author ����
 * 
 */
public class MyApp extends Application {
	public static ImageLoader imgLoader;

	@Override
	public void onCreate() {
		initBmob();
		initImageLoader();
		super.onCreate();
	}

	/**
	 * ��ʼ��Bmob
	 */
	private void initBmob() {
		Bmob.initialize(this, "a58185482f069d69f87eb6a0db7b6cfb");
		BmobUpdateAgent.setUpdateOnlyWifi(false);// �κ�����¶�������
	}

	/**
	 * ��ʼ��ImageLoader
	 */
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.defaultDisplayImageOptions(options).diskCacheFileCount(30)
				.memoryCacheSize(30 * 1024 * 1024).build();

		imgLoader = ImageLoader.getInstance();
		imgLoader.init(config);
	}

	/**
	 * ��ȡ��ǰ�İ汾��(�ǰ汾�ţ��汾��Ϊint����)
	 * 
	 * @param context
	 * @return �汾��
	 */
	public static String getVersionName(Context context) {
		String versionName = "";
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
}
