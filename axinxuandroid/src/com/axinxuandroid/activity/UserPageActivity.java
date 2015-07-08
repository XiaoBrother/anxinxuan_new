package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadRecordThread;
import com.axinxuandroid.activity.net.LoadUserInfoThread;
import com.axinxuandroid.activity.net.LoadUserRecordThread;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.EditTabelView;
import com.axinxuandroid.activity.view.MoreWindow;
import com.axinxuandroid.activity.view.TimeLineView;
import com.axinxuandroid.activity.view.UserVilleageView;
import com.axinxuandroid.activity.view.UserInfoView;
import com.axinxuandroid.activity.view.MoreWindow.OnClickItemListener;
import com.axinxuandroid.activity.view.TimeLineView.TimeLineLoadNextDatasListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.TimeOrderTool;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserRecord;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordTime;
import com.axinxuandroid.service.NetUpdateRecordService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.stay.pull.lib.PullToRefreshBase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserPageActivity extends NcpZsActivity {
	private static final int TAB_INDEX_USERRECORD = 1;
	private static final int TAB_INDEX_ADVOCATERECORD = 2;
	private static final int TAB_INDEX_USERINFO = 3;
	private UserService userservice;
	private UserVilleageService uvilleageservice;
	protected CommonTopView topview;
	private ProgressDialog progress;
	private LinearLayout advocaterecord;
	private Button  createrecord, userinfo;
	private UserInfoView infoview;
	private TimeLineView lineview;
	private RecordService recordservice;
	private User queryuser;
	private User loginuser;
	private int userid;
	private MoreWindow bottomwindow;
	private NetUpdateRecordService netupdateservice;
	private NetUpdateRecord userupdaterecord, advocateupdaterecord;
	private int type = -1;// 上一次加载的类型
	private boolean ispulldown = true;// 是否是下拉刷新
	private List<Record> datas;
	private List villeages;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userpage);
		userid = this.getIntent().getIntExtra("userid", -1);
		userservice = new UserService();
		loginuser = userservice.getLastLoginUser();
		topview = (CommonTopView) this.findViewById(R.id.userpage_topview);
		infoview = (UserInfoView) this.findViewById(R.id.userpage_infoview);
		createrecord = (Button) this.findViewById(R.id.userpage_record);
		// advocaterecord=(LinearLayout)
		// this.findViewById(R.id.userpage_advocate);
		userinfo = (Button) this.findViewById(R.id.userpage_info);
		lineview = (TimeLineView) this
				.findViewById(R.id.userpage_userrecordview);
		lineview.setShowstyle(TimeLineView.TIME_LINE_ITEM_STYLE_USERRECORD);
		recordservice = new RecordService();
		netupdateservice = new NetUpdateRecordService();
		uvilleageservice = new UserVilleageService();
		topview.setRightClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bottomwindow != null) {
					if (bottomwindow.isShowing()) {
						bottomwindow.dismiss();
					} else {
						bottomwindow.showAsDropDown(topview.getRightimg(),
								-DensityUtil.dip2px(150), -DensityUtil
										.dip2px(20));
					}
				}
			}
		});
		topview.setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reback();
			}
		});
		createrecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTabIndex(TAB_INDEX_USERRECORD);
			}
		});
		// advocaterecord.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// setTabIndex(TAB_INDEX_ADVOCATERECORD);
		// }
		// });
		userinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTabIndex(TAB_INDEX_USERINFO);
			}
		});
		lineview
				.setTimeLineLoadNextDatasListener(new TimeLineLoadNextDatasListener() {
					@Override
					public void loadDatas(int direct) {
						if (direct == PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH) {
							ispulldown = true;
						} else if (direct == PullToRefreshBase.MODE_PULL_UP_TO_REFRESH) {
							ispulldown = false;
						}
						new Thread() {
							@Override
							public void run() {
								prepareloadData(false);
							}
						}.start();
					}
				});
		new Thread() {
			@Override
			public void run() {
				prepareLoadUser();
			}

		}.start();
	}

	private void setTabIndex(int index) {
		((LinearLayout)createrecord.getParent()).setBackgroundResource(R.drawable.tabbg);
		createrecord.setTextColor(Color.rgb(64, 64, 64));
		// advocaterecord.setBackgroundResource(R.drawable.btnbg_normal);
		// ((TextView) advocaterecord.getChildAt(0)).setTextColor(Color.rgb(139,
		// 120, 103));
		((LinearLayout)userinfo.getParent()).setBackgroundResource(R.drawable.tabbg);
		userinfo.setTextColor(Color.rgb(64, 64, 64));
		NcpzsHandler hand = Gloable.getInstance().getCurHandler();
		switch (index) {
		case TAB_INDEX_USERRECORD:
			((LinearLayout)createrecord.getParent()).setBackgroundColor(Color.rgb(255, 255, 255));
		    createrecord.setTextColor(Color.rgb(93, 69, 64));
			if (queryuser == null) {
				hand.excuteMethod(new MessageDialogHandlerMethod("",
						"用户信息尚在加载中...."));
			} else {
				infoview.setVisibility(View.GONE);
				lineview.setVisibility(View.VISIBLE);
				new Thread() {
					@Override
					public void run() {
						loadUserRecords(true);
					}

				}.start();
			}
			break;
		case TAB_INDEX_ADVOCATERECORD:
			// advocaterecord.setBackgroundResource(R.drawable.btnbg_click);
			// ((TextView)
			// advocaterecord.getChildAt(0)).setTextColor(Color.rgb(148, 205,
			// 45));
			break;
		case TAB_INDEX_USERINFO:
			infoview.setVisibility(View.VISIBLE);
			lineview.setVisibility(View.GONE);
			((LinearLayout)userinfo.getParent()).setBackgroundColor(Color.rgb(255, 255, 255));
			userinfo.setTextColor(Color.rgb(93, 69, 64));
			break;
		}
	}

	private void init() {
		if (queryuser != null) {
			if (loginuser != null
					&& loginuser.getUser_id() == queryuser.getUser_id()) {
				 createrecord.setText("我的记录");
			} else
				createrecord.setText("他的记录");
			topview.setTitle(queryuser.getUser_name());
			infoview.setUserInfo(queryuser);
			villeages = uvilleageservice.getVilleageByUser(queryuser
					.getUser_id());
			if (villeages != null && villeages.size() > 0)
				bottomwindow = new MoreWindow(UserPageActivity.this, villeages,
						new OnClickItemListener() {
							@Override
							public void onclick(int index) {
								if (villeages.size() > index) {
									Villeage vil = (Villeage) villeages
											.get(index);
									Intent inte = new Intent(
											UserPageActivity.this,
											VilleageInfoActivity.class);
									inte.putExtra("villeage_id", vil
											.getVilleage_id());
									startActivity(inte);
								}
							}
						});
		}
	}

	/**
	 * 加载用户信息
	 */
	private void prepareLoadUser() {
		NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
		processhandler
				.setOnHandlerFinishListener(new OnHandlerFinishListener() {
					@Override
					public void onHandlerFinish(Object result) {
						if (result != null)
							progress = (ProgressDialog) ((Map) result)
									.get("process");
						loadUserInfo();
					}
				});
		processhandler.excuteMethod(new ProcessDialogHandlerMethod("",
				"数据加载中...."));
	}

	private void loadUserInfo() {
		queryuser = userservice.selectbyUserid(userid);
		if (queryuser == null) {
			LoadUserInfoThread lth = new LoadUserInfoThread(userid);
			lth.setLiserner(new NetFinishListener() {

				@Override
				public void onfinish(NetResult data) {
 					if (data.result == NetResult.RESULT_OF_SUCCESS) {
						queryuser = (User) data.returnData;
						userservice.saveOrUpdate(queryuser);
					}
					loadFinish();
				}
			});
			lth.start();
		} else {
			loadFinish();
		}
	}

	private void loadFinish() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (progress != null)
					progress.dismiss();
				init();
			}
		});
	}

	/**
	 * 加载用户创建的记录
	 * 
	 * @param batch
	 * @param type
	 * @param showdialog
	 */
	public void loadUserRecords(boolean showdialog) {
		if (queryuser != null && this.type != TAB_INDEX_USERRECORD) {
			ispulldown = true;
			if (userupdaterecord == null)
				userupdaterecord = netupdateservice.getByTypeWithObjId(
						NetUpdateRecord.TYPE_USER_RECORD, queryuser
								.getUser_id());
			this.type = TAB_INDEX_USERRECORD;
			prepareloadData(showdialog);
		}
	}

	/**
	 *准备 加载数据
	 */
	private void prepareloadData(boolean showdialog) {
		if (queryuser == null)
			return;
		if (showdialog) {
			NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
			processhandler
					.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if (result != null)
								progress = (ProgressDialog) ((Map) result)
										.get("process");
							startLoadData();
						}
					});
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("",
					"数据加载中...."));
		} else {
			startLoadData();
		}

	}

	/**
	 * 开始 加载数据
	 */
	private void startLoadData() {
		String starttime = null, endtime = null;

		if (ispulldown) {
			starttime = userupdaterecord == null ? null : userupdaterecord
					.getMaxTime(type);
		} else {
			NetUpdateRecordTime time = userupdaterecord == null ? null
					: userupdaterecord.findNeedToLoadTime(type);
			if (time != null) {
				starttime = time.stime;
				endtime = time.etime;
			}
		}
		final String loadmaxtime = endtime;
		final String loadmintime = starttime;
		LoadUserRecordThread th = new LoadUserRecordThread(queryuser
				.getUser_id(), starttime, endtime);
		th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
 				if (data.result == NetResult.RESULT_OF_SUCCESS) {
 					Map rdata=(Map) data.returnData;
					List<Record> records = (List<Record>) rdata.get("datas");
					if (records != null && records.size() > 0) {
						for (Record rec : records) {// 保存记录
							recordservice.saveOrUpdate(rec);
						}

						String[] tims = TimeOrderTool.getMaxMinTime(records);
						if (userupdaterecord == null) {
							userupdaterecord = new NetUpdateRecord();
							userupdaterecord.setObjid(queryuser.getUser_id());
							userupdaterecord
									.setType(NetUpdateRecord.TYPE_USER_RECORD);
						}
						int totalcount = (Integer)  rdata.get("datacount");
						if (tims != null) {
							// 如果是下拉的刷新，则最大日期和最小日期从数据里获取，如果是上拉更新，则最大日期是查询endtime
							if (ispulldown) {
								// 如果返回的数据个数大于等于约定的数量，则表示服务器端可能还有未查询的数据
								// 否则代表这一时间段的数据已经全部获取到
								if (loadmintime == null) {
									userupdaterecord.saveOrUpdateStatus(type,
											tims[1], tims[0]);
								} else {
									if (totalcount <= AppConstant.NET_RETURN_MAX_COUNT) {
										userupdaterecord.saveOrUpdateStatus(
												type, loadmintime, tims[0]);
									} else {
										userupdaterecord.saveOrUpdateStatus(
												type, tims[1], tims[0]);
									}
								}
							} else {
								if (loadmaxtime != null) {
									if (loadmintime != null) {
										if (totalcount <= AppConstant.NET_RETURN_MAX_COUNT) {
											userupdaterecord
													.saveOrUpdateStatus(type,
															loadmintime,
															loadmaxtime);
										} else {
											userupdaterecord
													.saveOrUpdateStatus(type,
															tims[1],
															loadmaxtime);
										}
									} else {
										userupdaterecord.saveOrUpdateStatus(
												type, tims[1], loadmaxtime);
									}

								}
							}
							netupdateservice.saveOrUpdate(userupdaterecord);
						}
					}
				}
				datas = recordservice.getUserRecords(queryuser.getUser_id(),
						userupdaterecord == null ? null : userupdaterecord
								.getFirstStartTime(type));
				finishload();
			}
		});
		th.start();
	}

	/**
	 * 加载完成后处理
	 */
	private void finishload() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				lineview.setShowDatas(datas, TAB_INDEX_USERRECORD);
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

			}
		});
	}

	/**
	 * 后退
	 */
	private void reback() {
		this.finish();
	}

	@Override
	protected void onDestroy() {
		lineview.destory();
		if (bottomwindow != null)
			bottomwindow.dismiss();
		super.onDestroy();
	}

}