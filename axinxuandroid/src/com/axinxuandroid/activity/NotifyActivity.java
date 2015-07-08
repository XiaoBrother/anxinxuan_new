package com.axinxuandroid.activity;

 
import com.axinxuandroid.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
/**
 * ֪ͨҳ
 * @author hubobo
 *
 */
public class NotifyActivity extends NcpZsActivity {
    /** Called when the activity is first created. */
	private TextView title;
	private TextView info;
	private Button backbtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);
        title=(TextView) this.findViewById(R.id.notify_title);
        info=(TextView) this.findViewById(R.id.notify_info);
        backbtn=(Button) this.findViewById(R.id.notify_backbtn);
        Intent intent = getIntent();
        String notificationId = intent
                .getStringExtra("id");
         String notificationTitle = intent
                .getStringExtra("title");
        String notificationMessage = intent
                .getStringExtra("message");
        title.setText(notificationTitle);
        info.setText(notificationMessage);
        
    }
    
}