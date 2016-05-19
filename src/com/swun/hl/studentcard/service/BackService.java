package com.swun.hl.studentcard.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.swun.hl.studentcard.client.StudentCardClient;

/**
 * ��̨��ֹ���ߵ�service����
 * 
 * @author LANTINGSHUXU
 * 
 */
public class BackService extends Service {

	private StudentCardClient client;
	private Timer timer;// ��ʱ��

	/**
	 * ���������������Fragment
	 */
	public static Fragment[] contains = null;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		client = StudentCardClient.getInstance(getApplication());
		startTimer();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * ��ֹSession���ڡ���ʱ�������·�����
	 */
	private void startTimer() {
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					client.getStudentInfo(null);
				}
			}, 0, 1 * 60 * 1000);
		}
	}

}
