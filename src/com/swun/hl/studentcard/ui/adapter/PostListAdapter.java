package com.swun.hl.studentcard.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobPost;

/**
 * 帖子列表的适配器
 * 
 * @author 何玲
 * 
 */
public class PostListAdapter extends BaseAdapter {

	private List<BmobPost> posts;
	private Context context;

	public PostListAdapter(List<BmobPost> posts, Context context) {
		super();
		this.posts = posts;
		this.context = context;
	}

	/**
	 * 为适配器设置数据
	 * 
	 * @param posts
	 */
	public void setData(List<BmobPost> posts) {
		this.posts = posts;
	}

	/**
	 * 获取适配器的数据
	 * 
	 * @return
	 */
	public List<BmobPost> getData() {
		return posts;
	}

	/**
	 * 在尾部添加数据
	 * 
	 * @param posts
	 */
	public void addData(List<BmobPost> posts) {
		if (this.posts == null) {
			this.posts = posts;
		} else {
			this.posts.addAll(posts);
		}
	}

	@Override
	public int getCount() {
		return posts == null ? 0 : posts.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(R.layout.item_cirle_post,
					parent, false);
			viewFind(holder, v);
			v.setTag(holder);
		} else {
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		valueSet(position, holder);
		return v;
	}

	/**
	 * 为控件设置值
	 * 
	 * @param position
	 * @param holder
	 */
	private void valueSet(int position, ViewHolder holder) {
		BmobPost bmobPost = posts.get(position);
		holder.tv_title.setText(bmobPost.getTitle());
		holder.tv_content.setText(bmobPost.getContent());
		String nick = "";
		// 如果昵称为空则显示用户名
		if (!bmobPost.getAuthor().getNick().trim().equals("")) {
			nick = bmobPost.getAuthor().getNick().trim();
		} else {
			nick = bmobPost.getAuthor().getUsername();
		}
		holder.tv_nick.setText(nick);
		holder.tv_time.setText(bmobPost.getUpdatedAt());
		holder.tv_comentNumber.setText("评论" + bmobPost.getCommentNumber());
	}

	private void viewFind(ViewHolder holder, View v) {
		holder.tv_comentNumber = (TextView) v
				.findViewById(R.id.item_circle_post_comentNumber);
		holder.tv_title = (TextView) v
				.findViewById(R.id.item_circle_post_title);
		holder.tv_content = (TextView) v
				.findViewById(R.id.item_circle_post_content);
		holder.tv_time = (TextView) v.findViewById(R.id.item_circle_post_date);
		holder.tv_nick = (TextView) v.findViewById(R.id.item_circle_post_nick);
	}

	class ViewHolder {
		public TextView tv_nick, tv_time, tv_title, tv_content,
				tv_comentNumber;
	}

}
