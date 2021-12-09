package com.example.email_assignment.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class EditTextWithScrollView extends androidx.appcompat.widget.AppCompatEditText {

    //The maximum boundary of the slip distance
    private int mOffsetHeight;

    //A sign indicating whether it has reached the top or bottom
    private boolean mBottomFlag = false;
    private boolean mCanVerticalScroll;

    public EditTextWithScrollView(Context context) {
        super(context);
        init();
    }

    public EditTextWithScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCanVerticalScroll = canVerticalScroll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            //If it is a new press down event, mBottomFlag is reinitialized
            mBottomFlag = false;
        //If the event has already been cancelled, then the cancellation signal, here is not very useful
        if (mBottomFlag)
            event.setAction(MotionEvent.ACTION_CANCEL);

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if (mCanVerticalScroll) {
            //Intercept again if it is needed, and this method will be called again after the onScrollChanged method
            if (!mBottomFlag)
                getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return result;
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (vert == mOffsetHeight || vert == 0) {
            //This triggers a sliding event for the parent or grandfather layout
            getParent().requestDisallowInterceptTouchEvent(false);
            mBottomFlag = true;
        }
    }

    /**
     * Whether vertical scrolling is possible
     *
     * @return true：can   false：cant
     */
    private boolean canVerticalScroll() {
        //Rolling distance
        int scrollY = getScrollY();
        //Total height of the content of the control
        int scrollRange = getLayout().getHeight();
        //Control the actual height displayed
        int scrollExtent = getHeight() - getCompoundPaddingTop() - getCompoundPaddingBottom();
        //Control the difference between the total height of the content and the actual display height
        mOffsetHeight = scrollRange - scrollExtent;

        if (mOffsetHeight == 0) {

            return false;
        }

        return (scrollY > 0) || (scrollY < mOffsetHeight - 1);
    }
}