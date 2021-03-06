package id.tech.dialogs;

import id.tech.util.Parameter_Collections;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class DialogLogout extends DialogFragment{
	private SharedPreferences sh;
	Context context;

	public DialogLogout() {

	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
	}

	public DialogLogout(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		sh = this.context.getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Yakin Keluar ?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sh.edit().clear().commit();
				Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_LONG).show();
//				Intent load = new Intent(getActivity(), Login_Activity.class);
//				getActivity().startActivity(load);
				getActivity().finish();	
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return builder.create();
	}

}
