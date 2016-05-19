package com.swun.hl.studentcard.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.github.pulltorefresh.library.PullToRefreshBase;
import com.github.pulltorefresh.library.PullToRefreshListView;
import com.github.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobAccount;
import com.swun.hl.studentcard.bmobBean.BmobComment;
import com.swun.hl.studentcard.bmobBean.BmobPost;
import com.swun.hl.studentcard.ui.adapter.CommentAdapter;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;
import com.swun.hl.studentcard.utils.ToastShowHelper;

/**
 * �����������
 * 
 * @author ����
 * 
 */
public class PostInfoActivity extends Activity {
	/**
	 * Intent�д��ݵ����ӵĶ����key
	 */
	public static final String INTENT_EXTRA_KEY = "post";
	private BmobPost post;// ���Ӷ���
	// ����ˢ�����
	private PullToRefreshListView pullToRefreshListView;
	// ��ʵ��ListView
	private ListView lst_content;
	private EditText edt_comment;// ���������
	private CommentAdapter adapter;// �б�������
	private List<BmobComment> comments = new ArrayList<BmobComment>();// ���е���������
	private boolean firstIn = true;// ����Ƿ��ǵ�һ�ν���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_info);
		getIntentData();
		viewFind();
		initListView();
		queryComments(true);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// ���˵�
		MenuInflater menuInflater = getMenuInflater();
		menu.setHeaderTitle("����");
		menuInflater.inflate(R.menu.aty_post_info_menu, menu);
		menu.removeItem(R.id.aty_post_info_menu_deleteComment);
		// ��ȡ�����position
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
		int position = minfo.position - 1;
		// position=0��ʾ���߷�������
		if (position == 0) {
			return;
		}
		// ��ȡ�����е���������
		List<BmobComment> comments = adapter.getCommentsData();
		BmobComment comment = comments.get(--position);
		// ��������۵����߿���ɾ������
		if (comment.getAuthor().getObjectId()
				.equals(BmobUser.getCurrentUser(this).getObjectId())) {
			menu.clear();
			menuInflater.inflate(R.menu.aty_post_info_menu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int menuId = item.getItemId();
		switch (menuId) {
		// �����ɾ���ظ�
		case R.id.aty_post_info_menu_deleteComment:
			deleteCommentSelected(item);
			break;

		case R.id.aty_post_info_menu_copyComment:
			copyCommentSelected(item);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * �������۰�ť����¼�
	 * 
	 * @param v
	 */
	public void clk_comment(View v) {
		// ������벻�Ϸ���ֱ�ӷ���
		if (!isCommentsCorrect()) {
			return;
		}
		ToastShowHelper.show(this, "�ύ��...");
		BmobComment comment = new BmobComment();
		comment.setAuthor(BmobUser.getCurrentUser(this, BmobAccount.class));
		comment.setContent(edt_comment.getText().toString().trim());
		comment.setPost(post);
		comment.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				updateCommentNumber(1);
				ToastShowHelper.show(PostInfoActivity.this, "�ύ�ɹ���");
				edt_comment.setText("");
				queryComments(false);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastShowHelper.show(PostInfoActivity.this, "�ύʧ�ܣ����Ժ����ԣ�");
			}
		});
	}

	/**
	 * ѡ���˸��ƻظ���item
	 * 
	 * @param item
	 */
	@SuppressWarnings("deprecation")
	private void copyCommentSelected(MenuItem item) {
		// �����ݸ��Ƶ����а�
		if (android.os.Build.VERSION.SDK_INT > 11) {
			android.content.ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setPrimaryClip(ClipData.newPlainText(
					"contentCopy", getSelectedTextContent(item)));
		} else {
			android.text.ClipboardManager cManager = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cManager.setText(getSelectedTextContent(item));
		}
	}

	/**
	 * ��ȡѡ��item�а����ĵ�����
	 * 
	 * @param item
	 *            �˵�item
	 * @return ѡ��item�е��ı�����
	 */
	private String getSelectedTextContent(MenuItem item) {
		int position = getPositionFromMenuItem(item);
		// �����¥������Ϣ
		if (position == 0) {
			return "���⣺" + post.getTitle() + "\n���ݣ�" + post.getContent();
		}
		// ����ǻظ�����Ϣ
		List<BmobComment> comments = adapter.getCommentsData();
		BmobComment comment = comments.get(--position);
		return comment.getContent();
	}

