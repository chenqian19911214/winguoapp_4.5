package com.winguo.mine.address.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hcpai
 * @desc 镇
 */
public class AddressTownBean  implements Serializable {
    @Override
    public String toString() {
        return "AddressTownBean{" +
                "town=" + town +
                '}';
    }

    private TownBean town;

    public TownBean getTown() {
        return town;
    }

    public void setTown(TownBean town) {
        this.town = town;
    }

    public static class TownBean implements Serializable {
        @Override
        public String toString() {
            return "TownBean{" +
                    "item=" + item +
                    '}';
        }

        private List<AddressInfoBean> item;

        public List<AddressInfoBean> getItem() {
            return item;
        }

        public void setItem(List<AddressInfoBean> item) {
            this.item = item;
        }
    }
}
