package id.tech.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import id.tech.REST;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class Public_Functions {
	public Context ctx;
	
	public Public_Functions(Context ctx) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
	}

	public static REST init_retrofit(){
		final OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.setReadTimeout(270, TimeUnit.SECONDS);
		okHttpClient.setConnectTimeout(270, TimeUnit.SECONDS);

		Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).
						baseUrl(Parameter_Collections.URL_BASE_RETROFIT).build();
		REST adapter = retrofit.create(REST.class);
		return adapter;
	}
	
	public static String md5(String string) {
	    byte[] hash;

	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {
	        int i = (b & 0xFF);
	        if (i < 0x10) hex.append('0');
	        hex.append(Integer.toHexString(i));
	    }

	    return hex.toString();
	}
	
//	public static String md5(String s) {
//	    try {
//	        // Create MD5 Hash
//	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
//	        digest.update(s.getBytes());
//	        byte messageDigest[] = digest.digest();
//
//	        // Create Hex String
//	        StringBuffer hexString = new StringBuffer();
//	        for (int i=0; i<messageDigest.length; i++)
//	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
//	        return hexString.toString();
//
//	    } catch (NoSuchAlgorithmException e) {
//	        e.printStackTrace();
//	    }
//	    return "";
//	}
	
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {

			android.net.NetworkInfo wifi = cm
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			android.net.NetworkInfo mobile = cm
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isConnected() || mobile.isConnected()) {
				Log.d("Koneksi Data ADA", "");
				return true;
			} else if (wifi.isConnected() && mobile.isConnected()) {
				Log.d("Koneksi Data wifi dan mobile ADA", "");
				return true;
			} else {
				Log.d("Koneksi Data TIDAK ADA", "!!!");
				return false;
			}
		} catch (NullPointerException e) {
			Log.d("Connection Null Pointer= ", e.getMessage().toString());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Exception = ", e.getMessage().toString());
			return false;
		}
	}
	
	
	public static void delete_Photos(){
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + Parameter_Collections.URL_FOLDER_IMG_ISSUE);
		if(myDir.isDirectory()){
			String[] children = myDir.list();
	        for (int i = 0; i < children.length; i++) {
	            File filenya = new File(myDir, children[i]);
	            
	            if (filenya.exists()) {
	                String deleteCmd = "rm -r " + filenya.getAbsolutePath();
	                Runtime runtime = Runtime.getRuntime();
	                try {
	                    runtime.exec(deleteCmd);
	                } catch (IOException e) { }
	            }
	            
	        }
		}
	}
	
	public static void DeleteRecursive(File fileOrDir) {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/HTC/images/issue");
	    if (myDir.isDirectory()){
	    	for (File child : myDir.listFiles()){
	    		Log.e("delete =", child.getAbsolutePath());	
	            DeleteRecursive(child);
	    		myDir.delete();
	    	}
	    }	    
	}
	
//	public static boolean isNetworkAvailable(Context ctx) {
//		ConnectivityManager cm = (ConnectivityManager) ctx
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		try {
//
//			android.net.NetworkInfo wifi = cm
//					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			android.net.NetworkInfo mobile = cm
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//			if (wifi.isConnected() || mobile.isConnected()) {
//				Log.d("Koneksi Data ADA", "");
//				return true;
//			} else if (wifi.isConnected() && mobile.isConnected()) {
//				Log.d("Koneksi Data wifi dan mobile ADA", "");
//				return true;
//			} else {
//				Log.d("Koneksi Data TIDAK ADA", "!!!");
//				return false;
//			}
//		} catch (NullPointerException e) {
//			Log.d("Connection Null Pointer= ", e.getMessage().toString());
//			return false;
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.d("Exception = ", e.getMessage().toString());
//			return false;
//		}
//	}

}
