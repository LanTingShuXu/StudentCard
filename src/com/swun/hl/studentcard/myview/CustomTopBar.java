package com.swun.hl.studentcard.myview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swun.hl.studentcard.R;

/**
 * �Զ��������
 * 
 * @author JYY
 * 
 */
public class CustomTopBar extends RelativeLayout {

	// --------------��������ء��Ļص��ӿ�---------------

	public interface callBackListener {
		public void leftClick(View view);

		public void rightClick(View view);
	}

	// --------------�����¼��ĺ���----------------

	private callBackListener callBackListener;

	public callBackListener getCallBackListener() {
		return callBackListener;
	}

	public void setCallBackListener(callBackListener callBackListener) {
		this.callBackListener = callBackListener;
	}

	private Activity activity;// ��ǰ��ҳ��

	private String title = "No title";// Ĭ����ʾΪ�ޱ���
	private String back_name = "����";// ���ص�ActivityĬ��Ϊ ����
	private boolean back_enable = false;// Ĭ�Ϸ��ذ�ť����ʾ
	private TextView tv_title;
	private ImageView imgv_titleLine;
	private ImageView imgv_back;

	private TextView tv_backname;
	private TextView tv_rightname;
	private ImageView imgv_right;

	private Drawable rightDrawable;
	private String right_name;

	private static final int ID_tv_title = 1000;
	private static final int ID_tv_back = 1001;
	private static final int ID_img_back = 1002;

	// -------------------һϵ�еĹ��췽��----------------------
	public CustomTopBar(Context context) {
		this(context, null);
	}

	public CustomTopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomTopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBackgroundResource(R.color.common_myBlue);

