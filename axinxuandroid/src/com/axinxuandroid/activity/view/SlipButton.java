package com.axinxuandroid.activity.view;

import com.axinxuandroid.activity.R;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SlipButton extends View implements OnTouchListener {
	private boolean NowChoose = false;// ��¼��ǰ��ť�Ƿ��,trueΪ��,flaseΪ�ر�
 	private boolean OnSlip = false;// ��¼�û��Ƿ��ڻ����ı���
	private float DownX, NowX;// ����ʱ��x,��ǰ��x
	private Rect Btn_On, Btn_Off;// �򿪺͹ر�״̬��,�α��Rect .
	private boolean isChgLsnOn = false;
	private OnChangedListener ChgLsn;
	private Bitmap bg_on, bg_off, slip_btn;
	private String leftMessage;
	private String rightMessage;
	private int position;
	public static final int LEFT_POSITION = 1;
	public static final int RIGHT_POSITION = 2;

	public SlipButton(Context context) {
		this(context, null, 0);
	}

	public SlipButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlipButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray tArray = context.obtainStyledAttributes(attrs,
				R.styleable.slipbutton);
		leftMessage = tArray.getString(R.styleable.commontop_title);
		rightMessage = tArray.getString(R.styleable.commontop_subtitle);
		tArray.recycle();// �ͷ�
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(bg_on.getWidth(), bg_on.getHeight());// ����õ�����������Ϊ��λ����ֵ
	}

	private void init() {// ��ʼ��
//		bg_on = BitmapFactory.decodeResource(getResources(),
//				R.drawable.slipbtn_leftbg);
		bg_on = BitmapFactory.decodeResource(getResources(),
				R.drawable.slipbtn_bg);
		bg_off = BitmapFactory.decodeResource(getResources(),
				R.drawable.slipbtn_bg);
 		slip_btn = BitmapFactory.decodeResource(getResources(),
				R.drawable.slipbtn_ball_small);
		Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
		Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0, bg_off
				.getWidth(), slip_btn.getHeight());
		setOnTouchListener(this);// ���ü�����,Ҳ����ֱ�Ӹ�дOnTouchEvent
	}

	@Override
	protected void onDraw(Canvas canvas) {// ��ͼ����
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x;
		if (NowX < (bg_on.getWidth() / 2)){// ������ǰ�������εı�����ͬ,�ڴ����ж�
			position = LEFT_POSITION;
			x = NowX - slip_btn.getWidth() / 2;
			canvas.drawBitmap(bg_off, matrix, paint);// �����ر�ʱ�ı���
		} else {
			position = RIGHT_POSITION;
			x = bg_on.getWidth() - slip_btn.getWidth() / 2;
			canvas.drawBitmap(bg_on, matrix, paint);// ������ʱ�ı���
		}
		if (OnSlip){// �Ƿ����ڻ���״̬,
 			if (NowX >= bg_on.getWidth())// �Ƿ񻮳�ָ����Χ,�������α��ܵ���ͷ,����������ж�
				x = bg_on.getWidth() - slip_btn.getWidth() / 2;// ��ȥ�α�1/2�ĳ���...
			else if (NowX < 0) {
				x = 0;
			} else {
				x = NowX - slip_btn.getWidth() / 2;
			}
		} else {// �ǻ���״̬
			if (NowChoose){// �������ڵĿ���״̬���û��α��λ��
 				x = Btn_Off.left;
 				position = RIGHT_POSITION;
				canvas.drawBitmap(bg_on, matrix, paint);// ��ʼ״̬ΪtrueʱӦ�û�����״̬ͼƬ
			} else{
				position = LEFT_POSITION;
				x = Btn_On.left;
			}
 		}
 		if (x < 0)// ���α�λ�ý����쳣�ж�...
			x = 0;
		else if (x > bg_on.getWidth() - slip_btn.getWidth())
			x = bg_on.getWidth() - slip_btn.getWidth();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);  //�������
 		paint.setTextSize(DensityUtil.sp2px(16));
 		//paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		FontMetrics fm = paint.getFontMetrics();
		double fweught = Math.ceil(fm.descent - fm.top);
		// ����������ʾ
		if ((position == LEFT_POSITION)) {
			if (rightMessage != null) {
				paint.setColor(Color.rgb(255, 255, 255));
				int marginLeft = (int) (this.getWidth() - paint
						.measureText(rightMessage))-DensityUtil.dip2px(20/rightMessage.length());
				canvas.drawText(rightMessage, marginLeft, (int) (this
						.getHeight() / 2 + fweught / 4), paint);
			}
		} else {
			if (leftMessage != null) {
 				paint.setColor(Color.rgb(255, 255, 255));
				// int marginLeft = (int) (slip_btn.getWidth() -
				// paint.measureText(leftMessage)) / 2;
				canvas.drawText(leftMessage, DensityUtil.dip2px(20/leftMessage.length()),
						(int) (this.getHeight() / 2 + fweught / 4), paint);
			}
		}
		canvas.drawBitmap(slip_btn, x, 0, paint);// �����α�.

	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction())
		// ���ݶ�����ִ�д���
		{
		case MotionEvent.ACTION_MOVE:// ����
			NowX = event.getX();
			break;
		case MotionEvent.ACTION_DOWN:// ����
			if (event.getX() > bg_on.getWidth()
					|| event.getY() > bg_on.getHeight())
				return false;
			OnSlip = true;
			DownX = event.getX();
			NowX = DownX;
			break;
		case MotionEvent.ACTION_CANCEL: // �Ƶ��ؼ��ⲿ
			OnSlip = false;
			boolean choose = NowChoose;
			if (NowX >= (bg_on.getWidth() / 2)) {
				NowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
				NowChoose = true;
			} else {
				NowX = NowX - slip_btn.getWidth() / 2;
				NowChoose = false;
			}
			if (isChgLsnOn && (choose != NowChoose)) // ��������˼�����,�͵����䷽��..
				ChgLsn.OnChanged(NowChoose);
			break;
		case MotionEvent.ACTION_UP:// �ɿ�
			OnSlip = false;
			boolean LastChoose = NowChoose;
			if (event.getX() >= (bg_on.getWidth() / 2)) {
				NowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
				NowChoose = true;
			} else {
				NowX = NowX - slip_btn.getWidth() / 2;
				NowChoose = false;
			}
			if (isChgLsnOn && (LastChoose != NowChoose)) // ��������˼�����,�͵����䷽��..
				ChgLsn.OnChanged(NowChoose);
			break;
		default:
		}
		invalidate();// �ػ��ؼ�
		return true;
	}

	public void SetOnChangedListener(OnChangedListener l) {// ���ü�����,��״̬�޸ĵ�ʱ��
		isChgLsnOn = true;
		ChgLsn = l;
	}

	public interface OnChangedListener {
		abstract void OnChanged(boolean CheckState);
	}

	public void setCheck(boolean choose) {
 		NowChoose = choose;
	}

	public int getPosition() {
		return this.position;
	}
}