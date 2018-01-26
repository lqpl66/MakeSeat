package com.yl.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yl.Utils.CodeUtils;
import com.yl.Utils.Constant;
import com.yl.Utils.GetProperties;
import com.yl.Utils.MSeatUtil;
import com.yl.Utils.RegexUtil;
import com.yl.bean.PageData;
import com.yl.bean.PageInfo;
import com.yl.bean.Userinfo;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.ExpenseMapper;

import net.sf.json.JSONObject;
import sun.applet.Main;

@Service
public class ExpenseService {

	private static Logger logger = Logger.getLogger(ExpenseService.class);
	
	@Autowired
	private  ExpenseMapper expenseMapper;
	

	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

 
	/**
	 * 
	* <pre> 
	* Title: saveExpenseUserAndSystemlog
	* Description：<pre>
	*       保存用户交易流水： 充值 ， 提现 ，购买座位  ，座位出售  ，退款
	* </pre>
	* @param paymentType 交易类型
	*  2  支付宝   <font style='color:red'>expenseType 只能为 1<div style='display:none'>.</div>，2<div style='display:none'>.</div></font>
	*  3  钱包余额 <font style='color:red'>expenseType 只能为 2<div style='display:none'>-</div>，3<div style='display:none'>-</div>，4<div style='display:none'>.</div>，5<div style='display:none'>-</div>，6<div style='display:none'>-</div></font>
	* @param expenseType 流水类型
	*  1  充值
	*  2  购买座位
	*  3  出售座位  （交易完成）（给卖家用户打钱）
	*  4  提现
	*  5 卖家同意 退款-出账
	*  6 买家申请 退款-入账
	* </pre>
	* @param userinfo 用户信息  (id，employeeCode属性必须有值)
	* @param serialNo 第三方流水号
	* @param tradeNo 订单号(充值用，其他不用空即可,不用null)
	* @param paymentAmount 金额<font style='color:red'>（注意 ：金额必须大于0）</font>
	* @param orderNo   订单编号（支付，取消订单时用, 其他用空值，不用null）
	* @return    
	* @return: JSONObject 
	* </pre>
	 */
	public   JSONObject saveExpenseUserAndSystemlog(Userinfo userinfo, String serialNo, String tradeNo, BigDecimal paymentAmount,
			String orderNo, Integer paymentType, Integer expenseType
			){
		JSONObject resultMap = new JSONObject();
		
		
		if(paymentAmount.compareTo(new BigDecimal("0"))==-1){
			paymentAmount=paymentAmount.multiply(new BigDecimal("-1"));
		} //转为正数后处理
		
		// 校验参数合法性
		String isSaveSystemLog="false";
		if(userinfo==null || userinfo.getId()==null || userinfo.getEmployeeCode()==null    ){
			resultMap.put("code", Constant.code.CODE_10);
			resultMap.put("msg", Constant.message.MESSAGE_10);
			return resultMap; 
		}else{
			
			String expenseUserNo = CodeUtils.gettransactionFlowCode(userinfo.getEmployeeCode().substring(3, 9));
			paymentAmount = paymentAmount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
			JSONObject interimParamsMap=new JSONObject();

			
			
			if(paymentType==2 && expenseType==1){
				interimParamsMap.put("remark", "支付宝充值");
				interimParamsMap.put("paymentAmount", paymentAmount);
				interimParamsMap.put("payAblementAmount", paymentAmount);
				interimParamsMap.put("transferCharge", new BigDecimal("0"));
				isSaveSystemLog="true";
				
			}else if(paymentType==2 && expenseType==2){
				interimParamsMap.put("remark", "支付宝购买座位");
				interimParamsMap.put("paymentAmount", new BigDecimal("-" + paymentAmount));
				interimParamsMap.put("payAblementAmount",  new BigDecimal("-" +paymentAmount));
				interimParamsMap.put("transferCharge", new BigDecimal("0"));
				isSaveSystemLog="true";
				
			}else if(paymentType==2 && expenseType==6){
				interimParamsMap.put("remark", "支付宝退款");

				String chargeStr=GetProperties.getFileUrl("charge");
				Map chargeInfo=MSeatUtil.getUserWithDrawCash(paymentAmount, new BigDecimal(chargeStr));
				interimParamsMap.put("payAblementAmount", chargeInfo.get("payAblementAmount"));
				interimParamsMap.put("transferCharge", chargeInfo.get("transferCharge"));
				interimParamsMap.put("paymentAmount",  chargeInfo.get("paymentAmount"));
				interimParamsMap.put("out_request_no", tradeNo);
				paymentAmount=new BigDecimal(chargeInfo.get("paymentAmount")+"");

				isSaveSystemLog="true";
				
			}else if(paymentType==3 && expenseType==2){
				interimParamsMap.put("remark", "余额购买座位");
				interimParamsMap.put("paymentAmount", new BigDecimal("-" + paymentAmount));
				interimParamsMap.put("payAblementAmount",  new BigDecimal("-" +paymentAmount));
				interimParamsMap.put("transferCharge", new BigDecimal("0"));
				
			}else if(paymentType==3 && expenseType==3){
				interimParamsMap.put("remark", "出售座位");
				interimParamsMap.put("paymentAmount", paymentAmount);
				interimParamsMap.put("payAblementAmount",   paymentAmount );
				interimParamsMap.put("transferCharge", new BigDecimal("0"));
				
			}else if(paymentType==3 && expenseType==4){
				interimParamsMap.put("remark", "余额提现");
				String chargeStr=GetProperties.getFileUrl("charge");
				Map chargeInfo=MSeatUtil.getUserWithDrawCash(paymentAmount, new BigDecimal(chargeStr));
				interimParamsMap.put("payAblementAmount", new BigDecimal("-"+ chargeInfo.get("payAblementAmount")));
				interimParamsMap.put("transferCharge", chargeInfo.get("transferCharge"));
				interimParamsMap.put("paymentAmount", new BigDecimal("-"+ chargeInfo.get("paymentAmount")));
				paymentAmount=new BigDecimal(chargeInfo.get("paymentAmount")+"");

				isSaveSystemLog="true";
				
			}else if(paymentType==3 && expenseType==5){
				interimParamsMap.put("remark", "退款成功，出账");
				interimParamsMap.put("paymentAmount", new BigDecimal("-" + paymentAmount));
				interimParamsMap.put("payAblementAmount",  new BigDecimal("-" +paymentAmount));
				interimParamsMap.put("transferCharge", new BigDecimal("0"));
				
			}else if(paymentType==3 && expenseType==6){
				interimParamsMap.put("remark", "退款成功，入账");
				interimParamsMap.put("paymentAmount", paymentAmount);
				interimParamsMap.put("payAblementAmount", paymentAmount );
				interimParamsMap.put("transferCharge", new BigDecimal("0"));
				
			}else{
				resultMap.put("code", Constant.code.CODE_10);
				resultMap.put("msg", Constant.message.MESSAGE_10);
				return resultMap;
			}

			interimParamsMap.put("expenseUserNo", expenseUserNo);
			interimParamsMap.put("serialNo", serialNo);
			interimParamsMap.put("userId", userinfo.getId());
			interimParamsMap.put("paymentType", paymentType);
			interimParamsMap.put("expenseType", expenseType);
			interimParamsMap.put("orderNo", orderNo);
			interimParamsMap.put("addTime", df.format(new Date()));
			
			
		    expenseMapper.saveExpenseUserlog(interimParamsMap);
		    if(isSaveSystemLog.equals("true")){//保存系统流水
		    	String expenseSystemNo = CodeUtils.gettransactionFlowCode(userinfo.getEmployeeCode().substring(3, 9));
		    	if (expenseType == 4 || expenseType == 6) {// 平台出账，需要转为负数
					paymentAmount = new BigDecimal("-" + paymentAmount);
				}
		    	interimParamsMap.clear();
		    	interimParamsMap.put("expenseSystemNo", expenseSystemNo);
		    	interimParamsMap.put("expenseNo", expenseUserNo);
		    	interimParamsMap.put("amount", paymentAmount);
		    	interimParamsMap.put("addTime", df.format(new Date()));
				expenseMapper.saveExpenseSystemlog(interimParamsMap);
				

				if(paymentType==2 && expenseType==1  && !tradeNo.equals("")){
					interimParamsMap.clear();
					interimParamsMap.put("tradeNo", tradeNo);
					interimParamsMap.put("expenseUserNo", expenseUserNo);
					expenseMapper.updateTemporaryOrder(interimParamsMap);
				}
				
		    }

			resultMap.put("code", Constant.code.CODE_1);
			resultMap.put("msg", Constant.message.MESSAGE_1);
			return resultMap; 
		}
		
	}
}
