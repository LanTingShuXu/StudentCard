package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Bmob帖子对象
 * 
 * @author LANTINGSHUXU
 * 
 */
public class BmobPost extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String content = "";
	private String title = "";
	private Integer commentNumber = 0;
	private BmobAccount author;// 作者
	private BmobRelation likes;// 喜欢者

	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BmobAccount getAuthor() {
		return author;
	}

	public void setAuthor(BmobAccount author) {
		this.author = author;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
