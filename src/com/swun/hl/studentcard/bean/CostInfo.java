package com.swun.hl.studentcard.bean;

/**
 * 消费详情类
 * 
 * @author LANTINGSHUXU
 * 
 */
public class CostInfo {

	private String cost = "";// 交易金额
	private String department = "";// 营业部门
	private String time = "";// 交易时间
	private String serialNumber = "";// 终端号

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
