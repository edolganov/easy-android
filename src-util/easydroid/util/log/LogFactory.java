package easydroid.util.log;

import java.util.Hashtable;

import easydroid.util.android.log.DefaultLogger;


@SuppressWarnings("rawtypes")
public class LogFactory {
	
	private static String tagPrefix = "";
	private static Hashtable<Class, Logger> logs = new Hashtable<Class, Logger>();
	
	public static void setTagPrefix(String prefix){
		tagPrefix = prefix;
	}
	
	
	public static Logger getLog(Class clazz){
		Logger log = logs.get(clazz);
		if(log == null){
			String className = clazz.getSimpleName();
			log = new DefaultLogger(tagPrefix+className);
			logs.put(clazz, log);
		}
		return log;
		
	}

}
