package com.swun.hl.studentcard.bean;

import java.io.Serializable;

/**
 * 学生javaBean对象。包含了学员的基本信息
 * 
 * @author 李长军
 * 
 */
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name = "";
	private String sex = "";
	private String country = "";
	private String position = "";// 身份：学生？教师？其他？
	private String department = "";// 部门：****级学生
	private String cashPledge = "";// 押金
	private String status = "";// 状态：正常
	private String createDate = "";// 创建日期
	private String bankCard = "";// 银行卡号
	private String address = "";// 地址
	private String serialNumber = "";// 编号：201231104068
	private String IDCard = "";// 身份证号码

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCashPledge() {
		return cashPledge;
	}

	public void setCashPledge(String cashPledge) {
		this.cashPledge = cashPledge;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

}
