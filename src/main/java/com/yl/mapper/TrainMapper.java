package com.yl.mapper;

import java.util.List;
import java.util.Map;
import com.yl.bean.Station;
import com.yl.bean.StationTrain;

public interface TrainMapper {
	/**
	 * 
	 * @description 查询车站信息
	 * @param
	 * @return
	 */
	List<Station> getStation(Map<String, Object> map);

	List<StationTrain> getStationTrain(Map<String, Object> map);

	void UpdateTrainlist(Map<String, Object> map);

	void saveTrain(Map<String, Object> map);

	/**
	 * 
	 * @description 热门城市推荐
	 * @param
	 * @return
	 */
	List<Station> getHotCity(Map<String, Object> map);

}
