package easydroid.util.android;

import android.os.Environment;

public class StorageUtil {
	
	public static class ExternalStorageStatus {
		
		public boolean available;
		public boolean writeable;
		
		public ExternalStorageStatus(boolean available, boolean writeable) {
			super();
			this.available = available;
			this.writeable = writeable;
		}

		@Override
		public String toString() {
			return "ExternalStorageStatus [available=" + available
					+ ", writeable=" + writeable + "]";
		}
		
	}
	
	public static boolean isExternalStorageAvailable(){
		return getStatus().available;
	}
	
	public static boolean isExternalStorageWriteable(){
		return getStatus().writeable;
	}
	
	public static ExternalStorageStatus getStatus(){
		
		boolean available;
		boolean writeable;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			available = writeable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			available = true;
			writeable = false;
		} else {
			available = writeable = false;
		}
		
		return new ExternalStorageStatus(available, writeable);
	}

}
