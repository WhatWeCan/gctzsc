package com.hdl.gctzsc.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 自定义控件  没有滑动功能的线性布局
 */
public class NoscrollLinearlayout extends LinearLayout {

	public NoscrollLinearlayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public NoscrollLinearlayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NoscrollLinearlayout(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
