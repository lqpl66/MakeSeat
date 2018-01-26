package com.yl.mapper;

import java.util.List;
import java.util.Map;
import com.yl.bean.Advertisement;

public interface AdMapper {
	/**
	 * 
	 * @description 广告位
	 * @param
	 * @return
	 */
	List<Advertisement> getAdList(Map<String, Object> map);

	List<Advertisement> getBannerList(Map<String, Object> map);
}
