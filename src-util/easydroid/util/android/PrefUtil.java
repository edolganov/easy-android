package easydroid.util.android;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefUtil {
	
	private final static Logger log = LogFactory.getLog(PrefUtil.class);
	

	
	public static void trySaveTo(SharedPreferences prefs, Object obj) {
		trySaveTo(prefs, obj, true);
	}
	
	public static void trySaveTo(SharedPreferences prefs, Object obj, boolean fullFieldName) {
		try {
			Editor edit = prefs.edit();
			saveTo(edit, obj, fullFieldName);
			edit.commit();
		}catch (Exception e) {
			log.info("can't save "+obj+" to prefs");
		}
	}
	
	public static void trySaveTo(Editor edit, Object obj) {
		try{
			saveTo(edit, obj);
		}catch (Exception e) {
			log.info("can't save "+obj+" to prefs");
		}
	}
	
	public static void saveTo(Editor edit, Object obj) throws IllegalStateException {
		saveTo(edit, obj, true);
	}
	
	public static void saveTo(Editor edit, Object obj, boolean fullFieldName) throws IllegalStateException {
		
		try {
			
			Field[] fields = obj.getClass().getDeclaredFields();
			if(fields == null){
				return;
			}
			
			for (Field field : fields) {
				putTo(edit, field, obj, fullFieldName);
			}
			
		}catch (Exception e) {
			throw new IllegalStateException("can't save "+obj, e);
		}
	}
	
	public static void tryRemoveFrom(SharedPreferences prefs, Class<?> type) {
		tryRemoveFrom(prefs, type, true);
	}
	
	public static void tryRemoveFrom(SharedPreferences prefs, Class<?> type, boolean fullFieldName) {
		
		try {
			Editor edit = prefs.edit();
			removeFrom(edit, type, fullFieldName);
			edit.commit();
		}catch (Exception e) {
			log.info("can't remove "+type+" from prefs");
		}
		
	}
	
	public static void tryRemoveFrom(Editor edit, Class<?> type) {
		tryRemoveFrom(edit, type, true);
	}
	
	public static void tryRemoveFrom(Editor edit, Class<?> type, boolean fullFieldName) {
		
		try {
			removeFrom(edit, type, fullFieldName);
		}catch (Exception e) {
			log.info("can't remove "+type+" from prefs");
		}
		
	}
	
	public static void removeFrom(Editor edit, Class<?> type) throws IllegalStateException {
		removeFrom(edit, type, true);
	}
	
	public static void removeFrom(Editor edit, Class<?> type, boolean fullFieldName) throws IllegalStateException {
		try {
			
			Field[] fields = type.getDeclaredFields();
			if(fields == null){
				return;
			}
			
			for (Field field : fields) {
				removeFrom(edit, field, fullFieldName);
			}
			
		}catch (Exception e) {
			throw new IllegalStateException("can't remove "+type, e);
		}
	}
	
	
	public static <T> T tryRestoreFrom(SharedPreferences prefs, Class<T> type){
		return (T)tryRestoreFrom(prefs, type, true);
	}
	
	public static <T> T tryRestoreFrom(SharedPreferences prefs, Class<T> type, boolean fullFieldName){
		try{
			return (T) restoreFrom(prefs, type, fullFieldName);
		}catch (Exception e) {
			log.info("can't restore "+type+" from prefs");
			return null;
		}
	}
	
	public static <T> T restoreFrom(SharedPreferences prefs, Class<T> type){
		return (T)restoreFrom(prefs, type, true);
	}


	public static <T> T restoreFrom(SharedPreferences prefs, Class<T> type, boolean fullFieldName){
		
		try {
			
			T obj = type.newInstance();
			
			Field[] fields = type.getDeclaredFields();
			if(fields == null){
				return obj;
			}
			
			boolean oneSetted = false;
			for (Field field : fields) {
				boolean setted = setFrom(prefs, field, obj, fullFieldName);
				if(setted){
					oneSetted = true;
				}
			}
			
			//return obj if restore something
			return oneSetted ? obj : null;
			
		}catch (Exception e) {
			throw new IllegalStateException("can't restore "+type, e);
		}
	}
	
	
	
	private static String getFieldKey(Field field, boolean fullFieldName){
		Class<?> declaringClass = field.getDeclaringClass();
		String fieldName = field.getName();
		String out = fullFieldName ? declaringClass.getName()+"#"+fieldName : fieldName;
		return out;
		
	}
	
	private static void removeFrom(Editor editor, Field field, boolean fullFieldName) throws Exception {
		
		if( ! isValidField(field)){
			return;
		}
		
		String key = getFieldKey(field, fullFieldName);
		editor.remove(key);

	}
	
	private static void putTo(Editor editor, Field field, Object obj, boolean fullFieldName) throws Exception {
		
		if( ! isValidField(field)){
			return;
		}
		
		Class<?> type = field.getType();
		String key = getFieldKey(field, fullFieldName);
		
		field.setAccessible(true);
		Object value = field.get(obj);
		
		if(value == null){
			return;
		}
		
		if(String.class.isAssignableFrom(type)){
			editor.putString(key, (String)value);
		}
		else if(Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)){
			editor.putInt(key, (Integer)value);
		}
		else if(Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)){
			editor.putLong(key, (Long)value);
		}
		else if(Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)){
			editor.putBoolean(key, (Boolean)value);
		}
		else if(Float.class.isAssignableFrom(type)|| float.class.isAssignableFrom(type)){
			editor.putFloat(key, (Float)value);
		}
		else {
			//TODO use annotations
			throw new IllegalStateException("unknow type for save: "+type);
		}

	}


	
	
	private static boolean setFrom(SharedPreferences prefs, Field field, Object obj, boolean fullFieldName) throws Exception {
		
		if( ! isValidField(field)){
			return false;
		}
		
		String key = getFieldKey(field, fullFieldName);
		if( ! prefs.contains(key)){
			return false;
		}
		
		Class<?> type = field.getType();
		Object value = null;
		if(String.class.isAssignableFrom(type)){
			value = prefs.getString(key, null);
		}
		else if(Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)){
			value = prefs.getInt(key, 0);
		}
		else if(Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)){
			value = prefs.getLong(key, 0l);
		}
		else if(Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)){
			value = prefs.getBoolean(key, false);
		}
		else if(Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)){
			value = prefs.getFloat(key, 0f);
		}
		else {
			//TODO use annotations
			throw new IllegalStateException("unknow type for restore: "+type);
		}
		
		field.setAccessible(true);
		field.set(obj, value);
		
		return true;
		
	}
	
	private static boolean isValidField(Field field) {
		int modifiers = field.getModifiers();
		return ! Modifier.isStatic(modifiers) && ! Modifier.isFinal(modifiers);
	}

}
