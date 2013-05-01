package easydroid.util.android.file;

import java.io.File;

import easydroid.util.file.AbstractFileStorage;

import android.content.Context;

public class ExternalFileStorage extends AbstractFileStorage {
	
	Context context;

	public ExternalFileStorage(Context context) {
		super();
		this.context = context;
	}

	@Override
	public File getFile(String path) throws Exception {
		
		File appDir = context.getExternalFilesDir(null);
		if(appDir == null){
			throw new IllegalStateException("External storage is unmount");
		}
		
		appDir.mkdirs();
		
		if( ! path.startsWith("/")){
			path = "/"+path;
		}
		File out = new File(appDir.getPath()+path);
		return out;
	}
	
	
	

}
