package com.guobi.account;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.gfc.gbmiscutils.thread.RunnableBus;
import com.winguo.app.StartApp;
import com.winguo.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.guobi.gfc.gbmiscutils.singleton.GBSingletonManager.TAG;

/**
 * 国笔帐号管理类
 * Created by chenq on 2017/1/3.
 */

public final class GBAccountMgr {


    /**
     * 帐号状态改变广播
     */
    public static final String ACTION_ACCOUNT_STATUS_CHANGED =
            GBAccountMgr.class.getSimpleName() + ".ACTION_ACCOUNT_STATUS_CHANGED";

    /**
     * 自动注册客户的初始密码
     */
    public static final String DEFAULT_PWD = "gb168168";


    private static GBAccountMgr _instance;

    private final Context mContext;
    private final Object mStatusLock = new Object();
    private final Object mTaskLock = new Object();
    private final RunnableBus mRBus;
    private OPTask mCurTask;
    private int mResultCode;
    private String mErrMsg;
    public GBAccountInfo mAccountInfo = new GBAccountInfo();

    private GBAccountMgr(Context context) {
        mContext = context;
        mRBus = RunnableBus.getInstance();
    }

    public static final GBAccountMgr createInstance(Context context) {
        if (_instance == null) {
            _instance = new GBAccountMgr(context.getApplicationContext());
        }
        return _instance;
    }

    public static final GBAccountMgr getInstance() {
        return _instance;
    }

    public static final void destroyInstance() {
        if (_instance != null) {
            _instance.trash();
            _instance = null;
        }
    }


    private void trash() {
        stopWorkingTask();
    }


    private void stopWorkingTask() {
        synchronized (mTaskLock) {
            if (mCurTask != null) {
                mCurTask.cancel();
                mCurTask = null;
            }
        }
    }

    private void notifyStatusChanged() {
        mRBus.postMain(new Runnable() {
            @Override
            public final void run() {
                // TODO Auto-generated method stub
                //通知个人钱包 账户发生改变
                Intent intent = new Intent();
                intent.setAction(ACTION_ACCOUNT_STATUS_CHANGED);
                mContext.sendBroadcast(intent);

            }
        });
    }

    /**
     * 启动帐号系统，进行初始化
     *
     * @return
     */
    public final boolean startup() {

        synchronized (mStatusLock) {
            if (mAccountInfo.status != GBAccountStatus.not_startup) {
                if (GBLogUtils.DEBUG) {
                    GBLogUtils.DEBUG_DISPLAY(this, "GBAccount system has been started up, ignore!!");
                }
                return false;
            }

            synchronized (mTaskLock) {
                if (GBLogUtils.DEBUG) {
                    if (mCurTask != null) {
                        throw new IllegalStateException("Wanna Fuck!");
                    }
                }
                new InitTask().start();
            }

            mAccountInfo.status = GBAccountStatus.initializing;
            return true;
        }
    }

    /**
     * 获取当前的基本账户信息
     *
     * @return
     */
    public final GBAccountInfo getAccountInfo() {
        synchronized (mStatusLock) {
            return mAccountInfo.copy();
        }
    }

    /**
     * 取消已经投递的操作
     * 操作被取消后，发出onTaskCanceled
     */
    public final void cancelOp() {
        stopWorkingTask();
    }

    /////////////////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////////////////