	/**
	 * ѡ����ɾ���ظ���item,ɾ��ָ���ظ�
	 * 
	 * @param item
	 *            ѡ�еĲ˵�item����
	 */
	private void deleteCommentSelected(MenuItem item) {
		int position = getPositionFromMenuItem(item);
		final Toast t = Toast.makeText(this, "\n�����ύ��..\n", Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
		List<BmobComment> comments = adapter.getCommentsData();
		BmobComment comment = comments.get(--position);
		comment.delete(this, new DeleteListener() {

			@Override
			public void onSuccess() {
				t.cancel();
				updateCommentNumber(-1);
				queryComments(true);
			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}

	/**
	 * ��MenuItem�л�ȡposition��Ϣ
	 * 
	 * @param item
	 *            �˵�item����
	 * @return item��Ӧ��position
	 */
	private int getPositionFromMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		return minfo.position - 1;
	}

	/**
	 * ��ѯ�����е���������
	 * 
	 * @param clear
	 *            true��ʾ���List����
	 */
	private void queryComments(final boolean clear) {
		int skip = 0;
		if (!clear) {
			skip = comments.size();
		}
		BmobQuery<BmobComment> query = new BmobQuery<BmobComment>();
		query.addWhereEqualTo("post", post);
		query.include("author");// ϣ���ڲ�ѯ������Ϣ��ͬʱҲ�ѷ����˵���Ϣ��ѯ����
		query.setLimit(30);// һ����ȡ30������
		query.setSkip(skip);// ��ҳ
		query.findObjects(this, new FindListener<BmobComment>() {
			@Override
			public void onSuccess(List<BmobComment> bmobComments) {
				if (clear) {
					comments.clear();
				}
				// û����������
				if (bmobComments == null || bmobComments.size() == 0) {
					// ���ǵ�һ�ν���ʱ���û�ˢ����ɵģ�
					if (!firstIn) {
						ToastShowHelper.show(PostInfoActivity.this, "û��������");
					}
				} else {
					comments.addAll(bmobComments);
					adapter.notifyDataSetChanged();
				}
				firstIn = false;
				pullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				ToastShowHelper.show(PostInfoActivity.this, "���ݻ�ȡʧ�ܣ�������������");
				firstIn = false;
				pullToRefreshListView.onRefreshComplete();
			}
		});
	}

	/**
	 * ����������
	 * 
	 * @param count
	 *            ������ʾ���ӣ�������ʾ����
	 */
	private void updateCommentNumber(int count) {
		// ԭ�Ӽ���
		post.increment("commentNumber", count);
		post.update(this);
	}

	private void initListView() {
		adapter = new CommentAdapter(this, post);
		// ������������
		adapter.setCommentData(comments);
		lst_content.setAdapter(adapter);
		registerForContextMenu(lst_content);

		// ��������ˢ�º�����ˢ�µļ����¼�
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						queryComments(true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						queryComments(false);
					}
				});
	}

	/**
	 * �ж��������۵������Ƿ�Ϸ�
	 * 
	 * @return true��ʾ�Ϸ���false��ʾ���Ϸ�
	 */
	private boolean isCommentsCorrect() {
		boolean isCorrect = true;
		// �������Ϊ��
		if (edt_comment.getText().toString().trim().equals("")) {
			ToastShowHelper.show(this, "����δ��д����Ӵ~");
			return isCorrect = false;
		}
		return isCorrect;
	}

	/**
	 * ��ȡ���ݹ�����Intent�е�BmobPost����
	 */
	private void getIntentData() {
		Object o = getIntent().getSerializableExtra(INTENT_EXTRA_KEY);
		if (o == null) {
			// ������ô˽��棬û�д���Postʱ����������
			onBackPressed();
		} else {
			post = (BmobPost) o;
		}
	}

	private void viewFind() {
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.aty_post_info_listView);
		lst_content = pullToRefreshListView.getRefreshableView();
		edt_comment = (EditText) findViewById(R.id.aty_post_info_comment);
	}
}
