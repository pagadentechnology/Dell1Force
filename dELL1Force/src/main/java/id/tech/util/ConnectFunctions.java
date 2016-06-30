package id.tech.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

//import ch.boye.httpclientandroidlib.client.ClientProtocolException;
//import ch.boye.httpclientandroidlib.client.methods.HttpPost;
//import ch.boye.httpclientandroidlib.HttpEntity;
//import ch.boye.httpclientandroidlib.HttpResponse;
//import ch.boye.httpclientandroidlib.entity.ByteArrayEntity;
//import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
//import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
//import ch.boye.httpclientandroidlib.entity.mime.content.ByteArrayBody;
//import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
//import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
//import ch.boye.httpclientandroidlib.impl.client.CloseableHttpClient;
//import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ConnectFunctions {
	private JSONParser jsonParser;
	private JSONObject jObj;

	private String empty_json_prefix = "{Status: \"empty\",Message: \"";
	private String empty_json_end = "\"}";
	
	public ConnectFunctions() {
		// TODO Auto-generated constructor stub
		jsonParser = new JSONParser();
	}
	
	public JSONObject getSalesStore(String store_id){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_STORE_ID, store_id));
				
		jObj = jsonParser.makeHttpRequest(Parameter_Collections.URL_GET_SALESSTORE, "POST", params);
		Log.e("RESPONSE LOGIN = ", jObj.toString());
		return jObj;
	}
	
	public JSONObject login(String username, String password){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_USERNAME, username));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_PASSWORD, password));

		try{
			jObj = jsonParser.makeHttpRequest(Parameter_Collections.URL_LOGIN, "POST", params);
//			jObj = null;

		}catch (NullPointerException e){
			try{
				jObj = new JSONObject(empty_json_prefix + e.getMessage().toString() + empty_json_end);
				Log.e("RESPONSE LOGIN = ", jObj.toString());
			}catch (JSONException ee){
				ee.printStackTrace();
			}
		}catch (RuntimeException e){
			try{
				jObj = new JSONObject(empty_json_prefix + e.getMessage().toString() + empty_json_end);
				Log.e("RESPONSE LOGIN = ", jObj.toString());
			}catch (JSONException ee){
				ee.printStackTrace();
			}
		}catch (Exception e){
			try{
				jObj = new JSONObject(empty_json_prefix + e.getMessage().toString() + empty_json_end);
				Log.e("RESPONSE LOGIN = ", jObj.toString());
			}catch (JSONException ee){
				ee.printStackTrace();
			}
		}

		if(jObj == null){
			try{
				jObj = new JSONObject(empty_json_prefix + "Internal Server Error" + empty_json_end);
				Log.e("RESPONSE LOGIN = ", jObj.toString());
			}catch (JSONException ee){
				ee.printStackTrace();
			}
		}
		return jObj;
	}
	
	public JSONObject absence(String username, String store_id, String absence,
			String latitude, String longitude){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_USERNAME, username));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_STORE_ID, store_id));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_ABSENCE, absence));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_LATITUDE, latitude));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_LONGITUDE, longitude));
//		params.add(new BasicNameValuePair(Parameter_Collections.TAG_REMARKS, remarks));
		
		jObj = jsonParser.makeHttpRequest(Parameter_Collections.URL_ABSEN, "POST", params);
		Log.e("RESPONSE LOGIN = ", jObj.toString());
		return jObj;
	}
	
	public JSONObject absence(String username, String absence,
			String latitude, String longitude){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_USERNAME, username));
		
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_ABSENCE, absence));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_LATITUDE, latitude));
		params.add(new BasicNameValuePair(Parameter_Collections.TAG_LONGITUDE, longitude));
//		params.add(new BasicNameValuePair(Parameter_Collections.TAG_REMARKS, remarks));
		
		jObj = jsonParser.makeHttpRequest(Parameter_Collections.URL_ABSEN, "POST", params);
		Log.e("RESPONSE LOGIN = ", jObj.toString());
		return jObj;
	}
}
