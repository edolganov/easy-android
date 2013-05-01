package easydroid.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class Util {
	
	public static boolean isEmpty(Object o) {
		return o == null;
	}

	public static boolean isEmpty(Collection<?> col) {
		return col == null || col.size() == 0;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}

	public static boolean isEmpty(Object[] arr) {
		return arr == null || arr.length == 0;
	}
	
	public static <T> ArrayList<T> list(T... elems) {

		if(elems == null){
			return null;
		}
		
		ArrayList<T> list = new ArrayList<T>();
		for (T elem : elems) {
			list.add(elem);
		}
		return list;
	}

	public static <T> ArrayList<T> toArrayList(Collection<T> collection) {

		if (collection == null) {
			return null;
		}

		ArrayList<T> out = null;
		if (collection instanceof ArrayList<?>) {
			out = (ArrayList<T>) collection;
		} else {
			out = new ArrayList<T>(collection);
		}
		return out;
	}
	
	
	public static Integer tryParseInt(Object obj, Integer defaultVal){
		try {
			String val = obj.toString();
			return Integer.parseInt(val);
		}catch (Exception e) {
			return defaultVal;
		}
	}
	
	
	public static Long tryParseLong(Object obj, Long defaultVal){
		try {
			String val = obj.toString();
			return Long.parseLong(val);
		}catch (Exception e) {
			return defaultVal;
		}
	}
	
    public static String toObjectString(Object ob) {
        return ob.getClass().getName() + "@" + Integer.toHexString(ob.hashCode());
    }
    
	public static void checkArgumentForEmpty(Object ob, String errorMsg)
			throws IllegalArgumentException {
		if (isEmpty(ob)) {
			throw new IllegalArgumentException(errorMsg);
		}
	}

	public static void checkArgument(boolean state, String errorMsg)
			throws IllegalStateException {
		if (!state) {
			throw new IllegalArgumentException(errorMsg);
		}
	}

	public static void checkState(boolean state, String errorMsg)
			throws IllegalStateException {
		if (!state) {
			throw new IllegalStateException(errorMsg);
		}
	}
	
	public static String randomUUID() {
		UUID uuid = UUID.randomUUID();
		String out = uuid.toString();
		return out.toString();
	}
	
	public static boolean[] array(boolean... elems) {
		return elems;
	}
	
    public static int compareTo(long a, long b) {
		return (a<b ? -1 : (a==b ? 0 : 1));
    }
    
    public static int compareTo(int a, int b) {
		return (a<b ? -1 : (a==b ? 0 : 1));
    }
    
	public static String getDeltaTime(long start) {
		long stop = System.currentTimeMillis();
		long delta = Math.abs(stop - start);
		String deltaStr = delta>1000 ? ""+(delta/1000.)+"sec" : ""+delta+"ms";
		return deltaStr;
	}

}
