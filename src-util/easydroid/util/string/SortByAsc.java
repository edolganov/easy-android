package easydroid.util.string;

import java.util.Comparator;

public class SortByAsc implements Comparator<String> {
	
	public static final SortByAsc instance = new SortByAsc();

	@Override
	public int compare(String a, String b) {
		return a.compareTo(b);
	}
	
}
