package easydroid.util.android.file;

import java.io.File;

import easydroid.util.file.AbstractFileStorage;

import android.content.Context;

public class InternalFileStorage extends AbstractFileStorage {
	
	Context context;

	public InternalFileStorage(Context context) {
		super();
		this.context = context;
	}

	@Override
	public File getFile(String path) throws Exception {
		
		File appDir = context.getFilesDir();
		if(appDir == null){
			throw new IllegalStateException("Internal file dir is null");
		}
		
		appDir.mkdirs();
		
		if( ! path.startsWith("/")){
			path = "/"+path;
		}
		File out = new File(appDir.getPath()+path);
		return out;
	}
	
	
	

}
