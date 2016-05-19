package com.swun.hl.studentcard.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.github.pulltorefresh.library.PullToRefreshBase;
import com.github.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.github.pulltorefresh.library.PullToRefreshListView;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobAccount;
import com.swun.hl.studentcard.bmobBean.BmobPost;
import com.swun.hl.studentcard.ui.InputPostActivity;
import com.swun.hl.studentcard.ui.PostInfoActivity;
import com.swun.hl.studentcard.ui.adapter.PostListAdapter;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;
import com.swun.hl.studentcard.utils.ToastShowHelper;

/**
 * “圈子”Fragment
 * 
 * @author 何玲
 * 
 */
public class CircleFragment extends Fragment implements View.OnClickListener {

	// 发表帖子按钮
	private View img_inputPost;
	// 用户中心view和容器view
	private View view_userCenter, view_content;
	// 用户中心的按钮和折叠按钮
	private ImageView img_userCenter, img_collpase;
	// 用户中心中显示账户和昵称的TextView
	private TextView tv_userCenterAccount, tv_userCenterNick;
	// 下拉刷新组件
	private PullToRefreshListView pullToRefreshListView;
	// 下拉刷新组件的真实ListView
	private ListView lst_content;
	private PostListAdapter adapter;

	// 所有正在显示的帖子数据
	private List<BmobPost> posts = new ArrayList<BmobPost>();
	// 标记用户中心的动画是否正在播放
	private boolean userCenterAnimatorIsRunning = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View content = inflater.inflate(R.layout.fragment_circle, container,
				false);
		viewFind(content);
		initListener();
		initListView();
		initUserCenter();
		getPostFromServer(true);
		return content;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击添加帖子按钮
		case R.id.fragment_circle_input:
			clk_inputPost(v);
			break;
		// 点击“圈子”的个人中心按钮
		case R.id.fragment_circle_img_user:
			clk_userCenter(v);
			break;
		// 点击“圈子”的昵称按钮
		case R.id.include_fragment_circle_user_center_nick:
			clk_userCenterAccount(v);
			break;
		// 点击“圈子”的折叠按钮
		case R.id.include_fragment_circle_user_center_collpase:
			clk_userCenterCollpase(v);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String title = "操作";
		int index = minfo.position - 1;
		BmobPost post = adapter.getData().get(index);
		// 如果作者相同可以删除记录
		if (post.getAuthor()
				.getObjectId()
				.equals(BmobUser.getCurrentUser(getActivity(),
						BmobAccount.class).getObjectId())) {
			MenuInflater inflater = getActivity().getMenuInflater();
			menu.setHeaderTitle(title);
			inflater.inflate(R.menu.fragment_circle_context_menu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int menuId = item.getItemId();
		switch (menuId) {
		// 选择的是删除帖子
		case R.id.fragment_circle_context_menu_delete:
			deletePostSelected(item);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 当选择的删除帖子选项的执行时间
	 * 
	 * @param item
	 *            被选择的条目对象
	 */
	private void deletePostSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Toast t = Toast.makeText(getActivity(), "\n请求提交中..\n",
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
		int index = minfo.position;
		final BmobPost post = adapter.getData().get(index);
		post.delete(getActivity(), new DeleteListener() {
			@Override
			public void onSuccess() {
				getPostFromServer(true);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(getActivity(), "最新数据获取失败！", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * 从服务端获取帖子
	 * 
	 * @param clear
	 *            true表示需要清空Post列表数据（一般是下拉刷新）
	 */
	private void getPostFromServer(final boolean clear) {
		int skip = 0;// 分页跳过的条数
		if (!clear) {// 如果需要重新加载，则从第一条开始加载
			skip = posts.size();
		}
		BmobQuery<BmobPost> bmobQuery = new BmobQuery<BmobPost>();
		bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		bmobQuery.setLimit(30);// 一次性获取30条数据
		bmobQuery.setSkip(skip);// 跳过我们取了的数据
		bmobQuery.order("-updatedAt");// 最新的在最前
		bmobQuery.include("author");
		bmobQuery.findObjects(getActivity(), new FindListener<BmobPost>() {
			@Override
			public void onSuccess(List<BmobPost> bmobPosts) {
				// 如果需要清空帖子数据
				if (clear) {
					posts.clear();
				}
				// 如果获取到的数据已经是空了，表示没有新数据了
				if (bmobPosts == null || bmobPosts.size() == 0) {
					ToastShowHelper.show(CircleFragment.this.getActivity(),
							"没有新数据");
				} else {
					posts.addAll(bmobPosts);
					adapter.notifyDataSetChanged();
				}
				pullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(getActivity(), "最新数据获取失败！", Toast.LENGTH_SHORT)
						.show();
				pullToRefreshListView.onRefreshComplete();
			}
		});
	}

	private void viewFind(View v) {
		img_inputPost = v.findViewById(R.id.fragment_circle_input);
		pullToRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.fragment_circle_list);
		lst_content = pullToRefreshListView.getRefreshableView();
		view_userCenter = v.findViewById(R.id.fragment_circle_userCenter);
		img_userCenter = (ImageView) v
				.findViewById(R.id.fragment_circle_img_user);
		view_content = v.findViewById(R.id.fragment_circle_content);
		tv_userCenterAccount = (TextView) v
				.findViewById(R.id.include_fragment_circle_user_center_account);
		tv_userCenterNick = (TextView) v
				.findViewById(R.id.include_fragment_circle_user_center_nick);
		img_collpase = (ImageView) v
				.findViewById(R.id.include_fragment_circle_user_center_collpase);
	}

	/**
	 * 统一设置监听器
	 */
	private void initListener() {
		img_inputPost.setOnClickListener(this);
		img_userCenter.setOnClickListener(this);
		tv_userCenterNick.setOnClickListener(this);
		img_collpase.setOnClickListener(this);
		registerForContextMenu(lst_content);
	}

	/**
	 * 开启用户中心显示的动画
	 */
	private void startUserCenterInAnimator() {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view_userCenter,
				View.TRANSLATION_Y, -view_content.getHeight(), 0);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.setDuration(400);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				view_userCenter.setVisibility(View.VISIBLE);
				userCenterAnimatorIsRunning = true;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				userCenterAnimatorIsRunning = false;
			}

		});
		animator.start();
	}

	/**
	 * 关闭用户中心显示的动画
	 */
	private void hideUserCenterOutAnimator() {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view_userCenter,
				View.TRANSLATION_Y, 0, -view_content.getHeight());
		animator.setInterpolator(new DecelerateInterpolator(0.5f));
		animator.setDuration(200);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				userCenterAnimatorIsRunning = true;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				view_userCenter.setVisibility(View.GONE);
				userCenterAnimatorIsRunning = false;
			}
		});
		animator.start();
	}

	/**
	 * 切换隐藏或者显示userCenter界面的时候的右上角按钮状态
	 * 
	 * @param isShowUserCenter
	 *            true 表示显示userCenter界面
	 */
	private void switchUserActionImage(final boolean isShowUserCenter) {
		ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(img_userCenter,
				View.ALPHA, 1, 0);
		hideAnimator.setDuration(300);
		hideAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				if (isShowUserCenter) {
					img_userCenter
							.setImageResource(R.drawable.fragment_circle_user_show);
				} else {
					img_userCenter
							.setImageResource(R.drawable.fragment_circle_user_hide);
				}
			}
		});

		ObjectAnimator showAnimator = ObjectAnimator.ofFloat(img_userCenter,
				View.ALPHA, 0, 1);
		showAnimator.setDuration(300);
		showAnimator.setStartDelay(300);

		hideAnimator.start();
		showAnimator.start();

	}

	/**
	 * 初始化用户中心界面中的数据
	 */
	private void initUserCenter() {
		// 获取当前用户信息
		BmobAccount user = (BmobAccount) BmobAccount.getCurrentUser(
				CircleFragment.this.getActivity(), BmobAccount.class);
		if (user != null) {
			tv_userCenterAccount.setText(user.getUsername());
			tv_userCenterNick.setText(user.getNick());
		}
	}

	// 初始化ListView
	private void initListView() {
		adapter = new PostListAdapter(posts, getActivity());
		lst_content.setAdapter(adapter);
		// 设置ListView的item点击事件
		lst_content.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BmobPost post = adapter.getData().get(position - 1);
				Intent intent = new Intent(CircleFragment.this.getActivity(),
						PostInfoActivity.class);
				intent.putExtra(PostInfoActivity.INTENT_EXTRA_KEY, post);
				CircleFragment.this.getActivity().startActivity(intent);
				Anim_BetweenActivity.leftOut_rightIn(CircleFragment.this
						.getActivity());
			}

		});
		// 设置下拉刷新和上拉刷新的监听事件
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getPostFromServer(true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getPostFromServer(false);
					}
				});

	}

	/**
	 * 点击添加帖子的按钮。
	 * 
	 * @param v
	 */
	private void clk_inputPost(View v) {
		startActivity(new Intent(getActivity(), InputPostActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击“圈子”的个人中心按钮
	 * 
	 * @param v
	 */
	private void clk_userCenter(View v) {
		// 等到动画执行完毕后再相应
		if (userCenterAnimatorIsRunning) {
			return;
		}
		if (view_userCenter.isShown()) {
			hideUserCenterOutAnimator();
			switchUserActionImage(false);
		} else {
			initUserCenter();
			startUserCenterInAnimator();
			switchUserActionImage(true);
		}
	}

	/**
	 * 点击折叠按钮
	 * 
	 * @param v
	 */
	private void clk_userCenterCollpase(View v) {
		img_userCenter.performClick();
	}

	/**
	 * 点击修改昵称
	 * 
	 * @param v
	 */
	private void clk_userCenterAccount(View v) {
		TextView tv = (TextView) v;
		final EditText editText = getEditteEditText(tv.getText().toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("修改昵称");
		builder.setView(editText);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String nickName = editText.getText().toString().trim();
				// 获取当前用户信息
				BmobAccount user = (BmobAccount) BmobAccount.getCurrentUser(
						CircleFragment.this.getActivity(), BmobAccount.class);
				user.setNick(nickName);
				updateUserInfo(user);
			}
		}).setNegativeButton("取消", null);
		builder.create().show();
	}

	// 生成对话框中的输入框
	private EditText getEditteEditText(String content) {
		EditText editText = new EditText(getActivity());
		editText.setSingleLine(true);
		editText.setMaxEms(30);// 最长三十个字符
		editText.setText(content);
		Selection.setSelection(editText.getText(), content.length());
		return editText;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 *            用户信息对象
	 */
	private void updateUserInfo(BmobAccount user) {
		if (user != null) {
			user.update(getActivity(), new UpdateListener() {

				@Override
				public void onSuccess() {
					initUserCenter();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					ToastShowHelper.show(CircleFragment.this.getActivity(),
							"信息修改失败！");
				}
			});
		}

	}
}