    /**
     * 登录
     * 如果现在的状态不是未登录状态, 或游客状态，不能登录
     *
     * @param account
     * @param pwd
     * @param cb
     * @return 返回TRUE表示成功发起登录操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是未登录状态, 或游客状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean login(final String account,
                               final String pwd,
                               final GBAccountCallback cb) {
        return loginImpl(account, pwd, cb, false);
    }

    /**
     * 切换用户登录
     * 如果之前处于登录状态，会自动进行登出操作，然后再进行登录
     * 如果处于未启动或正在初始化状态，不能进行登录
     *
     * @param account
     * @param pwd
     * @param cb
     * @return 返回TRUE表示成功发起登录操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态处于未启动或正在初始化状态，不能进行登录
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean switchLogin(final String account,
                                     final String pwd,
                                     final GBAccountCallback cb) {
        return loginImpl(account, pwd, cb, true);
    }

    private boolean loginImpl(final String account, final String pwd, final GBAccountCallback cb, final boolean forceLogin) {


        boolean needClearAccount = false;

        if (mAccountInfo.status == GBAccountStatus.tourist || mAccountInfo.status == GBAccountStatus.usr_not_logged_in) {
            //肯定OK
        } else {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                if (forceLogin) {
                    needClearAccount = true;// OK
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (mCurTask != null) {
            return false;
        }
        final boolean shouldClear = needClearAccount;
        new WinguoAccountOPTask(cb) {
            @Override
            protected final int doTask() {
                // TODO Auto-generated method stub
                int resultCode = 0;
                while (true) {
                    if (shouldClear) {
                        resultCode = doClear();
                        if (resultCode != 0) {
                            break;
                        }
                    }

                    resultCode = doLogin(account, pwd);
                    if (resultCode != 0) {
                        break;
                    }

                    break;
                }
                return resultCode;
            }
        }.start();


        return true;

    }

    /**
     * 自动登录
     * 需要处于已登录状态
     *
     * @return
     */
    public final boolean autoLogin() {
        synchronized (mStatusLock) {

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(null) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub\
                        WinguoAccountKey winguoAccountKey = WinguoAccountDataMgr.getWinguoAccountKey(mContext);
                        return WinguoAccountImpl.autoLogin(mContext, winguoAccountKey);
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 登出
     * 只有处于登录状态，才可以登出
     *
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean logout(final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        while (true) {
                            resultCode = WinguoAccountImpl.logout(mContext);
                            if (resultCode != 0) {
                                break;
                            }

                            synchronized (mStatusLock) {
                                mAccountInfo.status = GBAccountStatus.usr_not_logged_in;
                                mAccountInfo.winguoGeneral = null;
                                mAccountInfo.winguoBalance = null;
                                mAccountInfo.winguoCurrency = null;
                            }
                            notifyStatusChanged();
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }


    /**
     * 刷新账户信息
     * 只有处于登录状态，才可以刷新账户信息
     * 刷新完毕，用getAccountInfo获取即可
     *
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean refreshAccountInfo(final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = doGetInfo();
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    WinguoAccountKey winguoAccountKey = WinguoAccountDataMgr.getWinguoAccountKey(mContext);
                                    resultCode = WinguoAccountImpl.autoLogin(mContext,winguoAccountKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 获取银行卡列表
     * 只有处于登录状态，才可以刷新银行卡列表
     * 刷新完毕，结果被添加到列表中
     *
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean getBankCardList(final ArrayList<WinguoAccountBankCard> cards, final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.getBankCardList(mContext, cards);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 获取银行卡其余信息，例如完整的卡号和BID
     * 只有处于登录状态，才可以执行此操作
     * 刷新完毕，用信息被填入INFO中
     *
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean getBankCardDetail(
            final WinguoAccountBankCard info, final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.getBankCardDetail(mContext, info.p_bid, info);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 设置（修改）银行卡信息
     * 只有处于登录状态，才可以执行此操作
     *
     * @param info
     * @param cb
     * @return
     */
    public final boolean setBankCardDetail(
            final WinguoAccountBankCard info, final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.setBankCardDetail(mContext, info.p_bid, info);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 添加银行卡
     *
     * @param info
     * @param cb
     * @return
     */
    public final boolean addBankCard(
            final WinguoAccountBankCard info, final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.setBankCardDetail(mContext, null, info);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 删除银行卡
     *
     * @param p_bid
     * @param cb
     * @return
     */
    public final boolean deleteBankCard(
            final String p_bid, final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.deleteBankCard(mContext, p_bid);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 获取银行列表
     * 只有处于登录状态，才可以执行此操作
     *
     * @param pay_method 支付方式: “2”快捷支付（借记卡），“3”快捷支付（信用卡）
     * @param pay_type   支付类型：“0”支付宝支付，“1”网银支付，“2”货到付款，“3”连连支付
     * @param banks      操作结束后，银行信息填入该列表中
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean getBankList(
            final String pay_method,
            final String pay_type,
            final ArrayList<WinguoAccountBank> banks,
            final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.getBankList(mContext,
                                    pay_method, pay_type, banks);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                      //  GBAccountMgr.getInstance().mAccountInfo.banks.addAll(banks);
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 获取省份列表
     *
     * @param provinces 操作结束后，省份信息填入该列表中
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean getProvinceList(
            final ArrayList<WinguoAccountBranchesEntity> provinces,
            final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.getProvinceList(mContext, provinces);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 获取城市列表
     *
     * @param provinces_id 操作结束后，城市信息填入该列表中
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体 结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean getCityList(final String provinces_id,
                                     final ArrayList<WinguoAccountBranchesEntity> cities,
                                     final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.getCityList(mContext, provinces_id, cities);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    WinguoAccountKey winguoAccountKey = WinguoAccountDataMgr.getWinguoAccountKey(mContext);
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, winguoAccountKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 获取支行列表
     *
     * @param bank_id 操作结束后，支行信息填入该列表中
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.现在的状态不是处于登录状态
     * 2.之前已经发起了其他操作，需要等待
     */
    public final boolean getBankBrancheList(
            final String bank_id,
            final String city_id,
            final ArrayList<WinguoAccountBranchesEntity> bankBranches,
            final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                // OK
            } else {
                return false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.getBankBrancheList(mContext, bank_id, city_id, bankBranches);
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 发送验证码
     * 跟账户状态没关系，随时都可以发
     *
     * @param phoneNumber
     * @param usage       表示验证码用途
     *                    “1“ 注册验证,  ”2”忘记登录密码 , “3“忘记支付密码 ,”16”绑定手机
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.之前已经发起了其他操作，需要等待
     */
    public final boolean sendVerificationCode(final String phoneNumber, final String usage, final GBAccountCallback cb) {
        synchronized (mTaskLock) {
            if (mCurTask != null) {
                return false;
            }
            new WinguoAccountOPTask(cb) {
                @Override
                protected final int doTask() {
                    // TODO Auto-generated method stub
                    return WinguoAccountImpl.sendVerificationcode(mContext, phoneNumber, usage);
                }
            }.start();
            return true;
        }
    }

    /**
     * 因为忘记密码，需要重置密码
     * 跟账户状态没关系，随时都可以重置密码
     * 但如果处于登录状态，重置密码后，会重新登录
     *
     * @param account     账户名
     *                    如果您使用手机号码注册或登录，则账户名就是手机号码
     * @param phoneNumber 手机号码
     *                    用于接收验证码
     * @param authCode    验证码，先调用sendVerificationCode（usage="2"）得到
     * @param newPwd      新密码
     * @param cb
     * @return 返回TRUE表示成功发起操作，具体结果由CALLBACK返回
     * 返回FALSE表示失败，原因包括
     * 1.之前已经发起了其他操作，需要等待
     */
    public final boolean resetPwd(final String account,
                                  final String phoneNumber,
                                  final String authCode,
                                  final String newPwd,
                                  final GBAccountCallback cb) {
        synchronized (mStatusLock) {
            final boolean needLogin;
            if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                needLogin = true;
            } else {
                needLogin = false;
            }

            synchronized (mTaskLock) {
                if (mCurTask != null) {
                    return false;
                }
                new WinguoAccountOPTask(cb) {
                    @Override
                    protected final int doTask() {
                        // TODO Auto-generated method stub
                        int resultCode = 0;
                        boolean autoLogined = false;
                        while (true) {
                            resultCode = WinguoAccountImpl.resetPwd(mContext, account, phoneNumber, authCode, newPwd,"2");
                            if (resultCode != 0) {
                                if (autoLogined == false && resultCode == GBAccountError.SESSION_TIMEOUT) {
                                    resultCode = WinguoAccountImpl.autoLogin(mContext, StartApp.mKey);
                                    if (resultCode == 0) {
                                        autoLogined = true;
                                        continue;
                                    }
                                }
                                break;
                            }

                            if (needLogin) {
                                resultCode = WinguoAccountImpl.login(mContext, account, newPwd, StartApp.mKey);
                                if (resultCode != 0) {
                                    break;
                                }
                            }
                            break;
                        }
                        return resultCode;
                    }
                }.start();
            }
            return true;
        }
    }

    /**
     * 判断当前账户是否使用默认密码
     *
     * @return
     */
    public final boolean isUseDeafultPWD() {
        return WinguoAccountDataMgr.getUseDeaultPWD(mContext);
    }

    /**
     * 获取登录状态下的商城跳转地址
     *
     * @param redirectUrl
     * @return
     */
    public final String getLoggedinWinguoSessionUrl(String redirectUrl) {
        synchronized (mStatusLock) {
            if (mAccountInfo.status != GBAccountStatus.usr_logged) {
                return redirectUrl;
            }

            final String baseUrl = WinguoAccountDataMgr.getSessionUrl(mContext);
            if (baseUrl == null || baseUrl.length() <= 0) {
                return redirectUrl;
            }

            try {
                StringBuffer strBuf = new StringBuffer(baseUrl);
                strBuf.append("&WG_SSO_redirect_url=").append(URLEncoder.encode(redirectUrl, "UTF-8"));
                String finalUrl = strBuf.toString();
                if (GBLogUtils.DEBUG) {
                    GBLogUtils.DEBUG_DISPLAY(TAG, "@@@ final WinguoSessionUrl:" + finalUrl);
                }

                return finalUrl;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////////
    // 用户操作任务
    // 通常需要回调，并且不能取消
    //////////////////////////////////////////////////////////////////////

    private abstract class OPTask extends Thread {
        private final GBAccountCallback _callbacks;
        private final Object _cancelLock = new Object();
        private boolean _isCanceled;

        protected OPTask(GBAccountCallback cb) {
            _callbacks = cb;
        }

        public final void cancel() {
            synchronized (_cancelLock) {
                _isCanceled = true;
            }
        }

        protected final boolean isCanceled() {
            synchronized (_cancelLock) {
                return _isCanceled;
            }
        }

        @Override
        public final void run() {
            // TODO Auto-generated method stub
            synchronized (mTaskLock) {
                mCurTask = this;
            }

            final long t = SystemClock.uptimeMillis();
            try {
                if (isCanceled()) {
                    if (GBLogUtils.DEBUG) {
                        GBLogUtils.DEBUG_DISPLAY(this, "OP Task is canceled");
                    }
                    return;
                }
                if (_callbacks != null) {
                    mRBus.postMain(new Runnable() {
                        @Override
                        public final void run() {
                            // TODO Auto-generated method stub
                            _callbacks.onTaskBegin();
                        }
                    });
                }
                if (isCanceled()) {
                    if (GBLogUtils.DEBUG) {
                        GBLogUtils.DEBUG_DISPLAY(this, "OP Task is canceled");
                    }
                    if (_callbacks != null) {
                        mRBus.postMain(new Runnable() {
                            @Override
                            public final void run() {
                                // TODO Auto-generated method stub
                                _callbacks.onTaskCanceled();
                            }
                        });
                    }
                    return;
                }

                mResultCode = doTask();
            } catch (Exception e) {
                e.printStackTrace();
                mResultCode = GBAccountError.UNKNOWN;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                mResultCode = GBAccountError.UNKNOWN;
            } finally {
                synchronized (mTaskLock) {
                    if (mCurTask == this) {
                        mCurTask = null;
                    }
                }
            }

            if (isCanceled()) {
                if (GBLogUtils.DEBUG) {
                    GBLogUtils.DEBUG_DISPLAY(this, "OP Task is canceled");
                }
                if (_callbacks != null) {
                    mRBus.postMain(new Runnable() {
                        @Override
                        public final void run() {
                            // TODO Auto-generated method stub
                            _callbacks.onTaskCanceled();
                        }
                    });
                }
                return;
            }

            if (mResultCode != 0) {
                mErrMsg = GBAccountError.getErrorMsg(mContext, mResultCode);
                if (mErrMsg == null) {
                    mErrMsg = getErrMsg(mResultCode);
                }
            }

            if (_callbacks != null) {
                mRBus.postMain(new Runnable() {
                    @Override
                    public final void run() {
                        // TODO Auto-generated method stub
                        _callbacks.onTaskEnd(mResultCode, mErrMsg);
                    }
                });
            }

            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(this, "OP Task finished"
                        + " in " + (SystemClock.uptimeMillis() - t) + "ms");
            }
        }

        protected abstract int doTask();

        protected abstract String getErrMsg(int code);
    }

    private abstract class WinguoAccountOPTask extends OPTask {
        public WinguoAccountOPTask(GBAccountCallback cb) {
            super(cb);
        }

        protected final String getErrMsg(int code) {
            return WinguoAccountImpl.getErrorMsg(mContext, code);
        }
    }

    //////////////////////////////////////////////////////////////////////
    // 启动任务
    // 进行初始化，并确定账户状态
    //////////////////////////////////////////////////////////////////////

    private final class InitTask extends OPTask {
        public InitTask() {
            super(null);
        }

        @Override
        public final int doTask() {
            // TODO Auto-generated method stub
            if (isCanceled()) {
                return GBAccountError.TASK_CANCELED;
            }
            final long t = SystemClock.uptimeMillis();
            int status;
            String accountName = "";
            byte[] accountIco = null;

            final String usr = WinguoAccountDataMgr.getUserName(mContext);
            if (usr != null) {
                accountName = usr;
                accountIco = WinguoAccountDataMgr.getUserIco(mContext);
                WinguoAccountKey mKey = WinguoAccountDataMgr.getWinguoAccountKey(mContext);

                final int result = WinguoAccountImpl.autoLogin(mContext, mKey);
                if (result == 0) {
                    status = GBAccountStatus.usr_logged;
                } else {
                    status = GBAccountStatus.usr_not_logged_in;
                }
            } else {
                /*
                accountName = MobileUtils.getMobileNumber(mContext);
				if(isCanceled()){
					return;
				}

				if( accountName!=null && accountName.length()>0){
					int result = WinguoAccountImpl.mobileRegister(mContext,accountName,DEFAULT_PWD);
					if(isCanceled()){
						return;
					}

					if( result ==0 ){
						WinguoAccountDataMgr.setUseDeaultPWD(mContext, true);
						result = WinguoAccountImpl.login(mContext, accountName, DEFAULT_PWD);
						if(result==0){
							status=GBAccountStatus.usr_logged;
						}else{
							status=GBAccountStatus.usr_not_logged_in;
						}
					}else{
						if(result==-43){
							status=GBAccountStatus.usr_not_logged_in; // 帐号已存在
						}else{
							status=GBAccountStatus.tourist;
						}
					}
				}else*/
                {
                    status = GBAccountStatus.tourist;
                }
            }

            if (isCanceled()) {
                return GBAccountError.TASK_CANCELED;
            }

            if (status == GBAccountStatus.usr_logged) {
                doGetInfo();
            }

            if (isCanceled()) {
                return GBAccountError.TASK_CANCELED;
            }

            synchronized (mStatusLock) {
                mAccountInfo.status = status;
                mAccountInfo.accountName = accountName;
                mAccountInfo.accountIco = accountIco;
            }

            notifyStatusChanged();

            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(this, "Init Task finished"
                        + " in " + (SystemClock.uptimeMillis() - t) + "ms");
                mAccountInfo.dump();
            }

            return 0;
        }//end run

        @Override
        protected String getErrMsg(int code) {
            // TODO Auto-generated method stub
            return null;
        }
    }// End init task

    /**
     * 执行登出操作，并清除保存的账户名，变为游客状态
     *
     * @return
     */
    public int doClear() {
        final int resultCode = WinguoAccountImpl.logout(mContext, true);
        if (resultCode == 0) {
            synchronized (mStatusLock) {
                mAccountInfo.status = GBAccountStatus.tourist;
                mAccountInfo.accountName = "";
                mAccountInfo.winguoGeneral = null;
                mAccountInfo.winguoBalance = null;
                mAccountInfo.winguoCurrency = null;
            }
            notifyStatusChanged();
        }
        return resultCode;
    }
    public void doClears() {
        //final int resultCode = WinguoAccountImpl.logout(mContext, true);

        synchronized (mStatusLock) {
            mAccountInfo.status = GBAccountStatus.tourist;
            mAccountInfo.accountName = "";
            mAccountInfo.winguoGeneral = null;
            mAccountInfo.winguoBalance = null;
            mAccountInfo.winguoCurrency = null;
        }
        notifyStatusChanged();
        //return resultCode;
    }

    /**
     * 执行登录操作，并获取账户信息
     *
     * @param account
     * @param pwd
     * @return
     */
    private int doLogin(final String account, final String pwd) {
        int resultCode = 0;
        while (true) {
            resultCode = WinguoAccountImpl.login(mContext, account, pwd, StartApp.mKey);
            if (resultCode != 0) {
                break;
            }

            resultCode = doGetInfo();
            if (resultCode != 0) {
                break;
            }
            mAccountInfo.status = GBAccountStatus.usr_logged;
            mAccountInfo.accountName = account;

            notifyStatusChanged();
            break;
        }
        return resultCode;
    }

    private int doGetInfo() {
        int resultCode = 0;
        while (true) {

            WinguoAccountGeneral info = new WinguoAccountGeneral();
            resultCode = WinguoAccountImpl.getGeneralInfo2(mContext, info);
            if (resultCode != 0) {
                break;
            }

            WinguoAccountBalance balance = new WinguoAccountBalance();

            resultCode = WinguoAccountImpl.getBalance2(mContext, balance);

            if (resultCode != 0) {
                break;
            }

            WinguoAccountCurrency currency = new WinguoAccountCurrency();
            resultCode = WinguoAccountImpl.getCurrency2(mContext, currency);
            if (resultCode != 0) {
                break;
            }

            synchronized (mStatusLock) {
                mAccountInfo.winguoGeneral = info;
                mAccountInfo.winguoBalance = balance;
                mAccountInfo.winguoCurrency = currency;
            }

            break;
        }
        return resultCode;
    }
}
