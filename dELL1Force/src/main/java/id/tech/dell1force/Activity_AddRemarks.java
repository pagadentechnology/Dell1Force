package id.tech.dell1force;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import id.tech.dialogs.DialogFragmentProgress;
import id.tech.dialogs.UploadImageDialog;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;

public class Activity_AddRemarks extends ActionBarActivity{
	Button btn;
	String mUrl_Img_00;
	ImageView imgview_00;
	public static int CODE_UPLOAD = 111;
	SharedPreferences spf;
	EditText ed_Remarks;
	HorizontalScrollView horizontalScroll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addremarks);
		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		getAllView();
		
		ActionBar ac = getSupportActionBar();
		ac.setDisplayHomeAsUpEnabled(true);
		ac.setTitle("Add Remarks");
	}
	
	private void getAllView(){
		horizontalScroll = (HorizontalScrollView)findViewById(R.id.wrapper_horizontalview);
		ed_Remarks = (EditText)findViewById(R.id.ed_remarks);	
		btn = (Button)findViewById(R.id.btn);		
		imgview_00 = (ImageView)findViewById(R.id.img_00);
		
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent upload = new Intent(getApplicationContext(),
						UploadImageDialog.class);
				startActivityForResult(upload, CODE_UPLOAD);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Public_Functions.delete_Photos();
		Toast.makeText(getApplicationContext(), "Canceled. Images deleted", Toast.LENGTH_LONG).show();
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			if (requestCode == CODE_UPLOAD) {
				if (mUrl_Img_00 == null) {
					horizontalScroll.setVisibility(View.VISIBLE);					
					mUrl_Img_00 = data.getStringExtra("mUrl_Img");					
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_00);
					imgview_00.setVisibility(View.VISIBLE);
					imgview_00.setImageBitmap(b);
				} 
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_inputsellout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Public_Functions.delete_Photos();
			Toast.makeText(getApplicationContext(), "Canceled. Images deleted",
					Toast.LENGTH_LONG).show();
			finish();
			
			break;
		case R.id.action_send:
			if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
				new Async_Addremarks().execute();
			}else{
				Toast.makeText(getApplicationContext(),
						"No connections. Check your connections",
						Toast.LENGTH_LONG).show();
			}
			
			break;

		default:
			break;
		}		
		return super.onOptionsItemSelected(item);		
	}
	
	private class Async_Addremarks extends AsyncTask<Void, Void, JSONObject> {
		int serverRespondCode = 0;
		byte[] dataX;
		long totalSize = 0;
		String respondMessage;
		JSONObject jsonResult;
		DialogFragmentProgress pDialog;
		String cStatus, cMessage;
		String cRemarks, cUsername, cStoreID;
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
			
			cUsername = spf.getString(Parameter_Collections.SH_USERNAME, "");
			cStoreID = spf.getString(Parameter_Collections.SH_STOREID, "");
			cRemarks = ed_Remarks.getText().toString();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return uploadDataForm(mUrl_Img_00);
			// ConnectFunctions cf = new ConnectFunctions();
			// Ion.with(getApplicationContext()).load(Parameter_Collections.URL_INPUT_SELLOUT)
			// .setMultipartParameter("Proof",
			// "proof").setMultipartFile("test.jpg", new File(mUrl_Img_00))
			// .asJsonObject();
			// return cf.input_sellout(mUrl_Img_00);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			try {
				cStatus = result
						.getString(Parameter_Collections.TAG_RESULT_STATUS);
				cMessage = result
						.getString(Parameter_Collections.TAG_RESULT_MESSAGE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {

				finish();
			}
			Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
					.show();
		}

		private JSONObject uploadDataForm(String url_gambar) {
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			String url_file = url_gambar;
			File sourceFile = new File(url_file);

			try {
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(Parameter_Collections.URL_ADD_REMARKS);

				Log.e("url gambar", url_file);

				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");

				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("Picture", url_file);

				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"Picture\";filename=\""
						+ url_file + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// param username
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "Username" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cUsername + lineEnd);

				// param kode toko
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "StoreID" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cStoreID + lineEnd);
//				dos.writeBytes("STR-290" + lineEnd);

				// param USD
				 dos.writeBytes(twoHyphens + boundary + lineEnd);
				 dos.writeBytes("Content-Disposition: form-data; name=\""
				 + "Remarks" + "\"" + lineEnd);
				 dos.writeBytes(lineEnd);
				 dos.writeBytes(cRemarks + lineEnd);

				// param imei produk
//				 dos.writeBytes(twoHyphens + boundary + lineEnd);
//				 dos.writeBytes("Content-Disposition: form-data; name=\""
//				 + Parameter_Collections.TAG_OTHER_1 + "\"" + lineEnd);
//				 dos.writeBytes(lineEnd);
//				 dos.writeBytes("Test" + lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				serverRespondCode = conn.getResponseCode();
				respondMessage = conn.getResponseMessage();

				Log.e("RESPOND", respondMessage);

				if (serverRespondCode == 200) {
					Log.e("CODE ", "Success Upload");
				} else {
					Log.e("CODE ", "Success failed");
				}

				fileInputStream.close();
				dos.flush();

				InputStream is = conn.getInputStream();
				int ch;

				StringBuffer buff = new StringBuffer();
				while ((ch = is.read()) != -1) {
					buff.append((char) ch);
				}

				jsonResult = new JSONObject(buff.toString());
				dos.close();

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonResult;
		}
	}
}