package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobUser;

/**
 * Bmob�˻�javaBean����
 * 
 * @author LANTINGSHUXU
 * 
 */
public class BmobAccount extends BmobUser {
	private static final long serialVersionUID = 1L;
	private Boolean sex = true;// true��ʾ�С�false��ʾŮ
	private String nick = "";

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
