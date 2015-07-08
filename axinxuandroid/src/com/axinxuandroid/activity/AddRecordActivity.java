package com.axinxuandroid.activity;

import java.io.File;
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
 
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.EditTabelView;
import com.axinxuandroid.activity.view.RecordAudioViewV2;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.RecordAudioViewV2.DeleteAudioListener;
import com.axinxuandroid.activity.view.ResourcesView.Resource;
import com.axinxuandroid.activity.view.ResourcesView.ResourceClickListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.SystemSet;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.SystemSet.SystemSetType;
import com.axinxuandroid.data.SystemSet.SystemSetType.CommonType;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemSetService;
import com.axinxuandroid.service.TemplateService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
 

import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.NetUtil;
import com.ncpzs.util.SendShareInfo;

 
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
 
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
 
public class AddRecordActivity extends NcpZsActivity {
	protected Record record=null;
	protected ImageView cameraFolder;// 文件夹
	protected ProgressDialog processDialog;
	protected ImageView cameraPhoto; // 照相
	protected ImageView tablebtn; // 表格
	protected ImageView textbtn; // 文字
	protected ImageView locationbtn; // 地理位置
 	protected Button addrecord_audiobtn;//按住录音按钮
 	protected ImageView videobtn; // 视频
 	protected RecordAudioViewV2 audioview;
 	protected EditText records_text;
	protected String labelname;
	protected String trace_code;
	protected String batch_id;
	protected int variety_id;
	protected int villeage_id;
	protected int usertype;
	protected String variety_name;
	protected Handler handler = new Handler();
	protected ResourcesView rview;
	protected CommonTopView topview;
	protected EditTabelView edittable;
	protected RecordService recordservice;
	protected BatchService batchservice;
	protected  TemplateService templateservice;
	protected RecordResourceService sourceservice;
	protected  SystemSetService setservice;
	protected  SystemSet systemset;
	private UserService uservice;
 	private User user;
	protected	SendShareInfo send;
	public static final int RETURN_FROM_TAKE_CAMERA = 1;
	public static final int RETURN_FROM_EXTERNAL_LIBRIARY = 2;
	public static final int RETURN_FROM_IMAGE_DELETE = 3;
	public static final int RETURN_FROM_IMAGE_VEDIO = 4;
	public static final int RETURN_FROM_SELECT_BATCH = 5;
 	public static final int SAVE_SUCCESS = 1;//全部保存完成
 	public static final int SAVE_LOCATE_SUCCESS = 2;//只保存在本地
	public static final int SAVE_FAILED_BY_RECORDINFO = -1;//记录基本信息保存失败
	public static final int SAVE_FAILED_BY_IMAGES = -2;//图片保存失败
	public static final int SAVE_FAILED_BY_VIDEO = -3;//视频保存失败
	public static final int SAVE_FAILED_BY_AUDIO = -4;//音频保存失败
	public static final int SAVE_FAILED_BY_UNSETCONTEXT = -5;//未输入内容
	public static final int SAVE_FAILED_BY_UNWIFI = -6;//非wifi下不能发送
 	protected String photo_img_save_path;//拍摄图片保存地址
 	protected String video_save_path;//拍摄视频保存地址
 	//private RecordSaveFinishListener savelistener;
  	 
