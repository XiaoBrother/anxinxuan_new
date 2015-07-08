package com.axinxuandroid.activity;

import java.util.List;

import org.w3c.dom.Text;

import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectVilleageActivity extends NcpZsActivity{

	private List<Villeage> villeages;
	private ListView list;
	private CommonTopView topview;
	private UserService uservice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.selectvilleage);
 		list=(ListView) this.findViewById(R.id.selectvilleage_list);
 		topview=(CommonTopView) this.findViewById(R.id.selectvilleage_topview);
 		uservice=new UserService();
 		User user=uservice.getLastLoginUser();
 		if(user!=null)
 			villeages=user.getVilleages();
 		list.setAdapter(new SelectVilleageAdapter(this));
 		list.setOnItemClickListener(new OnItemClickListener() {
 			@Override
			public void onItemClick(AdapterView<?> viewadapter, View view, int position,
					long id) {
  				RadioButton rdio=(RadioButton) viewadapter.getChildAt(0);
 				rdio.setChecked(true);
 				backWithResult(villeages.get(position));
			}
		});
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				SelectVilleageActivity.this.finish();
			}
		});
	}

	public void backWithResult(Villeage uvilleage){
		 Intent aintent = new Intent(SelectVilleageActivity.this, AddBatchActivity.class);
		 aintent.putExtra("villeage_name", uvilleage.getVilleage_name());
		 aintent.putExtra("villeage_id", uvilleage.getVilleage_id());
		 setResult(RESULT_OK,aintent);
		 SelectVilleageActivity.this.finish();
	}

	public class SelectVilleageAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater inflater;
        public SelectVilleageAdapter(Context context){
        	this.context=context;
        	inflater=LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
 			return villeages==null?0:villeages.size();
		}

		@Override
		public Object getItem(int position) {
 			return position;
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Villeage villeage=villeages.get(position);
			if(villeage!=null){
				final LinearLayout view=(LinearLayout) inflater.inflate(R.layout.selectvilleage_item, null);
				TextView text=(TextView) view.findViewById(R.id.selectvilleage_item_name);
				text.setText(villeage.getVilleage_name());
				view.setOnTouchListener(new OnTouchListener() {
 					@Override
					public boolean onTouch(View v, MotionEvent event) {
 						if(event.getAction()==MotionEvent.ACTION_DOWN){
 							int start=list.getFirstVisiblePosition();
 							int end=list.getLastVisiblePosition();
 							for(int i=start;i<=end;i++){
 								LinearLayout sview=(LinearLayout) list.getChildAt(i);
 								RadioButton sdio=(RadioButton) sview.getChildAt(0);
 								sdio.setChecked(false);
 							}
  							RadioButton rdio=(RadioButton) view.getChildAt(0);
 	 		 				rdio.setChecked(true);
  						}
 						return false;
					}
				});
				view.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
  		 				backWithResult(villeage);
					}
				});
				return view;
			}
 			return null;
		}
		
	}

	 
}
