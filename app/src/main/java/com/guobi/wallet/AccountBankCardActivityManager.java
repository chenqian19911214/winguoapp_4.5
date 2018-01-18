package com.guobi.wallet;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.guobi.account.WinguoAccountBank;
import com.guobi.account.WinguoAccountBankCard;
import com.winguo.R;

/**
 * 银行卡Activity管理
 */
public class AccountBankCardActivityManager {
    private Object mStackLock = new Object();
    private static Stack<Activity> activityStack;
    private static AccountBankCardActivityManager instance;

    private final ArrayList<WinguoAccountBank> banks = new ArrayList<WinguoAccountBank>();
    private final ArrayList<WinguoAccountBankCard> cards = new ArrayList<WinguoAccountBankCard>();

    private AccountBankCardActivityManager() {
    }

    public static AccountBankCardActivityManager getInstance() {
        if (instance == null) {
            instance = new AccountBankCardActivityManager();
        }
        return instance;
    }

    /**
     * 移除栈顶的activity
     */
    public void popActivity() {
        synchronized (mStackLock) {
            try {
                Activity activity = activityStack.peek();
                activityStack.pop();
                if (activity != null) {
                    activity.finish();
                }
            } catch (EmptyStackException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除并结束指定的activity
     */
    public void popActivity(Activity activity) {
        synchronized (mStackLock) {
            if (activityStack != null) {
                activityStack.remove(activity);
            }
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 没有拦截back键，所以当activity结束时再做一次移除
     * 移除指定的activity
     */
    public void removeActivity(Activity activity) {
        synchronized (mStackLock) {
            if (activityStack != null) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 移除指定的activity
     */
    public void popActivity(Class<?> activityClass) {
        synchronized (mStackLock) {
            try {
                int size = activityStack.size();
                for (int i = 0; i < size; i++) {
                    Activity activity = activityStack.get(i);
                    if (activity != null && activity.getClass().equals(activityClass)) {
                        popActivity(activity);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当前栈顶的activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        synchronized (mStackLock) {
            try {
                activity = activityStack.lastElement();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        return activity;
    }

    /**
     * 压入栈顶
     */
    public void pushActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        synchronized (mStackLock) {
            if (activityStack == null) {
                activityStack = new Stack<Activity>();
            }
            activityStack.push(activity);
        }
    }

    /**
     * 清空栈中除当前activity之外的所有activity
     */
    public void popActivityExcept(Class<?> activityClass) {
        synchronized (mStackLock) {
            if (activityStack == null) {
                return;
            }
            try {
                Iterator<Activity> iterator = activityStack.iterator();
                while (iterator.hasNext()) {
                    Activity activity = iterator.next();
                    if (activity.getClass().equals(activityClass)) {
                        continue;
                    }
                    iterator.remove();
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空栈中所有activity
     */
    public void popAllActivity() {
        synchronized (mStackLock) {
            if (activityStack == null) {
                return;
            }
            try {
                Iterator<Activity> iterator = activityStack.iterator();
                while (iterator.hasNext()) {
                    Activity activity = iterator.next();
                    iterator.remove();
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查看是否已存在activity
     */
    public boolean activityCreated(Class<?> activityClass) {
        synchronized (mStackLock) {
            if (activityStack == null) {
                return false;
            }
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity != null) {
                    if (activity.getClass().equals(activityClass)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public int getActivityCount() {
        synchronized (mStackLock) {
            if (activityStack != null) {
                return activityStack.size();
            }
            return 0;
        }
    }

    /**
     * 跳转到指定activity
     */
    public void jumpToActivity(Context context, Class<?> activityClass) {
        boolean activityCreated = activityCreated(activityClass);
        if (activityCreated) {
            popActivityExcept(activityClass);
        } else {

            try {
                Intent intent = new Intent(context, activityClass);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Toast mToast;

    public void toast(Context applicationContext, CharSequence msg) {
        if (msg == null) {
            return;
        }
        try {
            if (mToast == null) {
                mToast = new Toast(applicationContext);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setView(Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).getView());
            }
            mToast.setText(msg);
            mToast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void toast(Context applicationContext, int id) {
        try {
            if (mToast == null) {
                mToast = new Toast(applicationContext);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.setView(Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).getView());
            }
            mToast.setText(id);
            mToast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStatusLockErr(Context context, boolean finishSelf) {
        try {
            toast(context.getApplicationContext(), R.string.hybrid4_account_bankcard_status_lock);
            if (finishSelf) {
                popActivity(context.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        mToast = null;
        popAllActivity();
        clearDatas();
    }

    public void clearDatas() {
        banks.clear();
        cards.clear();
    }

    public ArrayList<WinguoAccountBank> getBanks() {
        return banks;
    }

    public ArrayList<WinguoAccountBankCard> getCards() {
        return cards;
    }

}
