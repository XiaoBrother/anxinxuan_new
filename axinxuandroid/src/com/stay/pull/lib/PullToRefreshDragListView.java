package com.stay.pull.lib;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.view.DragListView;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.stay.pull.lib.internal.EmptyViewMethodAccessor;

public class PullToRefreshDragListView extends PullToRefreshAdapterViewBase<DragListView> {
 	public void init(){
		setOnScrollListener(new OnScrollListener() {
 			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
 				getRefreshableView().onScrollStateChanged(view, scrollState);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				 getRefreshableView().onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
	}
 
	class InternalListView extends DragListView implements EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshDragListView.this.setEmptyView(emptyView);
		}
		
		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}

		@Override
		public void setAdapter(final ListAdapter adapter) {
 			super.setAdapter(adapter);
   			if(adapter!=null)
 				adapter.registerDataSetObserver(new DataSetObserver() {
 					@Override
					public void onChanged() {
 						super.onChanged();
 						setLine(adapter.getCount());
 						
					}
 					
				});
		}
		
		
	}

 	private void setLine(int size){
 		View lay=findViewById(R.id.pull_refresh_draglist_line);
		if (lay != null) {
			if (size > 0)
				lay.setVisibility(View.VISIBLE);
			else
				lay.setVisibility(View.GONE);
		}
 	}
	@Override
	protected boolean isReadyForPullDown() {
 		if(this.getRefreshableView().isMoving())
			return false;
 	    return super.isReadyForPullDown();
	}

	@Override
	protected boolean isReadyForPullUp() {
		if(this.getRefreshableView().isMoving())
			return false;
 		return super.isReadyForPullUp();
	}

	public PullToRefreshDragListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(false);
		init();
	}
	
	public PullToRefreshDragListView(Context context, int mode) {
		super(context, mode);
		this.setDisableScrollingWhileRefreshing(false);
		init();
	}

	public PullToRefreshDragListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDisableScrollingWhileRefreshing(false);
		init();
	}

	@Override
	public void onActionUpDoSomething(MotionEvent event) {
		this.getRefreshableView().onTouchEvent(event);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}
	
	

  
	@Override
	protected void addRefreshableView(Context context,
			DragListView refreshableView) {
 	    super.addRefreshableView(context, refreshableView);
   	  	//加最后一条分隔线
 	    LinearLayout	line=new LinearLayout(context);
		line.setBackgroundColor(Color.rgb(221, 221, 221));// #ddd
		line.setVisibility(View.GONE);
		line.setId(R.id.pull_refresh_draglist_line);
		addView(line, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(0.5f)));
   	    	 
  	}

	@Override
	protected final DragListView createRefreshableView(Context context, AttributeSet attrs) {
		DragListView lv = new InternalListView(context, attrs);
 		lv.setId(android.R.id.list);
		return lv;
	}
  
}
