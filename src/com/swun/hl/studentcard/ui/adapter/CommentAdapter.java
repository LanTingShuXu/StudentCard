package com.swun.hl.studentcard.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobComment;
import com.swun.hl.studentcard.bmobBean.BmobPost;

/**
 * �����б�������
 * 
 * @author ����
 * 
 */
public class CommentAdapter extends BaseAdapter {

	private BmobPost post;
	private Context context;
	private List<BmobComment> comments = null;

	public CommentAdapter(Context context, BmobPost post) {
		this.context = context;
		this.post = post;
	}

	/**
	 * ΪAdapter������������
	 * 
	 * @param comments
	 *            ��������
	 */
	public void setCommentData(List<BmobComment> comments) {
		this.comments = comments;
	}

	/**
	 * ����������������
	 * 
	 * @return ���������б�
	 */
	public List<BmobComment> getCommentsData() {
		return comments;
	}

	@Override
	public int getCount() {
		// ��һ�������߷������ӣ���˱����۶�һ����¼
		return comments == null ? 1 : comments.size() + 1;
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
		View v = null;
		ViewHolder holder;
		if (convertView == null) {
			v = LayoutInflater.from(context).inflate(
					R.layout.item_aty_post_info_comment, parent, false);
			holder = new ViewHolder();
			viewFind(v, holder);
			v.setTag(holder);
		} else {
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		valueSet(holder, position, comments);
		return v;
	}

	// ����ʹ��findViewById����
	private void viewFind(View parent, ViewHolder holder) {
		holder.tv_account = (TextView) parent
				.findViewById(R.id.item_aty_post_info_username);
		holder.tv_time = (TextView) parent
				.findViewById(R.id.item_aty_post_info_time);
		holder.tv_title = (TextView) parent
				.findViewById(R.id.item_aty_post_info_title);
		holder.tv_content = (TextView) parent
				.findViewById(R.id.item_aty_post_info_content);
		holder.authorTag = parent.findViewById(R.id.item_aty_post_info_tag);
		holder.topReply = parent.findViewById(R.id.item_aty_post_info_reply);
	}

	// ��������
	private void valueSet(ViewHolder holder, int position,
			List<BmobComment> comments) {

		// �����ġ��ظ�����־��ֻ�ڵ�һ��item����ʾ
		if (position == 1) {
			holder.topReply.setVisibility(View.VISIBLE);
		} else {
			holder.topReply.setVisibility(View.GONE);
		}

		// ��һ�������߷�������
		if (position == 0) {
			holder.tv_title.setVisibility(View.VISIBLE);
			holder.authorTag.setVisibility(View.VISIBLE);
			String name = post.getAuthor().getNick().trim();
			// ����ǳƲ�������ʹ���û���
			if ("".equals(name)) {
				name = post.getAuthor().getUsername();
			}
			holder.tv_account.setText(name);
			holder.tv_title.setText(post.getTitle().trim());
			holder.tv_content.setText(post.getContent().trim());
			holder.tv_time.setText(post.getCreatedAt());
		} else {
			holder.tv_title.setVisibility(View.GONE);
			holder.authorTag.setVisibility(View.GONE);
			BmobComment comment = comments.get(position - 1);
			String name = comment.getAuthor().getNick().trim();
			// ����ǳƲ�������ʹ���û���
			if ("".equals(name)) {
				name = comment.getAuthor().getUsername();
			}
			// ��һ��������ԭ���ߵģ������������Ӧ��ȡ��һ��
			holder.tv_account.setText(name);
			holder.tv_content.setText(comment.getContent().trim());
			holder.tv_time.setText(comment.getCreatedAt());
		}
	}

	private class ViewHolder {
		public TextView tv_account, tv_time, tv_title, tv_content;
		// ¥����־�Ͷ����ġ��ظ���
		public View authorTag, topReply;

	}

}
