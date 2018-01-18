package com.winguo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
/**
 * 带滚动监听的scrollview
 *
 */
public class GradationScrollView extends ScrollView {



	private ScrollViewListener scrollViewListener = null;

	public GradationScrollView(Context context) {
		super(context);
	}

	public GradationScrollView(Context context, AttributeSet attrs,
							   int defStyle) {
		super(context, attrs, defStyle);
	}

	public GradationScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}
	public interface ScrollViewListener {

		void onScrollChanged(GradationScrollView scrollView, int x, int y,
							 int oldx, int oldy);

	}
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}