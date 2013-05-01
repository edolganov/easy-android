package easydroid.util.android.async;

import easydroid.util.async.SingleTimerAction;
import easydroid.util.model.Shutdownable;
import android.app.Activity;

public class SingleTimerUiAction implements Shutdownable {
	
	SingleTimerAction timerAction;
	Activity activity;

	public SingleTimerUiAction(Activity activity, long delay) {
		timerAction = new SingleTimerAction(delay);
		this.activity = activity;
	}
	
	public void doSingleUiAction(final Runnable original) {
		
		timerAction.doSingleAction(new Runnable() {
			
			@Override
			public void run() {
				activity.runOnUiThread(original);
			}
		});
		
	}

	@Override
	public void shutdown() {
		timerAction.shutdown();
	}
	
	

}