 	protected String saveBeforeState;//保存前状态
 	private List<String>  tableques;
 	private boolean issaving=false;//是否正在保存
 	private CountDownTimer timer;//倒计时
 	private boolean set_location=true;
   	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrecords);
		recordservice=new RecordService();
		sourceservice=new RecordResourceService();
		templateservice=new TemplateService();
		uservice=new UserService();
		user=uservice.getLastLoginUser();
		setservice=new SystemSetService();
		batchservice=new BatchService();
		systemset=setservice.getByType(SystemSetType.SYSTEMSETTYPE_COMMONTYPE);
		if(systemset==null){
 			systemset=new SystemSet();
 			systemset.setSet(CommonType.DEFAULT_SET);//默认设置
		}
		Intent intent = getIntent();
 		trace_code = intent.getStringExtra("trace_code");
		batch_id = intent.getStringExtra("batch_id");
		variety_id = intent.getIntExtra("variety_id", -1);
		labelname = intent.getStringExtra("label_name");
		variety_name=intent.getStringExtra("variety_name");
		villeage_id=intent.getIntExtra("villeage_id", -1);
		cameraFolder = (ImageView) findViewById(R.id.addrecord_imglibrary);
 		cameraPhoto = (ImageView) findViewById(R.id.addrecord_camera);
		videobtn = (ImageView) findViewById(R.id.addrecord_video);
		records_text = (EditText) findViewById(R.id.addrecord_text);
 		rview = (ResourcesView) this.findViewById(R.id.addrecord_resourceview);
		topview = (CommonTopView) this.findViewById(R.id.addrecord_topview);
		edittable = (EditTabelView) this.findViewById(R.id.addrecord_edittable);
		tablebtn = (ImageView) this.findViewById(R.id.addrecord_tablebtn);
		textbtn=(ImageView) this.findViewById(R.id.addrecord_textbtn);
		addrecord_audiobtn= (Button) this.findViewById(R.id.addrecord_audio);
		audioview=(RecordAudioViewV2) this.findViewById(R.id.addrecord_audioview);
		locationbtn=(ImageView) findViewById(R.id.addrecord_location);
 		send=new SendShareInfo(this);
 		locationbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(set_location){
 					locationbtn.setImageResource(R.drawable.location);
 				}else{
 					locationbtn.setImageResource(R.drawable.location_click);
 				}
 				set_location=!set_location;
			}
		});
 		addrecord_audiobtn.setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if (event.getAction() == MotionEvent.ACTION_UP) {
  	 				//
 					addrecord_audiobtn.setText("      按住说话");
   					audioview.stopRecord();
   					dealMedia(audioview.getSavePath(), audioview.getRecordTime()+"");
  				}else if (event.getAction() == MotionEvent.ACTION_DOWN) {
  					addrecord_audiobtn.setText("      松开停止");
 	 				records_text.setVisibility(View.GONE);
 					edittable.setVisibility(View.GONE);
 					audioview.setVisibility(View.VISIBLE);
 					audioview.startRecord();
  				}
 				return false;
			}
		}); 
 		audioview.setDeleteAudioListener(new DeleteAudioListener() {
 			@Override
			public void onDelete() {
 				records_text.setVisibility(View.VISIBLE);
 			    audioview.setVisibility(View.GONE);
			}
		});
		 
 		((LinearLayout)cameraPhoto.getParent()).setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction()==MotionEvent.ACTION_UP){
  					takecamera();
 				}
 				return false;
			}
		});
 		((LinearLayout)videobtn.getParent()).setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction()==MotionEvent.ACTION_UP){
  					takevideo();
  				}
 				return false;
			}
		});
 		((LinearLayout)cameraFolder.getParent()).setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction()==MotionEvent.ACTION_UP){
  				  //打开图片浏览器
					Intent inte = new Intent();
					inte.setClass(AddRecordActivity.this,
							PhotoAlbumActivity.class);
					startActivityForResult(inte, RETURN_FROM_EXTERNAL_LIBRIARY);
 				}
 				return false;
			}
		});
 		((LinearLayout)tablebtn.getParent()).setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction()==MotionEvent.ACTION_DOWN){
 					if(tableques!=null)
 						tablebtn.setImageResource(R.drawable.tableicon_click);
 					return true;
 				}else if(event.getAction()==MotionEvent.ACTION_UP){
  					changeTabelText();
 				}
 				return false;
			}
		});
		
 		((LinearLayout)textbtn.getParent()).setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if(event.getAction()==MotionEvent.ACTION_UP){
 					tablebtn.setImageResource(R.drawable.tableicon);
  					changeTabelText();
 				}
 				return false;
			}
		});
		
		rview.setOnResourceClickListener(new ResourceClickListener() {
			@Override
			public void click(Resource res) {
				toDeleteResource(res);
			}
		});
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				startsaveRecord(false);
			}
		});
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				reback();
 			}
		});
 		if(isSelectBatchAndLabel()){
			topview.setSubTitle("选择批次");
			topview.setSubTitleClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
 					Intent  inte=new Intent(AddRecordActivity.this,SelectBatchActivity.class);
 					startActivityForResult(inte, RETURN_FROM_SELECT_BATCH);
				}
			});
		}else {
			topview.setTitle("创建"+labelname+"记录");
			topview.setSubTitle("NO."+trace_code);
		}
  		saveBeforeState=getState();
 		initTableState();
 		  
	}
   /**
    * 初始化表格按钮状态
    */
  	protected void initTableState(){
  		if(labelname!=null&&!"".equals(labelname)){
 			tableques=templateservice.selectByCategoryWithLabelNameToList(variety_id,labelname);
 			if (tableques != null && tableques.size() > 0){
 				for (int i = 0; i < tableques.size(); i++) {
					edittable.addItem(i, tableques.get(i));
			    }
 				tablebtn.setImageResource(R.drawable.tableicon);
  			}
 			 
  		}
  	}
	/**
	 * 跳转到删除页面
	 * 
	 * @param res
	 */
	public void toDeleteResource(Resource res) {
//		Intent intent = new Intent(AddRecordActivity.this,
//				DeleteResourceActivity.class);
//		intent.putExtra("path", res.path);
//		intent.putExtra("thumbimg", res.thumbimg);
//		intent.putExtra("type", res.type);
//		startActivityForResult(intent, RETURN_FROM_IMAGE_DELETE);
	}

	/**
	 * 切换表格和输入框
	 */
	public void changeTabelText() {
		int visi = records_text.getVisibility();
		if (visi == View.GONE) {
			records_text.setVisibility(View.VISIBLE);
			edittable.setVisibility(View.GONE);
			audioview.setVisibility(View.GONE);
 			((LinearLayout)tablebtn.getParent().getParent()).setVisibility(View.VISIBLE);
 			((LinearLayout)textbtn.getParent().getParent()).setVisibility(View.GONE);
 		} else {
			 if(edittable.hasData()){
				records_text.setVisibility(View.GONE);
				edittable.setVisibility(View.VISIBLE);
				audioview.setVisibility(View.GONE);
				((LinearLayout)tablebtn.getParent().getParent()).setVisibility(View.GONE);
	 			((LinearLayout)textbtn.getParent().getParent()).setVisibility(View.VISIBLE);
 			 }else{
				 NcpzsHandler handler=Gloable.getInstance().getCurHandler();
				 if(labelname!=null)
				   handler.excuteMethod(new MessageDialogHandlerMethod("","没有该标签对应的模板！"));
				 else handler.excuteMethod(new MessageDialogHandlerMethod("","请先选择批次和环节！"));
			 }
   		}
	}
 
	/**
	 * 录像
	 */
	public void takevideo() {
		Intent inte = new Intent();
		inte.setClass(this, MediaRecorderActivity.class);
		startActivityForResult(inte, RETURN_FROM_IMAGE_VEDIO);
// 		video_save_path=AppConstant.VIDEO_DIR+System.currentTimeMillis()+".mp4";
// 		Intent inte = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//		inte.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // 设置画面质量为高
//		//限制时长
//		inte.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//		//限制大小
//		inte.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 320*240);
// 		//inte.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(video_save_path)));//设置图片保存路径 
//        startActivityForResult(inte, RETURN_FROM_IMAGE_VEDIO); 
	}
	 
	/**
	 * 拍照
	 */
	public void takecamera() {
//		Intent inte = new Intent();
//		inte.setClass(this, TakeCameraActivity.class);
//		startActivityForResult(inte, RETURN_FROM_TAKE_CAMERA);
		photo_img_save_path=AppConstant.CAMERA_DIR+System.currentTimeMillis()+".jpg";
 		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photo_img_save_path)));//设置图片保存路径 
        startActivityForResult(i, RETURN_FROM_TAKE_CAMERA); 
	}

	/**
	 * 处理返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case RETURN_FROM_TAKE_CAMERA:// 处理添加标签返回的数据
			dealTakeCamera(resultCode, data);
			break;
		case RETURN_FROM_EXTERNAL_LIBRIARY:
			dealImageLibrary(resultCode, data);
			break;
		case RETURN_FROM_IMAGE_DELETE:
			dealImageDelete(resultCode, data);
			break;
		case RETURN_FROM_IMAGE_VEDIO:
			dealVideo(resultCode, data);
			break;
		case RETURN_FROM_SELECT_BATCH:
			dealBackBatchinfo(resultCode, data);
			break;
 		}
	}
	/**
	 * 返回选择的批次信息处理
	 * 
	 * @param resultCode
	 * @param data
	 */
	public void dealBackBatchinfo(int resultCode, Intent data) {
		 if(resultCode==RESULT_OK){
	        trace_code = data.getStringExtra("trace_code");
		    batch_id = data.getStringExtra("batch_id");
		    variety_id = data.getIntExtra("variety_id", -1);
		    labelname = data.getStringExtra("label_name");
		    variety_name=data.getStringExtra("variety_name");
		    topview.setTitle("创建"+labelname+"记录");
		    topview.setSubTitle("NO."+trace_code);
		    if(batch_id!=null&&!"".equals(batch_id)){
		    	Batch btac=this.batchservice.getByBatchId(Integer.parseInt(batch_id));
		    	villeage_id=btac.getVilleage_id();
		    	
		    }
		    //LogUtil.logInfo(getClass(), trace_code+","+batch_id+","+variety_id+","+labelname+","+variety_name);
		    initTableState();
		}
	}
	/**
	 * 返回的拍摄图片处理
	 * 
	 * @param resultCode
	 * @param data
	 */
	public void dealTakeCamera(int resultCode, Intent data) {
		 if(resultCode==RESULT_OK){
			 rview.addResource(photo_img_save_path,ResourcesView.RESROUCETYPE_OF_IMAGE,null,"");
		}
	}

	/**
	 * 图片库选择的图片处理
	 * 
	 * @param resultCode
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void dealImageLibrary(int resultCode, Intent data) {
		if (data != null) {
			if(resultCode==RESULT_OK){
				ArrayList<String> paths=new ArrayList<String>();
				 paths= (ArrayList<String>) data.getExtras().get("path");
				 if(paths!=null&&paths.size()>0)
				 for(String url:paths){
					// LogUtil.logError(AddRecordActivity.class, url);
						if (url.indexOf("content://") != -1) {
							url = getMediaUrl(url);
						}
						rview.addResource(url, ResourcesView.RESROUCETYPE_OF_IMAGE,null,"");
				}
			}
 		}
	}
	 
	/**
	 * 录音处理
	 * 
	 * @param resultCode 
	 * @param data
	 */
	public void dealMedia(String takemedia,String info) {
 				if (takemedia != null && !takemedia.equals("") ) {
 					rview.addResource(takemedia,ResourcesView.RESROUCETYPE_OF_AUDIO,null,info);
						
				}
 	}
	/**
	 * 删除录音
	 * 
	 * @param resultCode rview.deleteResourceByType(ResourcesView.RESROUCETYPE_OF_AUDIO);
	 * @param data
	 */
	public void deletMedia() {
 		rview.deleteResourceByType(ResourcesView.RESROUCETYPE_OF_AUDIO);
	}
	/**
	 * 视频处理
	 * 
	 * @param resultCode
	 * @param data
	 */
	public void dealVideo(int resultCode, Intent data) {
		if (resultCode==RESULT_OK) {
			//调用系统相机的处理方式
// 			String url=data.getData().toString();
// 			if (data.getData().toString().indexOf("content://") != -1) {
//				url = getMediaUrl(url);
//			}
//   			rview.addResource(url, ResourcesView.RESROUCETYPE_OF_VIDEO,null,getVideoLength(url)+"");
			//自定义相机处理方式
			String videourl=data.getStringExtra("videourl");
			String imgurl=data.getStringExtra("thumburl");
			String length=data.getStringExtra("time");
			rview.addResource(videourl, ResourcesView.RESROUCETYPE_OF_VIDEO,imgurl,length);
		}
	}
	/**
	 * 删除图片处理
	 * 
	 * @param resultCode
	 * @param data
	 */
	public void dealImageDelete(int resultCode, Intent data) {
		if (resultCode==RESULT_OK) {
 			String path = data.getStringExtra("path");
			rview.deleteReource(path);
 		}
	}

	/**
	 * 从系统数据库查询文件地址
	 * @param data
	 * @return
	 */
	public String getMediaUrl(String data) {
		Uri uri = Uri.parse(data);
		//LogUtil.logError(AddRecordActivity.class, data);
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = this.managedQuery(uri, proj, null, null,
				null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor
				.getString(actual_image_column_index);
		return img_path;
	}
	/**
	 * 从系统数据库获取视频长度
	 * @param data
	 * @return
	 */
	public long getVideoLength(String url) {
 		String selection = MediaStore.Video.Media.DATA + "="
		+ "'"+url+"'";
 		String[] proj = { MediaStore.Video.Media.DURATION };
		Cursor actualimagecursor = this.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj, selection, null,
				null);
 		actualimagecursor.moveToFirst();
		long length = actualimagecursor.getLong(0);
		return length/1000;
	}
	/**
 	 * 开始保存，显示进度
 	 * @param onlysavetodraft 是否只需要保存到草稿箱
	 */
	public void startsaveRecord(final boolean onlysavetodraft) {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		if(this.user==null){
			handler.excuteMethod(new MessageDialogHandlerMethod("", "请先登录！"));
		}else{
			if(isEmpty()){
				handler.excuteMethod(new MessageDialogHandlerMethod("", "记录内容不能为空！"));
			}else{
				if(issaving) return ;
				issaving=true;
 				handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
					@Override
					public void onHandlerFinish(Object result) {
						if (result != null) {
							processDialog = (ProgressDialog) ((Map) result)
									.get("process");
							prepareToSave(onlysavetodraft);
						}
					}
				});
				handler.excuteMethod(new ProcessDialogHandlerMethod("保存", "记录保存中...."));
			}
			
		}
 	}
    /**
     * 构造上传信息,保存本地备份
     */
	public void prepareToSave(boolean onlysavetodraft){
		if(record==null)
  		   record = new Record();
 		String date = DateUtil.dateToStrWithPattern(new Date(),
		"yyyy-MM-dd HH:mm:ss");
		int save_type = 0;
		String context=null;
		int visi_edittable = edittable.getVisibility();
		int visi = records_text.getVisibility();
		if (visi != View.GONE) {
			save_type = Record.BATCHRECORD_TYPE_OF_TEXT;
			context = records_text.getText().toString();
 		} else if(visi_edittable != View.GONE){
			save_type = Record.BATCHRECORD_TYPE_OF_TEMPLATE;
			context = edittable.getItemJson();
		}else{
			save_type = Record.BATCHRECORD_TYPE_OF_AUDIO;
 		}
 		if(!rview.hasResources()&&(context==null||"".equals(context.trim()))){
 			 finishSave(SAVE_FAILED_BY_UNSETCONTEXT);
			 return ;
		}
		List<Resource> imgs = rview.getByType(ResourcesView.RESROUCETYPE_OF_IMAGE);
		List<Resource> audios = rview.getByType(ResourcesView.RESROUCETYPE_OF_AUDIO);
		List<Resource> videos = rview.getByType(ResourcesView.RESROUCETYPE_OF_VIDEO);
		if (imgs!=null&&imgs.size() > 0) {
			save_type += Record.BATCHRECORD_TYPE_OF_IMAGE;
 		}
 		if (videos!=null&&videos.size() > 0) {
			save_type += Record.BATCHRECORD_TYPE_OF_VIDEO;
 		}
 		record.setPhone(user.getPhone());
 		record.setLabel_name(labelname);
 		record.setContext(context);
 		record.setSave_date(date);
 		record.setSend_date(date);
 		record.setUser_id(user.getUser_id()+"");
 		record.setUser_img(user.getLocal_imgurl());
 		record.setVilleage_id(villeage_id);
		if(batch_id!=null)
			record.setBatch_id(Integer.parseInt(batch_id));
		record.setSave_type(save_type);
		record.setNick_name(user.getUser_name());
		record.setTrace_code(trace_code);
 		record.setVariety_id(variety_id);
		record.setVariety_name(variety_name);
		double[] lction=new double[]{0,0};
		if(set_location){
			 lction=SharedPreferenceService.getUserLocation();
 		}
		if(lction!=null){
			 record.setLat(lction[0]);
			 record.setLng(lction[1]);
		}
		usertype=uservice.getUserInVilleageType(user.getUser_id(), villeage_id);
		record.setUser_type(usertype);
		if(onlysavetodraft)
		  record.setSavestatus(Record.BATCHRECORD_SAVESTATE_DRAFT); 
		else   record.setSavestatus(Record.BATCHRECORD_SAVESTATE_PREPARESAVE); 
		if(record.getId()<=0){
			record.setState(Record.BATCHRECORD_STATE_UNSAVED);
			recordservice.saveRecord(record);//保存记录到本地，
		}
 		else {
 			//更新
 			recordservice.update(record);
			sourceservice.deleteByRecord(record.getId());
		}
 		//保存资源信息
		if (imgs!=null&&imgs.size() > 0) {
			for(Resource img:imgs){
				RecordResource rs=new RecordResource();
				rs.setLocalurl(img.path);
				rs.setRecordid(record.getId());
 				rs.setState(RecordResource.RESOURCE_STATE_UNSYNCHRONISE);
				rs.setType(Record.BATCHRECORD_TYPE_OF_IMAGE);
				record.addResource(rs);
				sourceservice.insertResource(rs);
			}
 		}
 		if (audios!=null&&audios.size() > 0) {
			for(Resource audio:audios){
				RecordResource rs=new RecordResource();
				rs.setLocalurl(audio.path);
 				rs.setState(RecordResource.RESOURCE_STATE_UNSYNCHRONISE);
				rs.setType(Record.BATCHRECORD_TYPE_OF_AUDIO);
				rs.setRecordid(record.getId());
				rs.setInfo(audio.info);
				record.addResource(rs);
				sourceservice.insertResource(rs);
			}
  		}
		if (videos!=null&&videos.size() > 0) {
			for(Resource video:videos){
				RecordResource rs=new RecordResource();
				rs.setLocalurl(video.path);
 				rs.setState(RecordResource.RESOURCE_STATE_UNSYNCHRONISE);
				rs.setType(Record.BATCHRECORD_TYPE_OF_VIDEO);
				rs.setRecordid(record.getId());
				rs.setInfo(video.info);
				record.addResource(rs);
				sourceservice.insertResource(rs);
			}
  		}
		if(onlysavetodraft){
			 finishSave(SAVE_LOCATE_SUCCESS);
		}else{
 			//判断网络状态
			if(!NetUtil.isInNetworkState()||isSelectBatchAndLabel()||!uploadToNet(save_type)){
				//未联网或者未选择批次和标签,或者未在wifi情况下用户设置的不允许上传
 				record.setSavestatus(Record.BATCHRECORD_SAVESTATE_DRAFT);
				recordservice.update(record);
				pagereset();//清空设置
				if(isSelectBatchAndLabel())
				   finishSave(SAVE_LOCATE_SUCCESS);
				else finishSave(SAVE_FAILED_BY_UNWIFI);
			}else{
  	 			Intent inte = new Intent(AddRecordActivity.this,TimelineActivity.class);
				inte.putExtra("id", trace_code);
				inte.putExtra("loadtype", usertype);
				inte.putExtra("from", TimelineActivity.FROM_ADD_RECORD);
				pagereset();//清空设置
				startActivity(inte);
	 		}
		}
		
		 
	}
	/**
	 * 判断要不要传到网路
	 * @param recordtype
	 * @return
	 */
	private boolean uploadToNet(int recordtype){
		boolean iswifi=NetUtil.isWifiNetwork();
		if(iswifi) return true;
 		if((recordtype& Record.BATCHRECORD_TYPE_OF_IMAGE)== Record.BATCHRECORD_TYPE_OF_IMAGE){
			//包含图片
			String imgset=systemset.getSetValue(SystemSetType.CommonType.IMAGE_WIFI);
 			if(imgset!=null&&SystemSetType.TRUE_VALUE.equals(imgset))//仅在wifi下上传
 				return false;
 		}
		if((recordtype& Record.BATCHRECORD_TYPE_OF_AUDIO)== Record.BATCHRECORD_TYPE_OF_AUDIO){
			//包含音频
			String audioset=systemset.getSetValue(SystemSetType.CommonType.AUDIO_WIFI);
 			if(audioset!=null&&SystemSetType.TRUE_VALUE.equals(audioset))//仅在wifi下上传
 				return false;
 		}
		if((recordtype& Record.BATCHRECORD_TYPE_OF_VIDEO)== Record.BATCHRECORD_TYPE_OF_VIDEO){
			//包含视频
			String videoset=systemset.getSetValue(SystemSetType.CommonType.VIDEO_WIFI);
 			if(videoset!=null&&SystemSetType.TRUE_VALUE.equals(videoset))//仅在wifi下上传
 				return false;
 		}
		return true;
	}
	/**
	 * 上传到网络，基本信息
	 */
