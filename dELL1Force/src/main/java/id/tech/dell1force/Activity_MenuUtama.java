package id.tech.dell1force;


import id.tech.adapters.RecyclerAdapter_MenuUtama;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

public class Activity_MenuUtama extends ActionBarActivity {
	RecyclerView rv;
	RecyclerView.Adapter adapter;
//	RecyclerView.LayoutManager layoutManager;
	RecyclerView.ItemDecoration decoration;
	ImageView img_logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menuutama);
		
		ActionBar ac = getSupportActionBar();
		ac.hide();
		
		rv = (RecyclerView)findViewById(R.id.recycler_view);
//		rv.setHasFixedSize(false);
//		layoutManager = new GridLayoutManager(getApplicationContext(), 1);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		
		rv.setLayoutManager(layoutManager);
		
//		adapter = new RecyclerAdapter_MenuUtama(getApplicationContext(), this);
		adapter = new RecyclerAdapter_MenuUtama(getApplicationContext(), Activity_MenuUtama.this, getSupportFragmentManager());
		
		rv.setAdapter(adapter);
		
	}

}
