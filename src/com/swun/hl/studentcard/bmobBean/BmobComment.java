package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * Bmob评论对象
 * 
 * @author 何玲
 * 
 */
public class BmobComment extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String content;
	private BmobAccount author;
	private BmobPost post;// 评论的帖子

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BmobAccount getAuthor() {
		return author;
	}

	public void setAuthor(BmobAccount author) {
		this.author = author;
	}

	public BmobPost getPost() {
		return post;
	}

	public void setPost(BmobPost post) {
		this.post = post;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
