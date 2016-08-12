package appbookmaster.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import android.widget.TextView;


public class ScrollTextView extends TextView {
	
	private static final int INVALID_POINTER = -1;
	private Scroller mScroller;
	private int mTouchSlop;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	
	private float mLastMotionY;
	private boolean mIsBeingDragged;
	private VelocityTracker mVelocityTracker;
	private int mActivePointerId = INVALID_POINTER;

	private TextView status = null;
	@Override
	public void computeScroll() {
		super.computeScroll();
		final Scroller scroller = mScroller;
		if (scroller.computeScrollOffset()) { // 正在滚动，让view滚动到当前位置
			int scrollY = scroller.getCurrY();
			final int maxY = (getLineCount() * getLineHeight() + getPaddingTop() + getPaddingBottom()) - getHeight();
			boolean toEdge = scrollY < 0 || scrollY > maxY;
			if (scrollY < 0) {
				scrollY = 0;
			} else if (scrollY > maxY) {
				scrollY = maxY;
			}
			scrollTo(0, scrollY);
			if (toEdge) {
				awakenScrollBars(); // 移到两端，由于位置没有发生变化，导致滚动条不显示
			}
			// 设置当前的滚动进度
			status.setText("阅读到 " + Math.round(((double) scrollY / (double) maxY) * 100) + "%");
		}
	}

	public void fling(int velocityY) {
		final int maxY = (getLineCount() * getLineHeight() + getPaddingTop() + getPaddingBottom()) - getHeight();
		mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0, Math.max(0, maxY));
		// 刷新，让父控件调用computeScroll()
		invalidate();
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handled = false;
		final int contentHeight = getLineCount() * getLineHeight();
		if (contentHeight > getHeight()) {
			handled = processScroll(ev);
		}
		return handled | super.onTouchEvent(ev);
	}

	private boolean processScroll(MotionEvent ev) {
		boolean handled = false;
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev); // 帮助类，用来在fling时计算移动初速度
		final int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN: {
				if (!mScroller.isFinished()) {
					mScroller.forceFinished(true);
				}
				mLastMotionY = ev.getY();
				mActivePointerId = ev.getPointerId(0);
				mIsBeingDragged = true;
				handled = true;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final int pointerId = mActivePointerId;
				if (mIsBeingDragged && INVALID_POINTER != pointerId) {
					final int pointerIndex = ev.findPointerIndex(pointerId);
					final float y = ev.getY(pointerIndex);
					int deltaY = (int) (mLastMotionY - y);
					if (Math.abs(deltaY) > mTouchSlop) { // 移动距离(正负代表方向)必须大于ViewConfiguration设置的默认值
						mLastMotionY = y;
						mScroller.startScroll(getScrollX(), getScrollY(), 0, deltaY, 0); // 默认滚动时间为250ms，建议立即滚动，否则滚动效果不明显 或者直接使用scrollBy(0, deltaY);
						invalidate();
						handled = true;
					}
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				final int pointerId = mActivePointerId;
				if (mIsBeingDragged && INVALID_POINTER != pointerId) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					int initialVelocity = (int) velocityTracker.getYVelocity(pointerId);
					if (Math.abs(initialVelocity) > mMinimumVelocity) {
						fling(-initialVelocity);
					}
					mActivePointerId = INVALID_POINTER;
					mIsBeingDragged = false;
					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
					handled = true;
				}
				break;
			}
		}
		return handled;
	}
	
	public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ScrollTextView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		final Context cx = getContext();
		// 设置滚动减速器，在fling中会用到
		mScroller = new Scroller(cx, new DecelerateInterpolator(0.5f));
		final ViewConfiguration configuration = ViewConfiguration.get(cx);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	public TextView getStatus() {
		return status;
	}

	public void setStatus(TextView status) {
		this.status = status;
	}
}
