package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobAccount;
import com.swun.hl.studentcard.bmobBean.BmobPost;
import com.swun.hl.studentcard.utils.AlertDialogHelper;

/**
 * �������ӵĽ���
 * 
 * @author LANTINGSHUXU
 * 
 */
public class InputPostActivity extends Activity {
	private EditText edt_title, edt_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_post);
		viewFind();
	}

	@Override
	public void onBackPressed() {
		AlertDialogHelper.showDialogOkToExit(this, "��Ҫ�����༭��");
	}

	/**
	 * �������ļ�����
	 * 
	 * @param v
	 */
	public void clk_post(View v) {
		if (edt_title.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(this, "��Ҫ���������ܷ���Ӵ~");
		} else {
			final Toast t = Toast.makeText(this, "\n Ϊ���ύ��... \n",
					Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			BmobPost post = new BmobPost();
			post.setTitle(edt_title.getText().toString().trim());
			post.setContent(edt_content.getText().toString().trim());
			post.setAuthor(BmobUser.getCurrentUser(this, BmobAccount.class));
			post.save(this, new SaveListener() {
				@Override
				public void onSuccess() {
					t.cancel();
					AlertDialogHelper.showDialogOkToExit(
							InputPostActivity.this, "�����ɹ���");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					AlertDialogHelper.showCommonDialog(InputPostActivity.this,
							"�ύʧ�ܣ�ʧ����Ϣ��" + arg1);
				}
			});

		}
	}

	private void viewFind() {
		edt_content = (EditText) findViewById(R.id.aty_input_post_content);
		edt_title = (EditText) findViewById(R.id.aty_input_post_title);
	}

}
