package com.winguo.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


import com.winguo.R;
import com.winguo.productList.bean.CityItemBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ProvinceItemBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 城市Picker
 */
public class CityPicker extends LinearLayout {
    private static ProvinceCityBean provinceCityBean;
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private Context context;
    private List<ProvinceItemBean> province_list = new ArrayList<>();
    private HashMap<String, List<CityItemBean>> city_map = new HashMap<>();
    private ArrayList<String> provinceCodes = new ArrayList<>();
    private String city_code_string;
    private String city_string;
    public testCity city;

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
    }

    public CityPicker(Context context) {
        super(context);
        this.context = context;
        initData();
    }

    public void setCity(testCity testCityWbb) {
        this.city = testCityWbb;
    }

    private void initData() {
        if (province_list.size() > 0) {
            province_list.clear();
        }
        if (city_map.size() > 0) {
            city_map.clear();
        }
        List<ProvinceItemBean> provinceItems = provinceCityBean.province.item;
        for (int i = 0; i < provinceItems.size(); i++) {
            province_list.add(provinceItems.get(i));
            List<CityItemBean> cityItemBeen = provinceItems.get(i).cities.item;
            city_map.put(String.valueOf(provinceItems.get(i).code), cityItemBeen);
        }
    }

    // 获取城市信息
    public static void getaddressinfo(ProvinceCityBean provinceCityBean) {
        CityPicker.provinceCityBean = provinceCityBean;
    }

    public List<String> getCity(
            HashMap<String, List<CityItemBean>> cityHashMap, String provicecode) {
        List<String> cityNames = new ArrayList<>();
        List<CityItemBean> cityItemBeen = cityHashMap.get(provicecode);
        for (CityItemBean cityItemBean : cityItemBeen) {
            cityNames.add(cityItemBean.name);
        }
        return cityNames;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        ArrayList<String> provinceNames = new ArrayList<>();
        if (provinceCodes.size() > 0) {
            provinceCodes.clear();
        }
        ArrayList<String> cityNames = new ArrayList<>();
        for (int i = 0; i < province_list.size(); i++) {
            provinceNames.add(province_list.get(i).name);
            provinceCodes.add(String.valueOf(province_list.get(i).code));
        }
        Iterator<Entry<String, List<CityItemBean>>> iterator = city_map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, List<CityItemBean>> entry = iterator.next();
            List<CityItemBean> cityItemBeens = entry.getValue();
            for (CityItemBean cityItemBean : cityItemBeens) {
                cityNames.add(cityItemBean.name);
            }
        }
        provincePicker.setData(provinceNames);
        provincePicker.setDefault(1);
        cityPicker.setData(getCity(city_map, provinceCodes.get(1)));
        cityPicker.setDefault(1);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    //String selectMonth = counyPicker.getSelectedText();
                    //if (selectMonth == null || selectMonth.equals(""))
                    //return;
                    // 城市数组
                    List<String> citys = getCity(city_map, provinceCodes.get(id));
                    cityPicker.setData(citys);
                    //TODO 有些地方是只有一个城市,默认设置为1(港澳台...)
                    cityPicker.setDefault(citys.size() > 1 ? 1 : 0);
                    //counyPicker.setData(citycodeUtil.getCouny(couny_map,
                    //citycodeUtil.getCity_list_code().get(1)));
                    //counyPicker.setDefault(1);
                    //得到市级
                    String shi = cityPicker.getSelectedText();
                    //String quxian=citycodeUtil.getCouny(couny_map,
                    //citycodeUtil.getCity_list_code().get(cityPicker.getSelected())).get(1);
                    //设置 值
                    CityPicker.this.city.cityAll(text, shi);

                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {

                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    //					String selectMonth = counyPicker.getSelectedText();
                    //					if (selectMonth == null || selectMonth.equals(""))
                    //						return;
                    //					counyPicker.setData(citycodeUtil.getCouny(couny_map,
                    //							citycodeUtil.getCity_list_code().get(id)));
                    //					counyPicker.setDefault(1);
                    //得到 区县
                    //					ArrayList<String> quxians=citycodeUtil.getCouny(couny_map,
                    //							citycodeUtil.getCity_list_code().get(id));
                    //					String quxian=quxians.get(1);
                    //设置 值
                    city.cityAll(selectDay, text);

                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
        //		counyPicker.setOnSelectListener(new OnSelectListener() {
        //
        //			@Override
        //			public void endSelect(int id, String text) {
        //				// TODO Auto-generated method stub
        //				if (text.equals("") || text == null)
        //					return;
        //				if (tempCounyIndex != id) {
        //					String selectDay = provincePicker.getSelectedText();
        //					if (selectDay == null || selectDay.equals(""))
        //						return;
        //					String selectMonth = cityPicker.getSelectedText();
        //					if (selectMonth == null || selectMonth.equals(""))
        //						return;
        //
        //					//设置 值
        //					cityWbb.cityAll(selectDay,selectMonth,text);
        //
        //					// 城市数组
        //					city_code_string = citycodeUtil.getCouny_list_code()
        //							.get(id);
        //					int lastDay = Integer.valueOf(counyPicker.getListSize());
        //					if (id > lastDay) {
        //						counyPicker.setDefault(lastDay - 1);
        //					}
        //				}
        //				tempCounyIndex = id;
        //				Message message = new Message();
        //				message.what = REFRESH_VIEW;
        //				handler.sendMessage(message);
        //			}
        //
        //			@Override
        //			public void selecting(int id, String text) {
        //				// TODO Auto-generated method stub
        //
        //			}
        //		});
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        return city_code_string;
    }

    public String getCity_string() {
        city_string = provincePicker.getSelectedText()
                + " " + cityPicker.getSelectedText();
        return city_string;
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }

    public interface testCity {

        public void cityAll(String sheng, String shi);

    }
}
