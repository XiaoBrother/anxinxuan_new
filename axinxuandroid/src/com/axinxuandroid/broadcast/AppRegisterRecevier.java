package com.axinxuandroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axinxuandroid.oauth.OAuthConstant;
import com.ncpzs.util.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegisterRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.logInfo(getClass(), "registe app");
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
 		// 将该app注册到微信
		api.registerApp(OAuthConstant.WebChat.APP_ID);
	}
}
