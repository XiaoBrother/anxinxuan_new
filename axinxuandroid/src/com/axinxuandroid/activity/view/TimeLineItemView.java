package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.CommentActivity;
import com.axinxuandroid.activity.EditRecordActivity;
import com.axinxuandroid.activity.PlayVideoHtmlActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.PlayVideoActivity;
import com.axinxuandroid.activity.RecordDetailedActivity;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.UserPageActivity;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AdvocateThread;
import com.axinxuandroid.activity.net.CheckVedioStatusThread;
import com.axinxuandroid.activity.net.DeleteRecordThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.data.Advocate;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.VideoInfo;
import com.axinxuandroid.data.UserVilleage.UserVilleageRole;
import com.axinxuandroid.service.AdvocateService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.GloableBitmap;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.axinxuandroid.sys.gloable.UploadRecordManager;
import com.axinxuandroid.sys.gloable.UploadRecordManager.OnRecordCompleteUpload;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.recorder.SpeexRecorder;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpDownloadUtil;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.MediaController.MediaPlayerControl;

public class TimeLineItemView extends ViewGroup  implements MediaPlayer.OnPreparedListener ,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener  {
	private TextView labelText;
	private TextView timeText;
	private TextView nameText;
	private TextView recordTimeText,timeitem_play_msg;
	private EditTabelView jsonTable;
	private TextView contextText;
	private TextView audioinfo;
	private ImageView image,userimg,fanjiaoimg;
	private LinearLayout auidolayout, videolayout;
	private ImageView loading,audioimg,replayimg;
	private Context context;
	private LinearLayout leftlayout,bottombannerlay;
	private LinearLayout timeitem_layout;
 	private RecordResourceService rresourceservice;
	private UserVilleageService uvilservice;
	private AdvocateService advservice;
	private LinearLayout editbtn, deletebtn, pinglunbtn,sharebtn,zanbtn;
    private View view;
	public Record record;
 	private List<Bitmap> bmps;
	private TimeLineView root;
    private int imgwidth;
    private AudioPlayListener auidolistener;
    private LoadImgListener loadimglistener;
    private AnimationDrawable frameanim;
    private User user;
    private ImageView zan_anim;
    private Advocate user_record_adv;
    private android.widget.VideoView videoview;
    private ProgressBar progressBar;
   	public TimeLineItemView(Context context, TimeLineView root) {
		super(context);
		this.root = root;
		initview(context);
 	}