//	public void saveRecordInfo(final Record rec) {
// 		SaveRecordThread th = new SaveRecordThread(rec);
//		th.setLiserner(new NetFinishListener() {
//			@Override
//			public void onfinish(Object data) {
//				Map rdata = (Map) data;
//				int result = (Integer) rdata.get(SaveRecordThread.RESULT);
//				if (result == NetResult.RESULT_OF_SUCCESS) {
//					if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEXT)==Record.BATCHRECORD_TYPE_OF_TEXT)
// 					   rec.setState(rec.getState()+Record.BATCHRECORD_STATE_TEXTSAVED);
//					if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEMPLATE)==Record.BATCHRECORD_TYPE_OF_TEMPLATE)
//	 				   rec.setState(rec.getState()+Record.BATCHRECORD_STATE_TEMPLATESAVED);
//					recordservice.update(rec);//更新本地数据库
//					uploadImgs(rec);
//				} else {
//					finishSave(SAVE_FAILED_BY_RECORDINFO);
//				}
//			}
//		});
//		th.start();
//		LogUtil.logInfo(getClass(), "save record...");
//	}

	/**
	 * 上传图片
	 * 
	 * @param res
	 */
//	private void uploadImgs(final Record rec ) {
//		List<RecordResource> imgs =  rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
//		if (imgs != null && imgs.size() > 0) {
//			UploadNetImgThread upthread = new UploadNetImgThread(imgs,
//					batch_id, rec.getRecord_id()+"");
//			upthread.setLiserner(new NetFinishListener() {
//				@Override
//				public void onfinish(Object data) {
//					Map rdata = (Map) data;
//					int result = (Integer) rdata.get(UploadNetImgThread.RESULT);
//					if (result == NetResult.RESULT_OF_SUCCESS) {
//						//获取上传成功的图片,更新本地数据库
//						List<RecordResource> sucessimgs=(List<RecordResource>) rdata.get(UploadNetImgThread.RETURNDATA);
//						sourceservice.updateResources(sucessimgs);
//						rec.setState(rec.getState()+Record.BATCHRECORD_STATE_IMAGESAVED);
//						recordservice.update(rec);//更新本地数据库
//						uploadAudio(rec);// 上传音频
//					} else {
//						finishSave(SAVE_FAILED_BY_IMAGES);
//					}
//				}
//			});
//			upthread.start();
//
//		} else {
// 			uploadAudio(rec);// 上传音频
//		}
//		// 上传图片
//		LogUtil.logInfo(getClass(), "upload image...");
//	}

	/**
	 * 上传音频
	 * 
	 * @param res
	 */
