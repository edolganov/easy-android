package easydroid.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class NetUtils {
	
	public static String sendRequest(String url) throws Exception {
		
		HttpURLConnection conn=null;
        try{
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            //It may happen due to keep-alive problem http://stackoverflow.com/questions/1440957/httpurlconnection-getresponsecode-returns-1-on-second-invocation
            if (code==-1) {
            	throw new IOException("conn.getResponseCode() is -1");
            }
            
            InputStream is = new BufferedInputStream(conn.getInputStream());
            String enc = conn.getHeaderField("Content-Encoding");
            if(enc !=null && enc.equalsIgnoreCase("gzip")){
                is = new GZIPInputStream(is);
            }
            
            String response = StreamUtil.convertStreamToString(is);
            return response;
        }
        finally{
            if(conn!=null){
                conn.disconnect();
            }
        }
		
	}

}
