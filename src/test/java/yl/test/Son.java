package yl.test;

/*
 * @author  lqpl66
 * @date 创建时间：2017年8月18日 下午1:28:14 
 * @function     
 */
public class Son extends Father {
	public String name = "Son'name";

	public static void main(String[] args) {
		Father instance = new Son();
		System.out.println(instance.name);
		Son s = new Son();
		System.out.println(s.name);
	}
}

class Father {
	public String name = "Father'name";
}
