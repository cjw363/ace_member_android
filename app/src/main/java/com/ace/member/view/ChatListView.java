package com.ace.member.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ace.member.R;

public class ChatListView extends ListView implements AbsListView.OnScrollListener {

	// 状态
	private static final int STATUS_0_NORMAL = 0;
	private static final int STATUS_1_REFRESHING = 1;
	private static final int STATUS_2_BAN_REFRESH = 2;

	private final Context mContext;
	// 当前刷新状态
	private int mRefreshStatus;
	private View mHeaderView;
	private onRefreshListener mOnRefreshListener;

	private int startY;
	private int scrolledY;

	public ChatListView(Context context) {
		this(context, null);
	}

	public ChatListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		initListView();
	}

	private void initListView() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeaderView = inflater.inflate(R.layout.view_chat_header_refresh, this, false);
		LinearLayout mHeaderParent = new LinearLayout(mContext);
		mHeaderParent.addView(mHeaderView);//在header的最外面再套一层LinearLayout,才能隐藏

		mRefreshStatus = STATUS_0_NORMAL;
		addHeaderView(mHeaderParent);
		mHeaderView.setVisibility(View.GONE);

		setOnScrollListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				int firstVisiblePosition = getFirstVisiblePosition();

				if (firstVisiblePosition != 0) break;
				if (mRefreshStatus == STATUS_1_REFRESHING || mRefreshStatus == STATUS_2_BAN_REFRESH) break;

				int endY = (int) ev.getY();
				int dy = endY - startY;

				if (dy > 0 && mRefreshStatus == STATUS_0_NORMAL) {
					mRefreshStatus = STATUS_1_REFRESHING;
					mHeaderView.setVisibility(View.VISIBLE);
					setSelection(0);
					if (mOnRefreshListener != null) mOnRefreshListener.onRefresh();

					return true;//消费掉事件
				}

		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 不滚动时记录当前滚动到的位置
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			record();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

	/**
	 * 记录位置
	 */
	public void record() {
		scrolledY = getMyScrollY();
	}

	/**
	 * 获取ListView的ScrollY
	 *
	 * @return
	 */
	public int getMyScrollY() {
		View c = this.getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = this.getFirstVisiblePosition();
		int top = c.getTop();
		return -top + firstVisiblePosition * c.getHeight();
	}

	/**
	 * 恢复位置
	 */
	public void restore() {
		this.post(new Runnable() {
			@Override
			public void run() {
				smoothScrollBy(scrolledY, 0);
			}
		});
	}

	public interface onRefreshListener {
		void onRefresh();
	}

	public void setOnRefreshListener(onRefreshListener listener) {
		this.mOnRefreshListener = listener;
	}

	/**
	 * 刷新成功
	 */
	public void refreshFinish() {
		mRefreshStatus = STATUS_0_NORMAL;
		mHeaderView.setVisibility(View.GONE);
	}

	/**
	 * 禁止刷新
	 */
	public void banRefresh() {
		mRefreshStatus = STATUS_2_BAN_REFRESH;
		mHeaderView.setVisibility(View.GONE);
	}

}
