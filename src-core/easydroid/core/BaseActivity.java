package easydroid.core;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import easydroid.core.init.CreateUI;
import easydroid.core.init.FindOnActivityResultMethods;
import easydroid.core.init.InjectNonView;
import easydroid.core.init.InjectViewAndUI;
import easydroid.core.init.SetContentView;
import easydroid.core.init.FindOnActivityResultMethods.MethodWrapper;
import easydroid.core.shutdown.ShutdownOnDestroy;
import easydroid.core.view.BaseUI;
import easydroid.core.view.UICache;
import easydroid.util.android.async.BaseAsyncTask;
import easydroid.util.android.async.BaseAsyncTaskContext;
import easydroid.util.log.LogFactory;
import easydroid.util.log.Logger;


public abstract class BaseActivity extends Activity implements BaseAsyncTaskContext {
	
	protected Logger log = LogFactory.getLog(getClass());
	
	UICache uiCache = new UICache();
	
	List<MethodWrapper> onActivityResultMethods = Collections.emptyList();
	
	Dialog singleDialog;
	
	boolean destroyed;
	
	@Override
	public boolean isValidTaskContext() {
		return !destroyed;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new InjectNonView(this).invoke();
		new SetContentView(this).invoke();
		this.onActivityResultMethods = new FindOnActivityResultMethods(this).invoke();
		
		onCreateSafe(savedInstanceState);
	}
	
	protected void onCreateSafe(Bundle savedInstanceState){/* override if need */}
	
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		new InjectViewAndUI(this).invoke();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		for (MethodWrapper invokeCandidat : onActivityResultMethods) {
			if(invokeCandidat.isTarget(requestCode, resultCode)){
				invokeCandidat.invoke(data);
			}
		}
		
	}
	
	public <T extends BaseUI> T getUIByActivity(Class<T> type){
		return (T) getUIFromObject(this, type);
	}
	
	public <T extends BaseUI> T getUI(View view, Class<T> type){
		return (T) getUIFromObject(view, type);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends BaseUI> T getUIFromObject(Object viewOrActivity, Class<T> type){
		T out = (T)uiCache.get(viewOrActivity, type);
		if(out ==  null){
			out = (T) new CreateUI<T>(viewOrActivity, type).invoke();
			uiCache.put(viewOrActivity, type, out);
		}
		return out;
	}
	
	public void startActivity(Class<? extends Activity> activityType) {
		Intent intent = new Intent();
		intent.setClass(this, activityType);
		startActivity(intent);
	}

	public void startActivityForResult(Class<? extends Activity> activityType, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, activityType);
		startActivityForResult(intent, requestCode);
	}

	public void finishWithOkResult() {
		finishWithOkResult(null);
	}
	
	public void finishWithOkResult(Intent data) {
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(BaseAsyncTask task){
		task.setTaskContext(this);
		execute((AsyncTask)task);
	}
	
	public <T> void execute(AsyncTask<T, ?, ?> task, T... params){
		task.execute(params);
	}
	
	public void postOnUiThread(Runnable r){
		new Handler().post(r);
	}
	
	public void showLoadindDialog(String msg){
		showLoadindDialog("", msg);
	}
	
	public void showLoadindDialog(String title, String msg){
		checkDialogAlreadyExists();
		singleDialog = ProgressDialog.show(this, title, msg, true);
	}
	
	public void showOkDialog(String title, String msg, DialogInterface.OnClickListener onClick){
		showOkDialog(title, msg, "Ok", onClick);
	}
	
	public void showOkDialog(String title, String msg, String okText, DialogInterface.OnClickListener onClick){
		checkDialogAlreadyExists();
		singleDialog = new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton(okText, onClick)
			.show();
	}
	
	private void checkDialogAlreadyExists() {
		if(singleDialog != null){
			throw new IllegalStateException("singleDialog already exists");
		}
	}
	
	public void dismissCreatedDialog(){
		if(singleDialog != null){
			singleDialog.dismiss();
			singleDialog = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		destroyed = true;
		
		if(singleDialog != null){
			log.info("remove exists dialog");
			singleDialog = null;
		}
		
		shutdownFields();
		
		onDestroySafe();
	}
	
	private void shutdownFields() {
		new ShutdownOnDestroy(this).invoke();
	}

	protected void onDestroySafe(){/* override if need */}
	
	@Override
	protected void onStart() {
		super.onStart();
		onStartSafe();
	}
	
	protected void onStartSafe(){/* override if need */}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		onCreateContextMenuSafe(menu, v, menuInfo);
	}
	
	protected void onCreateContextMenuSafe(ContextMenu menu, View v, ContextMenuInfo menuInfo) {/* override if need */}
	

}
