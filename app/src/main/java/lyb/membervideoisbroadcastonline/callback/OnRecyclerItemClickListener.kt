package lyb.membervideoisbroadcastonline.callback

import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by 刘远博 on 2016/12/19.
 */

abstract class OnRecyclerItemClickListener(private val recyclerView: RecyclerView) : RecyclerView.SimpleOnItemTouchListener() {
    private val mGestureDetector: GestureDetectorCompat

    init {
        mGestureDetector = GestureDetectorCompat(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    val vh = recyclerView.getChildViewHolder(childView)
                    onItemClick(vh)
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    val vh = recyclerView.getChildViewHolder(childView)
                    onItemLongClick(vh)
                }
            }
        })
    }

    //点击事件交给mGestureDetector处理
    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(e)
        return false
    }

    //点击回掉
    abstract fun onItemClick(vh: RecyclerView.ViewHolder)

    //长按监听
    abstract fun onItemLongClick(vh: RecyclerView.ViewHolder)
}
