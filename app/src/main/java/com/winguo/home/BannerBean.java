package com.winguo.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/2/8.
 */

public class BannerBean implements Serializable{


    /**
     * items : {"data":[{"id":1484,"image":{"content":"http://g1.imgdev.winguo.com/group1/M00/00/0B/wKgAoViaz0uAHiTmAAIvEeUbksA573.png","modifyTime":1486540619},"url":"http://q.winguo.com/maker_sign?from=","name":"banner1"},{"id":1485,"image":{"content":"http://g1.imgdev.winguo.com/group1/M00/00/0B/wKgAoViaz7aAL3aKAANpacPHhF0485.png","modifyTime":1486540726},"url":"http://m.winguo.com","name":"banner2"},{"id":1486,"image":{"content":"http://g1.imgdev.winguo.com/group1/M00/00/0B/wKgAoViaz8qAF2y8AAN8_MpsKwo299.png","modifyTime":1486540746},"url":"http://m.winguo.com/shop/crystalpurify","name":"banner3"}]}
     */

    public MessageBean message;

    @Override
    public String toString() {
        return "BannerBean{" +
                "message=" + message +
                '}';
    }


}
