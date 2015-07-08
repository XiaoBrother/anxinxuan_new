package com.axinxuandroid.activity;

import java.util.Calendar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class SystemActivity extends Activity{
	private EditText editTextDate;

	/**
	 * @param editText
	 * @param focusable
	 * @return dateListener
	 * ��ȡ���ڴ��ڵļ�����
	 */
	public OnClickListener getDateListener(EditText editText, boolean focusable) {
		this.editTextDate = editText;
		initPoputEditText(editText, focusable);
		return dateListener;
	}
	
	/**
	 * @param editText
	 * @param focusable
	 * ��ʼ������ѡ�񴰿ڵı༭��
	 */
	public void initPoputEditText(EditText editText, boolean focusable) {
		editText.setFocusable(focusable);
	}
	
	/**
	 * ��������ѡ�񴰿ڣ��ڱ༭������ʾ��yyyy-MM-dd����ʽ������
	 */
	private Button.OnClickListener dateListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			InputMethodManager manager = (InputMethodManager)SystemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(editTextDate.getWindowToken(), 0);
			Calendar calendar = Calendar.getInstance();
			AlertDialog dialog = new DatePickerDialog(SystemActivity.this, new OnDateSetListener() {
		    	
		    	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		    		StringBuffer dateBuffer = new StringBuffer();
		    		dateBuffer.append(String.valueOf(year) + "-");
		    		if (monthOfYear < 9) {
		    			dateBuffer.append("0" + String.valueOf(monthOfYear + 1));
					} else {
						dateBuffer.append(String.valueOf(monthOfYear + 1));
					}
		    		dateBuffer.append("-");
		    		if (dayOfMonth < 10) {
		    			dateBuffer.append("0" + dayOfMonth);
					} else {
						dateBuffer.append(dayOfMonth);
					}
		    		editTextDate.setText(dateBuffer.toString());
		        }

		    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			dialog.show();
		}
		
	};
	

}
