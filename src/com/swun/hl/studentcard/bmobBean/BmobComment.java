package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * Bmob���۶���
 * 
 * @author ����
 * 
 */
public class BmobComment extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String content;
	private BmobAccount author;
	private BmobPost post;// ���۵�����

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
