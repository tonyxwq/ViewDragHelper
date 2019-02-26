package com.rx.viewdraghelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:XWQ
 * Time   2019/2/22
 * Descrition: this is MyDragViewLayout
 */
public class MyDragViewLayout extends ViewGroup
{
    private boolean isShow = false;
    private boolean toRightOrLeft = true;//true to right or false to left
    private int toRigthDistance;
    private ViewDragHelper viewDragHelper;
    private View mTopView;
    private View mLeftView;

    public MyDragViewLayout(Context context)
    {
        this(context, null);
    }

    public MyDragViewLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
        //左边界拖动时 做侧滑栏时可用
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++)
        {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTopView = getChildAt(0);
        mLeftView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom)
    {
        mTopView.layout(0, 0, mTopView.getMeasuredWidth(), mTopView.getMeasuredHeight());
        mLeftView.layout(-mLeftView.getMeasuredWidth(), 0, 0, mLeftView.getMeasuredHeight());
    }

    private void viewReleased()
    {
        if (toRightOrLeft)
        {
            viewDragHelper.smoothSlideViewTo(mLeftView, 0, 0);
            toRightOrLeft = false;
            isShow = true;
        } else
        {
            viewDragHelper.smoothSlideViewTo(mLeftView, -(mLeftView.getMeasuredWidth() + toRigthDistance), 0);
            toRightOrLeft = true;
            isShow = false;
        }
        //中间布局左右 始终不能移动
        //viewDragHelper.smoothSlideViewTo(mTopView,0,0);
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback
    {
        /**
         * View位置变化了。可以在这个回调方法中，修改View的大小，实现QQ的效果。
         *
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy)
        {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * 手指抬起的时候，会释放View。当前手指操作的子view
         *
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel)
        {
            super.onViewReleased(releasedChild, xvel, yvel);
            toRigthDistance = 0;
            if (releasedChild == mLeftView)
            {
                viewReleased();

            } else if (releasedChild == mTopView)
            {
                viewReleased();
            }
            //刷新布局
            invalidate();
        }

        /**
         * 如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent，在onTouchEvent的DOWN的时候就确定了captureView。如
         * 果消耗事件，那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法：
         * getViewHorizontalDragRange和getViewVerticalDragRange，只有这两个方法返回大于0的值才能正常的捕获。
         * ---------------------
         *
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child)
        {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child)
        {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }


        /**
         * 在该方法中对child移动的边界进行控制,left表示即将移动到的位置。比如横向的情况下，我希望只在ViewGroup的内部移动，即：
         * 最小>=paddingleft，最大<=ViewGroup.getWidth()-paddingright-child.getWidth
         *
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx)
        {

            if (child == mTopView)
            {
                return super.clampViewPositionHorizontal(child, left, dx);
            }
            int moveX = left < 0 ? left : 0;
            toRigthDistance = moveX;
            return moveX;
        }

        /**
         * 与上面的类似，控制纵向移动的时候边界的控制
         *
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy)
        {
            return super.clampViewPositionVertical(child, top, dy);
        }

        //根据回调回来的view 用于判断自定义控件的子控件中，那些可以滑动，那些不允许滑动
        @Override
        public boolean tryCaptureView(@NonNull View view, int i)
        {
            if (view == mLeftView)
            {
                return true;

            } else if (view == mTopView)
            {
                if (isShow)
                {
                    return true;

                } else
                {
                    return false;
                }
            }
            return false;
        }

        /**
         * 该方法可以绕过tryCaptureView方法，不管tryCaptureView返回真假。能够在边界拖动还要加上：
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId)
        {
            //super.onEdgeDragStarted(edgeFlags, pointerId);
            if (edgeFlags == ViewDragHelper.EDGE_LEFT)
            {
                viewDragHelper.captureChildView(mLeftView, pointerId);
            }
            Log.d("mmp", "onEdgeDragStarted:");
        }
    }

    /**
     * 添加此方法回弹才可生效
     */
    @Override
    public void computeScroll()
    {
        if (viewDragHelper.continueSettling(true))
        {
            invalidate();
        }
    }

    private float xInterDown;
    private float xInterMove;
    private int distance = -1;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    private boolean mmp = false;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        Log.d("mmp", "======onTouchEvent=========");

        viewDragHelper.processTouchEvent(event);
        return true;

    }
}
