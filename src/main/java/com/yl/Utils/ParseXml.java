package com.yl.Utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

public class ParseXml {
	public static String xmlElements(String xmlDoc) {
		// 创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		String s = null;
		try {
			// 通过输入源构造一个Document
			Document doc = sb.build(source);
			// 取的根元素
			Element root = doc.getRootElement();
			s = root.getValue();
//			System.out.println("2:" + root.getValue());
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {
		ParseXml doc = new ParseXml();
		String xml = "<?xml version=\"1.0\" encoding=\"gb2312\"?>"
				+ "<Result xmlns=\"http://www.fiorano.com/fesb/activity/DBQueryOnInput2/Out\">"
				+ "<row resultcount=\"1\">" + "<users_id>1001     </users_id>" + "<users_name>wangwei   </users_name>"
				+ "<users_group>80        </users_group>" + "<users_address>1001号   </users_address>" + "</row>"
				+ "<row resultcount=\"1\">" + "<users_id>1002     </users_id>" + "<users_name>wangwei   </users_name>"
				+ "<users_group>80        </users_group>" + "<users_address>1002号   </users_address>" + "</row>"
				+ "</Result>";

		String xml2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<string xmlns=\"http://tempuri.org/\">2314714261673695379</string>";
		 doc.xmlElements(xml2);
	}
}
