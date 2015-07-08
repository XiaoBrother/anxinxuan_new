package com.axinxuandroid.sys.gloable;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiDuLocation {
	 //�ٶȶ�λ����ȡ�û�λ����Ϣ
    private  LocationClient lc;
     private GetBaiduLocationPositionListener lis;
    public static final int GET_POSITION_RESULT_SUCCESS=1;
    public static final int GET_POSITION_RESULT_FAILD=-1;
    public BaiDuLocation(Context context){
    	lc = new LocationClient(context.getApplicationContext());     //����LocationClient��
    	lc.registerLocationListener( new LocationListener() );    //ע���������
   	 	LocationClientOption option = new LocationClientOption();
  	 	option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
  	 	option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
  	 	//�����������ֵ���ڵ���1000��ms��ʱ����λSDK�ڲ�ʹ�ö�ʱ��λģʽ��
  	 	//����requestLocation( )��ÿ���趨��ʱ�䣬��λSDK�ͻ����һ�ζ�λ��
  	 	//�����λSDK���ݶ�λ���ݷ���λ��û�з����仯���Ͳ��ᷢ����������
  	 	//������һ�ζ�λ�Ľ�����������λ�øı䣬�ͽ�������������ж�λ���õ��µĶ�λ�����
  	 	//��ʱ��λʱ������һ��requestLocation���ᶨʱ��������λ�����
   	 	//���������������������ֵС��1000��ms��ʱ������һ�ζ�λģʽ��ÿ����һ��requestLocation( )����λSDK�ᷢ��һ�ζ�λ������λ��������һһ��Ӧ��
   	 	//�趨�˶�ʱ��λ�󣬿������л���һ�ζ�λ����Ҫ��������ʱ����С��1000��ms�����ɡ�
  	 	//locationClient����stop�󣬽����ٽ��ж�λ������趨�˶�ʱ��λģʽ�󣬶�ε���requestLocation������
  	 	//����ÿ��һ��ʱ�����һ�ζ�λ��ͬʱ����Ķ�λ����Ҳ����ж�λ����Ƶ�ʲ��ᳬ��1��һ�Ρ�
  	 	option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
   	 	lc.setLocOption(option);
  	 	
  	  
    }
   
    
    
    public void start(GetBaiduLocationPositionListener lis){
    	this.lis=lis;
    	if(lc!=null&&!lc.isStarted()){
    		 lc.start();//������λ
    	}
    	lc.requestLocation();
     }
      
    public void stop(){
    	if(lc!=null&&lc.isStarted())
    	  lc.stop();//ֹͣ��λ
    	lc=null;
    }
    
     
    
    public class LocationListener implements BDLocationListener {
    	@Override
    	public void onReceiveLocation(BDLocation location) {
    		stop();//ֹͣ��λ 
    		double lat=-1;
    		double ltu=-1;
    		int result=GET_POSITION_RESULT_FAILD;
    		if (location != null){
    			result=GET_POSITION_RESULT_SUCCESS;
    			lat=location.getLatitude();
    			ltu=location.getLongitude();
    		}
      		if(lis!=null)
      			lis.position(result, lat, ltu);
    		 
     	}
     }

    public interface GetBaiduLocationPositionListener{
    	public void position(int result,double lat,double litu);
    }
}
