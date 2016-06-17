package Engine;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Jakub on 2016-06-16.
 */
public class FlyOutContainer extends LinearLayout
{
    private View content;
    private View menu;

    protected final static int MENU_MARGIN = 150;
    protected int CURR_OFFSET = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED;
    private int menuAnimationDuration = 100;
    private int menuAnimationPollingInterval = 16;

    public FlyOutContainer(Context context) {
        super(context);
    }

    public FlyOutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlyOutContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public enum MenuState
    {
        CLOSED, OPEN, CLOSING, OPENING
    }

    protected Scroller menuAnimationScroller = new Scroller(this.getContext(), new SmoothInterpolator());
    protected Runnable menuAnimationRunnable = new AnimationRunnable();
    private Handler menuAnimationHandler = new Handler();

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);
        this.menu.setVisibility(View.GONE);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if(changed)
            calculateChildDimensions();

        this.menu.layout(left, top, right - MENU_MARGIN, bottom);
        this.content.layout(left + CURR_OFFSET, top, right + CURR_OFFSET, bottom);

    }

    public void toggleMenu()
    {
        switch (this.menuCurrentState)
        {
            case CLOSED:
                this.menuCurrentState = MenuState.OPENING;
                this.menu.setVisibility(View.VISIBLE);
                this.menuAnimationScroller.startScroll(0, 0, this.getMenuWidth(),
                        0, menuAnimationDuration);
                break;

            case OPEN:
                this.menuCurrentState = MenuState.CLOSING;
                this.menuAnimationScroller.startScroll(this.CURR_OFFSET, 0, -this.CURR_OFFSET,
                        menuAnimationDuration);
                break;

            default:
                return;
        }

        this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable,
                menuAnimationPollingInterval);
        this.invalidate();
    }

    private int getMenuWidth()
    {
        return this.menu.getLayoutParams().width;
    }

    private void calculateChildDimensions()
    {
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();
        this.menu.getLayoutParams().height = this.getHeight();
        this.menu.getLayoutParams().width = this.getWidth() - MENU_MARGIN;
    }

    public void adjustContentPosition(boolean isAnimationOngoing)
    {
        int ScrollerOffset = this.menuAnimationScroller.getCurrX();
        this.content.offsetLeftAndRight(ScrollerOffset - this.CURR_OFFSET);
        this.CURR_OFFSET = ScrollerOffset;
        this.invalidate();

        if(isAnimationOngoing)
            this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable, menuAnimationPollingInterval);

        else
            this.onMenuTransitionComplete();
    }

    private void onMenuTransitionComplete()
    {
        switch (this.menuCurrentState)
        {
            case OPENING:
                this.menuCurrentState = MenuState.OPEN;
                break;

            case CLOSING:
                this.menuCurrentState = MenuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;

            default:
                return;
        }
    }

    protected class SmoothInterpolator implements Interpolator
    {

        @Override
        public float getInterpolation(float input)
        {
            return (float) Math.pow(input - 1, 5) + 1;
        }
    }

    protected class AnimationRunnable implements Runnable
    {
        @Override
        public void run()
        {
            boolean isAnimationOngoing = FlyOutContainer.this.menuAnimationScroller.computeScrollOffset();
            FlyOutContainer.this.adjustContentPosition(isAnimationOngoing);
        }
    }
}
