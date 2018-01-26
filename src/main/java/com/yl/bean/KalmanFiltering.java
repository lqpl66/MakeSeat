package com.yl.bean;

/*
 * @author  lqpl66
 * @date 创建时间：2017年10月18日 上午10:47:51 
 * @function     
 */
public class KalmanFiltering {
	private float forecast_covar;// 为预测的协方差 10
	private float kalman_gain;// 为卡尔曼增益  0.5
	private float last_covar;// 本次最优的协方差，默认初始值为10.0
	private float process_noise_covar;// 设process_noise_covar_为过程噪声，默认值为0.000001
	private float measure_noise_covar;// 设measure_noise_covar_为测量噪声，默认值为0.1
	private float last_position_x;// 设last_position_为上一个定位点，其x,y坐标分别为last_position_x,
	private float last_position_y;// last_position_y
	private float dist_thresh; // 为距离阈值，默认值为2.3

	public float getForecast_covar() {
		return forecast_covar;
	}

	public void setForecast_covar(float forecast_covar) {
		this.forecast_covar = forecast_covar;
	}


	public float getKalman_gain() {
		return kalman_gain;
	}

	public void setKalman_gain(float kalman_gain) {
		this.kalman_gain = kalman_gain;
	}

	public float getLast_covar() {
		return last_covar;
	}

	public void setLast_covar(float last_covar) {
		this.last_covar = last_covar;
	}

	public float getProcess_noise_covar() {
		return process_noise_covar;
	}

	public void setProcess_noise_covar(float process_noise_covar) {
		this.process_noise_covar = process_noise_covar;
	}

	public float getMeasure_noise_covar() {
		return measure_noise_covar;
	}

	public void setMeasure_noise_covar(float measure_noise_covar) {
		this.measure_noise_covar = measure_noise_covar;
	}

	public float getLast_position_x() {
		return last_position_x;
	}

	public void setLast_position_x(float last_position_x) {
		this.last_position_x = last_position_x;
	}

	public float getLast_position_y() {
		return last_position_y;
	}

	public void setLast_position_y(float last_position_y) {
		this.last_position_y = last_position_y;
	}

	public float getDist_thresh() {
		return dist_thresh;
	}

	public void setDist_thresh(float dist_thresh) {
		this.dist_thresh = dist_thresh;
	}

}
