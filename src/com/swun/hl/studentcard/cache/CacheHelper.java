package com.swun.hl.studentcard.cache;

import java.io.File;

import android.content.Context;

/**
 * ��������ࡣ��Ҫ���ڻ�ȡ����·��֮��ġ�
 * 
 * @author LANTINGSHUXU
 * 
 */
public class CacheHelper {

	/**
	 * ��ȡͼƬ����·��
	 * 
	 * @param context
	 * @return ����ͼƬ��·��
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
