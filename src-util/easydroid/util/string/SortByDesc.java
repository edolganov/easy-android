package easydroid.util.string;

import java.util.Comparator;

public class SortByDesc implements Comparator<String> {
	
	public static final SortByDesc instance = new SortByDesc();

	@Override
	public int compare(String a, String b) {
		return (-1) * a.compareTo(b);
	}
	
}
