package easydroid.util;

public class StringUtil {
	
	
	public static String cropWithDots(String str, int maxLenght){
		return crop(str, maxLenght, true);
	}
	
	public static String crop(String str, int maxLenght, boolean useDots){
		if(Util.isEmpty(str)){
			return str;
		}
		
		if(str.length() <= maxLenght){
			return str;
		}
		
		String out = str.substring(0, maxLenght);
		if(useDots){
			out += "...";
		}
		return out;
	}

}
