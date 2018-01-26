package yl.test;
/*
 * @author  lqpl66
 * @date 创建时间：2017年5月3日 下午2:35:23 
 * @function     
 */
public class StringTest {  
	  
    public static String toHexString(String s) {  
        String str = "";  
        for (int i = 0; i < s.length(); i++) {  
            int ch = (int) s.charAt(i);  
            String s4 = Integer.toHexString(ch);  
            str = str + s4;  
        }  
        return "0x" + str;//0x表示十六进制  
    }  
  
    //转换十六进制编码为字符串  
    public static String toStringHex(String s) {  
        if ("0x".equals(s.substring(0, 2))) {  
            s = s.substring(2);  
        }  
        byte[] baKeyword = new byte[s.length() / 2];  
        for (int i = 0; i < baKeyword.length; i++) {  
            try {  
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(  
                        i * 2, i * 2 + 2), 16));  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
  
        try {  
            s = new String(baKeyword, "utf-8");//UTF-16le:Not  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        System.out.println(s);
        return s;  
    }  
  
    public static void main(String[] args) throws Exception {  
//        String str = "test";  
//        printHexString(str.getBytes());  
    	String str = "10000111";
    	toStringHex(str);
    }  
  
    public static void printHexString(byte[] b) {  
        for (int i = 0; i < b.length; i++) {  
            String hex = Integer.toHexString(b[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            System.out.print(hex.toUpperCase());  
        }  
    }  
  
}  
