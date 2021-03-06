/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.winguo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.GBAccountStatus;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.account.WinguoAccountKey;
import com.guobi.account.WinguoEncryption;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.bean.WeixinLoginBean;
import com.winguo.utils.Constants;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler, WinguoAcccountManagerUtils.WeixinLoginCallBack {

	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}


	private static final int RETURN_MSG_TYPE_LOGIN = 1;
	private static final int RETURN_MSG_TYPE_SHARE = 2;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//如果没回调onResp，八成是这句没有写
		StartApp.mWxApi.handleIntent(getIntent(), this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	//app发送消息给微信，处理返回消息的回调
	@Override
	public void onResp(BaseResp resp) {
		//  onResp:-6;;;getType:1
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtil.showToast(this,"微信分享失败");
				else ToastUtil.showToast(this,"微信登录失败");
				break;
			case BaseResp.ErrCode.ERR_OK:
				switch (resp.getType()) {
					case RETURN_MSG_TYPE_LOGIN:

						//拿到了微信返回的code,立马再去请求access_token(我们后台服务器todo)
						final String code = ((SendAuth.Resp) resp).code;
						//就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
						if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
							WinguoAcccountManagerUtils.getKey(this, new WinguoAcccountManagerUtils.IPublicKey() {
								@Override
								public void publicKey(WinguoAccountKey key) {
									//获取所需token UUID
									mkey = key;
									WinguoAcccountManagerUtils.weixinLoginSendCode(getApplicationContext(),key,code,WXEntryActivity.this);
								}

								@Override
								public void publicKeyErrorMsg(int error) {
									ToastUtil.showToast(WXEntryActivity.this,getString(R.string.no_net_or_service_no));
								}
							});
						}else{
							ToastUtil.showToast(this,getString(R.string.no_net));
						}

						break;

					case RETURN_MSG_TYPE_SHARE:
						ToastUtil.showToast(this,"微信分享成功");
						finish();
						break;
				}
				break;
		}
	}
	private WinguoAccountKey mkey = null;

	@Override
	public void weixinLoginCallBack(WeixinLoginBean weixinLoginBean) {
		String hashCommon = "id=" + weixinLoginBean.getName() + "&token=" + mkey.getToken() + "&uuid=" + mkey.getUUID();
		hashCommon = WinguoEncryption.commonEncryption(hashCommon, mkey);
		WinguoAccountDataMgr.setUserName(this, weixinLoginBean.getName());
		WinguoAccountDataMgr.setKEY(this, mkey.getKey());
		WinguoAccountDataMgr.setTOKEN(this, mkey.getToken());
		WinguoAccountDataMgr.setUUID(this, mkey.getUUID());
		WinguoAccountDataMgr.setHashCommon(this, hashCommon);

		WinguoAcccountManagerUtils.requestWeixinInfo(this, new WinguoAcccountManagerUtils.IGeneralData() {
			@Override
			public void getGeneral(WinguoAccountGeneral winguoAccountGeneral) {
				if (winguoAccountGeneral != null) {
					GBAccountMgr.getInstance().mAccountInfo.winguoGeneral = winguoAccountGeneral;
					GBAccountMgr.getInstance().mAccountInfo.status = GBAccountStatus.usr_logged;
					//登陆成功后发出一条广播
					Intent intent = new Intent(Constants.LOGIN_SUCCESS);
					intent.putExtra("info", winguoAccountGeneral);
					intent.putExtra(Constants.WX_LONING_SUCCESS,Constants.WX_LONING_SUCCESS);
					sendBroadcast(intent);
				}
			}

			@Override
			public void generalDataErrorMsg(int message) {
				// LoadDialog.dismiss(mContext);
				//  noNetView.setVisibility(View.VISIBLE);
				ToastUtil.showToast(WXEntryActivity.this,getString(R.string.no_net_or_service_no));
			}
		});


	}

	@Override
	public void weixinLoginCallBackErrorMsg(int message) {
		switch (message) {
			case GBAccountError.REQUST_TIMEOUT:
				ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
				break;
			case GBAccountError.UNKNOWN:
				ToastUtil.showToast(this,"后台数据不稳定，请稍后再试！");
				break;
		}

	}
}
