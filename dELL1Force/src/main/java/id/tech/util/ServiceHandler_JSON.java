package id.tech.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServiceHandler_JSON {
	private List<NameValuePair> params;
	private JSONObject jObj;
	private String result, url;
	private DefaultHttpClient hClient;
	private HttpEntity hEntity;
	private HttpResponse hResponse;

	public ServiceHandler_JSON() {
		// TODO Auto-generated constructor stub
		hClient = new DefaultHttpClient();
		hEntity = null;
		hResponse = null;
		params = null;
	}

	public JSONObject json_absen(String kode_toko, String id_pegawai,
			String longitude, String latitude, String jenis_absen) {
		try {
			params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair(Parameter_Collections.KIND,
			// Parameter_Collections.KIND_ABSEN ));

			// url = Parameter_Collections.URL_INSERT +
			// URLEncodedUtils.format(params, Parameter_Collections.UTF);

			HttpPost hPost = new HttpPost(url);
			hResponse = hClient.execute(hPost);
			hEntity = hResponse.getEntity();
			result = EntityUtils.toString(hEntity);

			jObj = new JSONObject(result);
		} catch (JSONException e) {

		} catch (UnsupportedEncodingException e) {

		} catch (ClientProtocolException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO: handle exception
		}
		return jObj;
	}

	public JSONObject json_login(String username, String password) {
		try {
			params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair(Parameter_Collections.KIND,
			// Parameter_Collections.KIND_LOGIN_MOBILE));
			params.add(new BasicNameValuePair(
					Parameter_Collections.TAG_USERNAME, username));
			params.add(new BasicNameValuePair(
					Parameter_Collections.TAG_PASSWORD, password));

			url = Parameter_Collections.URL_LOGIN
					+ URLEncodedUtils.format(params, Parameter_Collections.UTF);

			HttpGet hGet = new HttpGet(url);
			hResponse = hClient.execute(hGet);
			hEntity = hResponse.getEntity();
			result = EntityUtils.toString(hEntity);

			jObj = new JSONObject(result);
		} catch (JSONException e) {

		} catch (UnsupportedEncodingException e) {

		} catch (ClientProtocolException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO: handle exception
		}
		return jObj;
	}
}
