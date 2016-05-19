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
 * Html������
 * 
 * @author ����
 * 
 */
public class HtmlInfoParser {

	/**
	 * ��https://218.194.85.250/ExpressWeb/userInfo.action ���������ѧ����Ϣ��
	 * 
	 * @param html
	 *            ��Ҫ������html
	 * @return ѧ������
	 */
	public static Student parseToStudent(String html) {
		Student student = new Student();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getElementsByClass("tb_item");
		for (Element e : elements) {
			String item = e.text();
			String content = e.parent().nextElementSibling().text().trim();
			if (item.contains("����")) {
				student.setName(content);
			} else if (item.contains("Ѻ��")) {
				student.setCashPledge(content);
			} else if (item.contains("�Ա�")) {
				student.setSex(content);
			} else if (item.contains("״̬")) {
				student.setStatus(content);
			} else if (item.contains("��������")) {
				student.setCreateDate(content);
			} else if (item.contains("����")) {
				student.setCountry(content);
			} else if (item.contains("���п�")) {
				student.setBankCard(content);
			} else if (item.contains("���") && !item.contains("���֤")) {
				student.setPosition(content);
			} else if (item.contains("��ַ")) {
				student.setAddress(content);
			} else if (item.contains("���")) {
				student.setSerialNumber(content);
			} else if (item.contains("����")) {
				student.setDepartment(content);
			} else if (item.contains("���֤")) {
				student.setIDCard(content);
			}
		}
		return student;
	}

	/**
	 * ��https://218.194.85.250/ExpressWeb/userBal.action �е������Ϣ��������
	 * 
	 * @param html
	 *            ��ҳhtml����
	 * @return �������������Ϣ
	 */
	public static String getChargeInfo(String html) {
		String info = "���������Ϣ����";
		try {
			Document document = Jsoup.parse(html);
			Element e = document.getElementById("tip");
			String temp = e.text();
			if (temp != null) {
				int index = temp.indexOf("���");
				info = temp.substring(index, temp.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * ������https://218.194.85.250/ExpressWeb/cost.action �����а�����������Ϣ
	 * 
	 * @param html
	 *            ��ҳhtml����
	 * @return ������Ϣ����
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
						info.setCost(e.text());// ��ȡ��һ�������

						e = e.nextElementSibling();
						info.setDepartment(e.text());// ��ȡ�ڶ���������

						e = e.nextElementSibling();
						info.setTime(e.text());// ��ȡ��������ʱ��

						e = e.nextElementSibling();
						info.setSerialNumber(e.text());// ��ȡ���ĸ����˿ں�

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
	 * ������ѯ����Ʒ������ҳ
	 * 
	 * @param html
	 * @return ����ַ������磺��ǰ��20Ԫ
	 */
	public static String parseQueryNetCharge(String html) {
		String info = "";
		if (html != null) {
			int index = html.indexOf("�����˻����");
			int end = 0;
			if (index != -1) {
				end = html.indexOf("Ԫ", index);
			}
			if (end != 0) {
				info = html.substring(index, end);
			}
		}
		return info;
	}

	/**
	 * ������ѯȦ����Ϣhtml
	 * 
	 * @param html
	 * @return Ȧ����Ϣ����
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
						info.setAccountName(e.text());// ��ȡ��һ��������

						e = e.nextElementSibling();
						info.setTransMoney(e.text());// ��ȡ�ڶ��������

						e = e.nextElementSibling();
						info.setTransTime(e.text());// ��ȡ��������ʱ��

						e = e.nextElementSibling();
						info.setSerialNumber(e.text());// ��ȡ���ĸ����˿ں�

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
	 * �����޸���������token��Ϣ(��ȡ����ɷ�ҳ���tokenҲ��ͬ�����㷨)
	 * 
	 * @param html
	 *            ����html����
	 * @return token��Ϣ
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