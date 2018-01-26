package com.yl.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/*
 * @author  lqpl66
 * @date 创建时间：2017年9月7日 上午10:59:58 
 * @function     
 */
public class ExcelUtils {
	public static void main(String[] args) throws IOException {
//		File file = new File("D:\\a.xls");
		File file = new File("D:\\白石山.xls");
		InputStream in = new FileInputStream(file);
		HSSFWorkbook st = new HSSFWorkbook(in);
		List<String> list = new ArrayList<String>();
		// 获取第一张Sheet表
		HSSFSheet sheet = st.getSheetAt(0);
		for (int rownum = 0; rownum <= sheet.getLastRowNum(); rownum++) {
			HSSFRow hr = sheet.getRow(rownum);
			HSSFCell hc = hr.getCell(0);
			list.add(hc.getStringCellValue());
		}
//		System.out.println(list);
		for(int i =0;i<list.size();i++){
			System.out.println("'"+list.get(i)+"',");
		}
		System.out.println(list.size());
	}
}
