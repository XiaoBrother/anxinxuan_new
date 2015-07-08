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

import com.ncpzs.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**  
 * A broadcast receiver to handle the changes in network connectiion states.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ConnectivityReceiver extends BroadcastReceiver {
      @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.logInfo(ConnectivityReceiver.class, "ConnectivityReceiver.onReceive()...");
         ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
         if (networkInfo != null) {
              if (networkInfo.isConnected()) {
                 ServerConnection.getInstance().connectServer();
            }
        } else {
        	if( ServerConnection.getInstance().isConnect())
        	   ServerConnection.getInstance().closeConnect();
        }
    }

}
