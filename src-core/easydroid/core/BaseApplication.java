package easydroid.core;

import android.app.Application;
import android.content.Context;
import easydroid.core.context.GlobalContext;
import easydroid.gf.android.AndroidClassScanner;

public class BaseApplication extends Application {
	
	@Override
	public void onCreate() {
		
		Context applicationContext = getApplicationContext();
		GlobalContext.getInstance().setApplicationContext(applicationContext);
		
		AndroidClassScanner.application = this;
		
		
	}

}