	public void initview(final Context context) {
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.timeline_item, null);
		addView(view);
		 //setBackgroundResource(R.drawable.timeline_bg);
		setBackgroundColor(Color.rgb(255, 255, 255));
 		timeitem_layout=(LinearLayout)view.findViewById(R.id.timeitem_layout);
		labelText = (TextView) view.findViewById(R.id.timeitem_label);
		timeText = (TextView) view.findViewById(R.id.timeitem_date);
		nameText = (TextView) view.findViewById(R.id.timeitem_name);
		jsonTable = (EditTabelView) view.findViewById(R.id.timeitem_tablelayout);
		contextText = (TextView) view.findViewById(R.id.timeitem_context);
		recordTimeText=(TextView) view.findViewById(R.id.timeitem_recordtime);
		image = (ImageView) view.findViewById(R.id.timeitem_image);
		fanjiaoimg=(ImageView) view.findViewById(R.id.timeitem_fanjiao_image);
		userimg=(ImageView) view.findViewById(R.id.timeitem_userimg);
		audioimg=(ImageView) view.findViewById(R.id.timeitem_audioimg);
		videolayout = (LinearLayout) view
				.findViewById(R.id.timeitem_videolayout);
		videoview = (android.widget.VideoView) view.findViewById(R.id.timeitem_playvideo);
		videoview.setOnErrorListener(this);
		videoview.setOnPreparedListener(this);
		videoview.setOnCompletionListener(this);
		replayimg=(ImageView) view.findViewById(R.id.timeitem_play_replay);
 		auidolayout = (LinearLayout) view
				.findViewById(R.id.timeitem_audiolayout);
		audioinfo = (TextView) view.findViewById(R.id.timeitem_audioinfo);
		timeitem_play_msg= (TextView) view.findViewById(R.id.timeitem_play_msg);
		loading = (ImageView) view.findViewById(R.id.timeitem_loadview);
		leftlayout = (LinearLayout) view.findViewById(R.id.timeitem_leftlayout);
 		editbtn = (LinearLayout) this.findViewById(R.id.timeitem_edit);
		deletebtn = (LinearLayout) this.findViewById(R.id.timeitem_delete);
		pinglunbtn = (LinearLayout) this.findViewById(R.id.timeitem_pinglun);
		sharebtn = (LinearLayout) this.findViewById(R.id.timeitem_share);
		zanbtn=(LinearLayout)this.findViewById(R.id.timeitem_zan);
		progressBar=(ProgressBar) this.findViewById(R.id.timeitem_play_loading);
		bottombannerlay= (LinearLayout) this.findViewById(R.id.timeitem_bottom_banner_lay);
 		bmps = new ArrayList<Bitmap>();
 		//80dp为@+id/timeitem_leftlayout的宽度
 		imgwidth = Gloable.getInstance().getScreenWeight()
 		- DensityUtil.dip2px(48);
 		rresourceservice=new RecordResourceService();
 		uvilservice=new UserVilleageService();
 		advservice=new AdvocateService();
 		BitmapUtils.fixBackgroundRepeat(audioinfo);
  		zan_anim= (ImageView) view.findViewById(R.id.timeitem_zan_anim);
  		
 	}

	public void setData(final Record rec,final User user) {
		if (rec == null)
			return;
		resetStatus();
		this.record = rec;
		this.user=user;
		if(root.getShowstyle()==TimeLineView.TIME_LINE_ITEM_STYLE_COMMON){
			//时间轴页面显示样式
			if(rec.getSavestatus()==Record.BATCHRECORD_SAVESTATE_NET){
				setopbanner(rec,user);
				if(rec.getSave_date()!=null){
	 				recordTimeText.setText(DateUtil.DateToStr(DateUtil.StrToDate(rec
							.getSend_date()), "yyyy/MM/dd HH:mm"));
				 
				}
			}else{
				leftlayout.setBackgroundResource(R.drawable.huiyuan);
				recordTimeText.setText("正在发布...");
				UploadRecordManager.getInstance().putUploadRecord(rec,new OnRecordCompleteUpload() {
	 				@Override
					public void onComplete(int status,Record newrec) {
	 					if(status==UploadRecordManager.SAVE_SUCCESS){
	 						root.notifyDataChange(TimeLineView.DATA_CHANGE_ADD);
	  						setopbanner(newrec,user);
	 						if(newrec.getSave_date()!=null)
	 							recordTimeText.setText(DateUtil.DateToStr(DateUtil.StrToDate(rec.getSend_date()), "yyyy/MM/dd HH:mm"));
	 					    setVideo(newrec);//查询视频状态
	 					}else{
	 						recordTimeText.setText("保存记录失败，已保存到草稿箱！");
	 					}
					}

					@Override
					public boolean notifyWarn() {
 						return true;
					}
				});
			}
			nameText.setText(rec.getNick_name() );
			if(rec.getUser_img()!=null){
				Bitmap userbtm=BitmapUtils.getImageBitmap(rec.getUser_img());
				if(userbtm!=null){
					userimg.setImageBitmap(userbtm);
					this.bmps.add(userbtm);
				}
	 		}else{
	 			userimg.setBackgroundResource(R.drawable.defaultpersonimg);
	 		}
 		}else if(root.getShowstyle()==TimeLineView.TIME_LINE_ITEM_STYLE_USERRECORD){
			//用户记录界面样式
 			setopbanner(rec,user);
 			((FrameLayout)userimg.getParent()).setVisibility(View.GONE);
 			nameText.setText(rec.getVariety_name());
 			recordTimeText.setText("NO."+rec.getTrace_code());
		}
		timeText.setText(DateUtil.DateToStr(DateUtil.StrToDate(rec
				.getSend_date()), "MM/dd"));
  		labelText.setText(rec.getLabel_name());
  		
		setImage(rec);//设置图片
		if ((rec.getSave_type() & Record.BATCHRECORD_TYPE_OF_VIDEO) == Record.BATCHRECORD_TYPE_OF_VIDEO) {
			setVideo(rec);
		} else if ((rec.getSave_type() & Record.BATCHRECORD_TYPE_OF_AUDIO) == Record.BATCHRECORD_TYPE_OF_AUDIO) {
			setAudio(rec);
		}
		if ((rec.getSave_type() & Record.BATCHRECORD_TYPE_OF_TEMPLATE) == Record.BATCHRECORD_TYPE_OF_TEMPLATE)
			setTable(rec);
		else if ((rec.getSave_type() & Record.BATCHRECORD_TYPE_OF_TEXT) == Record.BATCHRECORD_TYPE_OF_TEXT) {
			setContext(rec);
		}
		leftlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 				 Intent inte=new Intent(context,RecordDetailedActivity.class);
 				 inte.putExtra("recordid", record.getRecord_id());
  				 context.startActivity(inte);
 			}
		});
		timeitem_layout.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
  				Intent inte=new Intent(context,RecordDetailedActivity.class);
 				inte.putExtra("recordid", record.getRecord_id());
  				context.startActivity(inte);
			}
        });
		
		if(user!=null)
		  user_record_adv=advservice.getByUserWithRecord(user.getUser_id(), rec.getRecord_id());
		if(user_record_adv!=null){
			((ImageButton)zanbtn.getChildAt(0)).setImageResource(R.drawable.timeitem_zan_hui);
		}
		userimg.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View view) {
 	 			toUserPage(record.getUser_id());
			}
		});
		nameText.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View view) {
 	 			toUserPage(record.getUser_id());
			}
		});
		replayimg.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				replayimg.setVisibility(View.GONE);
 				videoview.start();
			}
		});
 	}

	
	/**
	 * 跳转到用户详细页
	 * @param user
	 */
	private void toUserPage(String userid){
		if(userid!=null){
			Intent inte=new Intent(context,UserPageActivity.class);
 			inte.putExtra("userid", Integer.parseInt(userid));
 			context.startActivity(inte);
		}
	}
	
	
	
	
	private void resetStatus() {
 		videolayout.setVisibility(View.GONE);
		//videoview.stopPlay();
		auidolayout.setVisibility(View.GONE);
		timeitem_play_msg.setVisibility(View.GONE);
		timeitem_play_msg.setText("");
		jsonTable.setVisibility(View.GONE);
		loading.setVisibility(View.GONE);
		image.setVisibility(View.GONE);
		image.setImageDrawable(new BitmapDrawable());
		fanjiaoimg.setVisibility(View.GONE);
		contextText.setVisibility(View.GONE);
		editbtn.setVisibility(View.GONE);
		deletebtn.setVisibility(View.GONE);
		pinglunbtn.setVisibility(View.GONE);
		sharebtn.setVisibility(View.GONE);
		bottombannerlay.setVisibility(View.GONE);
		zanbtn.setVisibility(View.GONE);
		replayimg.setVisibility(View.GONE);
		audioimg.setImageResource(R.drawable.playaudioicon);
  		loading.setImageBitmap(GloableBitmap.getInstance().getImgBitmap(GloableBitmap.GB_IMG_LOAD_IMG));
  		((ImageButton)zanbtn.getChildAt(0)).setImageResource(R.drawable.timeitem_zan);
 	}
	//设置操作栏
	private void setopbanner(final Record rec, User user){
		int recorduserid=rec.getUser_id()==null?-1:Integer.parseInt(rec.getUser_id());
		bottombannerlay.setVisibility(View.VISIBLE);
 		if (!rec.isAllSaved()) {
			leftlayout.setBackgroundResource(R.drawable.huiyuan);
			if(user!=null&&recorduserid==user.getUser_id()){
				editbtn.setVisibility(View.VISIBLE);
				deletebtn.setVisibility(View.VISIBLE);
				editbtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent inte = new Intent(context, EditRecordActivity.class);
						inte.putExtra("id", rec.getId());
						context.startActivity(inte);
					}
				});
			}
 		} else {
			leftlayout.setBackgroundResource(R.drawable.lvyuan);
			if(user!=null&&(recorduserid==user.getUser_id()||(uvilservice.getUserRoleInVilleage(user.getUser_id(), rec.getVilleage_id())==UserVilleageRole.VILLEAGE_ROLE_MASTER))){
			  deletebtn.setVisibility(View.VISIBLE);
			}else zanbtn.setVisibility(View.VISIBLE);
			pinglunbtn.setVisibility(View.VISIBLE);
			sharebtn.setVisibility(View.VISIBLE);
			pinglunbtn.setOnClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
 					Intent inte=new Intent(context,CommentActivity.class);
		 			inte.putExtra("record_id", rec.getRecord_id());
	 				context.startActivity(inte);
				}
			});
			sharebtn.setOnClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
 					if (root != null)
 						root.showShareWindow(rec);
				}
			});
			zanbtn.setOnClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
  						 advocate(rec);
				}
			});
		}
 		deletebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (root != null)
					root.prepareDelete(rec);
			}
		});
	}
	
    private void setContext(Record rec){
    	if(rec.getContext()!=null&&!"".equals(rec.getContext())){
    		contextText.setText("");
    		contextText.setVisibility(View.VISIBLE);
    		ImageGetter imageGetter = new ImageGetter() {
    			@Override
    			public Drawable getDrawable(String source) {
    				int id = Integer.parseInt(source);
     				Drawable d = getResources().getDrawable(id);
       				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
    				return d;
    			}
    		};
    		// contextText.setLineSpacing(3,1);//行间距android:lineSpacingExtra="4dp" android:lineSpacingMultiplier="1"
     		if (auidolayout.getVisibility()==View.GONE&&videolayout.getVisibility()==View.GONE&&loading.getVisibility()==View.GONE&&image.getVisibility()==View.GONE) {
    		  contextText.append(Html
    					.fromHtml(
    							"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + rec.getContext(),
    							imageGetter, null));
    		} else {
     			contextText.append(Html.fromHtml(
    					rec.getContext(), imageGetter, null));
    		}
    	}
 	 
    }
    
    private boolean isadvocating=false;//是否正在联网点赞
    /**
     * 点赞功能
     * @param record
     */
	public void advocate(final Record  record){
		final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		if(isadvocating){
			handler.excuteMethod(new MessageDialogHandlerMethod("","正在点赞中,请稍等！"));
		}
		isadvocating=true;
 		if(record!=null){
 			 int type=0;
 			 if(user_record_adv!=null)
 				 type=1;
 			 if(type==0)
 			    	zan_anim.setImageResource(R.drawable.add_one);
 			 else zan_anim.setImageResource(R.drawable.reduce_one);
			 AdvocateThread at=new AdvocateThread(record.getRecord_id(), user,type);
			 at.setLiserner(new NetFinishListener() {
				@Override
				public void onfinish(final NetResult data) {
  					if(data.result==NetResult.RESULT_OF_SUCCESS){
 						handler.post(new Runnable() {
 								@Override
 								public void run() {
 									showOneAnim();
 									if(user_record_adv==null){
 										user_record_adv=(Advocate) data.returnData;
 										advservice.saveOrUpdateBatch(user_record_adv);
 										((ImageButton)zanbtn.getChildAt(0)).setImageResource(R.drawable.timeitem_zan_hui);
									}else {
										 user_record_adv.setIsdel(1);
										 advservice.update(user_record_adv);
										 user_record_adv=null;
										((ImageButton)zanbtn.getChildAt(0)).setImageResource(R.drawable.timeitem_zan);
									}
 									isadvocating=false;
  								}
 						});
  					 
  						//handler.excuteMethod(new MessageDialogHandlerMethod("","点赞成功！"));
 					}else{
 						handler.excuteMethod(new MessageDialogHandlerMethod("",data.message));
 						if(data.returnData!=null){
 							handler.post(new Runnable() {
 								@Override
 								public void run() {
  									if(user_record_adv==null){
 										user_record_adv=(Advocate) data.returnData;
 										advservice.saveOrUpdateBatch(user_record_adv);
 										((ImageButton)zanbtn.getChildAt(0)).setImageResource(R.drawable.timeitem_zan_hui);
									}else {
 										((ImageButton)zanbtn.getChildAt(0)).setImageResource(R.drawable.timeitem_zan);
									}
  									isadvocating=false;
  								}
 						});
 					  }else isadvocating=false;
 					}
  					
				}
			});
			at.start();
		 }else{
			 handler.excuteMethod(new MessageDialogHandlerMethod("","无法点赞！"));
		 }
		
	}
	/**
	 * 展示加一,减一效果
	 */
	private void showOneAnim(){
  		zan_anim.clearAnimation();
		zan_anim.setVisibility(View.VISIBLE);
		Animation inanim=AnimationUtils.loadAnimation(context, R.anim.add_one_inanim);
		inanim.setAnimationListener(new AnimationListener() {
				@Override
			public void onAnimationStart(Animation animation) {
					
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
					
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Animation outanim=AnimationUtils.loadAnimation(context, R.anim.add_one_outanim);
				outanim.setAnimationListener(new AnimationListener() {
						@Override
					public void onAnimationStart(Animation animation) {
							
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
							
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						zan_anim.setVisibility(View.GONE);
					}
				});
				zan_anim.startAnimation(outanim);
			}
		});
		zan_anim.startAnimation(inanim);
	}
    /**
     * 设置视频信息
     * @param rec
     */
	private void setVideo(Record rec) {
		List<RecordResource> videos = rec
				.getResourceByType(Record.BATCHRECORD_TYPE_OF_VIDEO);
		if(rec.getState()==Record.BATCHRECORD_SAVESTATE_PREPARESAVE){
			//videoinfo.setText("正在保存中...");
			timeitem_play_msg.setVisibility(View.VISIBLE);
			timeitem_play_msg.setText("正在保存中...");
		}else if (videos != null && videos.size() > 0) {
			timeitem_play_msg.setVisibility(View.GONE);
  			final RecordResource fvideo = videos.get(0);
			videolayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			//String infotext="";
			//videoinfo.setText(fvideo.getInfo() == null ? "" : fvideo.getInfo()
			//		+ "''");
//			videolayout.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					String path = null;
//					if (fvideo.getState() == RecordResource.RESOURCE_STATE_UNSYNCHRONISE) {
//						path = fvideo.getLocalurl();
//					} else
//						path = fvideo.getNeturl();
//					Intent inte = new Intent(context, PlayVideoHtmlActivity.class);
//					inte.putExtra("path", path);
//					inte.putExtra("type", ResourcesView.RESROUCETYPE_OF_VIDEO);
//					context.startActivity(inte);
//				}
//			});
//			if(fvideo.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_UNCHECKED){
//				infotext=context.getString(R.string.check_video_status);
//				checkVideoStatus(fvideo);
//			}else if(fvideo.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_FAIL){
//				 if(Integer.parseInt(rec.getUser_id())==user.getUser_id()){
//					 infotext=RecordResource.getVideoStatus(RecordResource.VideoState.RESOURCE_VIDEO_STATE_FAIL);
//				 }
// 			}else if(fvideo.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_ENCODING){
// 				 infotext=RecordResource.getVideoStatus(RecordResource.VideoState.RESOURCE_VIDEO_STATE_ENCODING);
// 				 checkVideoStatus(fvideo);
//			}else if(fvideo.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_INREVIEW){
// 				 infotext=RecordResource.getVideoStatus(RecordResource.VideoState.RESOURCE_VIDEO_STATE_INREVIEW);
// 				 checkVideoStatus(fvideo);
//			}else if(fvideo.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_NORMAL){
//				infotext=fvideo.getInfo() == null ? "" : fvideo.getInfo()+ "''";
// 			}
//			videoinfo.setText(infotext);
			//videoview.startNetPlay(HttpUrlConstant.URL_HEAD+fvideo.getNeturl());
			LogUtil.logInfo(getClass(), "url:"+HttpUrlConstant.URL_HEAD+fvideo.getNeturl());
		    Uri uri = Uri.parse(HttpUrlConstant.URL_HEAD+fvideo.getNeturl());  
 		    //videoview.setMediaController(new MediaController(context));  添加媒体控制器，快进,快退
		    videoview.setVisibility(View.VISIBLE);
 		    videoview.setVideoURI(uri);  
		        //videoView.start();  
 		     videoview.start();
 		     
 		}

	}
 	
 	/**
	 * 查询视频状态
	 * @param rec
	 */
	
