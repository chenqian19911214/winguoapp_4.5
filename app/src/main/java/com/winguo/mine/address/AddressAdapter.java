package com.winguo.mine.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.mine.address.bean.AddressInfoBean;

import java.util.List;


/**
 * @author hcpai
 * @desc 地址的adapter
 */
public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddressInfoBean> mList;
    private IAddressListener mIAddressListener;

    public AddressAdapter(Context context, List<AddressInfoBean> list) {
        mContext = context;
        mList = list;
    }

    public void setListen(IAddressListener iAddressListener) {
        mIAddressListener = iAddressListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.address_add_item, null);
            viewHolder.address_name_tv = (TextView) convertView.findViewById(R.id.address_name_tv);
            viewHolder.address_item_rl = (RelativeLayout) convertView.findViewById(R.id.address_item_rl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final AddressInfoBean addressInfoBean = mList.get(position);
        viewHolder.address_name_tv.setText(addressInfoBean.getName());
        viewHolder.address_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIAddressListener != null) {
                    mIAddressListener.onClick(addressInfoBean);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView address_name_tv;
        private RelativeLayout address_item_rl;
    }

    public interface IAddressListener {
        void onClick(AddressInfoBean addressInfoBean);
    }
}
