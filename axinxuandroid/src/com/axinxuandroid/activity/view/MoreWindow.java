package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.EditVilleageDescActivity;
import com.axinxuandroid.activity.ProustActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.SelectTypeActivity;
 import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.UploadPhotoActivity;
import com.axinxuandroid.activity.VilleageInfoActivity;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddUserFavoriteThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.RemoveUserFavoriteThread;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.MoreWindowDataInterface;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.UserFavorite.FavoriteType;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.sys.gloable.Gloable;

import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.SendShareInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MoreWindow extends PopupWindow {
	public static final int EDIT_RECORD_FUN = 0;
	public static final int SHARE_WEIBO_FUN = 1;
	public static final int SHARE_COMMENTS_FUN = 2;
	public static final int COLLECT_FUN = 3;
	public static final int COPY_LINK_FUN = 4;
	public static final int BUY_LINK_FUN = 5;
	public static final int CONFIRM_BUY_FUN = 6;//ȷ���ջ�
	public static final int EDIT_VILLEAGEDESC_FUN = 7;
	public static final int UPLOAD_VILLEAGEIMG_FUN = 8;
	public static final String DATA_RECORD = "data_record";//��¼����
  	public static final String DATA_BATCH = "data_batch";//��������
	public static final String DATA_VILLEAGE = "data_villeage";//ũ������
	public static final String DATA_USER = "data_user";//�û�����
 	public static final String DATA_USER_FAVORITE = "data_userfavorite";//�û��ղ�
	private Activity context;
	private static final int WINDOW_WIDTH = 190;
	private View editrecord, shareweibo, sharecomments, collect, copylink,
			buylink, confirmbuy,editdesc,uploadimg;
	private SendShareInfo share;
    private Map<String,Object> datas;
    private boolean iscollect=false;
    private ProgressDialog progress;
    private EventListener eventlistener;
    private LinearLayout mainlay;
	public MoreWindow(Activity context, int[] config) {
		super(context);
		datas=new HashMap<String, Object>();
 		View view =initMainLay(context);
 		// �������ÿ��
 		int height = 0;
		if (config != null)
			height = DensityUtil.dip2px(45) * config.length
					+ DensityUtil.dip2px(8);
		this.setHeight(height);
  		editrecord = view.findViewById(R.id.timelinemorepop_editrecord);
		shareweibo = view.findViewById(R.id.timelinemorepop_shareweibo);
		sharecomments = view.findViewById(R.id.timelinemorepop_sharecomments);
		collect = view.findViewById(R.id.timelinemorepop_collect);
		copylink = view.findViewById(R.id.timelinemorepop_copylink);
		buylink = view.findViewById(R.id.timelinemorepop_buylink);
		confirmbuy = view.findViewById(R.id.timelinemorepop_confirmbuy);
		editdesc= view.findViewById(R.id.timelinemorepop_editdesc);
		uploadimg= view.findViewById(R.id.timelinemorepop_uploadimg);
 		initView(config);
		share = new SendShareInfo(context);
		uploadimg.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				toUploadVilleagePhoto();
			}
		});
		editrecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toEditRecord();
			}
		});
		editdesc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toEditVilleageDesc();
			}
		});
 		shareweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
  			    String message = getRecordShareMessage();
  			    String showimg=null;
 			    if(message==null){
 			    	message =  getVilleageShareMessage();
  			    }else{
 			    	showimg=getRecordShareImage();
 			    }
 					
 			    LogUtil.logInfo(getClass(),"share message"+ message+";img:"+showimg);
				share.sendSinaShare(message==null?"":message,showimg);
 			}
		});
		sharecomments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 String message = getRecordShareMessage();
	  			 String showimg=null;
	 			 if(message==null){
	 			    	message =  getVilleageShareMessage();
	  			 }else{
	 			    	showimg=getRecordShareImage();
	 			 }
				share.sendMomentsShare(message==null?"":message,showimg);
			}
		});
		collect.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				MoreWindow.this.dismiss();
 				prepareCollect();
			}
		});
		copylink.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				copyLink();
			}
		});
	}
	public MoreWindow(Activity context,List<MoreWindowDataInterface> showdatas,final OnClickItemListener lis){
		super(context);
		datas=new HashMap<String, Object>();
 		View view =initMainLay(context);
 		// �������ÿ��
 		int height = 0;
		if (showdatas != null)
			height = DensityUtil.dip2px(45) * showdatas.size()
					+ DensityUtil.dip2px(8);
		this.setHeight(height);
		mainlay.removeAllViews();//�Ƴ����ж����ѡ��
		//����Զ���ѡ��
		if(showdatas!=null){
			int index=0;
			for(MoreWindowDataInterface data:showdatas){
				final int dataindex=index;
				LayoutInflater inflater = LayoutInflater.from(context);
				View itemview = inflater.inflate(R.layout.morewindow_data_item, null);
				TextView text=(TextView) itemview.findViewById(R.id.morewindow_item_text);
				String textstr=data.getShowText();
				text.setText(textstr==null?"":textstr);
				itemview.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
 						if(MoreWindow.this.isShowing())
 							MoreWindow.this.dismiss();
 						if(lis!=null)
 							lis.onclick(dataindex);
					}
				});
				mainlay.addView(itemview);
				index++;
			}
		}
	}
	
	/**
	 * �����ܳ�ʼ��
	 * @param context
	 * @return
	 */
	private View initMainLay(Activity context){
  		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.morewindow, null);
		view.setFocusableInTouchMode(true);
		this.setContentView(view);
		// �������ÿ��
		this.setWidth(DensityUtil.dip2px(WINDOW_WIDTH));
  		// ColorDrawable dw = new ColorDrawable(-00000);
		ColorDrawable dw = new ColorDrawable();
		this.setBackgroundDrawable(dw);
		// this.setFocusable(true);
		this.setTouchable(true); // ����PopupWindow�ɴ���
		this.setOutsideTouchable(true); // ���÷�PopupWindow����ɴ���
 		this.setAnimationStyle(R.style.TimeLineBottomAnimation);
 		mainlay=(LinearLayout) view.findViewById(R.id.timelinemorepop_mainlay);
		return view;
	}
	 
	 
	
	
	public void addData(String key,Object val){
		this.datas.put(key, val);
	}
	 
	private void initView(int[] configs) {
		if (configs != null) {
			for (int conf : configs) {
				View menu=getMenuView(conf);
				if(menu!=null)
					menu.setVisibility(View.VISIBLE);
 			}
		}
	}

	private View getMenuView(int menuid){
	  View menuview=null;
	  switch (menuid) {
		case EDIT_RECORD_FUN:
			menuview=editrecord;break;
		case SHARE_WEIBO_FUN:
			menuview=shareweibo;break;
		case SHARE_COMMENTS_FUN:
			menuview=sharecomments;break;
		case COLLECT_FUN:
			menuview=collect;break;
		case COPY_LINK_FUN:
			menuview=copylink;break;
		case BUY_LINK_FUN:
			menuview=buylink;break;
		case CONFIRM_BUY_FUN:
			menuview=confirmbuy;break;
		case EDIT_VILLEAGEDESC_FUN:
			menuview=editdesc;break;
		case UPLOAD_VILLEAGEIMG_FUN:
			menuview=uploadimg;break;
 		}
	  return menuview;
	}
	
	private boolean validUserState(){
		if(datas.get(DATA_USER)==null){
			  NcpzsHandler hand=Gloable.getInstance().getCurHandler();
			  hand.excuteMethod(new MessageDialogHandlerMethod("","���ȵ�¼"));
			  return false;
		}
		return true;
	}
	
	private void toEditRecord() {
	   if(this.isShowing())
		   this.dismiss();
	   if(validUserState()){
		   if(this.eventlistener!=null)
				this.eventlistener.beforeEventDeal();
	 		Batch batch=(Batch) datas.get(DATA_BATCH);
			if (batch != null) {
				Intent openCameraIntent = new Intent(context,
						SelectTypeActivity.class);
				openCameraIntent.putExtra("batch_id", batch.getBatch_id() + "");
				openCameraIntent.putExtra("villeage_id", (int) batch
						.getVilleage_id());
				openCameraIntent.putExtra("trace_code", batch.getCode());
				openCameraIntent.putExtra("variety_name", batch.getVariety());
				openCameraIntent.putExtra("categoryid", batch.getCategoryid());
	 			openCameraIntent
						.putExtra("variety_id", (int) batch.getVariety_id());
				context.startActivity(openCameraIntent);
	   		}
			if(this.eventlistener!=null)
				this.eventlistener.afterEventDeal();
	   }
	   
	}
	private void toEditVilleageDesc() {
		   if(this.isShowing())
			   this.dismiss();
		   if(this.eventlistener!=null)
				this.eventlistener.beforeEventDeal();
			Villeage vil=(Villeage) datas.get(DATA_VILLEAGE);
			if (vil != null) {
				Intent openCameraIntent = new Intent(context,
						EditVilleageDescActivity.class);
 				openCameraIntent.putExtra("villeageid",  vil
						.getVilleage_id());
 				context.startActivity(openCameraIntent);
	   		}
			if(this.eventlistener!=null)
				this.eventlistener.afterEventDeal();
	}
	private void toUploadVilleagePhoto() {
		   if(this.isShowing())
			   this.dismiss();
		   if(this.eventlistener!=null)
				this.eventlistener.beforeEventDeal();
			Villeage vil=(Villeage) datas.get(DATA_VILLEAGE);
			if (vil != null) {
				Intent openCameraIntent = new Intent(context,
						UploadPhotoActivity.class);
				openCameraIntent.putExtra("villeageid",  vil
						.getVilleage_id());
				context.startActivity(openCameraIntent);
	   		}
			if(this.eventlistener!=null)
				this.eventlistener.afterEventDeal();
	}
	public void setCollected(boolean collected){
		this.iscollect=collected;
		View menuview=getMenuView(COLLECT_FUN);
		if(menuview!=null){
			if(this.iscollect){
				((TextView)menuview.findViewById(R.id.timelinemorepop_collect_text)).setText("ȡ���ղ�");
 				((ImageView)menuview.findViewById(R.id.timelinemorepop_collect_img)).setImageResource(R.drawable.morewindow_cancelcollect);
			}else{
				((TextView)menuview.findViewById(R.id.timelinemorepop_collect_text)).setText("�����ղ�");
				((ImageView)menuview.findViewById(R.id.timelinemorepop_collect_img)).setImageResource(R.drawable.morewindow_collect);
 			}
		}
		
 	}
	private void prepareCollect() {
		 if(validUserState()){
			NcpzsHandler handler = Gloable.getInstance().getCurHandler();
	 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
				@Override
				public void onHandlerFinish(Object result) {
					if (result != null) {
						progress = (ProgressDialog) ((Map) result).get("process");
					}
	  				collect(); 
	 			}
			});
	 		String message="���ڼ����ղ�....";
	 		if(this.iscollect)
	 			message="����ȡ���ղ�";
			handler.excuteMethod(new ProcessDialogHandlerMethod("",message));
		 }
	}
	private void collect() {
		 if(this.iscollect){
			 //ȡ���ղ�
			 final UserFavorite fav=(UserFavorite) datas.get(DATA_USER_FAVORITE);
			 if(fav!=null){
				 RemoveUserFavoriteThread rth=new RemoveUserFavoriteThread(fav.getFavid());
 				 rth.setLiserner(new NetFinishListener() {
 					@Override
					public void onfinish(NetResult data) {
 	 					if(data.result==NetResult.RESULT_OF_SUCCESS){
	 						UserFovoriteService ufservice=new UserFovoriteService();
	 						ufservice.deleteByFavId(fav.getFavid());
	 						datas.remove(DATA_USER_FAVORITE);
	 						finishCollect(true);
	 					}else finishCollect(false);
					}
				});
 				rth.start();
			 }else finishCollect(false);
		 }else{
			 //����ղ�
			 Batch batch=(Batch) datas.get(DATA_BATCH);
			 User user=(User) datas.get(DATA_USER);
			 if(batch!=null&&user!=null){
				 UserFavorite uf=new UserFavorite();
				 uf.setFavorite_id((int)batch.getBatch_id());
				 uf.setFavorite_type(FavoriteType.FavoriteType_OF_BATCH);
				 uf.setUser_id(user.getUser_id());
				 AddUserFavoriteThread th=new AddUserFavoriteThread(uf);
				 th.setLiserner(new NetFinishListener() {
  					@Override
					public void onfinish(NetResult data) {
 	 					if(data.result==NetResult.RESULT_OF_SUCCESS){
	 						UserFavorite nuf=(UserFavorite)data.returnData;
	 						UserFovoriteService ufservice=new UserFovoriteService();
	 						ufservice.saveOrUpdate(nuf);
	 						datas.put(DATA_USER_FAVORITE, nuf);
	 						finishCollect(true);
	 					}else finishCollect(false);
					}
				});
				th.start();
			 }else finishCollect(false);
		 }
  	}
	private void finishCollect(final boolean success){
		final NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				String message="�ղسɹ���";
 				if(iscollect)
 					message="ȡ���ղسɹ���";
  				if(success)
 				  setCollected(!iscollect);
  				handler.excuteMethod(new MessageDialogHandlerMethod("", message));
 			}
		});
	}

	private void copyLink() {
		Batch batch=(Batch) datas.get(DATA_BATCH);
		if(batch!=null){
			ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(HttpUrlConstant.URL_HEAD+"code/"+batch.getCode());
			 if(this.isShowing())
				   this.dismiss();
			NcpzsHandler hand=Gloable.getInstance().getCurHandler();
			hand.excuteMethod(new MessageDialogHandlerMethod("","������ɣ�"));
		}
	}

	private void buyLink() {

	}

	private void confrimBuy() {

	}
	/**
	 * ��ȡ������Ϣ
	 * @return
	 */
	public String getRecordShareMessage() {
		Batch batch=(Batch) datas.get(DATA_BATCH);
	    Record rec=(Record) datas.get(DATA_RECORD);
	    User user=(User) datas.get(DATA_USER);
		String message="";
  		if(rec!=null){
			if(user!=null&&rec.getUser_id().equals(user.getUser_id()+"")){
				if((rec.getSave_type() & Record.BATCHRECORD_TYPE_OF_TEXT) == Record.BATCHRECORD_TYPE_OF_TEXT){
					if(rec.getContext()!=null&&rec.getContext().length()>100)
				    	message+=rec.getContext().substring(0,99)+"...";
					else message+=rec.getContext()+".";
				}
			}else{
 				if(batch!=null)
				  message= "���ڰ���ѡ����"+HttpUrlConstant.URL_HEAD+"������"+batch.getVilleage_name()+"��"+rec.getVariety_name()+",����ȥ�ܲ��� �����������ɡ�";
			    else  message= "���ڰ���ѡ����"+HttpUrlConstant.URL_HEAD+"������"+rec.getVariety_name()+",����ȥ�ܲ��� �����������ɡ�";

			}
			message+=HttpUrlConstant.URL_HEAD+"/no/"+rec.getTrace_code();
		}else if(batch!=null){
				message= "���ڰ���ѡ����"+HttpUrlConstant.URL_HEAD+"������"+batch.getVilleage_name()+"��"+batch.getCode()+",����ȥ�ܲ��� �����������ɡ�";
				message+=HttpUrlConstant.URL_HEAD+"/no/"+batch.getCode();
		}
  		return message;
  }
	/**
	 * ��ȡ���εķ�����Ϣ
	 * @return
	 */
	public String getRecordShareImage() {
		Record rec=(Record) datas.get(DATA_RECORD);
		if(rec!=null){
			List<RecordResource> res=rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
			if(res!=null){
				return res.get(0).getLocalurl();
			}
		}
 		return null;
   }
	/**
	 * ��ȡũ���ķ�����Ϣ
	 * @return
	 */
	public String getVilleageShareMessage() {
		Villeage villeage=(Villeage) datas.get(DATA_VILLEAGE);
	    String message =null;
		if (villeage != null){
			message = "��ӭ�����ҵ�ũ��:"+villeage.getVilleage_name()+"!";
		}
		return message;
	}
	
	
 		
	/**
	 * ����ϵͳ���������ʾ����
	 * @param id
	 * @param message
	 */
	public void setShowInfo(int id,String message){
		LinearLayout view=(LinearLayout)getMenuView(id);
		if(view!=null){
			LinearLayout textlay=(LinearLayout)view.getChildAt(0);
 			if(textlay!=null){
				TextView tx=(TextView) textlay.getChildAt(1);
				if(tx!=null)
					tx.setText(message);
			}
 		}
	}
	
	/**
	 * ����ϵͳ�Ķ�����
	 * @param id
	 * @param message
	 */
	public void hiddenSystemItem(int id){
		LinearLayout view=(LinearLayout)getMenuView(id);
		if(view!=null){
			view.setVisibility(View.GONE);
			int height=this.getHeight();//�ı�߶�
			this.setHeight(height-DensityUtil.dip2px(43));
 		}
	}
	
	public interface EventListener{
		public void beforeEventDeal();//�ڴ����¼�ǰ����
		public void afterEventDeal();//�ڴ����¼������
	}
	
	public void setEventListener(EventListener lis){
		this.eventlistener=lis;
	}
	/**
	 * �Զ���ѡ�����¼�
	 * @author Administrator
	 *
	 */
	public interface OnClickItemListener{
		public void onclick(int index);
	}
	 
}
