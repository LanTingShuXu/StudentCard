package com.swun.hl.studentcard.client;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.swun.hl.studentcard.bean.CostInfo;
import com.swun.hl.studentcard.bean.Student;
import com.swun.hl.studentcard.bean.TransInfo;

/**
 * Html解析器
 * 
 * @author 何玲
 * 
 */
public class HtmlInfoParser {

	/**
	 * 将https://218.194.85.250/ExpressWeb/userInfo.action 界面解析出学生信息来
	 * 
	 * @param html
	 *            需要解析的html
	 * @return 学生对象
	 */
	public static Student parseToStudent(String html) {
		Student student = new Student();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getElementsByClass("tb_item");
		for (Element e : elements) {
			String item = e.text();
			String content = e.parent().nextElementSibling().text().trim();
			if (item.contains("姓名")) {
				student.setName(content);
			} else if (item.contains("押金")) {
				student.setCashPledge(content);
			} else if (item.contains("性别")) {
				student.setSex(content);
			} else if (item.contains("状态")) {
				student.setStatus(content);
			} else if (item.contains("创建日期")) {
				student.setCreateDate(content);
			} else if (item.contains("国家")) {
				student.setCountry(content);
			} else if (item.contains("银行卡")) {
				student.setBankCard(content);
			} else if (item.contains("身份") && !item.contains("身份证")) {
				student.setPosition(content);
			} else if (item.contains("地址")) {
				student.setAddress(content);
			} else if (item.contains("编号")) {
				student.setSerialNumber(content);
			} else if (item.contains("部门")) {
				student.setDepartment(content);
			} else if (item.contains("身份证")) {
				student.setIDCard(content);
			}
		}
		return student;
	}

	/**
	 * 将https://218.194.85.250/ExpressWeb/userBal.action 中的余额信息解析出来
	 * 
	 * @param html
	 *            网页html代码
	 * @return 解析出的余额信息
	 */
	public static String getChargeInfo(String html) {
		String info = "解析余额信息错误！";
		try {
			Document document = Jsoup.parse(html);
			Element e = document.getElementById("tip");
			String temp = e.text();
			if (temp != null) {
				int index = temp.indexOf("余额");
				info = temp.substring(index, temp.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 解析：https://218.194.85.250/ExpressWeb/cost.action 界面中包含的消费信息
	 * 
	 * @param html
	 *            网页html代码
	 * @return 消费信息集合
	 */
	public static List<CostInfo> getCostInfo(String html) {
		List<CostInfo> costInfos = new ArrayList<CostInfo>();
		Document doc = Jsoup.parse(html);
		try {
			Element element = doc.getElementById("mytable");
			Elements es = element.getAllElements();
			for (Element ee : es) {
				if (ee.tagName().equals("tr")) {
					Element e = ee.child(0);
					if (e.tagName().equals("td")) {
						CostInfo info = new CostInfo();
						info.setCost(e.text());// 获取第一个：金额

						e = e.nextElementSibling();
						info.setDepartment(e.text());// 获取第二个：部门

						e = e.nextElementSibling();
						info.setTime(e.text());// 获取第三个：时间

						e = e.nextElementSibling();
						info.setSerialNumber(e.text());// 获取第四个：端口号

						costInfos.add(info);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return costInfos;
	}

	/**
	 * 解析查询网络计费余额网页
	 * 
	 * @param html
	 * @return 余额字符串：如：当前余额：20元
	 */
	public static String parseQueryNetCharge(String html) {
		String info = "";
		if (html != null) {
			int index = html.indexOf("上网账户余额");
			int end = 0;
			if (index != -1) {
				end = html.indexOf("元", index);
			}
			if (end != 0) {
				info = html.substring(index, end);
			}
		}
		return info;
	}

	/**
	 * 解析查询圈存信息html
	 * 
	 * @param html
	 * @return 圈存信息集合
	 */
	public static List<TransInfo> parseQueryTransInfo(String html) {
		List<TransInfo> infos = new ArrayList<TransInfo>();
		Document doc = Jsoup.parse(html);
		try {
			Element element = doc.getElementById("mytable");
			Elements es = element.getAllElements();
			for (Element ee : es) {
				if (ee.tagName().equals("tr")) {
					Element e = ee.child(0);
					if (e.tagName().equals("td")) {

						TransInfo info = new TransInfo();
						info.setAccountName(e.text());// 获取第一个：姓名

						e = e.nextElementSibling();
						info.setTransMoney(e.text());// 获取第二个：金额

						e = e.nextElementSibling();
						info.setTransTime(e.text());// 获取第三个：时间

						e = e.nextElementSibling();
						info.setSerialNumber(e.text());// 获取第四个：端口号

						infos.add(info);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * 解析修改密码界面的token信息(获取网络缴费页面的token也是同样的算法)
	 * 
	 * @param html
	 *            界面html代码
	 * @return token信息
	 */
	public static String parseChangePswToken(String html) {
		String info = "";
		Document doc = Jsoup.parse(html);
		Elements es = doc.getElementsByAttributeValue("name", "token");
		Element e = es.get(0);
		if (e != null) {
			info = e.attr("value");
		}
		return info;
	}

}