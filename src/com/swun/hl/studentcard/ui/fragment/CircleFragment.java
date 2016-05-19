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
 * ��Ȧ�ӡ�Fragment
 * 
 * @author ����
 * 
 */
public class CircleFragment extends Fragment implements View.OnClickListener {

	// �������Ӱ�ť
	private View img_inputPost;
	// �û�����view������view
	private View view_userCenter, view_content;
	// �û����ĵİ�ť���۵���ť
	private ImageView img_userCenter, img_collpase;
	// �û���������ʾ�˻����ǳƵ�TextView
	private TextView tv_userCenterAccount, tv_userCenterNick;
	// ����ˢ�����
	private PullToRefreshListView pullToRefreshListView;
	// ����ˢ���������ʵListView
	private ListView lst_content;
	private PostListAdapter adapter;

	// ����������ʾ����������
	private List<BmobPost> posts = new ArrayList<BmobPost>();
	// ����û����ĵĶ����Ƿ����ڲ���
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
		// ���������Ӱ�ť
		case R.id.fragment_circle_input:
			clk_inputPost(v);
			break;
		// �����Ȧ�ӡ��ĸ������İ�ť
		case R.id.fragment_circle_img_user:
			clk_userCenter(v);
			break;
		// �����Ȧ�ӡ����ǳư�ť
		case R.id.include_fragment_circle_user_center_nick:
			clk_userCenterAccount(v);
			break;
		// �����Ȧ�ӡ����۵���ť
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
		String title = "����";
		int index = minfo.position - 1;
		BmobPost post = adapter.getData().get(index);
		// ���������ͬ����ɾ����¼
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
		// ѡ�����ɾ������
		case R.id.fragment_circle_context_menu_delete:
			deletePostSelected(item);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * ��ѡ���ɾ������ѡ���ִ��ʱ��
	 * 
	 * @param item
	 *            ��ѡ�����Ŀ����
	 */
	private void deletePostSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Toast t = Toast.makeText(getActivity(), "\n�����ύ��..\n",
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
				Toast.makeText(getActivity(), "�������ݻ�ȡʧ�ܣ�", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * �ӷ���˻�ȡ����
	 * 
	 * @param clear
	 *            true��ʾ��Ҫ���Post�б����ݣ�һ��������ˢ�£�
	 */
	private void getPostFromServer(final boolean clear) {
		int skip = 0;// ��ҳ����������
		if (!clear) {// �����Ҫ���¼��أ���ӵ�һ����ʼ����
			skip = posts.size();
		}
		BmobQuery<BmobPost> bmobQuery = new BmobQuery<BmobPost>();
		bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		bmobQuery.setLimit(30);// һ���Ի�ȡ30������
		bmobQuery.setSkip(skip);// ��������ȡ�˵�����
		bmobQuery.order("-updatedAt");// ���µ�����ǰ
		bmobQuery.include("author");
		bmobQuery.findObjects(getActivity(), new FindListener<BmobPost>() {
			@Override
			public void onSuccess(List<BmobPost> bmobPosts) {
				// �����Ҫ�����������
				if (clear) {
					posts.clear();
				}
				// �����ȡ���������Ѿ��ǿ��ˣ���ʾû����������
				if (bmobPosts == null || bmobPosts.size() == 0) {
					ToastShowHelper.show(CircleFragment.this.getActivity(),
							"û��������");
				} else {
					posts.addAll(bmobPosts);
					adapter.notifyDataSetChanged();
				}
				pullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(getActivity(), "�������ݻ�ȡʧ�ܣ�", Toast.LENGTH_SHORT)
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
	 * ͳһ���ü�����
	 */
	private void initListener() {
		img_inputPost.setOnClickListener(this);
		img_userCenter.setOnClickListener(this);
		tv_userCenterNick.setOnClickListener(this);
		img_collpase.setOnClickListener(this);
		registerForContextMenu(lst_content);
	}

	/**
	 * �����û�������ʾ�Ķ���
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
	 * �ر��û�������ʾ�Ķ���
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
	 * �л����ػ�����ʾuserCenter�����ʱ������Ͻǰ�ť״̬
	 * 
	 * @param isShowUserCenter
	 *            true ��ʾ��ʾuserCenter����
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
	 * ��ʼ���û����Ľ����е�����
	 */
	private void initUserCenter() {
		// ��ȡ��ǰ�û���Ϣ
		BmobAccount user = (BmobAccount) BmobAccount.getCurrentUser(
				CircleFragment.this.getActivity(), BmobAccount.class);
		if (user != null) {
			tv_userCenterAccount.setText(user.getUsername());
			tv_userCenterNick.setText(user.getNick());
		}
	}

	// ��ʼ��ListView
	private void initListView() {
		adapter = new PostListAdapter(posts, getActivity());
		lst_content.setAdapter(adapter);
		// ����ListView��item����¼�
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
		// ��������ˢ�º�����ˢ�µļ����¼�
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
	 * ���������ӵİ�ť��
	 * 
	 * @param v
	 */
	private void clk_inputPost(View v) {
		startActivity(new Intent(getActivity(), InputPostActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * �����Ȧ�ӡ��ĸ������İ�ť
	 * 
	 * @param v
	 */
	private void clk_userCenter(View v) {
		// �ȵ�����ִ����Ϻ�����Ӧ
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
	 * ����۵���ť
	 * 
	 * @param v
	 */
	private void clk_userCenterCollpase(View v) {
		img_userCenter.performClick();
	}

	/**
	 * ����޸��ǳ�
	 * 
	 * @param v
	 */
	private void clk_userCenterAccount(View v) {
		TextView tv = (TextView) v;
		final EditText editText = getEditteEditText(tv.getText().toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("�޸��ǳ�");
		builder.setView(editText);
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String nickName = editText.getText().toString().trim();
				// ��ȡ��ǰ�û���Ϣ
				BmobAccount user = (BmobAccount) BmobAccount.getCurrentUser(
						CircleFragment.this.getActivity(), BmobAccount.class);
				user.setNick(nickName);
				updateUserInfo(user);
			}
		}).setNegativeButton("ȡ��", null);
		builder.create().show();
	}

	// ���ɶԻ����е������
	private EditText getEditteEditText(String content) {
		EditText editText = new EditText(getActivity());
		editText.setSingleLine(true);
		editText.setMaxEms(30);// ���ʮ���ַ�
		editText.setText(content);
		Selection.setSelection(editText.getText(), content.length());
		return editText;
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param user
	 *            �û���Ϣ����
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
							"��Ϣ�޸�ʧ�ܣ�");
				}
			});
		}

	}
}
