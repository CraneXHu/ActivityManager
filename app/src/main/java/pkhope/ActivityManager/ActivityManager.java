package pkhope.ActivityManager;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by thinkpad on 2015/9/4.
 */
public class ActivityManager {

    private Stack<Activity> mActivityStack;

    public ActivityManager(){
        mActivityStack = new Stack<Activity>();
    }

    public void addActivity(Activity activity){
        mActivityStack.push(activity);
    }

    public void removeActivity(Activity activity){
        mActivityStack.remove(activity);
    }

    public void removeActivity(){
        mActivityStack.pop();
    }

    public Activity getActivity(int index){
        return mActivityStack.get(index);
    }

    public int getCount(){
        return mActivityStack.size();
    }

    public void finishAll(){
        for(Activity activity : mActivityStack){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    public void clear(){
        mActivityStack.clear();
    }
}
