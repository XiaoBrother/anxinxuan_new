/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xmpp.client.connect;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/** 
 * A listener class for monitoring changes in phone connection states. 
 * 监听手机网络状态（包括GPRS，WIFI， UMTS等)
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class PhoneStateChangeListener extends PhoneStateListener {
  
    public PhoneStateChangeListener() {
    }

    @Override
    public void onDataConnectionStateChanged(int state) {
         super.onDataConnectionStateChanged(state);
         LogUtil.logInfo(PhoneStateChangeListener.class, "Data Connection State = " + getState(state));
         // TelephonyManager.DATA_DISCONNECTED://网络断开
     	 // TelephonyManager.DATA_CONNECTING://网络正在连接
    	 // TelephonyManager.DATA_CONNECTED://网络连接上
         if (state == TelephonyManager.DATA_CONNECTED) {
            //ServerConnection.getInstance().connectServer();
          } 
     }

    private String getState(int state) {
        switch (state) {
        case 0: // '\0'
            return "DATA_DISCONNECTED";
        case 1: // '\001'
            return "DATA_CONNECTING";
        case 2: // '\002'
            return "DATA_CONNECTED";
        case 3: // '\003'
            return "DATA_SUSPENDED";
        }
        return "DATA_<UNKNOWN>";
    }

}