		// ��ȡ���е�����
		getAttr(context, attrs);
	}

	// --------------------------------------------------------

	private void getAttr(Context context, AttributeSet attrs) {

		// ��ȡ���е�����

		TypedArray typeArray = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTopBar);
		// �Զ������Ը���
		int childCount = typeArray.getIndexCount();

		for (int i = 0; i < childCount; i++) {
			// ��ȡ����ID
			int ID = typeArray.getIndex(i);
			switch (ID) {
			// ������Զ���ı���
			case R.styleable.CustomTopBar_title:
				// ��ȡ�û��Լ�����ı���
				title = typeArray.getString(ID);
				break;
			// ������Զ���ķ��ر���
			case R.styleable.CustomTopBar_back_name:
				// ��ȡ�û��Լ�����ı���
				back_name = typeArray.getString(ID);
				break;
			// ��ȡ�Ƿ���Ҫ�з��ذ�ť
			case R.styleable.CustomTopBar_backenable:
				// ��ȡ�û��Լ�����ı���
				back_enable = typeArray.getBoolean(i, false);
				break;
			// ��ȡ�Ƿ���Ҫ�����Ͻǰ�ť����
			case R.styleable.CustomTopBar_right_name:
				// ��ȡ�û��Լ���������Ͻ�����
				right_name = typeArray.getString(ID);
				break;
			// ��ȡ�Ƿ���Ҫ�����Ͻǰ�ťͼƬ
			case R.styleable.CustomTopBar_right_img:
				// ��ȡ�û��Լ���������Ͻǵ�ͼƬ
				rightDrawable = typeArray.getDrawable(ID);
				break;
			}
		}
		typeArray.recycle();

		// ��ʼ��������Ҫ�����
		tv_title = new TextView(getContext());
		imgv_titleLine = new ImageView(getContext());
		imgv_back = new ImageView(getContext());
		tv_backname = new TextView(getContext());
		tv_rightname = new TextView(getContext());
		imgv_right = new ImageView(getContext());

		// ���ñ���
		setTitle();

		setBack();
		setRight();

	}

	/**
	 * �������Ͻǵ���ʾ���ݣ����ֺ�ͼƬ��
	 * 
	 */
	private void setRight() {

		if (rightDrawable != null) {
			imgv_right.setImageDrawable(rightDrawable);
			imgv_right.setClickable(true);
			LayoutParams layoutParams = new LayoutParams(getResources()
					.getDimensionPixelSize(R.dimen.titleBar_imgv_right),
					getResources().getDimensionPixelSize(
							R.dimen.titleBar_imgv_right));
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			layoutParams.rightMargin = (int) getResources().getDimension(
					R.dimen.titleBar_imgv_right_rightMagin);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);

			imgv_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (callBackListener != null) {
						callBackListener.rightClick(v);
					}
				}
			});

			addView(imgv_right, layoutParams);
		} else if (right_name != null) {
			tv_rightname.setText(right_name);
			tv_rightname.setTextSize(16);
			tv_rightname.setTextColor(Color.WHITE);
			tv_rightname.setClickable(true);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			layoutParams.rightMargin = (int) getResources().getDimension(
					R.dimen.titleBar_imgv_right_rightMagin);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);

			tv_rightname.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (callBackListener != null) {
						callBackListener.rightClick(v);
					}
				}
			});

			addView(tv_rightname, layoutParams);
		}

	}

	/**
	 * �������Ͻǵ���ʾ���ݣ����ֺ�ͼƬ��
	 */
	private void setBack() {
		// ΪImageView����ͼƬ
		imgv_titleLine.setImageResource(R.drawable.line_title);
		LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// �����Լ��ײ���margin
		int topMargin = (int) getResources().getDimension(
				R.dimen.titleBar_imgv_titleLine);
		lp2.topMargin = topMargin;
		lp2.bottomMargin = topMargin;
		lp2.addRule(RelativeLayout.BELOW, ID_tv_title);
		addView(imgv_titleLine, lp2);

		// ����û���Ҫ�з��ذ�ť
		if (back_enable) {
			// ΪBackImageView����ͼƬ
			imgv_back.setImageResource(R.drawable.back);
			imgv_back.setId(ID_img_back);
			imgv_back.setBackgroundResource(R.drawable.selector_bg_topbar);
			imgv_back.setOnClickListener(backClick);
			LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			int leftMargin = (int) getResources().getDimension(
					R.dimen.titleBar_imgv_back);
			// lp3.leftMargin = leftMargin;
			imgv_back.setPadding(leftMargin, 0, 0, 0);
			lp3.addRule(RelativeLayout.CENTER_VERTICAL);
			addView(imgv_back, lp3);

			// Ϊ�ؼ������Զ���ķ������� tv_backname.setText(back_name);
			tv_backname.setText(back_name);
			tv_backname.setTextSize(16);
			tv_backname.setId(ID_tv_back);
			tv_backname.setTextColor(Color.WHITE);
			// tv_backname.setBackgroundResource(R.drawable.selector_bg_topbar);
			LayoutParams lp4 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					lp3.height);
			lp4.leftMargin = lp3.leftMargin
					+ (int) getResources().getDimension(
							R.dimen.titleBar_tv_backname);
			lp4.addRule(RelativeLayout.CENTER_VERTICAL);
			// ���õ���ļ�����
			// tv_backname.setOnClickListener(backClick);
			addView(tv_backname, lp4);
		}

	}

	/**
	 * ���ñ���
	 */
	private void setTitle() {
		// Ϊ�ؼ������Զ�����������
		tv_title.setText(title);
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setId(ID_tv_title);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		addView(tv_title, lp);
	}

	/**
	 * �����Զ������
	 * 
	 * @param title
	 *            ����
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * ������Ҫ���صĽ��档
	 * 
	 * @param activity
	 *            ��ǰ����
	 */
	public CustomTopBar setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	/**
	 * ������صļ�����
	 * 
	 * @author ���
	 * 
	 */
	private OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// �������Ϊ�գ���ת��ָ���Ľ���
			if (activity != null) {
				/*
				 * //��ȡ��Ļ��С WindowManager windowManager =
				 * activity.getWindowManager(); Display display =
				 * windowManager.getDefaultDisplay(); DisplayMetrics metrics =
				 * new DisplayMetrics(); display.getMetrics(metrics);
				 * 
				 * int displsyWidth = metrics.widthPixels;
				 * 
				 * TranslateAnimation leftIn = new
				 * TranslateAnimation(-1*displsyWidth,0,0,0);
				 * leftIn.setDuration(300);
				 * 
				 * TranslateAnimation rightOut= new TranslateAnimation(0,
				 * displsyWidth, 0, 0); rightOut.setDuration(300);
				 */

				activity.finish();
				activity.overridePendingTransition(
						R.anim.between_activity_left_in,
						R.anim.between_activity_right_out);
			} else {
				((Activity) getContext()).finish();
				((Activity) getContext()).overridePendingTransition(
						R.anim.between_activity_left_in,
						R.anim.between_activity_right_out);
			}
		}

	};

}