//	private void uploadAudio(final Record rec ) {
//		List<RecordResource> audios =  rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_AUDIO);
//		if (audios != null && audios.size() > 0) {
//			UploadNetMediaThread upmediathread = new UploadNetMediaThread(audios,
//					batch_id, rec.getRecord_id()+"");
//			upmediathread.setLiserner(new NetFinishListener() {
//				@Override
//				public void onfinish(Object data) {
//					Map rdata = (Map) data;
//					int result = (Integer) rdata.get(UploadNetMediaThread.RESULT);
//					if (result == NetResult.RESULT_OF_SUCCESS) {
//						//获取上传成功的音频,更新本地数据库
//						List<RecordResource> sucessimgs=(List<RecordResource>) rdata.get(UploadNetMediaThread.RETURNDATA);
//						sourceservice.updateResources(sucessimgs);
//						rec.setState(rec.getState()+Record.BATCHRECORD_STATE_AUDIOSAVED);
//						recordservice.update(rec);//更新本地数据库
//						uploadVideo(rec);// 上传视频
//					} else {
//						finishSave(SAVE_FAILED_BY_AUDIO);
//					}
//				}
//			});
//			upmediathread.start();
//		} else {
// 			uploadVideo(rec);
//		}
//		LogUtil.logInfo(getClass(), "upload audio...");
//	}

	/**
	 * 上传视频
	 * 
	 * @param res
	 */
