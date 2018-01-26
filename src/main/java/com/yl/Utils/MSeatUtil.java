package com.yl.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MSeatUtil {

	/**
	* Title: getUserWithDrawCash
	* Description：<pre> 扣除费率 到手金额
	*   服务费计算规则：transferCharge（服务费）=payAblementAmount（应付金额）*charge（费率）
	*          服务费采用入
	*  </pre> 
	* @param payAblementAmount  BigDecimal 应付金额 
	* @param charge  BigDecimal  费率
	* @return: Map<String,Object>  返回结果集，均只保存到小数点后两位<pre> 
	*     payAblementAmount  BigDecimal  应付金额
	*     transferCharge     BigDecimal  服务费
	*     paymentAmount      BigDecimal  到手金额
	* </pre>  
	 */
	public static Map<String, Object>  getUserWithDrawCash(BigDecimal payAblementAmount,BigDecimal charge ){
		
		BigDecimal transferCharge=payAblementAmount.multiply(charge).setScale(2, BigDecimal.ROUND_UP);
		if(payAblementAmount.compareTo(new BigDecimal("1"))==-1){
			transferCharge=new BigDecimal("0");
		}
		BigDecimal paymentAmount=payAblementAmount.subtract(transferCharge).setScale(2, BigDecimal.ROUND_UP);
		Map<String, Object> payInfo=new HashMap<>();
		payInfo.put("payAblementAmount", payAblementAmount);//应付金额
		payInfo.put("transferCharge", transferCharge);//服务费
		payInfo.put("paymentAmount", paymentAmount);//到手金额
//		System.out.println("1应付金额:"+payAblementAmount);
//		System.out.println("2服务费:"+transferCharge);
//		System.out.println("3到手金额:"+paymentAmount);
		return payInfo;
	}
}
