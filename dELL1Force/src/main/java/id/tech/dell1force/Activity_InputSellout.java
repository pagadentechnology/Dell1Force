package id.tech.dell1force;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.ion.Ion;
import id.tech.dialogs.DialogDatePicker;
import id.tech.dialogs.DialogFragmentProgress;
import id.tech.dialogs.UploadImageDialog;
import id.tech.util.ConnectFunctions;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.RowData_Sales;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import id.tech.dell1force.R;

public class Activity_InputSellout extends ActionBarActivity {
	Button btn;
	String mUrl_Img_00;
	ImageView imgview_00;
	public static int CODE_UPLOAD = 111;
	SharedPreferences spf;
	EditText ed_ServiceTag, ed_PriceIDR, ed_PriceUSD;
	HorizontalScrollView horizontalScroll;
	ImageView btn_Add_Other1, btn_Add_Other2, btn_Add_Other3, btn_Add_Other4,
			btn_Add_Other5;
	View wrapper_Other1, wrapper_Other2, wrapper_Other3, wrapper_Other4,
			wrapper_Other5;
	ImageView btn_Delete_Other1, btn_Delete_Other2, btn_Delete_Other3,
			btn_Delete_Other4, btn_Delete_Other5;
	EditText ed_Other1, ed_Other2, ed_Other3, ed_Other4, ed_Other5;
	List<RowData_Sales> data_sales;
	Spinner spinner_Salesman;
	
	String cTgl_Transcation = "01/01/2015";
	TextView tv_Transcation;
	View btn_Date_Transcation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_sellout);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		getAllView();

		ActionBar ac = getSupportActionBar();
		ac.setDisplayHomeAsUpEnabled(true);
		ac.setTitle("Input Sell Out");

		new ASync_GetSalesStore().execute();
	}

	private void getAllView() {
		spinner_Salesman = (Spinner) findViewById(R.id.spinner_salesman);

		horizontalScroll = (HorizontalScrollView) findViewById(R.id.wrapper_horizontalview);
		ed_ServiceTag = (EditText) findViewById(R.id.ed_servicetag);
		ed_PriceIDR = (EditText) findViewById(R.id.ed_priceIdr);
		ed_PriceUSD = (EditText) findViewById(R.id.ed_priceUsd);
		
		tv_Transcation = (TextView)findViewById(R.id.tv_date_transaction);
		btn_Date_Transcation = (View)findViewById(R.id.btn_date_transaction);
		
		final OnDateSetListener listenerDate_Transaction = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				if((monthOfYear + 1) < 10){
					if(dayOfMonth  < 10){
						cTgl_Transcation = "0"+ String.valueOf(dayOfMonth) + "/"
								+ "0"+String.valueOf(monthOfYear + 1) + "/"
								+ String.valueOf(year);
					}else{
						cTgl_Transcation =  String.valueOf(dayOfMonth) + "/"
								+ "0"+String.valueOf(monthOfYear + 1) + "/"
								+ String.valueOf(year);
					}
				}else{
					if(dayOfMonth < 10){
						cTgl_Transcation = "0"+ String.valueOf(dayOfMonth) + "/"
								+String.valueOf(monthOfYear + 1) + "/"
								+ String.valueOf(year);
					}else{
						cTgl_Transcation = String.valueOf(dayOfMonth) + "/"
								+String.valueOf(monthOfYear + 1) + "/"
								+ String.valueOf(year);
					}
										
				}
				
				
				
				tv_Transcation.setText(cTgl_Transcation);

			}
		};
		
		btn_Date_Transcation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogDatePicker fragment = new DialogDatePicker(listenerDate_Transaction);