//	private void uploadVideo(final Record rec ) {
// 		final List<RecordResource> videos =  rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_VIDEO);
//		if (videos != null && videos.size() > 0) {
// 			UploadVideoThread th=new UploadVideoThread(rec.getRecord_id()+"",labelname,labelname,videos,user);
//			th.setLiserner(new NetFinishListener() {
// 				@Override
//				public void onfinish(Object data) {
// 					Map rdata = (Map) data;
//					int result = (Integer) rdata.get(UploadNetImgThread.RESULT);
//					if (result == NetResult.RESULT_OF_SUCCESS) {
// 						sourceservice.updateResources(videos);
// 						rec.setState(rec.getState()+Record.BATCHRECORD_STATE_VIDEOSAVED);
//						recordservice.update(rec);//更新本地数据库
// 						finishSave(SAVE_SUCCESS);
//					}else{
//						finishSave(SAVE_FAILED_BY_VIDEO);
//					}
//  				}
//			});
//			th.start();
//		} else {
// 			finishSave(SAVE_SUCCESS);
//		}
//		LogUtil.logInfo(getClass(), "upload video...");
//	}
  /**
   * 保存结果处理
   * @param status
   */
	private void finishSave(final int status) {
		saveBeforeState=getState();//更新保存前状态
 		handler.post(new Runnable() {
			@Override
			public void run() {
				if (processDialog != null )
					processDialog.dismiss();
				NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
 				if (processDialog != null && processDialog.isShowing())
					processDialog.dismiss();
				String message="";
				if (status == SAVE_SUCCESS) {
					message="记录保存完成！";
				} else if (status == SAVE_FAILED_BY_IMAGES) {
					message="图片上传失败,已保存本地！";
				} else if (status == SAVE_FAILED_BY_RECORDINFO) {
					message="记录上传失败,已保存本地！";
				} else if (status == SAVE_FAILED_BY_AUDIO) {
					message="音频上传失败,已保存本地！";
				} else if (status == SAVE_FAILED_BY_VIDEO) {
					message="视频上传失败,已保存本地！";
				}else if (status == SAVE_FAILED_BY_UNSETCONTEXT) {
					message="请先输入内容";
				}else if (status == SAVE_LOCATE_SUCCESS) {
					message="记录已保存在本地！";
				}else if (status == SAVE_FAILED_BY_UNWIFI) {
					message="当前网络未连接或在非wifi状态下，记录暂时保存在本地，以后请上传服务器！！";
				}
				messagehandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 					@Override
					public void onHandlerFinish(Object result) {
  						//恢复初始状态
 						pagereset();
 						if(status != SAVE_FAILED_BY_UNSETCONTEXT){
 							if(status== SAVE_SUCCESS){//保存成功，更改数量，跳到时间轴
 								Batch batch=batchservice.getByBatchId(Integer.parseInt(batch_id));
 								if(batch!=null){
 									if(usertype==Record.BATCHRECORD_USERTYPE_OF_CREATOR)
 									  batchservice.updateBatchRecordCount(1, usertype, (int)batch.getBatch_id());
 									else batchservice.updateBatchRecordCount(1, usertype,(int) batch.getBatch_id());
 								}
 								String val=systemset.getSetValue(SystemSetType.CommonType.SHOW_SHARE);
 								if(val!=null&&SystemSetType.TRUE_VALUE.equals(val))
 								   showShareOpWindow();
 	 						}else{
 	 							//跳转到草稿箱
 	 							Intent inte=new Intent(AddRecordActivity.this,DraftActivity.class);		
  	 				            startActivity(inte);
 	 						}
 						}
 						issaving=false;
  					}
				});
 			   messagehandler.excuteMethod(new MessageDialogHandlerMethod("",message));
			}
		});
		
	}
	
	/**
	 * 显示分享操作窗口
	 */
	private void showShareOpWindow() {
		final String[] ops = new String[] { "分享到微博", "分享到朋友圈" };
		final int MAX_TIME = 6;
		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent inte = new Intent(AddRecordActivity.this,
						TimelineActivity.class);
				inte.putExtra("id", trace_code);
				startActivity(inte);
			}
		};
		if (this == Gloable.getInstance().getCurContext()) {
			final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(
					"").setItems(ops, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String message = "我刚发布了一条" + variety_name + "品种的记录，快来看看吧！";
					if (which == 0) {
						send.sendSinaShare(message,null);
					} else {
						send.sendMomentsShare(message,null);
					}
				}
			}).setPositiveButton("查看记录(" + MAX_TIME + ")", listener).show();
			dialog.setOnKeyListener(new OnKeyListener() {
 				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
  					if (keyCode == KeyEvent.KEYCODE_BACK )  { 
 						dialog.dismiss();
 						if(timer!=null){
 							timer.cancel();
 							timer=null;
 						}
 						 return true;
 			        }  
 					return false;
				}
			});
			
			// 倒计时
			timer=new CountDownTimer(MAX_TIME * 1000, 1000) {
				public void onTick(long millisUntilFinished) {
					long time = millisUntilFinished / 1000;
					if (dialog != null)
						dialog.getButton(AlertDialog.BUTTON1).setText(
								"查看记录(" + time + ")");

				}

				public void onFinish() {
					if (dialog != null)
						dialog.dismiss();
					if(timer!=null){
						//LogUtil.logInfo(getClass(), "to timeline");
						Intent inte = new Intent(AddRecordActivity.this,
								TimelineActivity.class);
						inte.putExtra("id", trace_code);
						inte.putExtra("loadtype", usertype);
						startActivity(inte);
					}
 				}
			}.start();
		}

	}
	 

	/**
	 * 判断是否需要选择批次和环节 
	 * @return
	 */
	public boolean isSelectBatchAndLabel(){
		if(this.batch_id==null||"".equals(this.batch_id)||this.labelname==null||"".equals(this.labelname)){
			return true;
		}
		return false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
 		if(issaving){
			 return true;
		}
  		if (keyCode == KeyEvent.KEYCODE_BACK )  {  
			 reback();
			 return true;
        }  
  		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 后退
	 */
	private void reback(){
		String nowstate=getState();
  		if(!nowstate.equals(saveBeforeState)){
 			final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
   			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					Integer res=(Integer) ((Map)result).get("result");
 					if(res==1){
 						startsaveRecord(true);
 					}else AddRecordActivity.this.finish();
				}
			});
 			handler.excuteMethod(new ConfirmDialogHandlerMethod("","是否保存到草稿箱?","确定","放弃"));
 		}
 		else this.finish();
 	}
 
	
	
 
	
	/**
	 * 获取用户操作状态
	 * @return
	 */
	protected String getState(){
 		StringBuffer  state=new StringBuffer(this.trace_code==null?"":this.trace_code);
 		state.append("resourceopcount:"+rview.getOpcount()+";");
 		state.append("contexttext:"+records_text.getEditableText().toString()+";");
 		state.append("contexttable:"+edittable.getAnswerString()+";");
 		//LogUtil.logInfo(getClass(), "state:"+state.toString());
		return state.toString();
	}
	/**
	 * 判断用户输入没输入内容
	 */
	protected boolean isEmpty(){
		if(records_text.getVisibility()==View.VISIBLE){
			if(!"".equals(records_text.getEditableText().toString().trim()))
				return false;
		}
		if(edittable.getVisibility()==View.VISIBLE){
			if(edittable.hasData())
				return false;
		}
		if(audioview.getVisibility()==View.VISIBLE){
			if(audioview.getSavePath()!=null)
				return false;
		}
		if(rview.hasResources()) return false;
		
		return true;
	}
	/**
	 * 重置页面状态
	 */
	private void pagereset(){
		record=null;
		records_text.setText("");
		edittable.clearAnswer();
		audioview.removeAudio();
		rview.clearResource();
		records_text.setVisibility(View.VISIBLE);
		edittable.setVisibility(View.GONE);
		audioview.setVisibility(View.GONE);
		saveBeforeState=getState();
	}
	@Override
	protected void onDestroy() {
		pagereset();//清空设置
		super.onDestroy();
	}
	 
	
}
