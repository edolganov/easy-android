package easydroid.util.android.pref;

import easydroid.util.android.PrefUtil;
import android.content.Context;
import android.content.SharedPreferences;

public class Pref2Type<T> {
	
	private boolean fullFieldName = false;
	Class<T> type;
	SharedPreferences prefs;

	public Pref2Type(Context context, Class<T> type) {
		this(context, type, null);
	}
	
	public Pref2Type(Context context, Class<T> type, String fileNameSuffix) {
		
		this.type = type;
		
		if(fileNameSuffix == null){
			fileNameSuffix = "";
		}
		String fileName = type.getName() + fileNameSuffix + "_preferences";
		prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
	}
	
	
	public void save(Object obj) {
		PrefUtil.trySaveTo(prefs, obj, fullFieldName);
	}
	
	public void clear(){
		PrefUtil.tryRemoveFrom(prefs, type, fullFieldName);
	}
	
	
	public T get(){
		return PrefUtil.tryRestoreFrom(prefs, type, fullFieldName);
	}
	

}
