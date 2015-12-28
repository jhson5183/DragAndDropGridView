package com.lib.draganddropgridview;

import android.os.Handler;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;

/**
 * drag and drop scrolling control
 * @author Jung Hyun
 *
 */
public class DragScrollControl { 
    private static final int SCROLL_DURATION = 300; 
    private static final int DIR_UP = 1; 
    private static final int DIR_DOWN = 2; 
    private static final int SCROLL_DOWN = 3; 
 
    int targetPosition = AdapterView.INVALID_POSITION; 
    int direction = AdapterView.INVALID_POSITION; 
    int lastSeenPosition = AdapterView.INVALID_POSITION; 
    int localExtraScroll; 
    DropGridView dropGrid;
    int scrollDistance = 0;
 
    public DragScrollControl(DropGridView grid) {
    	dropGrid = grid; 
        localExtraScroll = ViewConfiguration.get(dropGrid.getContext()).getScaledFadingEdgeLength(); 
    } 
 
    Handler mHandler = new Handler(); 
    Runnable mScroller = new Runnable() { 
        public void run() { 
            int firstPos = dropGrid.getFirstVisiblePosition(); 
            switch(direction) { 
            case DIR_UP: { 
 
                final View firstView = dropGrid.getChildAt(0); 
                if (firstView == null) { 
                    return; 
                } 
                final int firstViewTop = firstView.getTop(); 
                final int extraScroll = firstPos > 0 ? localExtraScroll : dropGrid.getPaddingTop(); 
 
                dropGrid.smoothScrollBy(firstViewTop - extraScroll, SCROLL_DURATION); 
 
                break; 
            } 
 
            case DIR_DOWN: { 
                final int lastViewIndex = dropGrid.getChildCount() - 1; 
                final int lastPos = firstPos + lastViewIndex; 
 
                if (lastViewIndex < 0) { 
                    return; 
                } 
 
                final View lastView = dropGrid.getChildAt(lastViewIndex); 
                final int lastViewHeight = lastView.getHeight(); 
                final int lastViewTop = lastView.getTop(); 
                final int lastViewPixelsShowing = dropGrid.getHeight() - lastViewTop; 
                final int extraScroll = lastPos < dropGrid.getAdapter().getCount() - 1 ? localExtraScroll : dropGrid.getPaddingBottom(); 
 
                dropGrid.smoothScrollBy(lastViewHeight - lastViewPixelsShowing + extraScroll, SCROLL_DURATION); 
 
                break; 
            } 
            
            case SCROLL_DOWN: {
            	dropGrid.smoothScrollBy(scrollDistance, SCROLL_DURATION - 100);
            	break;
            }
 
            default: 
                break; 
            } 
        } 
    }; 
    
    /**
     * 스크롤을 올린다.
     * @author Jung Hyun
     */
    public void scrollToUp(){
    	lastSeenPosition = AdapterView.INVALID_POSITION;
    	direction = DIR_UP;
    	mHandler.post(mScroller);
    }
    /**
     * 스크롤을 내린다.
     * @author Jung Hyun
     */
    public void scrollToDown(){
    	lastSeenPosition = AdapterView.INVALID_POSITION;
    	direction = DIR_DOWN;
    	mHandler.post(mScroller);
    }
    /**
     * 스크롤을 정해진 값 만큼 내린다.
     * @param scrollDistance
     */
    public void scrollToDown(int scrollDistance){
    	dropGrid.animationHandler.removeMessages(1);
    	lastSeenPosition = AdapterView.INVALID_POSITION;
    	direction = SCROLL_DOWN;
    	this.scrollDistance = scrollDistance;
    	mHandler.post(mScroller);
    }
} 
