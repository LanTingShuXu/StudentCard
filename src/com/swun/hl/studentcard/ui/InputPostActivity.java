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
 * 输入帖子的界面
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
		AlertDialogHelper.showDialogOkToExit(this, "您要放弃编辑吗？");
	}

	/**
	 * 点击发表的监听器
	 * 
	 * @param v
	 */
	public void clk_post(View v) {
		if (edt_title.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(this, "您要输入标题才能发布哟~");
		} else {
			final Toast t = Toast.makeText(this, "\n 为您提交中... \n",
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
							InputPostActivity.this, "发布成功！");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					AlertDialogHelper.showCommonDialog(InputPostActivity.this,
							"提交失败！失败信息：" + arg1);
				}
			});

		}
	}

	private void viewFind() {
		edt_content = (EditText) findViewById(R.id.aty_input_post_content);
		edt_title = (EditText) findViewById(R.id.aty_input_post_title);
	}

}
