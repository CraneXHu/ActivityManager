package pkhope.ActivityManager;

import android.app.Activity;
import android.os.Bundle;
//import android.util.DisplayMetrics;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by thinkpad on 2015/9/9.
 */
public class BaseActivity extends Activity {

    private static ActivityManager mActivityManager = new ActivityManager();
    private float mWndWidth;
    private float mWndHeight;

    private WindowManager.LayoutParams mLayoutParams;
    private View mWnd;

    private float mOriginX;
    private float mOriginY;

    private int mMode = 0;

    private float mLastDistance;

//    private boolean mScaled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivityManager.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityManager.removeActivity(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mWndHeight = dm.widthPixels;
        mWndWidth = dm.heightPixels;

        mWnd = getWindow().getDecorView();
        mLayoutParams = (WindowManager.LayoutParams) mWnd.getLayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;

//        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        mLayoutParams.width = dm.widthPixels;
        mLayoutParams.height = dm.heightPixels;

        getWindowManager().updateViewLayout(mWnd, mLayoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
 //       super.onTouchEvent(event);

        float currentX = event.getRawX();
        float currentY = event.getRawY();

        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mMode = 1;
                mOriginX = event.getX();
                mOriginY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mMode = 0;
                mOriginX = 0;
                mOriginY = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mMode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mLastDistance = distance(event);
                mMode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mMode >= 2){

                    float newDistance = distance(event);
                    if (newDistance > mLastDistance + 1 ||
                            newDistance < mLastDistance - 1){

                        zoom(newDistance/mLastDistance);

                        mLastDistance = newDistance;
                    }
                }
                else if (mMode == 1){

                    mLayoutParams.x = (int)(currentX - mOriginX);
                    mLayoutParams.y = (int)(currentY - mOriginY);
                    getWindowManager().updateViewLayout(mWnd,mLayoutParams);
                }
                break;
        }

        return true;
    }

    private void zoom(float scale){

        mLayoutParams.width = (int)(mLayoutParams.width * scale);
        mLayoutParams.height = (int)(mLayoutParams.height * scale);

        getWindowManager().updateViewLayout(mWnd, mLayoutParams);
    }

    private float distance(MotionEvent event){

        float dx = event.getX(0) - event.getX(1);
        float dy = event.getY(0) - event.getY(1);
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        return dist;
    }

    public static ActivityManager getActivityManager(){
        return mActivityManager;
    }
}
