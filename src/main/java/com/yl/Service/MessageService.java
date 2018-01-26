package com.yl.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yl.Http.httpUtilPushMessage;
import com.yl.Push.AndroidNotification;
import com.yl.Push.PushConfig;
import com.yl.Push.Android.AndroidUnicast;
import com.yl.Push.IOS.IOSUnicast;
import com.yl.Utils.BaseParseImage;
import com.yl.Utils.CodeUtils;
import com.yl.Utils.CommonDateParseUtil;
import com.yl.Utils.Constant;
import com.yl.Utils.DateUtil;
import com.yl.Utils.FileUtils;
import com.yl.Utils.GetProperties;
import com.yl.Utils.RegexUtil;
import com.yl.Utils.SystemMessageTemplate;
import com.yl.bean.Message;
import com.yl.bean.PushMessageDevice;
import com.yl.bean.Userinfo;
import com.yl.mapper.EmployeeMapper;
import com.yl.mapper.MessageMapper;
import com.yl.mapper.SystemMapper;
import net.sf.json.JSONObject;

/*
 * 消息处理
 */
@Service
public class MessageService {

	@Autowired
	private SystemService systemService;
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private EmployeeMapper employeeMapper;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 * @Title: SaveAPPDevice
	 * @Description: 唤醒app保存驱动信息
	 * 
	 *               <pre style='font-weight:bold;color:#d2d2d2'>
	 * 业务分析：
	 * 1：一台设备，
	 * 1.1-uuid 为空，
	 *     1.1.1-设备已存在
	 *           1.1.1.1-设备已绑定 >解除绑定
	 *           1.1.1.2-设备未绑定 >（不做操作）
	 *     1.1.2-设备不存在 >设备入库
	 * 1.2-uuid 不为空  ，校验登录状态
	 *     1.2.1-如果用户uuid登录：
	 *           1.2.1.1-设备是否存在
	 *                   1.2.1.1.1-有设备
	 *                            1.2.1.1.1.1-用户绑定是当前设备 >（不做任何操作）
	 *                            1.2.1.1.1.2-用户绑定不是当前设备 >发送提示到之前手机通知（并解除绑定），绑定当前设备
	 *                   1.2.1.1.2-没设备 
	 *                            1.2.1.1.2.1-用户有绑定  >发送提示到之前手机通知（并解除绑定），绑定当前设备
	 *                            1.2.1.1.2.2-用户没绑定  >新绑定
	 *     1.2.2-如果用户uuid登录失效：
	 *           1.2.2.1-设备存在  > 该设备关联用户清除
	 *           1.2.2.1-设备不存在 > 新增设备
	 *               </pre>
	 * 
	 * @param paramsMap
	 *            mbSystemType int 1 ios 2android
	 * @param resultMap
	 *            void
	 */
	@SuppressWarnings("unchecked")
	public void SaveAPPDevice(JSONObject paramsMap, JSONObject resultMap) {
		// 临时参数
		JSONObject interimMap = new JSONObject();
		JSONObject interimResultMap = new JSONObject();
		Integer mbSystemType = paramsMap.optInt("mbSystemType");
		// 1： 一台设备
		String device_token = paramsMap.optString("device_token");
		String uuid = paramsMap.optString("uuid");
		Map<String, Object> mbSystemTypeMap = new HashMap<>();
		// 先查询设备消息关联是否存在，如果不存在则创建关联关系

		interimMap.clear();
		interimMap.put("device_token", device_token);
		int count = messageMapper.selectSystemmessageDeviceStatus(interimMap);
		if (count == 0) {
			// 如果设备为新设备，如果系统消息，活动消息存在有效期内消息，创建关联关系
			interimMap.clear();
			Map mtOnu = messageMapper.getMSGTotalOfNewUser(interimMap);

			if (mtOnu != null) {
				String sysmMaxAeadTime = (String) mtOnu.get("sysmMaxAeadTime");
				int sysmNum = Integer.parseInt(mtOnu.get("sysmNum") + "");
				String acmMaxAeadTime = (String) mtOnu.get("acmMaxAeadTime");
				int acmNum = Integer.parseInt(mtOnu.get("acmNum") + "");
				if (sysmNum != 0) {
					sysmNum = 1;
					System.out.println("发现新设备");
				} else {
					sysmNum = 0;
				}
				if (acmNum != 0) {
					acmNum = 1;
				} else {
					acmNum = 0;
				}
				interimMap.put("device_token", device_token);
				interimMap.put("mbSystemType", mbSystemType);
				interimMap.put("SI_aeadDate", sysmMaxAeadTime);
				interimMap.put("systemInfo_isRead", sysmNum);
				interimMap.put("AI_aeadDate", acmMaxAeadTime);
				interimMap.put("activityInfo_isRead", acmNum);
				messageMapper.saveSystemmessageDeviceStatus(interimMap);
			}
		}

		mbSystemTypeMap.put("1", 1);
		mbSystemTypeMap.put("2", 2);
		if (device_token.isEmpty() || mbSystemType == 0) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			if (mbSystemTypeMap.get(mbSystemType.toString()) == null) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				// 1.1 uuid空
				if (uuid.isEmpty()) {
					List<PushMessageDevice> pmdListbyDevicve = getPushMessageDeviceList(null, "2", device_token,
							mbSystemType);
					// 1.1.1--设备已存在 (查询是否存在该)
					if (pmdListbyDevicve.size() > 0) {
						PushMessageDevice pmd = pmdListbyDevicve.get(0);
						// 1.1.1.1-设备已绑定 >解除绑定
						if (pmd.getOperateId() != null && pmd.getOperateId() != 0) {
							updatePushMessageDevice(0, null, device_token, mbSystemType);
							interimResultMap.put("code", Constant.code.CODE_1);
							interimResultMap.put("msg", Constant.message.MESSAGE_1);
							// 1.1.1.2-设备未绑定 >（不做操作）
						} else {
							interimResultMap.put("code", Constant.code.CODE_1);
							interimResultMap.put("msg", Constant.message.MESSAGE_1);
						}
						// 1.1.2-设备不存在 >新增绑定
					} else {

						savePushMessageDevice(0, 0, device_token, mbSystemType);
						interimResultMap.put("code", Constant.code.CODE_1);
						interimResultMap.put("msg", Constant.message.MESSAGE_1);
					}
					// 1.2不为空，校验登录状态
				} else {
					interimMap.put("uuid", uuid);
					systemService.validUser(interimMap, interimResultMap);
					// 1.2.1-如果用户uuid登录：
					if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
						Map<String, Object> em = (Map<String, Object>) interimResultMap.opt("data");
						Integer operateId = (Integer) em.get("id");
						// 获取用户下设备
						List<PushMessageDevice> pmdListbyUser = getPushMessageDeviceList(operateId, "1", device_token,
								mbSystemType);
						// 获取设备
						List<PushMessageDevice> isExistDeviceList = getPushMessageDeviceList(null, "2", device_token,
								mbSystemType);

						// 1.2.1.1-设备是否存在
						if (isExistDeviceList != null && isExistDeviceList.size() > 0) {
							if (pmdListbyUser != null && pmdListbyUser.size() > 0) {
								for (PushMessageDevice pd : pmdListbyUser) {

									// 1.2.1.1.1.1-用户绑定是当前设备 >（不做任何操作）
									if (pd.getDevice_token().equals(device_token)
											&& pd.getMbSystemType().equals(mbSystemType)) {

										// 1.2.1.1.1.2-用户绑定不是当前设备
										// >发送提示到之前手机通知（并解除绑定），绑定当前设备
									} else {
										String content = "您的账号(" + CodeUtils.getAccountName(em.get("amount").toString())
												+ ")在另一个地方登录，请重新登录；如果不是您本人操作请及时修改密码!";
										if (pd.getMbSystemType() == 1) {// IOS
											pushUserMessageByIOSUnicast(pd.getDevice_token(), content, "1", "6");
										} else if (pd.getMbSystemType() == 2) {// Android
											pushUserMessageByAndroidUnicast(pd.getDevice_token(), content, "6", null);
										}
										// 解除用户下所有绑定设备
										updatePushMessageDevice(0, operateId, null, null);
										// 绑定当前设备
										updatePushMessageDevice(operateId, null, device_token, mbSystemType);
										interimResultMap.put("code", Constant.code.CODE_1);
										interimResultMap.put("msg", Constant.message.MESSAGE_1);
									}
								}
								// 如果设备存在，
							} else {
								updatePushMessageDevice(operateId, null, device_token, mbSystemType);
								interimResultMap.put("code", Constant.code.CODE_1);
								interimResultMap.put("msg", Constant.message.MESSAGE_1);
							}

							// 1.2.1.1.2-如果没设备 新增绑定（）
						} else {
							/* 获取用户下的绑定信息 */
							List<PushMessageDevice> pmdl = getPushMessageDeviceList(operateId, "1", null, null);
							// 1.2.1.1.2.1：-用户之前有绑定 >发送提示到之前手机通知（并解除绑定），绑定当前设备
							if (pmdl != null && pmdl.size() > 0) {

								for (PushMessageDevice pd : pmdListbyUser) {
									String content = "您的账号(" + CodeUtils.getAccountName(em.get("amount").toString())
											+ ")在另一个地方登录，请重新登录；如果不是您本人操作请及时修改密码!";
									if (pd.getMbSystemType() == 1) {// IOS
										pushUserMessageByIOSUnicast(pd.getDevice_token(), content, "1", null);
									} else if (pd.getMbSystemType() == 2) {// Android
										pushUserMessageByAndroidUnicast(pd.getDevice_token(), content, null, null);
									}
									// 解除用户下所有绑定设备
									updatePushMessageDevice(0, operateId, null, null);
									// 绑定当前设备
									savePushMessageDevice(operateId, 0, device_token, mbSystemType);
									interimResultMap.put("code", Constant.code.CODE_1);
									interimResultMap.put("msg", Constant.message.MESSAGE_1);
								}
								// 1.2.1.1.2.2：-用户之前没有绑定 >新绑定
							} else {
								savePushMessageDevice(operateId, 0, device_token, mbSystemType);
								interimResultMap.put("code", Constant.code.CODE_1);
								interimResultMap.put("msg", Constant.message.MESSAGE_1);
							}
						}
						// 1.2.2-如果用户uuid登录失效：
					} else {
						List<PushMessageDevice> pmdl = getPushMessageDeviceList(null, "2", device_token, mbSystemType);
						// 1.2.2.1-设备存在 > 该设备关联用户清除
						if (pmdl != null && pmdl.size() > 0) {
							updatePushMessageDevice(0, null, device_token, mbSystemType);
							interimResultMap.put("code", Constant.code.CODE_1);
							interimResultMap.put("msg", Constant.message.MESSAGE_1);
							// 1.2.2.1-设备不存在 > 新增设备
						} else {
							savePushMessageDevice(0, 0, device_token, mbSystemType);
							interimResultMap.put("code", Constant.code.CODE_1);
							interimResultMap.put("msg", Constant.message.MESSAGE_1);
						}
					}
				}
			}
		}
		resultMap.put("code", interimResultMap.optString("code"));
		resultMap.put("msg", interimResultMap.optString("msg"));

	}

	public void savePushMessageDevice(Integer operateId, Integer operateType, String device_token,
			Integer mbSystemType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.clear();
		/*
		 * map.put("placeX", new BigDecimal(x).setScale(6,
		 * BigDecimal.ROUND_HALF_UP)); map.put("placeY", new
		 * BigDecimal(y).setScale(6, BigDecimal.ROUND_HALF_UP));
		 */
		map.put("operateId", operateId);
		map.put("operateType", operateType);
		map.put("device_token", device_token);
		map.put("mbSystemType", mbSystemType);
		map.put("modifyTime", df.format(new Date()));
		map.put("addTime", df.format(new Date()));
		messageMapper.savePushMessageDevice(map);
	}

	/**
	 * 
	 * <pre>
	* &#64;Title: getPushMessageDeviceList 
	* &#64;Description:  
	* &#64;param operateId
	* &#64;param type 1：查出该用户下所有关联的设备；2：查出该设备下所有关联的用户，
	* &#64;param device_token
	* &#64;param mbSystemType
	* &#64;return
	 * </pre>
	 * 
	 * @returnType: List<PushMessageDevice>
	 */
	public List<PushMessageDevice> getPushMessageDeviceList(Integer operateId, String type, String device_token,
			Integer mbSystemType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.clear();
		if (type.equals("1")) {
			map.put("operateId", operateId);
		} else if (type.equals("2")) {
			map.put("device_token", device_token);
			map.put("mbSystemType", mbSystemType);
		}
		List<PushMessageDevice> pmdList = messageMapper.getPushMessageDevice(map);
		return pmdList;
	}

	/**
	 *
	 * @Title: pushUserMessageUnicastOfPGZ
	 * @Description: 拼个座消息推送
	 * @param interimMap
	 *            请求参数集
	 * 
	 *            <pre>
	*    mbSystemType   设备类型       必填    1 IOS 2 android
	*    device_token  设备唯一编号  必填
	*    messageContent 推送内容       必填
	*    tagType        消息类型       必填    0：系统通知；1：订单通知；2：活动通知；3：帐号变动,4：只唤醒APP,5,订单完成，（带订单编号）；
	 *            </pre>
	 * 
	 * @param interimResultMap
	 *            返回结果集
	 * 
	 *            <pre>
	 *   
	*    code   状态吗        0001操作成功！0002参数不全
	*    msg    信息说明
	 *            </pre>
	 * 
	 *            void
	 */
	public void pushUserMessageUnicastOfPGZ(JSONObject interimMap, JSONObject interimResultMap) {
		try {
			String device_token = interimMap.optString("device_token"),
					messageContent = interimMap.optString("messageContent"), tagType = interimMap.optString("tagType"),
					mbSystemType = interimMap.optString("mbSystemType"), type = interimMap.optString("type"),
					orderNo = interimMap.optString("orderNo");
			if (tagType.isEmpty() || device_token.isEmpty() || messageContent.isEmpty() || mbSystemType.isEmpty()) {
				interimResultMap.put("code", Constant.code.CODE_2);
				interimResultMap.put("msg", Constant.message.MESSAGE_2);
			} else {
				switch (mbSystemType) {
				case "1":// ios推送
					pushUserMessageByIOSUnicast(device_token, messageContent, type, tagType);

					interimResultMap.put("code", Constant.code.CODE_1);
					interimResultMap.put("msg", Constant.message.MESSAGE_1);
					break;
				case "2":// android推送
					pushUserMessageByAndroidUnicast(device_token, messageContent, tagType, orderNo);
					interimResultMap.put("code", Constant.code.CODE_1);
					interimResultMap.put("msg", Constant.message.MESSAGE_1);
					break;
				default:
					interimResultMap.put("code", Constant.code.CODE_10);
					interimResultMap.put("msg", Constant.message.MESSAGE_10);
					break;
				}

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * <pre>
	* &#64;Title: updatePushMessageDevice 
	* &#64;Description:  
	* &#64;param operateId （修改值） 用户id 
	* 
	* &#64;param operateIdWhere （条件） 用户id 
	* &#64;param device_token （条件） 设备编号
	* &#64;param mbSystemType （条件）  设备类型   1 IOS，2 Android
	 * </pre>
	 * 
	 * @returnType: void
	 */
	public void updatePushMessageDevice(Integer operateId, Integer operateIdWhere, String device_token,
			Integer mbSystemType) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("operateId1", operateId);
		map.put("operateId", operateIdWhere);
		map.put("modifyTime", df.format(new Date()));
		map.put("device_token", device_token);
		map.put("mbSystemType", mbSystemType);
		messageMapper.updatePushMessageDevice(map);
	}

	// 消息推送
	/**
	 * 
	 * @description
	 * @param tagType
	 *            0：系统通知；1：订单通知；2：活动通知；3：帐号变动,4：只唤醒APP,5,订单完成，（带订单编号）6:异地登录；
	 * @return
	 */
	public static void pushUserMessageByAndroidUnicast(String device_token, String messageContent, String tagType,
			String orderNo) {// 针对个人推送
		try {
			AndroidUnicast unicast = new AndroidUnicast(PushConfig.Android_appkey,
					PushConfig.Android_app_master_secret);
			unicast.setDeviceToken(device_token);
			unicast.setTicker("拼个座");
			if (tagType.equals("5") ) {
				unicast.setText(messageContent);
				if (tagType != null) {//
					unicast.goCustomAfterOpen("");
					unicast.setExtraField("tagType", "5");
					unicast.setExtraField("orderNo", orderNo);
				}
				unicast.setDisplayType(AndroidNotification.DisplayType.MESSAGE);
			} else {
				unicast.setTitle("拼个座");
				unicast.setText(messageContent);
				if (tagType != null) {//
					unicast.goAppAfterOpen();
					if (tagType.equals("0") || tagType.equals("1") || tagType.equals("2") || tagType.equals("3")) {
						unicast.goActivityAfterOpen("com.uto168.pgz.activity.message.MessageManageActivity");
					} else if (tagType.equals("6")) {
						unicast.goActivityAfterOpen("com.uto168.pgz.activity.MainActivity");
					}
					unicast.setProductionMode();
					unicast.setExtraField("tagType", tagType);
				}
				unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
			}

			System.out.println(unicast.toString());
			httpUtilPushMessage.send(unicast);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * 
	 * Title: pushUserMessageByAndroidUnicastOfEndOrder Description：
	 * 
	 * <pre>
	 * 完成订单
	 * </pre>
	 * 
	 * @param device_token
	 * @param messageContent
	 * @param tagType
	 * @param orderNo
	 * 
	 *            <pre></pre>
	 * 
	 * @return: void
	 *
	 */
	public static void pushUserMessageByAndroidUnicastOfEndOrder(String device_token, String messageContent,
			String tagType, String orderNo) {// 针对个人推送
		try {
			AndroidUnicast unicast = new AndroidUnicast(PushConfig.Android_appkey,
					PushConfig.Android_app_master_secret);
			unicast.setDeviceToken(device_token);
			unicast.setTicker("Android unicast ticker");
			unicast.setTitle("拼个座");
			unicast.setText(messageContent);
			if (tagType != null) {//
				unicast.goCustomAfterOpen("");
				;
				unicast.setExtraField("tagType", "5");
				unicast.setExtraField("orderNo", orderNo);
			}
			unicast.setDisplayType(AndroidNotification.DisplayType.MESSAGE);
			System.out.println(unicast.toString());
			httpUtilPushMessage.send(unicast);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 消息推送
	/**
	 * 
	 * @description
	 * @param tagType
	 *            0：系统通知；1：订单通知；2：活动通知；3：帐号变动,4：只唤醒APP,5,订单完成，（带订单编号）,6.ios帐号异地登录；
	 * @return
	 */
	public static void pushUserMessageByIOSUnicast(String device_token, String messageContent, String type,
			String tagType) {// 针对个人推送
		try {
			IOSUnicast unicast = new IOSUnicast(PushConfig.IOS_appkey, PushConfig.IOS_app_master_secret, "unicast");
			unicast.setDeviceToken(device_token);
			unicast.setAlert(messageContent);
			unicast.setBadge(0);
			unicast.setSound("default");
			unicast.setTestMode();
			unicast.setCustomizedField("tag", "1");
			// unicast.setProductionMode();
			unicast.setCustomizedField("tagType", tagType);
			unicast.setCustomizedField("type", type);
			System.out.println(unicast.toString());
			httpUtilPushMessage.send(unicast);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		// MessageService.pushUserMessageByAndroidUnicast("An8NxPkTa-dwMNBerYNMyM7_MzcS5muA0UxGLCYc9Isl"
		// , "您的账号(18****1489)在另一个地方登录，请重新登录；如果不是您本人操作请及时修改密码!"
		// , "3",null);
		pushUserMessageByIOSUnicast("5476c40285567308b957ee56ab5727099889ced4cc11f2cc5b3084c4ea970f50", "异地登录", "1",
				"6");

		System.out.println("推送消息");
	}

	/**
	 * 
	 * @Title: getMessageNum
	 * @Description: 获取用户消息总数
	 * @param paramsMap
	 * @param resultMap
	 *            void
	 */
	public void getMessageNum(JSONObject paramsMap, JSONObject resultMap) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject jsonobject = new JSONObject();
		jsonobject = JSONObject.fromObject(paramsMap);
		String uuid = jsonobject.optString("uuid");
		String device_token = jsonobject.optString("device_token");
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = "false";
		if (!("").equals(device_token)) {
			Userinfo userinfo = new Userinfo();
			if (!("").equals(uuid)) {
				map.put("uuid", uuid);
				userinfo = employeeMapper.Getuserinfo(map);
				result = checkUser(userinfo);
			} else {
				result.put("flag", true);
			}

			if (result.optBoolean("flag") && !("").equals(uuid)) {
				Date nowdate = CommonDateParseUtil.date2date(new Date());
				Date uuIDExpiry = CommonDateParseUtil.string2date(userinfo.getUuidExpiry());
				if (nowdate.getTime() >= uuIDExpiry.getTime()) {
					result.put("code", Constant.code.CODE_14);
					result.put("msg", Constant.message.MESSAGE_14);
				} else {
					map.clear();
					map.put("userId", userinfo.getId());
					map.put("operateType", 0);
					flag = "true";
				}
			} else {
				flag = "true";
			}
		} else {
			result.put("code", Constant.code.CODE_2);
			result.put("msg", Constant.message.MESSAGE_2);
		}

		if (flag == "true") {
			map.put("isRead", 0);
			map.put("device_token", device_token);
			Map<String, Object> dataTotal = messageMapper.getMessageNum(map);
			result.put("data", dataTotal);
			result.put("code", Constant.code.CODE_1);
			result.put("msg", Constant.message.MESSAGE_1);
		}
		resultMap.put("data", result.optString("data"));
		resultMap.put("code", result.optString("code"));
		resultMap.put("msg", result.optString("msg"));
	}

	/**
	 * 
	 * @param userinfo
	 * @param
	 * @return
	 */
	public JSONObject checkUser(Userinfo userinfo) {
		JSONObject result = new JSONObject();
		if (userinfo != null) {
			Date nowdate = CommonDateParseUtil.date2date(new Date());
			Date uuIDExpiry = CommonDateParseUtil.string2date(userinfo.getUuidExpiry());
			if (nowdate.getTime() >= uuIDExpiry.getTime()) {
				result.put("code", Constant.code.CODE_14);
				result.put("flag", false);
				result.put("msg", Constant.message.MESSAGE_14);
			} else if (userinfo.getUserStatus() == 1) {
				result.put("flag", false);
				result.put("msg", Constant.code.CODE_12);
				result.put("message", Constant.message.MESSAGE_12);
			} else if (userinfo.getUserStatus() == 2) {
				result.put("flag", false);
				result.put("code", Constant.code.CODE_6);
				result.put("msg", Constant.message.MESSAGE_6);
			} else {
				result.put("flag", true);
			}
		} else {
			result.put("flag", false);
			result.put("code", Constant.code.CODE_14);
			result.put("msg", Constant.message.MESSAGE_14);
		}
		return result;
	}

	/**
	 * 
	 * @Title: getMessage
	 * @Description: 获取用户消息列表
	 * @param paramsMap
	 * @param resultMap
	 *            void
	 */
	public void getMessage(JSONObject paramsMap, JSONObject resultMap, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject jsonobject = new JSONObject();
		jsonobject = JSONObject.fromObject(paramsMap);
		String uuid = jsonobject.optString("uuid");
		String messageType = jsonobject.optString("messageType");
		Integer pageNum = jsonobject.optInt("pageNum");
		Integer Num = jsonobject.optInt("num");
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = "fasle";

		Map<String, String> messageTypeMap = new HashMap<>();
		messageTypeMap.put("0", "0");
		messageTypeMap.put("1", "1");
		messageTypeMap.put("2", "2");
		messageTypeMap.put("3", "3");

		if (messageType != null && pageNum != null && pageNum > 0 && Num != null && Num > 0) {
			if (messageTypeMap.get(messageType) == null) {
				result.put("code", Constant.code.CODE_10);
				result.put("msg", Constant.message.MESSAGE_10);
			} else {
				if (messageType.equals("1") || messageType.equals("3")) {// 订单通知，帐号变动必须登录
					if (uuid.isEmpty()) {
						result.put("code", Constant.code.CODE_2);
						result.put("msg", Constant.message.MESSAGE_2);
					} else {// 登录校验
						map.put("uuid", uuid);
						Userinfo userinfo = employeeMapper.Getuserinfo(map);
						result = checkUser(userinfo);
						if (result.optBoolean("flag")) {
							Date nowdate = CommonDateParseUtil.date2date(new Date());
							Date uuIDExpiry = CommonDateParseUtil.string2date(userinfo.getUuidExpiry());
							if (nowdate.getTime() >= uuIDExpiry.getTime()) {
								result.put("code", Constant.code.CODE_14);
								result.put("msg", Constant.message.MESSAGE_14);
							} else {
								map.clear();
								map.put("operateId", userinfo.getId());
								map.put("operateType", 0);
								flag = "true";
							}
						}
					}
				} else {// 其他可登录，也可不登陆
					if (uuid != null && !uuid.isEmpty()) {
						map.put("uuid", uuid);
						Userinfo userinfo = employeeMapper.Getuserinfo(map);
						result = checkUser(userinfo);
						if (result.optBoolean("flag")) {
							Date nowdate = CommonDateParseUtil.date2date(new Date());
							Date uuIDExpiry = CommonDateParseUtil.string2date(userinfo.getUuidExpiry());
							if (nowdate.getTime() >= uuIDExpiry.getTime()) {
								result.put("code", Constant.code.CODE_14);
								result.put("msg", Constant.message.MESSAGE_14);
							} else {
								map.clear();
								map.put("operateId", userinfo.getId());
								map.put("operateType", 0);
								flag = "true";
							}
						}
					} else {
						// result.put("code", Constant.code.CODE_2);
						// result.put("msg", Constant.message.MESSAGE_2);
						flag = "true";
					}
				}

				if (flag.equals("true")) {
					map.put("messageType", messageType);
					map.put("start", (pageNum - 1) * Num);
					map.put("num", Num);
					List<Message> umList = null;
					umList = messageMapper.getMessageMore(map);
					if (umList != null && umList.size() > 0) {
						for (Message mg : umList) {
							String mImg = mg.getMessageImage();
							if (mImg != null && !mImg.equals("")) {
								String url = GetProperties.getFileUrl("pGZActivityUrl");
								mg.setMessageImage(url + mImg);
							}
							String path = request.getContextPath();
							String basePath = request.getScheme() + "://" + request.getServerName() + ":"
									+ request.getServerPort() + path + "/";
							if (messageType.equals("0")) {// 系统通知
								mg.setUrl(basePath + "comm/message/1/" + mg.getSysmId());
								mg.setMessageContent("");
							} else if (messageType.equals("2") && !mg.getMessageContent().isEmpty()) {// 活动通知
								mg.setUrl(basePath + "comm/message/2/" + mg.getSysmId());
								mg.setMessageContent("");
							}
						}
					}
					data.put("umList", umList);
					result.put("data", data);
					result.put("code", Constant.code.CODE_1);
					result.put("msg", Constant.message.MESSAGE_1);
				}
			}
		} else {
			result.put("code", Constant.code.CODE_2);
			result.put("msg", Constant.message.MESSAGE_2);
		}
		resultMap.put("data", result.optString("data"));
		resultMap.put("code", result.optString("code"));
		resultMap.put("msg", result.optString("msg"));
	}

	/**
	 * 
	 * @Title: operateMessage
	 * @Description: 获取用户消息列表
	 * @param paramsMap
	 * @param resultMap
	 *            void
	 */
	public void operateMessage(JSONObject paramsMap, JSONObject resultMap) throws Exception {

		JSONObject result = new JSONObject();
		JSONObject jsonobject = new JSONObject();
		jsonobject = JSONObject.fromObject(paramsMap);
		String uuid = jsonobject.optString("uuid");
		String device_token = jsonobject.optString("device_token");
		String mbSystemType = jsonobject.optString("mbSystemType");
		Integer messageType = jsonobject.optInt("messageType");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = false;
		String flagStr = "false";
		if (!("").equals(device_token) && !("").equals(mbSystemType)) {
			if (messageType == 0 || messageType == 2) {
				String next = "true";
				Userinfo userinfo = null;
				if (!("").equals(uuid)) {
					map.put("uuid", uuid);
					userinfo = employeeMapper.Getuserinfo(map);
					result = checkUser(userinfo);
					if (!result.optBoolean("flag")) {
						next = "false";
					} else {
						next = "true";
					}
				} else {
					next.equals("true");
				}
				if (next.equals("true")) {
					map.clear();
					if (userinfo != null) {
						map.clear();
						map.put("operateId", userinfo.getId());
						flagStr = "true";
						map.put("messageType", messageType);
						map.put("isRead", 1);
						map.remove("id");
						messageMapper.updateMessageMore(map);

						map.clear();
					}

					map.put("device_token", device_token);
					map.put("mbSystemType", mbSystemType);
					if (messageType.equals(0)) {
						map.put("systemInfo_isRead", "0");
					} else if (messageType.equals(2)) {
						map.put("activityInfo_isRead", "0");
					}
					messageMapper.updateSystemmessageDeviceStatus(map);

					JSONObject interimMap = new JSONObject();
					interimMap.put("uuid", uuid);
					interimMap.put("device_token", device_token);
					getMessageNum(interimMap, result);
				}
			} else if (!("").equals(uuid) && (messageType == 1 || messageType == 3)) {
				map.put("uuid", uuid);
				Userinfo userinfo = employeeMapper.Getuserinfo(map);
				result = checkUser(userinfo);
				if (!result.optBoolean("flag")) {// 登录失效
				}
				if (userinfo != null) {
					Date nowdate = CommonDateParseUtil.date2date(new Date());
					Date uuIDExpiry = CommonDateParseUtil.string2date(userinfo.getUuidExpiry());
					if (nowdate.getTime() >= uuIDExpiry.getTime()) {
						result.put("code", Constant.code.CODE_14);
						result.put("msg", Constant.message.MESSAGE_14);
					} else {
						map.clear();
						map.put("operateId", userinfo.getId());
						flagStr = "true";
						if (messageType != null && (messageType == 3 || messageType == 1)) {
							map.put("messageType", messageType);
							map.put("isRead", 1);
							map.remove("id");
							flagStr = "true";
						} else {
							flag = true;
						}
						if (flag) {
							result.put("code", Constant.code.CODE_2);
							result.put("msg", Constant.message.MESSAGE_2);
						} else {
							if (flagStr.equals("true")) {
								messageMapper.updateMessageMore(map);

								JSONObject interimMap = new JSONObject();
								interimMap.put("uuid", uuid);
								interimMap.put("device_token", device_token);
								getMessageNum(interimMap, result);
							} else {
								result.put("code", Constant.code.CODE_0);
								result.put("msg", Constant.message.MESSAGE_0);
							}
						}
					}
				} else {
					result.put("code", Constant.code.CODE_14);
					result.put("msg", Constant.message.MESSAGE_14);
				}
			}

		} else {
			result.put("code", Constant.code.CODE_2);
			result.put("msg", Constant.message.MESSAGE_2);
		}
		resultMap.put("code", result.optString("code"));
		resultMap.put("msg", result.optString("msg"));
		resultMap.put("data", result.optString("data"));
	}

	/**
	 * <pre>
	 * &#64;Title: saveMessageOfAccountChange
	 * &#64;Description: 消息--帐号变动校验及保存
	 * &#64;param paramsMap
	 *  String messageType  消息类型
	 *	String messageTitle  消息标题
	 *	String messageContent 消息内容
	 *	String loginType    登录类型
	 *	String uuid   登录唯一标示
	 * &#64;param interimMap
	 * &#64;param interimResultMap
	 * </pre>
	 * 
	 * @returnType: void
	 */
	public void saveMessageOfAccountChange(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap) {
		String messageType = paramsMap.optString("messageType");
		String messageTitle = paramsMap.optString("messageTitle");
		String messageContent = paramsMap.optString("messageContent");
		String orderNo = paramsMap.optString("orderNo");
		String operateType = "0";
		String operateId = "0";
		String userId = paramsMap.optString("userId");
		if (userId.isEmpty() || messageTitle.isEmpty() || messageContent.isEmpty()) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			String flag = "true";
			Userinfo userinfo = new Userinfo();
			// 输出设备
			// 根据id查出用户信息
			interimMap.clear();
			interimMap.put("id", userId);
			userinfo = employeeMapper.Getuserinfo(interimMap);

			if (flag.equals("true")) {// 校验成功后，进行保存

				// 保存消息，返回消息id
				interimMap.clear();
				interimMap.put("messageType", messageType);
				interimMap.put("messageTitle", messageTitle);
				interimMap.put("messageContent", messageContent);
				interimMap.put("showType", messageType);
				interimMap.put("orderNo", orderNo);
				interimMap.put("isDel", "0");
				interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				messageMapper.saveSystemMessage(interimMap);
				String sysMSGId = interimMap.optString("sysMSGId");

				if (!paramsMap.get("modelType").equals("AC004")) {
					// 保存推送消息
					interimMap.put("messageType", messageType);
					interimMap.put("messageContent", messageContent);
					interimMap.put("operateType", operateType);
					interimMap.put("operateId", operateId);
					interimMap.put("sysMSGId", sysMSGId);
					interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					messageMapper.savePushmessagelog(interimMap);
				}

				// 保存信息用户关联
				interimMap.clear();
				interimMap.put("userId", userinfo.getId());
				interimMap.put("sysMSGId", sysMSGId);
				interimMap.put("isRead", "0");
				interimMap.put("messageType", messageType);
				interimMap.put("mbSystemType", userinfo.getMbSystemType());
				interimMap.put("device_token", userinfo.getDevice_token());
				messageMapper.saveSystemMessageStatus(interimMap);
				// 发送通知
				String mbSystemType = userinfo.getMbSystemType();

				if (!paramsMap.get("modelType").equals("AC004")) {
					interimMap.clear();
					interimMap.put("device_token", userinfo.getDevice_token());
					interimMap.put("messageContent", messageContent);
					interimMap.put("tagType", messageType);
					interimMap.put("mbSystemType", mbSystemType);
					interimMap.put("type", "2");
					pushUserMessageUnicastOfPGZ(interimMap, interimResultMap);
					interimResultMap.put("code", Constant.code.CODE_1);
					interimResultMap.put("msg", Constant.message.MESSAGE_1);
				}
				interimResultMap.put("code", Constant.code.CODE_1);
				interimResultMap.put("msg", Constant.message.MESSAGE_1);
			}
		}
	}

	/**
	 * 
	 * @Title: saveMessageOfActivity
	 * @Description: 消息--活动
	 * @param interimMap
	 * @param interimResultMap
	 *            void
	 */
	@SuppressWarnings("unchecked")
	public void saveMessageOfActivity(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap)
			throws Exception {
		String messageType = paramsMap.optString("messageType");
		String messageTitle = paramsMap.optString("messageTitle");
		String messageContent = paramsMap.optString("messageContent");
		String messageImage = paramsMap.optString("messageImage");
		String url = paramsMap.optString("url");
		String urlContent = paramsMap.optString("urlContent");

		String saveType = paramsMap.optString("saveType");// 0直接输入url保存
															// ，1富文本编辑器保存html
		String operateType = "1";
		String operateId = "0";
		String uuid = paramsMap.optString("uuid");
		if (uuid.isEmpty() || messageTitle.isEmpty() || messageContent.isEmpty() || saveType.isEmpty()) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			String flag = "false";
			// 保存消息，返回消息id
			switch (saveType) {
			case "0":// 直接保存url
				if (url.isEmpty()) {
					interimResultMap.put("code", Constant.code.CODE_2);
					interimResultMap.put("msg", Constant.message.MESSAGE_2);
				} else {
					flag = "true";
				}
				break;
			case "1": // 先把内容保存到html,返回url地址
				if (urlContent.isEmpty()) {
					interimResultMap.put("code", Constant.code.CODE_2);
					interimResultMap.put("msg", Constant.message.MESSAGE_2);
				} else {
					flag = "true";
				}
				break;
			default:
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
				break;
			}

			if (flag.equals("true")) {
				// 验证web登录
				interimMap.clear();
				interimMap.put("uuid", uuid);
				systemService.validUser(interimMap, interimResultMap);
				if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {// 校验成功后，进行保存
					// 如果是富文本，则保存文件
					if (saveType.equals("1")) {
						String fileUrl = GetProperties.getFileUrl("PGZActivityUrl");
						String fileFlag = FileUtils.isExist(fileUrl);
						if (fileFlag.equals("mkSucc")) {
							interimMap.put("content", urlContent);
							interimMap.put("fileUrl", fileUrl);
							interimMap.put("postfix", ".html");
							FileUtils.saveContentToFile(interimMap, interimResultMap);
							String code = interimResultMap.optString("code");
							if (code.equals("SUCCESS")) {
								String fileName = interimResultMap.optString("fileName");
								fileUrl = GetProperties.getFileUrl("pGZActivityUrl");
								url = fileUrl + fileName;
								flag = "true";
							}
						} else {
							interimResultMap.put("code", Constant.code.CODE_21);
							interimResultMap.put("msg", Constant.message.MESSAGE_21);
						}
					}
					// 如果有图片。保存图片
					String messageImageName = "";
					if (!messageImage.isEmpty()) {
						String fileUrl = GetProperties.getFileUrl("PGZActivityUrl");
						String fileFlag = FileUtils.isExist(fileUrl);
						if (fileFlag.equals("mkSucc")) {
							messageImageName = BaseParseImage.generateImage(messageImage, fileUrl, ".jpg", null);

						}
					}

					// 查询所有设备信息
					List<Map<String, Object>> allDeviceInfo = systemMapper.getAllDeviceInfo(paramsMap);

					// 保存系统通知
					interimMap.clear();
					interimMap.put("messageType", messageType);
					interimMap.put("messageTitle", messageTitle);
					interimMap.put("messageContent", messageContent);
					interimMap.put("messageImage", messageImageName);
					interimMap.put("url", url);
					interimMap.put("showType", messageType);
					interimMap.put("isDel", "0");
					interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					messageMapper.saveSystemMessage(interimMap);
					String sysMSGId = interimMap.optString("sysMSGId");

					/**
					 * 为已有设备做推送
					 */
					int i = 0;
					for (Map deviceInfo : allDeviceInfo) {
						String device_token = deviceInfo.get("device_token") + "";
						String mbSystemType = deviceInfo.get("mbSystemType") + "";

						// 保存推送消息
						operateId = "" + ((Map) interimResultMap.get("data")).get("adminId");
						interimMap.clear();
						interimMap.put("messageType", messageType);
						interimMap.put("messageContent", messageContent);
						interimMap.put("operateType", operateType);
						interimMap.put("operateId", operateId);
						interimMap.put("sysMSGId", sysMSGId);
						interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
						messageMapper.savePushmessagelog(interimMap);

						// 发送通知
						interimMap.clear();
						interimMap.put("device_token", device_token);
						interimMap.put("messageContent", messageContent);
						interimMap.put("tagType", messageType);
						interimMap.put("mbSystemType", mbSystemType);
						interimMap.put("type", "2");
						pushUserMessageUnicastOfPGZ(interimMap, interimResultMap);
						interimResultMap.put("code", Constant.code.CODE_1);
						interimResultMap.put("msg", Constant.message.MESSAGE_1);
						if (i++ == 1000) {
							Thread.sleep(1000);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Title: saveMessageOfOrder
	 * @Description: 消息--订单通知
	 * @param interimMap
	 * @param interimResultMap
	 *            void
	 */
	public void saveMessageOfOrder(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap)
			throws Exception {
		String messageType = paramsMap.optString("messageType");
		String returnMT = "1";
		if (messageType.equals("5")) {
			messageType = "1";
			returnMT = "5";
		}
		String messageTitle = paramsMap.optString("messageTitle");
		String messageContent = paramsMap.optString("messageContent");
		String trainNo = paramsMap.optString("trainNo");
		String site = paramsMap.optString("site");
		String dispatchTime = paramsMap.optString("dispatchTime");
		String orderNo = paramsMap.optString("orderNo");
		String orderSellNo = paramsMap.optString("orderSellNo");
		String sellerId = paramsMap.optString("sellerId");// 卖家id

		String operateType = "0";
		String operateId = "0";
		// String uuid = paramsMap.optString("uuid");
		if (sellerId.isEmpty() || messageTitle.isEmpty() || messageContent.isEmpty() || trainNo.isEmpty()
				|| site.isEmpty() || dispatchTime.isEmpty() || orderNo.isEmpty()) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			Userinfo userinfo = new Userinfo();
			/*
			 * interimMap.put("uuid", uuid); userinfo =
			 * wxUserMapper.Getuserinfo(interimMap); JSONObject resultMap =
			 * checkUser(userinfo);
			 */

			/*
			 * interimResultMap.put("code", resultMap.optString("code"));
			 * interimResultMap.put("msg", resultMap.optString("message"));
			 */
			if (true) {

				// 保存消息，返回消息id
				interimMap.clear();
				interimMap.put("messageType", messageType);
				interimMap.put("messageTitle", messageTitle);
				interimMap.put("messageContent", messageContent);
				interimMap.put("trainNo", trainNo);
				interimMap.put("site", site);
				interimMap.put("dispatchTime", dispatchTime);
				interimMap.put("showType", messageType);
				interimMap.put("isDel", "0");
				 interimMap.put("orderNo", orderNo);
                interimMap.put("orderSellNo", orderSellNo);//消息orderNo代指卖家的座位编号
				interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				messageMapper.saveSystemMessage(interimMap);

				// 保存推送消息
				String sysMSGId = interimMap.optString("sysMSGId");
				interimMap.put("messageType", messageType);
				interimMap.put("messageContent", messageContent);
				interimMap.put("operateType", operateType);
				interimMap.put("operateId", operateId);
				interimMap.put("sysMSGId", sysMSGId);
				interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				messageMapper.savePushmessagelog(interimMap);

				// 保存信息用户关联
				interimMap.clear();

				interimMap.put("id", sellerId);
				userinfo = employeeMapper.Getuserinfo(interimMap);// 查出卖家id
				interimMap.put("userId", userinfo.getId());
				interimMap.put("sysMSGId", sysMSGId);
				interimMap.put("isRead", "0");
				interimMap.put("messageType", messageType);
				interimMap.put("mbSystemType", userinfo.getMbSystemType());
				interimMap.put("device_token", userinfo.getDevice_token());
				messageMapper.saveSystemMessageStatus(interimMap);
				// 发送通知
				String mbSystemType = userinfo.getMbSystemType();

				interimMap.clear();
				interimMap.put("device_token", userinfo.getDevice_token());
				interimMap.put("messageContent", messageContent);
				interimMap.put("tagType", returnMT);
				interimMap.put("orderNo", orderNo);
				interimMap.put("mbSystemType", mbSystemType);
				interimMap.put("type", "2");
				pushUserMessageUnicastOfPGZ(interimMap, interimResultMap);

				interimResultMap.put("code", Constant.code.CODE_1);
				interimResultMap.put("msg", Constant.message.MESSAGE_1);
			}
		}
	}

	/**
	 * 
	 * @Title: saveMessageOfSystem
	 * @Description: 消息--系统通知
	 * @param interimMap
	 * @param interimResultMap
	 *            void
	 */
	public void saveMessageOfSystem(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap)
			throws Exception {
		String messageType = paramsMap.optString("messageType");
		String messageTitle = paramsMap.optString("messageTitle");
		String messageContent = paramsMap.optString("messageContent");
		String trainNo = paramsMap.optString("trainNo");
		String site = paramsMap.optString("site");
		String dispatchTime = paramsMap.optString("dispatchTime");
		String saveType = "1";// 0后台推送系统消息 去掉
								// ，1指定发布订单通知
		String toUserId = paramsMap.optString("toUserId");// 被推送者id
		String orderSellNo = paramsMap.optString("orderSellNo");

		String operateType = "0";
		String operateId = "0";// 推送者id
		String uuid = paramsMap.optString("uuid");
		if (messageTitle.isEmpty() || messageContent.isEmpty() || saveType.isEmpty()) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			String flag = "false";

			Userinfo userinfo = new Userinfo();
			switch (saveType) {
			/*
			 * case "0":// 后台推送，校验后台登录状态 interimMap.clear();
			 * interimMap.put("uuid", uuid); adminService.validUser(interimMap,
			 * interimResultMap); if
			 * (interimResultMap.optString("code").equals(Constant.code.CODE_1))
			 * {// 校验成功后，进行保存 operateId = "" + ((Map)
			 * interimResultMap.get("data")).get("adminId"); operateType = "1";
			 * flag = "true"; } break;
			 */
			case "1":// app推送，校验app登录状态
				if (trainNo.isEmpty() || site.isEmpty() || dispatchTime.isEmpty() || toUserId.isEmpty()
						|| !RegexUtil.match(RegexUtil.IsIntNumber, toUserId)
						|| orderSellNo.isEmpty()) {
					interimResultMap.put("code", Constant.code.CODE_2);
					interimResultMap.put("msg", Constant.message.MESSAGE_2);
				} else {
					flag = "true";
					/*
					 * interimMap.clear(); interimMap.put("uuid", uuid);
					 * userinfo = wxUserMapper.Getuserinfo(interimMap);
					 * JSONObject resultMap = checkUser(userinfo);
					 * interimResultMap.put("code",
					 * resultMap.optString("code")); interimResultMap.put("msg",
					 * resultMap.optString("message")); if
					 * (resultMap.optBoolean("flag")) { flag = "true"; }
					 */
				}
			default:
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
				break;
			}
			if (flag.equals("true")) {
				// 保存消息，返回消息id
				interimMap.clear();
				interimMap.put("messageType", messageType);
				interimMap.put("messageTitle", messageTitle);
				interimMap.put("messageContent", messageContent);
				if (saveType.equals("0")) {
					interimMap.put("showType", "0");
				} else {
					interimMap.put("showType", "10");
					interimMap.put("trainNo", trainNo);
					interimMap.put("site", site);
					interimMap.put("dispatchTime", dispatchTime);
					interimMap.put("orderSellNo", orderSellNo);
				}
				interimMap.put("isDel", "0");
				interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				messageMapper.saveSystemMessage(interimMap);
				String sysMSGId = interimMap.optString("sysMSGId");
				if (saveType.equals("1")) {// 保存附属表
					// 被推送人的设备信息

					interimMap.clear();
					interimMap.put("operateId", toUserId);
					List<PushMessageDevice> pmd = (List<PushMessageDevice>) messageMapper
							.getPushMessageDevice(interimMap);
					Integer st = 0;
					String dt = "";
					if (pmd.size() > 0) {
						st = pmd.get(0).getMbSystemType();
						dt = pmd.get(0).getDevice_token();
					}
					interimMap.clear();
					interimMap.put("userId", toUserId);
					interimMap.put("sysMSGId", sysMSGId);
					interimMap.put("isRead", "0");
					interimMap.put("messageType", messageType);
					interimMap.put("mbSystemType", 0);
					interimMap.put("device_token", dt);
					messageMapper.saveSystemMessageStatus(interimMap);
				}

				// 如果是后台发送，则所有的都推送
				if (saveType.equals("0") || saveType.equals("1")) {// 查询出所有设备，推送消息，不记录通知
					interimMap.clear();
					interimMap.put("operateId", toUserId);
					List<PushMessageDevice> allDeviceInfo = messageMapper.getPushMessageDevice(interimMap);

					int i = 0;
					for (PushMessageDevice deviceInfo : allDeviceInfo) {
						String device_token = deviceInfo.getDevice_token() + "";
						String mbSystemType = deviceInfo.getMbSystemType() + "";

						// 保存推送消息
						interimMap.clear();
						interimMap.put("messageType", messageType);
						interimMap.put("messageContent", messageContent);
						interimMap.put("operateType", operateType);
						interimMap.put("operateId", operateId);
						interimMap.put("sysMSGId", sysMSGId);
						interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
						messageMapper.savePushmessagelog(interimMap);

						// 推送消息
						interimMap.clear();
						interimMap.put("device_token", device_token);
						interimMap.put("messageContent", messageContent);
						// interimMap.put("tagType", messageType);
						if (saveType.equals("0")) {
							interimMap.put("tagType", "0");
						} else {
							interimMap.put("tagType", "10");
						}
						interimMap.put("mbSystemType", mbSystemType);
						interimMap.put("type", "2");
						pushUserMessageUnicastOfPGZ(interimMap, interimResultMap);
						interimResultMap.put("code", Constant.code.CODE_1);
						interimResultMap.put("msg", Constant.message.MESSAGE_1);
						if (i++ == 1000) {
							Thread.sleep(1000);
						}
					}
				} else {// app推送到指定用户，并记录通知
					interimMap.put("trainNo", trainNo);

					// 保存推送消息
					interimMap.put("messageType", messageType);
					interimMap.put("messageContent", messageContent);
					interimMap.put("operateType", operateType);
					interimMap.put("operateId", operateId);
					interimMap.put("sysMSGId", sysMSGId);
					interimMap.put("addTime", DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					messageMapper.savePushmessagelog(interimMap);

					// 获取用户信息
					interimMap.clear();
					interimMap.put("operateId", toUserId);
					Map deviceInfo = employeeMapper.getUserDeviceInfo(interimMap);
					if (deviceInfo == null) {
						interimResultMap.put("code", Constant.code.CODE_48);
						interimResultMap.put("msg", Constant.message.MESSAGE_48);
					} else {
						String device_token = deviceInfo.get("device_token") + "";
						String mbSystemType = deviceInfo.get("mbSystemType") + "";

						// 保存信息用户关联
						interimMap.clear();
						interimMap.put("userId", toUserId);
						interimMap.put("sysMSGId", sysMSGId);
						interimMap.put("isRead", "0");
						interimMap.put("messageType", messageType);
						interimMap.put("mbSystemType", mbSystemType);
						interimMap.put("device_token", device_token);
						messageMapper.saveSystemMessageStatus(interimMap);

						// 发送通知
						interimMap.clear();
						interimMap.put("device_token", device_token);
						interimMap.put("messageContent", messageContent);
						interimMap.put("tagType", messageType);
						interimMap.put("mbSystemType", mbSystemType);
						interimMap.put("type", "2");
						pushUserMessageUnicastOfPGZ(interimMap, interimResultMap);
						interimResultMap.put("code", Constant.code.CODE_1);
						interimResultMap.put("msg", Constant.message.MESSAGE_1);
					}
				}
			}
		}
	}

	/**
	 * <pre>
	* &#64;Title: getMessageContenAndTitle 
	* &#64;Description:  根据modelType ，获取消息内容，及标题
	* 
	* &#64;param paramsMap
	*    消息提示:大致类型  
	*    <pre>
	*     messageType
	*     modelType   ST     系统 (值必须和messageType对应)
	*                  ST000  指定买家
	*                  ST001  系统发送消息
	*                 OD     订单
	*                  OD000   买家已支付 
	*                  OD001   买家已取消
	*                  OD002   交易已完成
	*                  OD003   座位超时关闭
	*                  OD004   买家已退款
	*                 AC    账户变动  注意：（modelType 为AC000，AC001，AC002时 paramsMap必须包含  date  money）
	*                  AC000   订单交易成功 
	*                  AC001  充值
	*                  AC002  提现
	*                  AC003  支付密码设置成功
	*                  AC004  登录密码设置成功
	*                  AC005  已提交身份验证信息
	*                  
	*                 AT	活动
	*                   AT000   后台发送通知
	 * </pre>
	 * 
	 * @param interimMap
	 * @param interimResultMap
	 *            </pre>
	 * @returnType: void
	 */
	public void getMessageContenAndTitle(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap) {
		String modelType = paramsMap.optString("modelType").replace(" ", "");
		paramsMap.put("modelType", modelType);
		String messageType = paramsMap.optString("messageType");
		interimResultMap.put("code", Constant.code.CODE_1);
		interimResultMap.put("msg", Constant.message.MESSAGE_1);
		switch (modelType) {
		case "ST000": // 指定买家
			if (!messageType.equals("0")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_ST000);
				paramsMap.put("messageContent", SystemMessageTemplate.content.content_ST000);
			}
			break;
		/*
		 * case "ST001":// 系统发送消息 if(!messageType.equals("0")){
		 * interimResultMap.put("code", Constant.code.CODE_10);
		 * interimResultMap.put("msg", Constant.message.MESSAGE_10); }else{
		 * paramsMap.put("messageTitle", paramsMap.get("messageTitle"));
		 * paramsMap.put("messageContent", paramsMap.get("messageContent")); }
		 * break;
		 */
		case "OD000":// 买家已支付
			if (!messageType.equals("1")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_OD000);
				paramsMap.put("messageContent", SystemMessageTemplate.content.content_OD_COMMON);
			}
			break;
		case "OD001":// 买家已取消
			if (!messageType.equals("1")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_OD001);
				paramsMap.put("messageContent", SystemMessageTemplate.content.content_OD_COMMON);
			}
			break;
		case "OD002":// 交易已完成
			if (!messageType.equals("1") && !messageType.equals("5")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_OD002);
				paramsMap.put("messageContent", SystemMessageTemplate.content.content_OD_COMMON);
			}
			break;
		case "OD003":// 座位超时关闭
			if (!messageType.equals("1")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_OD003);
				paramsMap.put("messageContent", SystemMessageTemplate.content.content_OD_COMMON);
			}
			break;

		case "OD004":// 买家已退款
			if (!messageType.equals("1")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_OD004);
				paramsMap.put("messageContent", SystemMessageTemplate.content.content_OD_COMMON);
			}
			break;

		case "AC000":// 订单交易成功
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				// 校验参数
				validParamsOfAccountChange(paramsMap, interimMap, interimResultMap);
				if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
					SystemMessageTemplate.getAccountChangeContent(paramsMap, interimMap, interimResultMap);
				}
			}
			break;
		case "AC001":// 充值
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				// 校验参数
				validParamsOfAccountChange(paramsMap, interimMap, interimResultMap);
				if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
					SystemMessageTemplate.getAccountChangeContent(paramsMap, interimMap, interimResultMap);
				}
			}
			break;
		case "AC002":// 提现
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				// 校验参数
				validParamsOfAccountChange(paramsMap, interimMap, interimResultMap);
				if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
					SystemMessageTemplate.getAccountChangeContent(paramsMap, interimMap, interimResultMap);
				}
			}
			break;
		case "ACZF1":// 支付
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				// 校验参数
				validParamsOfAccountChange(paramsMap, interimMap, interimResultMap);
				if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
					SystemMessageTemplate.getAccountChangeContent(paramsMap, interimMap, interimResultMap);
				}
			}
			break;
		case "ACTK1":// 退款
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				// 校验参数
				validParamsOfAccountChange(paramsMap, interimMap, interimResultMap);
				if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
					SystemMessageTemplate.getAccountChangeContent(paramsMap, interimMap, interimResultMap);
				}
			}
			break;
		case "AC003":// 支付密码设置成功
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_AC003);
				paramsMap.put("contentType", "content_AC003");
				SystemMessageTemplate.getContent(paramsMap, interimMap, interimResultMap);
				paramsMap.put("messageContent", interimResultMap.get("data"));
			}
			break;
		case "AC004":// 登录密码设置成功
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_AC004);
				paramsMap.put("contentType", "content_AC004");
				SystemMessageTemplate.getContent(paramsMap, interimMap, interimResultMap);
				paramsMap.put("messageContent", interimResultMap.get("data"));
			}
			break;
		case "AC005":// 已提交身份验证信息
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_AC005);
				paramsMap.put("contentType", "content_AC005");
				SystemMessageTemplate.getContent(paramsMap, interimMap, interimResultMap);
				paramsMap.put("messageContent", interimResultMap.get("data"));
			}
			break;
		case "AC006":// 用户信息已修改
			if (!messageType.equals("3")) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				paramsMap.put("messageTitle", SystemMessageTemplate.title.title_AC006);
				paramsMap.put("contentType", "content_AC006");
				SystemMessageTemplate.getContent(paramsMap, interimMap, interimResultMap);
				paramsMap.put("messageContent", interimResultMap.get("data"));
			}
			break;

		/*
		 * case "AT000":// 后台发送通知 if(!messageType.equals("1")){
		 * interimResultMap.put("code", Constant.code.CODE_10);
		 * interimResultMap.put("msg", Constant.message.MESSAGE_10); }else{
		 * paramsMap.put("messageTitle", paramsMap.get("messageTitle"));
		 * paramsMap.put("messageContent", paramsMap.get("messageContent")); }
		 * break;
		 */
		default:
			interimResultMap.put("code", Constant.code.CODE_10);
			interimResultMap.put("msg", Constant.message.MESSAGE_10);
			break;
		}

	}

	/**
	 * 
	 * @Title: validParamsOfAccountChange
	 * @Description: 账户变动，校验参数
	 * @param paramsMap
	 * @param interimMap
	 * @param interimResultMap
	 *            void
	 */
	public void validParamsOfAccountChange(JSONObject paramsMap, JSONObject interimMap, JSONObject interimResultMap) {
		String money = paramsMap.optString("money");

		if (money.isEmpty()) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			if (!RegexUtil.match(RegexUtil.nonnegativeNumber, money)) {
				interimResultMap.put("code", Constant.code.CODE_10);
				interimResultMap.put("msg", Constant.message.MESSAGE_10);
			} else {
				interimResultMap.put("code", Constant.code.CODE_1);
				interimResultMap.put("msg", Constant.message.MESSAGE_1);
			}
		}
	}

	/**
	 * <pre>
	 * &#64;Title: saveMessage
	 * &#64;Description: 保存用户消息()
	 * &#64;param   paramsMap 
	 * <pre>
	 *   <span style=
	'color:red;font-weight:bold'>  messageType Y int  通知类型</span>
	 *      <span style=
	' font-weight:bold'>  messageType=0     系统通知(app,web) </span>
	 *         参数(
	 *                trainNo        Y String 列车编号 ,
	 *                site           Y String 发布站点 ,
	 *                dispatchTime   Y String(yyyy-MM-dd) 发车时间 ,
	 *                orderNo        Y String 订单编号,
	 *                toUserId       Y int(正整数)    被推送人id
	 *           )
	 *       <span style=' font-weight:bold'>messageType=1     订单通知(app) </span>
	 *     <span style=
	' font-weight:bold'> messageType=5 bullNo 买家订单  订单通知（完成订单app) </span> 
	 *         参数(
	 *           trainNo        Y String 列车编号 ,
	 *           site           Y String 发布站点 ,
	 *           dispatchTime   Y String(yyyy-MM-dd) 发车时间 ,
	 *           orderNo        Y String 卖家订单编号,
	 *           sellerId       Y int    卖家id
	 *           )
	 *     <span style=' font-weight:bold'> messageType=3  帐号变动(app) </span> 
	 *         参数(
	 *           userId Y int 用户id
	 *           )
	 *           
	 *         参数(
	 *           userId Y int 用户id
	 *           )
	 *   <span style=
	'color:red;font-weight:bold'>  modelType Y String   返回消息类型（值必须和messageType 值 相对应）</span>
	 *           ----ST     系统   messageType=0 
	 *               ST000  指定买家 
	*            ----OD     订单   messageType=1 或5 
	*               OD000   买家已支付 
	*                  OD001   买家已取消
	*                  OD002   交易已完成
	*                  OD003   交易已关闭
	*                  OD004   买家已退款
	*            ----AC    账户变动  messageType=3  <span style=
	'color:red;font-weight:bold'> 
	*                 注意：（modelType 为AC000，AC001，AC002，ACZF1，ACTK1时 
	*                 paramsMap中    money(（非负数）double) 必须有值）
	*                             orderNo  string  订单编号 最好加上
	*                 </span>
	*                 AC000  订单交易成功 <span style=
	'color:red;font-weight:bold'>*</span>
	*               AC001  充值<span style='color:red;font-weight:bold'>*</span>
	*               AC002  提现<span style='color:red;font-weight:bold'>*</span>
	*               ACZF1  支付<span style='color:red;font-weight:bold'>*</span>  
	*               ACTK1  退款<span style='color:red;font-weight:bold'>*</span>  
	*               AC003  支付密码设置成功
	*                  AC004  登录密码设置成功
	*                  AC005  已提交身份验证信息
	 * 
	 * 
	 * </pre>
	 * 
	 * @param resultMap
	 *            code msg
	 *            </pre>
	 * @returnType: void
	 */
	public void saveMessage(JSONObject paramsMap, JSONObject resultMap) throws Exception {
		// 临时参数
		JSONObject interimMap = new JSONObject();
		JSONObject interimResultMap = new JSONObject();
		String messageType = paramsMap.optString("messageType");
		String modelType = paramsMap.optString("modelType");
		if (modelType == null || modelType.equals("")) {
			interimResultMap.put("code", Constant.code.CODE_2);
			interimResultMap.put("msg", Constant.message.MESSAGE_2);
		} else {
			getMessageContenAndTitle(paramsMap, interimMap, interimResultMap);// 获取对应消息内容
			if (interimResultMap.optString("code").equals(Constant.code.CODE_1)) {
				if (messageType == null || messageType.equals("")) {
					interimResultMap.put("code", Constant.code.CODE_2);
					interimResultMap.put("msg", Constant.message.MESSAGE_2);
				} else {
					if (!RegexUtil.match(RegexUtil.npIntNumber, messageType)) {
						interimResultMap.put("code", Constant.code.CODE_10);
						interimResultMap.put("msg", Constant.message.MESSAGE_10);
					} else {
						switch (messageType) {
						case "3":// 帐号变动
							saveMessageOfAccountChange(paramsMap, interimMap, interimResultMap);
							break;
						/*
						 * case "2":// 活动通知 saveMessageOfActivity(paramsMap,
						 * interimMap, interimResultMap); break;
						 */
						case "1":// 订单通知
							saveMessageOfOrder(paramsMap, interimMap, interimResultMap);
							break;

						case "5":// 完成订单
							saveMessageOfOrder(paramsMap, interimMap, interimResultMap);
							break;
						case "0":// 系统通知
							saveMessageOfSystem(paramsMap, interimMap, interimResultMap);
							break;
						default:
							interimResultMap.put("code", Constant.code.CODE_10);
							interimResultMap.put("msg", Constant.message.MESSAGE_10);
							break;
						}
					}
				}
			}
		}
		resultMap.put("code", interimResultMap.get("code"));
		resultMap.put("msg", interimResultMap.get("msg"));

	}

	// 消息入库和推送type1：不推送；2推送
	// paymentType 1 微信;2 支付宝; 3 游乐币
	// expenseType 1 充值;2 提现;3 消费;5:退款（腕带）
	// messageType1:系统消息 ;2:服务消息
	// pushType 接收对象，目前都是单个推送 3
	// operateType 1 游乐app用户operateType 2 商家用户operateType 3 平台管理者 operateType 4
	// 景区用户
	/*
	 * public void insertUserMessage(String type, Userinfo userinfo, Integer
	 * paymentType, Integer expenseType, BigDecimal total_amount) {
	 * HashMap<String, Object> map = new HashMap<String, Object>(); String
	 * messageContent = messageModel(paymentType, expenseType, total_amount); //
	 * 添加消息记录 map.clear(); map.put("parentId", -2); map.put("adminType", 1);
	 * Admin ad = expenseMapper.getAdmin(map); PushMessageLog pml = new
	 * PushMessageLog(); pml.setMessageType(1); pml.setAddTime(df.format(new
	 * Date())); pml.setMessageContent(messageContent); if (ad != null) {
	 * pml.setOperateId(ad.getId()); } pml.setOperateType(3);
	 * pml.setPushType(3); usermapper.savePushMessageLog(pml); // 插入用户消息表
	 * map.clear(); map.put("operateId", userinfo.getId());
	 * map.put("operateType", 1); List<PushMessageDevice> pmdList =
	 * usermapper.getPushMessageDevice(map); if (pmdList != null &&
	 * !pmdList.isEmpty()) { map.put("device_token",
	 * pmdList.get(0).getDevice_token()); map.put("mbSystemType",
	 * pmdList.get(0).getMbSystemType()); } else { map.put("device_token", "0");
	 * map.put("mbSystemType", 0); } map.put("messageType", 1);
	 * map.put("messageContent", messageContent); map.put("isDel", 0);
	 * map.put("addTime", df.format(new Date())); map.put("isRead", 0);
	 * map.put("operateType", 1); map.put("operateId", userinfo.getId());
	 * map.put("pmId", pml.getId()); usermapper.saveMessage(map); // 是否推送消息 if
	 * (type.equals("2")) { if (pmdList != null && !pmdList.isEmpty() &&
	 * pmdList.get(0).getDevice_token() != null &&
	 * !pmdList.get(0).getDevice_token().isEmpty() &&
	 * pmdList.get(0).getMbSystemType() != null) {
	 * if(pmdList.get(0).getMbSystemType()==1){//IOS推送
	 * pushUserMessageByIOSUnicast(pmdList.get(0).getDevice_token(),
	 * messageContent); }else if(pmdList.get(0).getMbSystemType()==2){//安卓推送
	 * pushUserMessageByAndroidUnicast(pmdList.get(0).getDevice_token(),
	 * messageContent);
	 * 
	 * } } } }
	 */

}
