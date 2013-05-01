package easydroid.gf.android;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Application;
import dalvik.system.DexFile;
import easydroid.gf.extra.scan.ClassScanner;

public class AndroidClassScanner implements ClassScanner {
	
	public volatile static Application application;

	@Override
	public Set<Class<?>> getClasses(String packageRoot, Class<?> parentClass) {
		Application app = application;
		if(app == null){
			throw new IllegalStateException("static application field is null");
		}
		
		try {
			Set<Class<?>> out = scan(app, packageRoot, parentClass);
			return out;
		}catch (Exception e) {
			throw new IllegalStateException("can't scan", e);
		}

	}
	
	private Set<Class<?>> scan(Application application, String packageRoot, Class<?> parentClass) throws Exception {
		
		HashSet<Class<?>> out = new HashSet<Class<?>>();
		
		String sourcePath = application.getApplicationInfo().sourceDir;
		List<String> paths = new ArrayList<String>();

		if (sourcePath != null) {
			DexFile dexfile = new DexFile(sourcePath);
			Enumeration<String> entries = dexfile.entries();

			while (entries.hasMoreElements()) {
				paths.add(entries.nextElement());
			}
		}
		// Robolectric fallback
		else {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> resources = classLoader.getResources("");

			while (resources.hasMoreElements()) {
				String path = resources.nextElement().getFile();
				if (path.contains("bin")) {
					paths.add(path);
				}
			}
		}

		for (String path : paths) {
			File file = new File(path);
			ClassLoader classLoader = application.getClass().getClassLoader();
			scanForModelClasses(file, packageRoot, parentClass, classLoader, out);
		}
		
		return out;
	}

	private void scanForModelClasses(File path, String packageRoot, Class<?> parentClass, ClassLoader classLoader, Set<Class<?>> out) {
		if (path.isDirectory()) {
			for (File file : path.listFiles()) {
				scanForModelClasses(file, packageRoot, parentClass, classLoader, out);
			}
		}
		else {
			String className = path.getName();
			if( ! className.startsWith(packageRoot)){
				return;
			}

			// Robolectric fallback
			if (!path.getPath().equals(className)) {
				className = path.getPath();

				if (className.endsWith(".class")) {
					className = className.substring(0, className.length() - 6);
				}
				else {
					return;
				}

				className = className.replace("/", ".");

				int packageNameIndex = className.lastIndexOf(packageRoot);
				if (packageNameIndex < 0) {
					return;
				}

				className = className.substring(packageNameIndex);
			}

			try {
				Class<?> candidat = Class.forName(className, false, classLoader);
				int modifiers = candidat.getModifiers();
				if( ! Modifier.isAbstract(modifiers) && parentClass.isAssignableFrom(candidat)){
					out.add(candidat);
				}
			}
			catch (ClassNotFoundException e) {
				//ok
			}
		}
	}

}
