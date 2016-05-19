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
 * 帖子详情界面
 * 
 * @author 何玲
 * 
 */
public class PostInfoActivity extends Activity {
	/**
	 * Intent中传递的帖子的对象的key
	 */
	public static final String INTENT_EXTRA_KEY = "post";
	private BmobPost post;// 帖子对象
	// 下拉刷新组件
	private PullToRefreshListView pullToRefreshListView;
	// 真实的ListView
	private ListView lst_content;
	private EditText edt_comment;// 评论输入框
	private CommentAdapter adapter;// 列表适配器
	private List<BmobComment> comments = new ArrayList<BmobComment>();// 所有的评论数据
	private boolean firstIn = true;// 标记是否是第一次进入

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
		// 填充菜单
		MenuInflater menuInflater = getMenuInflater();
		menu.setHeaderTitle("操作");
		menuInflater.inflate(R.menu.aty_post_info_menu, menu);
		menu.removeItem(R.id.aty_post_info_menu_deleteComment);
		// 获取点击的position
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
		int position = minfo.position - 1;
		// position=0表示作者发的帖子
		if (position == 0) {
			return;
		}
		// 获取到所有的评论数据
		List<BmobComment> comments = adapter.getCommentsData();
		BmobComment comment = comments.get(--position);
		// 如果是评论的作者可以删除帖子
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
		// 点击是删除回复
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
	 * 发表评论按钮点击事件
	 * 
	 * @param v
	 */
	public void clk_comment(View v) {
		// 如果输入不合法则直接返回
		if (!isCommentsCorrect()) {
			return;
		}
		ToastShowHelper.show(this, "提交中...");
		BmobComment comment = new BmobComment();
		comment.setAuthor(BmobUser.getCurrentUser(this, BmobAccount.class));
		comment.setContent(edt_comment.getText().toString().trim());
		comment.setPost(post);
		comment.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				updateCommentNumber(1);
				ToastShowHelper.show(PostInfoActivity.this, "提交成功！");
				edt_comment.setText("");
				queryComments(false);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastShowHelper.show(PostInfoActivity.this, "提交失败，请稍后再试！");
			}
		});
	}

	/**
	 * 选择了复制回复的item
	 * 
	 * @param item
	 */
	@SuppressWarnings("deprecation")
	private void copyCommentSelected(MenuItem item) {
		// 将内容复制到剪切板
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
	 * 获取选中item中包含的的内容
	 * 
	 * @param item
	 *            菜单item
	 * @return 选中item中的文本内容
	 */
	private String getSelectedTextContent(MenuItem item) {
		int position = getPositionFromMenuItem(item);
		// 如果是楼主的消息
		if (position == 0) {
			return "标题：" + post.getTitle() + "\n内容：" + post.getContent();
		}
		// 如果是回复的消息
		List<BmobComment> comments = adapter.getCommentsData();
		BmobComment comment = comments.get(--position);
		return comment.getContent();
	}

	/**
	 * 选择了删除回复的item,删除指定回复
	 * 
	 * @param item
	 *            选中的菜单item对象
	 */
	private void deleteCommentSelected(MenuItem item) {
		int position = getPositionFromMenuItem(item);
		final Toast t = Toast.makeText(this, "\n请求提交中..\n", Toast.LENGTH_SHORT);
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
	 * 从MenuItem中获取position信息
	 * 
	 * @param item
	 *            菜单item对象
	 * @return item对应的position
	 */
	private int getPositionFromMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		return minfo.position - 1;
	}

	/**
	 * 查询出所有的评论数据
	 * 
	 * @param clear
	 *            true表示清空List数据
	 */
	private void queryComments(final boolean clear) {
		int skip = 0;
		if (!clear) {
			skip = comments.size();
		}
		BmobQuery<BmobComment> query = new BmobQuery<BmobComment>();
		query.addWhereEqualTo("post", post);
		query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
		query.setLimit(30);// 一次性取30条数据
		query.setSkip(skip);// 分页
		query.findObjects(this, new FindListener<BmobComment>() {
			@Override
			public void onSuccess(List<BmobComment> bmobComments) {
				if (clear) {
					comments.clear();
				}
				// 没有新数据了
				if (bmobComments == null || bmobComments.size() == 0) {
					// 不是第一次进入时（用户刷新造成的）
					if (!firstIn) {
						ToastShowHelper.show(PostInfoActivity.this, "没有数据啦");
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
				ToastShowHelper.show(PostInfoActivity.this, "数据获取失败，请检查网络设置");
				firstIn = false;
				pullToRefreshListView.onRefreshComplete();
			}
		});
	}

	/**
	 * 更新评论数
	 * 
	 * @param count
	 *            正数表示增加，负数表示减少
	 */
	private void updateCommentNumber(int count) {
		// 原子计数
		post.increment("commentNumber", count);
		post.update(this);
	}

	private void initListView() {
		adapter = new CommentAdapter(this, post);
		// 设置评论数据
		adapter.setCommentData(comments);
		lst_content.setAdapter(adapter);
		registerForContextMenu(lst_content);

		// 设置下拉刷新和上拉刷新的监听事件
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
	 * 判断输入评论的数据是否合法
	 * 
	 * @return true表示合法，false表示不合法
	 */
	private boolean isCommentsCorrect() {
		boolean isCorrect = true;
		// 如果输入为空
		if (edt_comment.getText().toString().trim().equals("")) {
			ToastShowHelper.show(this, "您还未填写内容哟~");
			return isCorrect = false;
		}
		return isCorrect;
	}

	/**
	 * 获取传递过来的Intent中的BmobPost对象
	 */
	private void getIntentData() {
		Object o = getIntent().getSerializableExtra(INTENT_EXTRA_KEY);
		if (o == null) {
			// 如果调用此界面，没有传递Post时，立即返回
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
