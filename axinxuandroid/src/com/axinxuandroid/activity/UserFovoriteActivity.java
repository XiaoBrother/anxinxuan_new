package com.axinxuandroid.activity;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

 
import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddUserFavoriteThread;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.LoadUserFavoriteThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.RemoveUserFavoriteThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.DragListView;
import com.axinxuandroid.activity.view.MenuWindow;
import com.axinxuandroid.activity.view.UserFavoriteView;
import com.axinxuandroid.activity.view.BatchListView.BatchAdapter;
import com.axinxuandroid.activity.view.DragListView.DragListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.UserFavorite.FavoriteType;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.GPSUtil;
import com.ncpzs.util.LogUtil;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
 
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
 
public class UserFovoriteActivity extends NcpZsActivity{
	 private UserFavoriteView favoriteview;
  	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 favoriteview=new UserFavoriteView(this);
		 this.setContentView(favoriteview);
		 favoriteview.topview.setLeftImage(R.drawable.icon_back);
		 favoriteview.topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				UserFovoriteActivity.this.finish();
			}
		});
		 favoriteview.prepareUpdate(true);
  	}
	    
}