package com.yl.Utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class CodeUtils {

	private final static int OFFSET = 538309;
	// private static Logger log = Logger.getLogger(CodeUtils.class);
	private static DateFormat df = new SimpleDateFormat("MMyydd");

	/*
	 * // 返回一个六位随机数验证码
	 */
	public static String getCode(int num) {
		long seed = System.currentTimeMillis() + OFFSET;
		SecureRandom secureRandom = null; // 安全随机类
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed);
			// ThreadLocalRandom
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String codeList = "1234567890"; // 验证码数字取值范围
		String sRand = ""; // 定义一个验证码字符串变量
		for (int i = 0; i < num; i++) {
			// int code = secureRandom.nextInt(codeList.length() - 1); //
			// 随即生成一个0-9之间的整数
			int code = ThreadLocalRandom.current().nextInt(codeList.length() - 1);
			String rand = codeList.substring(code, code + 1);
			sRand += rand;
		}
		return sRand;
	}

	/*
	 * 返回一个三位随机数验证码
	 */
	// public static String getFiveCode(int) {
	// long seed = System.currentTimeMillis() + OFFSET;
	// SecureRandom secureRandom = null; // 安全随机类
	// try {
	// secureRandom = SecureRandom.getInstance("SHA1PRNG");
	// secureRandom.setSeed(seed);
	// // ThreadLocalRandom
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// String codeList = "1234567890"; // 验证码数字取值范围
	// String sRand = ""; // 定义一个验证码字符串变量
	// for (int i = 0; i < 5; i++) {
	// // int code = secureRandom.nextInt(codeList.length() - 1); //
	// // 随即生成一个0-9之间的整数
	// int code = ThreadLocalRandom.current().nextInt(codeList.length() - 1);
	// String rand = codeList.substring(code, code + 1);
	// sRand += rand;
	// }
	// return sRand;
	// }

	/*
	 * 返回一个四位随机数验证码
	 */
	// public static String getFourCode() {
	// long seed = System.currentTimeMillis() + OFFSET;
	// SecureRandom secureRandom = null; // 安全随机类
	// try {
	// secureRandom = SecureRandom.getInstance("SHA1PRNG");
	// secureRandom.setSeed(seed);
	// // ThreadLocalRandom
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// String codeList = "1234567890"; // 验证码数字取值范围
	// String sRand = ""; // 定义一个验证码字符串变量
	// for (int i = 0; i < 4; i++) {
	// // int code = secureRandom.nextInt(codeList.length() - 1); //
	// // 随即生成一个0-9之间的整数
	// int code = ThreadLocalRandom.current().nextInt(codeList.length() - 1);
	// String rand = codeList.substring(code, code + 1);
	// sRand += rand;
	// }
	// return sRand;
	// }

	/*
	 * 返回一个三位随机数验证码
	 */
	// public static String getThreeCode() {
	// long seed = System.currentTimeMillis() + OFFSET;
	// SecureRandom secureRandom = null; // 安全随机类
	// try {
	// secureRandom = SecureRandom.getInstance("SHA1PRNG");
	// secureRandom.setSeed(seed);
	// // ThreadLocalRandom
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// String codeList = "1234567890"; // 验证码数字取值范围
	// String sRand = ""; // 定义一个验证码字符串变量
	// for (int i = 0; i < 3; i++) {
	// // int code = secureRandom.nextInt(codeList.length() - 1); //
	// // 随即生成一个0-9之间的整数
	// int code = ThreadLocalRandom.current().nextInt(codeList.length() - 1);
	// String rand = codeList.substring(code, code + 1);
	// sRand += rand;
	// }
	// return sRand;
	// }

	/*
	 * 时间戳zifu
	 */
	public static String getfileName() {
		String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
		return fileName;
	}

	/*
	 * +月+年后两位+日
	 */
	public static String getMMYYDD() {
		String MMYYDD = df.format(Calendar.getInstance().getTime());
		return MMYYDD;
	}

	/**
	 * UserCode 拼个座app用户code（10位）：数据库最大id +日期截取（随id位数变化对应变化）
	 */
	public static String getuserCodeOfPGZ(int theLastIdOfTable) {
		int idLength=(String.valueOf(theLastIdOfTable)).length();
		String userCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		userCode = "PGZ"+userCode.substring(0, 10-idLength) +(theLastIdOfTable+1);
		return userCode;
	}
	/*
	 * UserCode（APP用户，管理员，商家）（15位）：0-9 随机6位数字 userCode
	 */
	public static String getuserCode() {
		// String userCode = getSixCode();
		
		String userCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		userCode = userCode.substring(userCode.length() - 11, userCode.length()) + getCode(4);
		return userCode;
	}
	/*
	 * 店铺编号（6位）：0-9 随机6位数字 shopNo
	 */
	public static String getshopNo() {
		String shopNo = getCode(6);
		return shopNo;
	}

	/*
	 * 景区编号：0-9 随机6位数字 scenicNo
	 */
	public static String getscenicCode() {
		String scenicCode = getCode(3);
		return scenicCode;
	}

	/*
	 * 景点编号：0-9 随机6位数字 attractsCode
	 */
	public static String getattractsCode() {
		String attractsCode = getCode(6);
		return attractsCode;
	}

	/**
	 * FileCode规则（15位）： 3位随机数+月+年后两位+日+6位userCode
	 */
	public static String getfileCode(String userCode) {
		String fileCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		fileCode = getCode(3) + getMMYYDD() +  userCode.substring(userCode.length() - 6);
		return fileCode;
	}

	/*
	 * 游乐卡绑定流水（20位）：3位随机+时间戳后11位+6位userCode
	 */
	public static String getylCardCode(String userCode) {
		String ylCardCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		ylCardCode = getCode(3) + ylCardCode.substring(ylCardCode.length() - 11, ylCardCode.length()) + userCode;
		return ylCardCode;
	}

	/*
	 * 交易流水号（20位）：3位随机数+时间戳后11位+6位userCode
	 */
	public static String gettransactionFlowCode(String userCode) {
		String transactionFlowCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		transactionFlowCode = getCode(3)
				+ transactionFlowCode.substring(transactionFlowCode.length() - 11, transactionFlowCode.length())
				+ userCode.substring(userCode.length() - 6);
		return transactionFlowCode;
	}
	
	/*
	 * 求购订单号（20位）：3位随机数+时间戳后11位+6位userCode
	 */
	public static String getOrderBuy(String userCode) {
		String transactionFlowCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		transactionFlowCode = getCode(3)
				+ transactionFlowCode.substring(transactionFlowCode.length() - 11, transactionFlowCode.length())
				+ userCode.substring(userCode.length() - 6);
		return transactionFlowCode;
	}
	/*
	 * 合并交易订单号（20位）：7位随机数+时间戳后13位
	 */
	public static String gettradeNo() {
		String tradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
		tradeNo = getCode(7) + tradeNo.substring(tradeNo.length() - 13, tradeNo.length());
		return tradeNo;
	}
	/**
	 * 退款流水号（13位）： 时间戳 13位
	 */
	public static String getRefundCode() {
		String tradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
		tradeNo =   tradeNo.substring(tradeNo.length() - 13, tradeNo.length());
		return tradeNo;
	}
	/*
	 * 订单编号（15位）：3位随机数+月+年后两位+日+6位userCode
	 */
	public static String getorderCode(String userCode) {
		// String orderCode =
		// String.valueOf(Calendar.getInstance().getTimeInMillis());
		String orderCode = getCode(3) + getMMYYDD() + userCode;
		return orderCode;
	}
	
	/*
	 * 商品编号（12位）：6位店铺编号+3位商品类型id+3位商品数（不够补全）
	 */
	public static String getgoodNo(String shopNo, Integer number, Integer num) {
		String goodNo = shopNo;
		String numberstring = getformatter(number, 3);
		String numstring = getformatter(num, 3);
		goodNo = shopNo + numberstring + numstring;
		return goodNo;
	}
 
	/*
	 * 商品条形码（15位）：6位店铺编号+4位商品数（不够补全）
	 */
	public static String getBarCode() {
		String barCode = String.valueOf(Calendar.getInstance().getTimeInMillis());
		return barCode;
	}
	
	/*
	 * 游乐卡编号（8位）：32位游乐卡类型+6位随机
	 */
	public static String getylCardNo(Integer scenicId, Integer typeId) {
		// String scenicId_str = getformatter(scenicId, 3);
		String typeId_str = getformatter(typeId, 2);
		String ylCardCode = typeId_str + getCode(6);
		return ylCardCode;
	}

	/**
	 * 补全数字
	 */
	public static String getformatter(Integer number, Integer length) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(length);
		formatter.setGroupingUsed(false);
		String s = formatter.format(number);
		return s;
	}

	// 账号隐藏 手机号 前3，后
	public static String getAccountName(String accountName) {
		String Name = null;
		if (accountName.contains("@")) {
			Name = accountName.substring(0, 1) + "****" + accountName.substring(accountName.indexOf("@")-1);
		} else {
			if (accountName.length() <= 6) {
				Name = accountName.substring(0, 3) + "****" + accountName.substring(accountName.length() - 4);
			} else {
				Name = accountName.substring(0, 3) + "****" + accountName.substring(accountName.length() - 4);
			}
		}
		return Name;
	}
	//身份证帐号隐藏 前3+11位*+后4位
	public static String getIdCard(String idCard) {
		idCard=idCard.substring(0,3)+"***********"+idCard.substring(idCard.length()-4,idCard.length());
		return idCard;
	}
	public static void main(String[] args) {
		// filecode
	/*	String s = "123212";
		String a = null;
		for (int i = 0; i < 25; i++) {
			a = getfileCode(s);
			System.out.println(a);
		}*/
		System.out.println(getRefundCode());
System.out.println(getAccountName("1501"));
		// BigDecimal dd = new BigDecimal(125.02);
		// // BigDecimal divisor = new BigDecimal(10);
		// // Integer score = dd.divide(divisor).intValue();
		// dd = dd.add(dd);
		// System.out.println(dd.setScale(2, BigDecimal.ROUND_UP));

		// String s = getfileCode("123212");
		// System.out.println(s);

	}

}
