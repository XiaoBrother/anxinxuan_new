package com.axinxuandroid.activity.view;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.UserPageActivity;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public class CommentAtClickSpan extends ClickableSpan {
	private Comment comment;
	public CommentAtClickSpan(Comment comment){
		super();
		this.comment=comment;
 	}
	@Override
	public void onClick(View arg0) {
	  //响应文字点击事件
		Intent intent=new Intent(Gloable.getInstance().getCurContext(),UserPageActivity.class);
		intent.putExtra("userid", Integer.parseInt(comment.getReplyUserId()));
		Gloable.getInstance().getCurContext().startActivity(intent);
	}
	@Override
	public void updateDrawState(TextPaint ds) {
	  super.updateDrawState(ds);
	  //设置没有下划线
	   ds.setUnderlineText(false);
	  //设置颜色高亮
	   ds.setARGB(255, 0, 71, 112);
	}
}