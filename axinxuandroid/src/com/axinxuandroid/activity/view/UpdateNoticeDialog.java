package com.axinxuandroid.activity.view;

import com.axinxuandroid.activity.R;
import com.ncpzs.util.DensityUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

public class UpdateNoticeDialog  extends Dialog{

	public UpdateNoticeDialog(Context context) {
		super(context);
 	}
	public UpdateNoticeDialog(Context context, int theme) {  
        super(context, theme);  
    }
	public static class Builder {  
        private Context context;  
        private String title;  
        private String message;  
        private String positiveButtonText;  
        private String negativeButtonText;  
        private View contentView;  
        private DialogInterface.OnClickListener positiveButtonClickListener;  
        private DialogInterface.OnClickListener negativeButtonClickListener;  
  
        public Builder(Context context) {  
            this.context = context;  
        }  
  
        public Builder setMessage(String message) {  
            this.message = message;  
            return this;  
        }  
  
        /** 
         * Set the Dialog message from resource 
         *  
         * @param title 
         * @return 
         */  
        public Builder setMessage(int message) {  
            this.message = (String) context.getText(message);  
            return this;  
        }  
  
        /** 
         * Set the Dialog title from resource 
         *  
         * @param title 
         * @return 
         */  
        public Builder setTitle(int title) {  
            this.title = (String) context.getText(title);  
            return this;  
        }  
  
        /** 
         * Set the Dialog title from String 
         *  
         * @param title 
         * @return 
         */  
  
        public Builder setTitle(String title) {  
            this.title = title;  
            return this;  
        }  
  
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        }  
  
        /** 
         * Set the positive button resource and it's listener 
         *  
         * @param positiveButtonText 
         * @return 
         */  
        public Builder setPositiveButton(int positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = (String) context  
                    .getText(positiveButtonText);  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setPositiveButton(String positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = positiveButtonText;  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(int negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = (String) context  
                    .getText(negativeButtonText);  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(String negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = negativeButtonText;  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
  
        public UpdateNoticeDialog create() {  
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            // instantiate the dialog with the custom Theme  
            final UpdateNoticeDialog dialog = new UpdateNoticeDialog(context,R.style.dialog);  
            View layout = inflater.inflate(R.layout.update_notice_dialog, null);  
             // set the dialog title  
            ((TextView) layout.findViewById(R.id.update_notice_wind_msgtext)).setText(this.message);  
            // set the confirm button  
             
             ((ImageButton) layout.findViewById(R.id.update_notice_wind_cancelbtn)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 if(negativeButtonClickListener!=null)
						 negativeButtonClickListener.onClick(dialog,  DialogInterface.BUTTON_NEGATIVE);
					dialog.dismiss();
				}
			}) ;
             ((ImageButton) layout.findViewById(R.id.update_notice_wind_okbtn)).setOnClickListener(new View.OnClickListener() {
  				@Override
 				public void onClick(View v) {
 					 if(positiveButtonClickListener!=null)
 						positiveButtonClickListener.onClick(dialog,  DialogInterface.BUTTON_POSITIVE);
 					dialog.dismiss();
 				}
 			}) ;       
             
            dialog.setContentView(layout, new LayoutParams(  
                    DensityUtil.dip2px(300),   DensityUtil.dip2px(350)));  
            return dialog;  
        }  
  
    }  
 
}
