package com.swun.hl.studentcard.cache;

import java.io.File;

import android.content.Context;

/**
 * 缓存帮助类。主要用于获取缓存路劲之类的。
 * 
 * @author LANTINGSHUXU
 * 
 */
public class CacheHelper {

	/**
	 * 获取图片缓存路径
	 * 
	 * @param context
	 * @return 缓存图片的路径
	 */
	public static String getImageCacheDir(Context context) {
		String dir = "";
		File file = context
				.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);
		if (file == null) {
			file = context.getCacheDir();
		}
		dir = file.getAbsolutePath() + "/image/";
		return dir;
	}
}