//	private void checkVideoStatus(final RecordResource rec){
//		CheckVedioStatusThread  cvt=new CheckVedioStatusThread(rec.getNeturl());
//		cvt.setLiserner(new NetFinishListener() {
// 			@Override
//			public void onfinish(NetResult data) {
// 				if(data.result==NetResult.RESULT_OF_SUCCESS){
// 					VideoInfo vinfo=(VideoInfo) data.returnData;
// 					rec.setInfo(vinfo.duration+"");
// 					rec.setPublishstate(vinfo.state);
// 					rresourceservice.updateVideoStatusWithTime((int)rec.getId(),vinfo.state,vinfo.duration);
// 					NcpzsHandler hand=Gloable.getInstance().getCurHandler();
// 					hand.post(new Runnable() {
// 						@Override
//						public void run() {
// 							String infotext="";
// 						    if(rec.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_ENCODING){
// 				 				 infotext+=RecordResource.getVideoStatus(RecordResource.VideoState.RESOURCE_VIDEO_STATE_ENCODING);
//  							}else if(rec.getPublishstate()==RecordResource.VideoState.RESOURCE_VIDEO_STATE_INREVIEW){
// 				 				 infotext+=RecordResource.getVideoStatus(RecordResource.VideoState.RESOURCE_VIDEO_STATE_INREVIEW);
//  							}else {
//				 				 infotext+="正在处理中...";
// 							}
// 						   videoinfo.setText(infotext);
//						}
//					});
// 				}
//			}
//		});
//		cvt.start();
//	}
	
	private void setAudio(Record rec) {
		List<RecordResource> audio = rec
				.getResourceByType(Record.BATCHRECORD_TYPE_OF_AUDIO);
		if (audio != null && audio.size() > 0) {
			final RecordResource faudio = audio.get(0);
			auidolayout.setVisibility(View.VISIBLE);
			audioinfo.setText(faudio.getInfo() == null ? "" : faudio.getInfo()
					+ "''");
			auidolayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(auidolistener.viewIsPlay(TimeLineItemView.this)){
						auidolistener.stopPlay(TimeLineItemView.this);
					}else{
						String path_localurl = null;
						if (faudio.getLocalurl()==null) {
							String path = faudio.getNeturl();
							if (path != null) {
								String filename = path.substring(path
										.lastIndexOf("/") + 1, path.length());
								HttpDownloadUtil.downFile(HttpUrlConstant.URL_HEAD
										+ path, AppConstant.MEDIA_DIR, filename);
								path_localurl = AppConstant.MEDIA_DIR + filename;
								rresourceservice.updateResourceLocalUrl((int)faudio.getId(), path_localurl);
								faudio.setLocalurl(path_localurl);
							}
						} else
							path_localurl = faudio.getLocalurl();
	 					if (path_localurl != null) {
							if(auidolistener!=null){
	 							auidolistener.playAudio(TimeLineItemView.this,path_localurl);
							}
 						}
					}
					
				}
			});
		}

	}

	private void setImage(Record rec) {
		List<RecordResource> imgs = rec
				.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
 		if (imgs != null && imgs.size() > 0) {
			RecordResource fimg = imgs.get(0);
			if (fimg.getLocalurl() != null
					&& !"".equals(fimg.getLocalurl().trim())&&FileUtil.hasFile(fimg.getLocalurl().trim())) {
 					setImageWithPath(fimg);
  			} else {
  				if(this.loadimglistener!=null){
  					loading.setVisibility(View.VISIBLE);
  					this.loadimglistener.load(fimg, TimeLineItemView.this);
  				}
 				//LogUtil.logInfo(TimelineActivity.class, "need load:"
				//		+ needLoadResource.getNeturl());
			}
 		 
		}
   	}
	private void setReloadImg(){
		if(loading.getVisibility()==View.GONE)
		   loading.setVisibility(View.VISIBLE);
		loading.setImageBitmap(GloableBitmap.getInstance().getImgBitmap(GloableBitmap.GB_IMG_LOAD_FAILD_IMG));
		loading.setOnClickListener(new OnClickListener() {
				@Override
			public void onClick(View v) {//重新加载图片
					loading.setImageBitmap(GloableBitmap.getInstance().getImgBitmap(GloableBitmap.GB_IMG_LOAD_IMG));
					if(loadimglistener!=null){
						List<RecordResource> imgs = record.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
		 		        if (imgs != null && imgs.size() > 0) {
		 		        	loadimglistener.reload(imgs.get(0),TimeLineItemView.this);
		 		        }
					}
			}
		});
	}
	public void setImageWithPath(RecordResource rr) {
   		if (rr.getLocalurl() == null||!FileUtil.hasFile(rr.getLocalurl())) {
   			setReloadImg();
		} else {
  			Bitmap img =null;
			img = BitmapUtils.getCompressImage(rr.getLocalurl());
  			if (img == null){
  				if(rr.getLocalurl()!=null)
  					 FileUtil.deleteFile(rr.getLocalurl());
  				setReloadImg();
  				return;
 			}
  			//LogUtil.logInfo(getClass(), "set img:"+rr.getLocalurl());
 			loading.setVisibility(View.GONE);// 隐藏加载中
 			image.setVisibility(View.VISIBLE);//显示真正的图片
 			bmps.add(img);
			LayoutParams lp = image.getLayoutParams();
			//lp.height = img.getHeight();
			double sc=img.getWidth()*1.0/imgwidth;
			if(sc<1)sc=1;
			lp.width = imgwidth;
			lp.height = (int)(img.getHeight()/sc);
 			image.setLayoutParams(lp);
			//image.setBackgroundDrawable(new BitmapDrawable());
			image.setImageBitmap(img);
			//LogUtil.logInfo(getClass(), "layout:"+lp.width+":"+lp.height);
  			List<RecordResource> imgs = record.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
			if(imgs!=null&&imgs.size()>1){
				LayoutParams fp = fanjiaoimg.getLayoutParams();
				if(img.getWidth()<imgwidth)
				  fp.width=img.getWidth()+DensityUtil.dip2px(3);
				else fp.width=imgwidth+DensityUtil.dip2px(3);
				fanjiaoimg.setLayoutParams(fp);
				fanjiaoimg.setVisibility(View.VISIBLE);
			}
		}
	}
	private void setTable(Record rec) {
		String template = rec.getContext();
		jsonTable.setVisibility(View.VISIBLE);
		jsonTable.setTextColor(Color.rgb(111, 110, 113));
		jsonTable.setReadOnly();
		jsonTable.setShowEmpty(false);
		jsonTable.clear();
   		try {
			// LogUtil.logInfo(getClass(), "tablejson:"+template);
			JSONObject jsonobj = new JSONObject(template);
			JSONArray answers_array = jsonobj.getJSONArray("answers");
			for (int j = 0; j < answers_array.length(); j++) {
				JSONObject joa = answers_array.getJSONObject(j);
				String joa_name = joa.getString("name");
				String joa_value = joa.getString("value");
				jsonTable.addItem(j, joa_name, joa_value);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

 

	
	// 遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View view = getChildAt(0);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		// LogUtil.logInfo(getClass(),
		// " onLayout height:"+view.getMeasuredHeight());
	}

	
	public void setPlayAudioStatus(boolean play){
 		if(play){
 			audioimg.setImageResource(R.drawable.play_audio_anim);
 			frameanim=(AnimationDrawable) audioimg.getDrawable();
 			if(frameanim!=null)
 			  frameanim.start();
 		}
 		else {
 			if(frameanim!=null&&frameanim.isRunning()){
 				frameanim.stop();
  			}
 			frameanim=null;	
 			audioimg.setImageResource(R.drawable.playaudioicon);
 		}
			
	}
 
	
	/**
	 * 播放音频监听
	 * 
	 * @author Administrator
	 * 
	 */
	public interface AudioPlayListener {
		public void playAudio(TimeLineItemView view,String url);
		public void stopPlay(TimeLineItemView view);
		public boolean viewIsPlay(TimeLineItemView view);
	}

	 
	public void setAudioPlayListener(AudioPlayListener listener) {
		this.auidolistener = listener;
	}
    /**
     * 当图片加载失败，点击失败图片事件处理
     * @author Administrator
     *
     */
	public interface  LoadImgListener{
		public void load(RecordResource resource,View view);
		public void reload(RecordResource resource,View view);
	}
	public void setLoadImgListener(LoadImgListener listener) {
		this.loadimglistener = listener;
	}
	/**
	 * 销毁图片，释放内存
	 */
	public void destory() {
		auidolistener=null;
		loadimglistener=null;
		if(videoview.isPlaying())
		   videoview.pause();
		if (this.bmps != null && this.bmps.size() > 0) {
			for (Bitmap bm : bmps) {
				if (bm != null && !bm.isRecycled()) {
					//LogUtil.logInfo(getClass(), "recycle...");
					bm.recycle();
				}
			}
			System.gc();
 		}
  	}

	@Override
	public boolean onError(MediaPlayer arg0, int what, int extra) {
		//NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		progressBar.setVisibility(View.GONE);
		String info="";
		switch(extra){
 		  case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
			  info="不支持的视频格式";
			  break;
		  case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
			  info="连接超时";
			  break;
		   default :
			   info="无法播放视频";
		}
		//handler.excuteMethod(new MessageDialogHandlerMethod("", info));
		 videoview.setVisibility(View.GONE);
		timeitem_play_msg.setVisibility(View.VISIBLE);
		timeitem_play_msg.setText(info);
 		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		progressBar.setVisibility(View.GONE);
//		mp.start();// 播放
//		mp.setLooping(true);
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		replayimg.setVisibility(View.VISIBLE);
	}
}
