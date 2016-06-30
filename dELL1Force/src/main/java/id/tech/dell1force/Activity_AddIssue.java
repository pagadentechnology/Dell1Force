package id.tech.dell1force;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.support.v7.app.ActionBar;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.test.MoreAsserts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.tech.dialogs.DialogFragmentProgress;
import id.tech.dialogs.UploadImageDialog;
import id.tech.dialogs.DialogDatePicker;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;

public class Activity_AddIssue extends ActionBarActivity {
	Button btn;
	String mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03;;
	ImageView imgview_00,imgview_01,imgview_02, imgview_03; 
	public static int CODE_UPLOAD = 111;
	SharedPreferences spf;
	EditText ed_StoreName, ed_BrandName, ed_ProgramName, ed_Remarks;
	HorizontalScrollView horizontalScroll;

	String cTglMulai, cTglAkhir;
	TextView tv_Start, tv_Finish;
	View btn_Date_Start, btn_Date_Finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addissue);
		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		getAllView();

		ActionBar ac = getSupportActionBar();
		ac.setDisplayHomeAsUpEnabled(true);
		ac.setTitle("Add Competitor Issue");
	}

	private void getAllView() {
		horizontalScroll = (HorizontalScrollView) findViewById(R.id.wrapper_horizontalview);
		ed_StoreName = (EditText) findViewById(R.id.ed_name_store);
		ed_BrandName = (EditText) findViewById(R.id.ed_name_brand);
		ed_ProgramName = (EditText) findViewById(R.id.ed_name_program);
		ed_Remarks = (EditText) findViewById(R.id.ed_remarks);
		
		btn = (Button) findViewById(R.id.btn);
		imgview_00 = (ImageView) findViewById(R.id.img_00);
		imgview_01 = (ImageView)findViewById(R.id.img_01);
		imgview_02 = (ImageView)findViewById(R.id.img_02);
		imgview_03 = (ImageView)findViewById(R.id.img_03);

		tv_Start = (TextView) findViewById(R.id.tv_date_start);
		tv_Finish = (TextView) findViewById(R.id.tv_date_finish);

		btn_Date_Start = (View) findViewById(R.id.btn_date_start);
		btn_Date_Finish = (View) findViewById(R.id.btn_date_finish);

		final OnDateSetListener listenerDate_mulai = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				if((monthOfYear + 1) < 10){
					if(dayOfMonth < 10){
						cTglMulai = String.valueOf(year) + "-"
								+ "0"+String.valueOf(monthOfYear + 1) + "-"
								+ "0"+ String.valueOf(dayOfMonth);
					}else{
						cTglMulai = String.valueOf(year) + "-"
								+ "0"+String.valueOf(monthOfYear + 1) + "-"
								+ String.valueOf(dayOfMonth);
					}
					
				}else{
					if(dayOfMonth < 10){
						cTglMulai = String.valueOf(year) + "-"
								+ String.valueOf(monthOfYear + 1) + "-"
								+ "0"+ String.valueOf(dayOfMonth);
					}else{
						cTglMulai = String.valueOf(year) + "-"
								+ String.valueOf(monthOfYear + 1) + "-"
								+ String.valueOf(dayOfMonth);
					}										
				}				
				
				tv_Start.setText(cTglMulai);

			}
		};

		final OnDateSetListener listenerDate_akhir = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				if((monthOfYear + 1) < 10){
					if(dayOfMonth < 10){
						cTglAkhir = String.valueOf(year) + "-"
								+ "0"+String.valueOf(monthOfYear + 1) + "-"
								+ "0"+ String.valueOf(dayOfMonth);
					}else{
						cTglAkhir = String.valueOf(year) + "-"
								+ "0"+String.valueOf(monthOfYear + 1) + "-"
								+ String.valueOf(dayOfMonth);
					}
					
				}else{
					if(dayOfMonth < 10){
						cTglAkhir = String.valueOf(year) + "-"
								+ String.valueOf(monthOfYear + 1) + "-"
								+ "0"+ String.valueOf(dayOfMonth);
					}else{
						cTglAkhir = String.valueOf(year) + "-"
								+ String.valueOf(monthOfYear + 1) + "-"
								+ String.valueOf(dayOfMonth);
					}										
				}
				tv_Finish.setText(cTglAkhir);
			}
		};

		btn_Date_Start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogDatePicker fragment = new DialogDatePicker(listenerDate_mulai);
				FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction().add(fragment, "").commit();
			}
		});

		btn_Date_Finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogDatePicker fragment = new DialogDatePicker(listenerDate_akhir);
				FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction().add(fragment, "").commit();
			}
		});

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
		Toast.makeText(getApplicationContext(), "Canceled. Images deleted",
				Toast.LENGTH_LONG).show();
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
				} else if (mUrl_Img_01 == null) {
					mUrl_Img_01 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_01);
					imgview_01.setVisibility(View.VISIBLE);
					imgview_01.setImageBitmap(b);
				} else if (mUrl_Img_02 == null) {
					mUrl_Img_02 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_02);
					imgview_02.setVisibility(View.VISIBLE);
					imgview_02.setImageBitmap(b);
				} else if (mUrl_Img_03 == null) {
					mUrl_Img_03 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_03);
					imgview_03.setVisibility(View.VISIBLE);
					imgview_03.setImageBitmap(b);
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
				new Async_AddIssue().execute();
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
	
	private class Async_AddIssue extends AsyncTask<Void, Void, JSONObject> {
		int serverRespondCode = 0;
		byte[] dataX;
		long totalSize = 0;
		String respondMessage;
		JSONObject jsonResult;
		DialogFragmentProgress pDialog;
		String cStatus, cMessage;
		String cStoreName, cBrandName,cProgramName, cLat, cLong, cUsername, cRemarks;
		String url_file00, url_file01, url_file02, url_file03;
		File sourceFile00,sourceFile01, sourceFile02, sourceFile03;
		FileInputStream fileInputStream00,fileInputStream01, fileInputStream02, fileInputStream03;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
			
			cUsername = spf.getString(Parameter_Collections.SH_USERNAME, "");
			cStoreName = ed_StoreName.getText().toString();
			cBrandName = ed_BrandName.getText().toString();
			cProgramName = ed_ProgramName.getText().toString();
			cRemarks = ed_Remarks.getText().toString();
			cLat = spf.getString(Parameter_Collections.SH_LAT, "0.0");
			cLong = spf.getString(Parameter_Collections.SH_LONG, "0.0");
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return uploadDataForm(mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03);
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

		private JSONObject uploadDataForm(String url_gambar00, String url_gambar01,
				String url_gambar02, String url_gambar03) {
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			if(url_gambar00 != null){
				url_file00 = url_gambar00;
				 sourceFile00 = new File(url_file00);				
			}
			if(url_gambar01 != null){
				 url_file01 = url_gambar01;
				 sourceFile01 = new File(url_file01);
			}
			if(url_gambar02 != null){
				 url_file02 = url_gambar02;
				 sourceFile02 = new File(url_file02);
			}if(url_gambar03 != null){
				 url_file03 = url_gambar03;
				 sourceFile03 = new File(url_file03);
			}			

			try {				
				URL url = new URL(Parameter_Collections.URL_ADD_ISSUE);

				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");

				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				if(url_gambar00 != null){
					conn.setRequestProperty("Pictures", url_file00);
				}
				if(url_gambar01 != null){
					conn.setRequestProperty("Pictures", url_file01);
				}
				if(url_gambar02 != null){
					conn.setRequestProperty("Pictures", url_file02);
				}
				if(url_gambar03 != null){
					conn.setRequestProperty("Pictures", url_file03);
				}	

				dos = new DataOutputStream(conn.getOutputStream());
				if(url_gambar00 != null){
					fileInputStream00 = new FileInputStream(
							sourceFile00);
					//img 00
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"Pictures\";filename=\""
							+ url_file00 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream00.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream00.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream00.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream00.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					
				}if(url_gambar01 != null){
					fileInputStream01 = new FileInputStream(
							sourceFile01);
					//img 01
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"Pictures\";filename=\""
							+ url_file01 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream01.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream01.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream01.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream01.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					
				}if(url_gambar02 != null){
					fileInputStream02 = new FileInputStream(
							sourceFile02);
					//img 02
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"Pictures\";filename=\""
							+ url_file02 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream02.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream02.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream02.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream02.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				}if(url_gambar03 != null){
					fileInputStream03 = new FileInputStream(
							sourceFile03);
					//img 03
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"Pictures\";filename=\""
							+ url_file03 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream03.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream03.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream03.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream03.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					
				}	

				// param username
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "Username" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cUsername + lineEnd);

				// param nama toko
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "StoreName" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cStoreName+ lineEnd);

				// param Brand Nae
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "BrandName" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cBrandName + lineEnd);

				// param ProgramName
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "ProgramName" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cProgramName + lineEnd);

				// param CampaignStart
				 dos.writeBytes(twoHyphens + boundary + lineEnd);
				 dos.writeBytes("Content-Disposition: form-data; name=\""
				 + "CampaignStart" + "\"" + lineEnd);
				 dos.writeBytes(lineEnd);
				 dos.writeBytes(cTglMulai + lineEnd);

				// param CampaignEnd
				 dos.writeBytes(twoHyphens + boundary + lineEnd);
				 dos.writeBytes("Content-Disposition: form-data; name=\""
				 + "CampaignEnd" + "\"" + lineEnd);
				 dos.writeBytes(lineEnd);
				 dos.writeBytes(cTglAkhir + lineEnd);
				 
				// param Remarks
				 dos.writeBytes(twoHyphens + boundary + lineEnd);
				 dos.writeBytes("Content-Disposition: form-data; name=\""
				 + "Remarks" + "\"" + lineEnd);
				 dos.writeBytes(lineEnd);
				 dos.writeBytes(cRemarks + lineEnd);
				 
				// param Latitude
				 dos.writeBytes(twoHyphens + boundary + lineEnd);
				 dos.writeBytes("Content-Disposition: form-data; name=\""
				 + "Latitude" + "\"" + lineEnd);
				 dos.writeBytes(lineEnd);
				 dos.writeBytes(cLat + lineEnd);
				 
				// param Longitude
				 dos.writeBytes(twoHyphens + boundary + lineEnd);
				 dos.writeBytes("Content-Disposition: form-data; name=\""
				 + "Longitude" + "\"" + lineEnd);
				 dos.writeBytes(lineEnd);
				 dos.writeBytes(cLong+ lineEnd);
				 
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				serverRespondCode = conn.getResponseCode();
				respondMessage = conn.getResponseMessage();

				Log.e("RESPOND", respondMessage);

				if (serverRespondCode == 200) {
					Log.e("CODE ", "Success Upload");
				} else {
					Log.e("CODE ", "Success failed");
				}

				if(url_gambar00 != null){
					fileInputStream00.close();
				}if(url_gambar01 != null){
					fileInputStream01.close();
				}if(url_gambar02 != null){
					fileInputStream02.close();
				}if(url_gambar03 != null){
					fileInputStream03.close();
				}	
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