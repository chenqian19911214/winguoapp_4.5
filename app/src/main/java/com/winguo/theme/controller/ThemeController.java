package com.winguo.theme.controller;

import android.content.Context;

import com.winguo.theme.IGoodsThemeCallBack;
import com.winguo.theme.modle.GoodsThemesBean;
import com.winguo.theme.modle.ThemeRequestNet;
import com.winguo.theme.view.IGoodsThemeView;

/**
 * Created by Admin on 2017/5/18.
 */

public class ThemeController implements IGoodsThemeCallBack{


    private IGoodsThemeView iGoodsThemeView;
    private final ThemeRequestNet themeRequestNet;

    public ThemeController(IGoodsThemeView iGoodsThemeView) {

        this.iGoodsThemeView = iGoodsThemeView;
        themeRequestNet = new ThemeRequestNet();
    }
    public void getData(Context context,String cateid){
        themeRequestNet.getThemeData(context,cateid,this);
    }

    @Override
    public void getGoodsTheme(GoodsThemesBean goodsThemesBean) {
        iGoodsThemeView.GoodsThemeData(goodsThemesBean);

    }
}
