package com.winguo.productList.bean;

import com.winguo.productList.bean.ValueEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class ValuesEntity implements Serializable{
    public List<ValueEntity> value;


    @Override
    public String toString() {
        return "ValuesEntity{" +
                "value=" + value +
                '}';
    }
}