//				DialogDatePicker fragment = new DialogDatePicker();
				FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction().add(fragment, "").commit();
			}
		});

		wrapper_Other1 = (View) findViewById(R.id.wrapper_other1);
		wrapper_Other2 = (View) findViewById(R.id.wrapper_other2);
		wrapper_Other3 = (View) findViewById(R.id.wrapper_other3);
		wrapper_Other4 = (View) findViewById(R.id.wrapper_other4);
		wrapper_Other5 = (View) findViewById(R.id.wrapper_other5);

		btn_Add_Other1 = (ImageView) findViewById(R.id.btn_other1);
		btn_Add_Other2 = (ImageView) findViewById(R.id.btn_other2);
		btn_Add_Other3 = (ImageView) findViewById(R.id.btn_other3);
		btn_Add_Other4 = (ImageView) findViewById(R.id.btn_other4);
		btn_Add_Other5 = (ImageView) findViewById(R.id.btn_other5);

		btn_Delete_Other1 = (ImageView) findViewById(R.id.btn_other1_delete);
		btn_Delete_Other2 = (ImageView) findViewById(R.id.btn_other2_delete);
		btn_Delete_Other3 = (ImageView) findViewById(R.id.btn_other3_delete);
		btn_Delete_Other4 = (ImageView) findViewById(R.id.btn_other4_delete);
		btn_Delete_Other5 = (ImageView) findViewById(R.id.btn_other5_delete);

		ed_Other1 = (EditText) findViewById(R.id.ed_other1);
		ed_Other2 = (EditText) findViewById(R.id.ed_other2);
		ed_Other3 = (EditText) findViewById(R.id.ed_other3);
		ed_Other4 = (EditText) findViewById(R.id.ed_other4);
		ed_Other5 = (EditText) findViewById(R.id.ed_other5);

		btn_Add_Other1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other1.setVisibility(View.VISIBLE);
				btn_Delete_Other1.setVisibility(View.VISIBLE);
				btn_Add_Other1.setVisibility(View.GONE);

				wrapper_Other2.setVisibility(View.VISIBLE);
			}
		});

		btn_Add_Other2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other2.setVisibility(View.VISIBLE);
				btn_Delete_Other2.setVisibility(View.VISIBLE);
				btn_Add_Other2.setVisibility(View.GONE);

				wrapper_Other3.setVisibility(View.VISIBLE);
			}
		});

		btn_Add_Other3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other3.setVisibility(View.VISIBLE);
				btn_Delete_Other3.setVisibility(View.VISIBLE);
				btn_Add_Other3.setVisibility(View.GONE);

				wrapper_Other4.setVisibility(View.VISIBLE);
			}
		});

		btn_Add_Other4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other4.setVisibility(View.VISIBLE);
				btn_Delete_Other4.setVisibility(View.VISIBLE);
				btn_Add_Other4.setVisibility(View.GONE);

				wrapper_Other5.setVisibility(View.VISIBLE);

			}
		});

		btn_Add_Other5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other5.setVisibility(View.VISIBLE);
				btn_Delete_Other5.setVisibility(View.VISIBLE);
				btn_Add_Other5.setVisibility(View.GONE);
			}
		});

		btn_Delete_Other1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other1.setVisibility(View.INVISIBLE);
				btn_Delete_Other1.setVisibility(View.GONE);
				btn_Add_Other1.setVisibility(View.VISIBLE);

				wrapper_Other2.setVisibility(View.GONE);
			}
		});

		btn_Delete_Other2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other2.setVisibility(View.INVISIBLE);
				btn_Delete_Other2.setVisibility(View.GONE);
				btn_Add_Other2.setVisibility(View.VISIBLE);

				wrapper_Other3.setVisibility(View.GONE);
			}
		});

		btn_Delete_Other3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other3.setVisibility(View.INVISIBLE);
				btn_Delete_Other3.setVisibility(View.GONE);
				btn_Add_Other3.setVisibility(View.VISIBLE);

				wrapper_Other4.setVisibility(View.GONE);
			}
		});

		btn_Delete_Other4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_Other4.setVisibility(View.INVISIBLE);
				btn_Delete_Other4.setVisibility(View.GONE);
				btn_Add_Other4.setVisibility(View.VISIBLE);

				wrapper_Other5.setVisibility(View.GONE);
			}
		});

		btn_Delete_Other5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// wrapper_Other5.setVisibility(View.GONE);
				ed_Other5.setVisibility(View.INVISIBLE);
				btn_Add_Other5.setVisibility(View.VISIBLE);
				btn_Delete_Other5.setVisibility(View.GONE);

			}
		});

		btn = (Button) findViewById(R.id.btn);
		imgview_00 = (ImageView) findViewById(R.id.img_00);

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
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_00, options);
					imgview_00.setVisibility(View.VISIBLE);
					imgview_00.setImageBitmap(b);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_inputsellout, menu);
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
				new Async_InputSellout().execute();
			} else {
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

	private class ASync_GetSalesStore extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			data_sales = new ArrayList<RowData_Sales>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ConnectFunctions cf = new ConnectFunctions();

			JSONObject jObj = cf.getSalesStore(spf.getString(
					Parameter_Collections.SH_STOREID, ""));
			try {
				JSONArray jArray = jObj
						.getJSONArray(Parameter_Collections.TAG_RESULT_DATA);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject c = jArray.getJSONObject(i);
					String username = c
							.getString(Parameter_Collections.TAG_USERNAME);
					String nama = c.getString(Parameter_Collections.TAG_NAME);

					data_sales.add(new RowData_Sales(username, nama));
				}
			} catch (JSONException e) {

			}

			// data_sales.add(new RowData_Sales("Ridho", "Ridho"));
			// data_sales.add(new RowData_Sales("Maulana", "Maulana"));
			// data_sales.add(new RowData_Sales("Aryasa", "Aryasa"));

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			List<String> nama = new ArrayList<String>();
			if (data_sales.size() == 0 || data_sales.isEmpty()) {
				nama.add(spf.getString(Parameter_Collections.SH_USERNAME, ""));
			} else {
				
				for (int i = 0; i < data_sales.size(); i++) {
					nama.add(data_sales.get(i).mUsername);
				}
			}
			

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.spinner_item, nama);
			spinner_Salesman.setAdapter(adapter);
		}
	}

	private class Async_InputSellout extends AsyncTask<Void, Void, JSONObject> {
		int serverRespondCode = 0;
		byte[] dataX;
		long totalSize = 0;
		String respondMessage;
		JSONObject jsonResult;
		DialogFragmentProgress pDialog;
		String cStatus, cMessage;
		String cUsername, cStoreID, cServiceTag, cPriceIDR, cPriceUSD, cOther1,
				cOther2, cOther3, cOther4, cOther5, cSalesman, cTgl_Transc;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			cUsername = spf.getString(Parameter_Collections.SH_USERNAME, "");
			cStoreID = spf.getString(Parameter_Collections.SH_STOREID, "");
			cServiceTag = ed_ServiceTag.getText().toString();
			cPriceIDR = ed_PriceIDR.getText().toString();
			cPriceUSD = ed_PriceUSD.getText().toString();
			cSalesman = spinner_Salesman.getSelectedItem().toString();
			cTgl_Transc = tv_Transcation.getText().toString();
			cOther1 = ed_Other1.getText().toString();
			cOther2 = ed_Other2.getText().toString();
			cOther3 = ed_Other3.getText().toString();
			cOther4 = ed_Other4.getText().toString();
			cOther5 = ed_Other5.getText().toString();
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
				URL url = new URL(Parameter_Collections.URL_INPUT_SELLOUT);

				Log.e("url gambar", url_file);

				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");

				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("Proof", url_file);

				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"Proof\";filename=\""
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

				// param service Tag
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "ServiceTag" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cServiceTag + lineEnd);

				// param IDR
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "PriceIDR" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cPriceIDR + lineEnd);

				// param USD
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "PriceUSD" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cPriceUSD + lineEnd);
				
				// param Transcation Date
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "TransactionDate" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cTgl_Transc + lineEnd);
				
				// param Transcation Date
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ "Salesman" + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cSalesman + lineEnd);

				// param other1
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_OTHER_1 + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cOther1 + lineEnd);

				// param other2
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_OTHER_2 + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cOther2 + lineEnd);

				// param other3
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_OTHER_3 + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cOther3 + lineEnd);

				// param other4
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_OTHER_4 + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cOther4 + lineEnd);

				// param other5
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_OTHER_5 + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cOther5 + lineEnd);

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
