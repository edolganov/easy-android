package easydroid.util.android.log;

import easydroid.util.Util;
import easydroid.util.log.Logger;
import android.util.Log;


public final class DefaultLogger implements Logger {
	
	static final boolean LOG = false;
	
	private String tag;

	public DefaultLogger(String tag) {
		super();
		this.tag = tag;
	}

	private static String getLocation() {
		String className = DefaultLogger.class.getName();
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		boolean found = false;

		for (int i = 0; i < traces.length; i++) {
			StackTraceElement trace = traces[i];

			try {
				if (found) {
					if (!trace.getClassName().startsWith(className)) {
						Class<?> clazz = Class.forName(trace.getClassName());
						return "[" + getClassName(clazz) + "."
								+ trace.getMethodName() + ":"
								+ trace.getLineNumber() + "]: ";
					}
				} else if (trace.getClassName().startsWith(className)) {
					found = true;
					continue;
				}
			} catch (ClassNotFoundException e) {
			}
		}

		return "[]: ";
	}

	private static String getClassName(Class<?> clazz) {
		if (clazz != null) {
			if ( ! Util.isEmpty(clazz.getSimpleName())) {
				return clazz.getSimpleName();
			}

			return getClassName(clazz.getEnclosingClass());
		}

		return "";
	}

	@Override
	public void verbose(Object message) {
		if (LOG) {
			Log.v(tag, getLocation() + String.valueOf(message));
		}
	}

	@Override
	public void verbose(Object message, Throwable t) {
		if (LOG) {
			Log.v(tag, getLocation() + String.valueOf(message), t);
		}
	}

	@Override
	public void debug(Object message) {
		if (LOG) {
			Log.d(tag, getLocation() + String.valueOf(message));
		}
	}

	@Override
	public void debug(Object message, Throwable t) {
		if (LOG) {
			Log.d(tag, getLocation() + String.valueOf(message), t);
		}
	}

	@Override
	public void info(Object message) {
		if (LOG) {
			Log.i(tag, getLocation() + String.valueOf(message));
		}
	}

	@Override
	public void info(Object message, Throwable t) {
		if (LOG) {
			Log.i(tag, getLocation() + String.valueOf(message), t);
		}
	}

	@Override
	public void warn(Object message) {
		if (LOG) {
			Log.w(tag, getLocation() + String.valueOf(message));
		}
	}

	@Override
	public void warn(Object message, Throwable t) {
		if (LOG) {
			Log.w(tag, getLocation() + String.valueOf(message), t);
		}
	}

	@Override
	public void error(Object message) {
		if (LOG) {
			Log.e(tag, getLocation() + String.valueOf(message));
		}
	}

	@Override
	public void error(Object message, Throwable t) {
		if (LOG) {
			Log.e(tag, getLocation() + String.valueOf(message), t);
		}
	}

	@Override
	public void wtf(Object message) {
		if (LOG) {
			Log.wtf(tag, getLocation() + String.valueOf(message));
		}
	}

	@Override
	public void wtf(Object message, Throwable t) {
		if (LOG) {
			Log.wtf(tag, getLocation() + String.valueOf(message), t);
		}
	}
}
