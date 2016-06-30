package id.tech.dialogs;

import org.json.JSONException;
import org.json.JSONObject;

import id.tech.REST;
import id.tech.dell1force.Activity_ScanAbsen;
import id.tech.dell1force.R;
import id.tech.model.PojoAbsence;
import id.tech.model.PojoLogin;
import id.tech.util.ConnectFunctions;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DialogAbsence extends FragmentActivity {
	Spinner spinner_AbsenseType;
	EditText ed_Remarks;
	TextView tv_Remarks;
	Button btn, btn_Absen;
	
	SharedPreferences sh;
	String cType_Absence, cRemarks, cUsername, cStoreID, cLat, cLong;
	
//	String cType_Absence;
//	SharedPreferences sh;
//	private String cUsername, cStoreID, 

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.dialog_absence_field);
		
		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		
		cUsername = sh.getString(Parameter_Collections.SH_USERNAME,"");
		
		cLat = sh.getString(Parameter_Collections.TAG_LATITUDE, "");
		cLong = sh.getString(Parameter_Collections.TAG_LONGITUDE, "");
		
		getAllView();
	}

	private void getAllView(){
		spinner_AbsenseType = (Spinner)findViewById(R.id.spinner_absence);
		ed_Remarks = (EditText)findViewById(R.id.ed_remarks);
		tv_Remarks = (TextView)findViewById(R.id.tv_remarks);
		
		btn = (Button)findViewById(R.id.btn);
		btn_Absen = (Button)findViewById(R.id.btn_absen);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//				getApplicationContext(),
//				android.R.layout.simple_dropdown_item_1line, getResources()
//						.getStringArray(R.array.absence));
//		spinner_AbsenseType.setAdapter(adapter);
		spinner_AbsenseType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				cType_Absence = parent.getSelectedItem().toString();
				if(cType_Absence.equals("In") || cType_Absence.equals("Out")){
					ed_Remarks.setVisibility(View.GONE);
					tv_Remarks.setVisibility(View.GONE);
					btn_Absen.setVisibility(View.GONE);
					btn.setVisibility(View.VISIBLE);
				}else if(cType_Absence.equals("Day Off")){
					ed_Remarks.setVisibility(View.VISIBLE);
					tv_Remarks.setVisibility(View.VISIBLE);
					btn_Absen.setVisibility(View.VISIBLE);
					btn.setVisibility(View.GONE);
				}else if(cType_Absence.equals("Others")){
					ed_Remarks.setVisibility(View.VISIBLE);
					tv_Remarks.setVisibility(View.VISIBLE);
					btn_Absen.setVisibility(View.VISIBLE);
					btn.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent load = new Intent(getApplicationContext(), Activity_ScanAbsen.class);
				load.putExtra("tipe", cType_Absence);
				load.putExtra("remarks", ed_Remarks.getText().toString());
				startActivity(load);
				
				finish();
			}
		});
		
		btn_Absen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				new Async_Submit().execute();
				new Async_Submit_Retrofit().execute();
			}
		});
	}

	private class Async_Submit_Retrofit extends AsyncTask<Void,Void,Void>{
		DialogFragmentProgress pDialog;
		private String cStatus, cMessage;
		private boolean isSukses = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			cRemarks = ed_Remarks.getText().toString();
		}

		@Override
		protected Void doInBackground(Void... params) {
			final REST adapter = Public_Functions.init_retrofit();

			Observable<PojoAbsence> observable = adapter.absence_others(cUsername, cStoreID, cType_Absence, cLat,cLong,
					cRemarks);

			observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<PojoAbsence>() {

						@Override
						public void onCompleted() {
							Log.e("LOG", "Completed");
							if (isSukses) {
								pDialog.dismiss();
								finish();
							}
						}

						@Override
						public void onError(Throwable e) {
							Log.e("LOG", "Error");
						}

						@Override
						public void onNext(PojoAbsence pojoAbsence) {
							Log.e("LOG", "Completed");
							if (pojoAbsence.getStatus().equals("Ok")) {
								isSukses = true;

							}
						}
					});
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

		}
	}
	
	private class Async_Submit extends AsyncTask<Void, Void, Void>{
		DialogFragmentProgress pDialog;
		private String cStatus, cMessage;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ConnectFunctions cf = new ConnectFunctions();
			JSONObject jObj = cf.absence(cUsername, cType_Absence,
					cLat,cLong, "");

			try {
				cStatus = jObj
						.getString(Parameter_Collections.TAG_RESULT_STATUS);
				cMessage = jObj
						.getString(Parameter_Collections.TAG_RESULT_MESSAGE);

				if (cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {

				}

			} catch (JSONException e) {

			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			
			if (cStatus.equals(Parameter_Collections.TAG_RESULT_STATUS_OK)) {
				Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
				.show();
				
				sh.edit().putBoolean(Parameter_Collections.SH_ABSENTED, true).commit();
//				sh.edit().putString(Parameter_Collections.SH_STOREID, cStoreID).commit();
				
				finish();
			}else{
				Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG)
				.show();
			}
		}
	}
}
