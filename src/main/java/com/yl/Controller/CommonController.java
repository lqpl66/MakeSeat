package com.yl.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yl.Utils.Constant;
import com.yl.Utils.GetProperties;
import com.yl.bean.Advertisement;
import com.yl.bean.Message;
import com.yl.bean.OrderSellInfo;
import com.yl.bean.Version;
import com.yl.mapper.AdMapper;
import com.yl.mapper.HelpMapper;
import com.yl.mapper.MessageMapper;
import com.yl.mapper.TrainOrderMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/comm")
public class CommonController {
	@Autowired
	private AdMapper adMapper;
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private HelpMapper helpMapper;
	@Autowired
	private TrainOrderMapper trainOrderMapper;
	private String url = GetProperties.getPGZAdUrl();
	private Logger log = Logger.getLogger(CommonController.class);

	@RequestMapping(value = "/redirect", method = { RequestMethod.GET, RequestMethod.POST })
	public String get(HttpServletRequest request, HttpServletResponse response) {
		return "redirect";
	}

	@RequestMapping(value = "/downloadApp", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadApp(HttpServletRequest request, HttpServletResponse response) {
		return "downloadApp";
	}

	/**
	 * 
	 * @description 获取广告位
	 * @param type
	 *            1：一级广告位；2：首页热点推荐广告位
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getAdList", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	public String getAdList(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Advertisement> list = adMapper.getBannerList(map);
			if (list.size() > 0) {
				for (Advertisement ad : list) {
					if (ad.getImg() != null && !ad.getImg().isEmpty()) {
						ad.setImg(url + ad.getImg());
					}
				}
			}
			result.put("data", list);
			result.put(Constant.code.CODE, Constant.code.CODE_1);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
		} catch (Exception e) {
			log.error("获取广告位：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	/**
	 * 
	 * @description 获取首页信息
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "finally" })
	@ResponseBody
	@RequestMapping(value = "/getMainInfo", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	public String getMainInfo(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			String cityName = json.getString("cityName");
			String type = json.getString("type");
			if (cityName != null && !cityName.isEmpty()) {
				map.put("startStationName", cityName);
			} else {
				cityName = "上海";
			}
			map.put("cityName", cityName);
			map.put("num", 15);
			List<Advertisement> list = adMapper.getAdList(map);
			if (list.size() < 15) {
				map.remove("cityName");
				map.remove("num");
				map.put("cityNameNo", cityName);
				map.put("num", 15 - list.size());
				List<Advertisement> list1 = adMapper.getAdList(map);
				for (Advertisement ad : list1) {
					list.add(ad);
				}
			}
			JSONArray js = new JSONArray();
			JSONArray js1 = new JSONArray();
			if (type != null && !type.isEmpty()) {
				if (type.equals("1")) {
					for (Advertisement ad : list) {
						if (js1.size() < 3) {
							js1.add(ad);
						} else {
							js.add(js1);
							js1.clear();
							js1.add(ad);
						}
					}
				} else if (type.equals("2")) {
					js.addAll(list);
				} else {
					result.put(Constant.code.CODE, Constant.code.CODE_10);
					result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_10);
					return result.toString();
				}
			} else {
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
				return result.toString();
			}

			JSONObject data = new JSONObject();
			data.put("adRcmdList", js);
			// 最新发布的
			map.clear();
			List<OrderSellInfo> oslist = trainOrderMapper.getOrderSellForMain(map);
			data.put("oslist", oslist);
			result.put("data", data);
			result.put(Constant.code.CODE, Constant.code.CODE_1);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);

		} catch (Exception e) {
			log.error("获取首页信息：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

	@RequestMapping(value = "/message/{type}/{id}", method = {
			RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public String getMessage(HttpServletRequest request, @PathVariable("id") Integer id,
			@PathVariable("type") String type, ModelMap returnMap) {
		String url = "404";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (type != null && id != null) {
				if (type.equals("1")) {// 系统通知
					url = "systemModel";
				} else if (type.equals("2")) {// 活动通知
					url = "activityModel";
				} else {
					url = "404";
				}
				map.put("sysmId", id);
				List<Message> ms = messageMapper.getMessageMore(map);
				returnMap.put("content", ms.get(0).getMessageContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
			url = "404";
		}
		return url;
	}

	/**
	 * 
	 * @description 获取是否更新
	 * @param mbSystemType
	 *            1：IOS；2：android
	 * @return
	 */
	@SuppressWarnings({ "finally", "unused" })
	@ResponseBody
	@RequestMapping(value = "/getVersion", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	public String getVersion(HttpServletRequest request, @RequestBody String jsonparam) {
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(jsonparam);
			Integer mbSystemType = json.getInt("mbSystemType");
			if (mbSystemType != null) {
				map.put("mbSystemType", mbSystemType);
				Version version = helpMapper.getVersion(map);
				result.put("data", version);
				result.put(Constant.code.CODE, Constant.code.CODE_1);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_1);
			} else {
				result.put(Constant.code.CODE, Constant.code.CODE_2);
				result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_2);
			}
		} catch (Exception e) {
			log.error("获取更新：", e);
			result.put(Constant.code.CODE, Constant.code.CODE_0);
			result.put(Constant.message.MESSAGE, Constant.message.MESSAGE_0);
		} finally {
			return result.toString();
		}
	}

}
