package yl.test;

import java.util.HashMap;
import java.util.Map;

/*
 * @author  lqpl66
 * @date 创建时间：2017年6月6日 下午3:30:27 
 * @function     
 */
public class T4 {
	public static void main(String args[]) {
//		int a[] = { 3, 2, 4 };
		int score[]={2,3, 4};
		System.out.println(twoSum(score, 6)[0]);
		System.out.println(twoSum(score, 6)[1]);
	}

	public static int[] twoSum(int[] nums, int target) {
		int[] result = new int[2];
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(target - nums[i])) {
				result[1] = i + 1;
				result[0] = map.get(target - nums[i]);
			}
			map.put(nums[i], i + 1);
		}
		return result;
	}
}
