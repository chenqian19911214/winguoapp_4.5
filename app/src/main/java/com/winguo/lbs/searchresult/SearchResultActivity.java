package com.winguo.lbs.searchresult;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guobi.account.NetworkUtils;
import com.winguo.R;
import com.winguo.activity.NearbyStoreHomeActivity;
import com.winguo.activity.ShopDetailActivity;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.SearchHistory;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.lbs.bean.LbsLocationBean;
import com.winguo.lbs.bean.SearchResultGoodsBean;
import com.winguo.lbs.bean.SearchResultShopBean;
import com.winguo.search.nearby.NearbySearchActivity;
import com.winguo.utils.LoadDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hcpai
 * @desc ${TODD}
 */

public class SearchResultActivity extends BaseTitleActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private TextView search_result_all_tv;
    private TextView search_result_number_tv;
    private TextView search_result_price_tv;
    private TextView search_result_type_tv;

    private ImageView search_result_all_iv;
    private ImageView search_result_number_iv;
    private ImageView search_result_price_iv;

    private boolean isOnclickAgainForNumber = false;
    private boolean isOnclickAgainFor_Price = false;
    private View emptyView;
    private View errorView;
    private View successView;
    private RecyclerView mNearby_rv;
    private LbsLocationBean mLbsLocationBean;
    private SearchHistory mSearchHistory;
    private int mPage = 1;
    private SearchResultGoodsAdapter mSearchResultGoodsAdapter;
    private SearchResultShopAdapter mSearchResultShopAdapter;
    private List<SearchResultGoodsBean.ContentBean> mGoodsData;
    private List<SearchResultShopBean.ResultBean> mShopData;
    private int mState = 0;
    private boolean isLoadFail = false;
    private boolean isFirst = true;
    /**
     * 是否为商品搜索
     */
    private boolean isShopSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initDatas();
        initView();
        //店铺搜索
        if (isShopSearch) {
            requestShopData(0);
            search_result_number_tv.setText(getString(R.string.search_result_per_consumption));
            search_result_price_tv.setText(getString(R.string.search_result_distance));
            //初始化适配器
            mSearchResultShopAdapter = new SearchResultShopAdapter(R.layout.nearby_list_item);
            mSearchResultShopAdapter.setOnLoadMoreListener(this, mNearby_rv);
            mSearchResultShopAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            mSearchResultShopAdapter.setLoadMoreView(new CustomLoadMoreView());
        } else {
            //商品搜索
            requestGoodsData(0);
            search_result_number_tv.setText(getString(R.string.search_result_number));
            search_result_price_tv.setText(getString(R.string.search_result_price));

            //初始化适配器
            mSearchResultGoodsAdapter = new SearchResultGoodsAdapter(R.layout.search_result_goods_item);
            mSearchResultGoodsAdapter.setOnLoadMoreListener(this, mNearby_rv);
            mSearchResultGoodsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            mSearchResultGoodsAdapter.setLoadMoreView(new CustomLoadMoreView());
        }

        if (isShopSearch) {
            mSearchResultShopAdapter.setOnItemClickListener(this);
        } else {
            mSearchResultGoodsAdapter.setOnItemClickListener(this);
        }
    }


    private void initView() {
        setBackBtn();
        findViewById(R.id.search_result_ll).setOnClickListener(this);
        search_result_type_tv = (TextView) findViewById(R.id.search_result_type_tv);
        search_result_all_tv = (TextView) findViewById(R.id.search_result_all_tv);
        search_result_all_tv.setOnClickListener(this);
        search_result_number_tv = (TextView) findViewById(R.id.search_result_number_tv);
        search_result_number_tv.setOnClickListener(this);
        search_result_price_tv = (TextView) findViewById(R.id.search_result_price_tv);
        search_result_price_tv.setOnClickListener(this);

        search_result_all_iv = (ImageView) findViewById(R.id.search_result_all_iv);
        search_result_number_iv = (ImageView) findViewById(R.id.search_result_number_iv);
        search_result_price_iv = (ImageView) findViewById(R.id.search_result_price_iv);
        FrameLayout fl_product_container = (FrameLayout) findViewById(R.id.fl_product_container);
        //给容器添加不同布局
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //1、返回数据为空
        emptyView = View.inflate(this, R.layout.loading_empty, null);
        TextView textView = (TextView) emptyView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

        fl_product_container.addView(emptyView, params);

        //2、没有网络或请求失败
        errorView = View.inflate(this, R.layout.no_net, null);
        TextView no_net_tv = (TextView) errorView.findViewById(R.id.no_net_tv);
        errorView.findViewById(R.id.btn_request_net).setOnClickListener(this);
        Drawable drawableTop1 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop1, null, null);
        fl_product_container.addView(errorView, params);

        //3.有数据
        successView = View.inflate(this, R.layout.nearby_rv, null);
        mNearby_rv = (RecyclerView) successView.findViewById(R.id.nearby_rv);
        mNearby_rv.setLayoutManager(new LinearLayoutManager(this));
        mNearby_rv.setItemAnimator(new DefaultItemAnimator());
        fl_product_container.addView(successView, params);

        if (isShopSearch) {
            //搜索店铺为空时
            textView.setText(getString(R.string.store_search_no_shop));
            search_result_type_tv.setText("店铺");
            mShopData = new ArrayList<>();
        } else {
            //搜索商品为空时
            textView.setText(getString(R.string.loading_empty));
            search_result_type_tv.setText("商品");
            mGoodsData = new ArrayList<>();
        }
        //默认选中综合
        changeTitleColor(1);
        //全部状态gone掉
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);

    }

    private void initDatas() {
        mSearchHistory = (SearchHistory) getIntent().getExtras().get(NearbySearchActivity.SEARCHRESULT);
        mLbsLocationBean = (LbsLocationBean) getIntent().getExtras().get(NearbySearchActivity.LOCATION);
        isShopSearch = mSearchHistory.type == 0;
    }

    /**
     * 首次请求实体店数据
     *
     * @param state
     */
    private void requestShopData(int state) {
        if (NetworkUtils.isConnectNet(this)) {
            LoadDialog.show(this);
            SearchResultControl.requestSearchShopResult(mSearchHistory.name, state, mLbsLocationBean.getLongitude(),
                    mLbsLocationBean.getLatitude(), 1, new SearchResultControl.INearbySearchShopResult() {
                        @Override
                        public void onBackWordsList(SearchResultShopBean searchResultShopBean) {
                            if (searchResultShopBean != null) {
                                LoadDialog.dismiss(SearchResultActivity.this);
                                handlerShopResult(searchResultShopBean);
                            } else {
                                LoadDialog.dismiss(SearchResultActivity.this);
                                errorView.setVisibility(View.VISIBLE);
                            }
                        }
                    });

        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void handlerShopResult(SearchResultShopBean searchResultShopBean) {
        successView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        List<SearchResultShopBean.ResultBean> result = searchResultShopBean.getResult();

        if (result != null) {
            mShopData.addAll(result);
            mSearchResultShopAdapter.setData(mShopData);
            if (isFirst) {
                mNearby_rv.setAdapter(mSearchResultShopAdapter);
                isFirst = false;
            } else {
                mSearchResultShopAdapter.notifyDataSetChanged();
            }
            if (searchResultShopBean.getHasmore() == 1) {
                //有更多
                mSearchResultShopAdapter.loadMoreComplete();
            } else {
                //没有更多
                mSearchResultShopAdapter.loadMoreEnd(true);
            }
        } else {
            emptyView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.GONE);
        }


    }

    /**
     * 首次请求商品数据
     *
     * @param state
     */
    private void requestGoodsData(int state) {
        if (NetworkUtils.isConnectNet(this)) {
            LoadDialog.show(this);
            SearchResultControl.requestSearchGoodsResult(mSearchHistory.name, state, mLbsLocationBean.getLongitude(),
                    mLbsLocationBean.getLatitude(), 1, new SearchResultControl.INearbySearchGoodsResult() {
                        @Override
                        public void onBackWordsList(SearchResultGoodsBean searchResultGoodsBean) {
                            LoadDialog.dismiss(SearchResultActivity.this);
                            if (searchResultGoodsBean != null) {
                                handlerGoodsResult(searchResultGoodsBean);
                            } else {
                                errorView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 处理第一次请求回来的数据
     *
     * @param searchResultGoodsBean
     */
    private void handlerGoodsResult(SearchResultGoodsBean searchResultGoodsBean) {
        successView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        int size = searchResultGoodsBean.getSize();
        if (size != 0) {
            mGoodsData.addAll(searchResultGoodsBean.getContent());
            mSearchResultGoodsAdapter.setData(mGoodsData);
            if (isFirst) {
                mNearby_rv.setAdapter(mSearchResultGoodsAdapter);
                isFirst = false;
            } else {
                mSearchResultGoodsAdapter.notifyDataSetChanged();
            }
            if (searchResultGoodsBean.getPage_count() == 1) {
                //有更多
                mSearchResultGoodsAdapter.loadMoreComplete();
            } else {
                //没有更多
                mSearchResultGoodsAdapter.loadMoreEnd(true);
            }
        } else {
            emptyView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.GONE);
        }
    }

    /**
     * 再次请求商品数据
     */
    private void requestGoodsDataAgain(int state) {
        if (NetworkUtils.isConnectNet(this)) {
            SearchResultControl.requestSearchGoodsResult(state, mPage, new SearchResultControl.INearbySearchGoodsResult() {
                @Override
                public void onBackWordsList(SearchResultGoodsBean searchResultGoodsBean) {
                    if (searchResultGoodsBean != null) {
                        handlerGoodsResultAgain(searchResultGoodsBean);
                    } else {
                        mSearchResultGoodsAdapter.loadMoreFail();
                        isLoadFail = true;
                    }
                }
            });
        } else {
            mSearchResultGoodsAdapter.loadMoreFail();
            isLoadFail = true;
        }
    }

    /**
     * 再次店铺数据
     */
    private void requestShopDataAgain(int state) {
        if (NetworkUtils.isConnectNet(this)) {
            SearchResultControl.requestSearchShopResult(state, mPage, new SearchResultControl.INearbySearchShopResult() {
                @Override
                public void onBackWordsList(SearchResultShopBean searchResultShopBean) {
                    if (searchResultShopBean != null) {
                        handlerShopResultAgain(searchResultShopBean);
                    } else {
                        mSearchResultShopAdapter.loadMoreFail();
                        isLoadFail = true;
                    }
                }
            });
        } else {
            if (isShopSearch) {
                mSearchResultShopAdapter.loadMoreFail();
            } else {
                mSearchResultGoodsAdapter.loadMoreFail();
            }
            isLoadFail = true;
        }
    }

    /**
     * 处理再次请求回来的商品数据
     *
     * @param searchResultGoodsBean
     */
    private void handlerGoodsResultAgain(SearchResultGoodsBean searchResultGoodsBean) {
        isLoadFail = false;
        mGoodsData.addAll(searchResultGoodsBean.getContent());
        mSearchResultGoodsAdapter.setData(mGoodsData);
        mSearchResultGoodsAdapter.notifyDataSetChanged();
        if (searchResultGoodsBean.getPage_count() == 1) {
            //有更多
            mSearchResultGoodsAdapter.loadMoreComplete();
        } else {
            //没有更多
            mSearchResultGoodsAdapter.loadMoreEnd(false);
        }
    }

    /**
     * 处理再次请求回来的店铺数据
     *
     * @param searchResultShopBean
     */
    private void handlerShopResultAgain(SearchResultShopBean searchResultShopBean) {
        isLoadFail = false;
        mShopData.addAll(searchResultShopBean.getResult());
        mSearchResultShopAdapter.setData(mShopData);
        mSearchResultShopAdapter.notifyDataSetChanged();
        if (searchResultShopBean.getHasmore() == 1) {
            //有更多
            mSearchResultShopAdapter.loadMoreComplete();
        } else {
            //没有更多
            mSearchResultShopAdapter.loadMoreEnd(false);
        }
    }

    @Override
    public void onClick(View v) {
        mPage = 1;
        if (isShopSearch) {
            mSearchResultShopAdapter.notifyDataSetChanged();
            mShopData.clear();
        } else {
            mGoodsData.clear();
            mSearchResultGoodsAdapter.notifyDataSetChanged();
        }
        switch (v.getId()) {
            //跳转到附近搜索
            case R.id.search_result_ll:
                Intent intent = new Intent();
                intent.setClass(this, NearbySearchActivity.class);
                intent.putExtra(NearbySearchActivity.LOCATION, mLbsLocationBean);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_request_net:
                if (isShopSearch) {
                    requestShopData(mState);
                } else {
                    requestGoodsData(mState);
                }

                break;
            //综合
            case R.id.search_result_all_tv:
                changeTitleColor(1);
                if (isShopSearch) {
                    requestShopData(0);
                } else {
                    requestGoodsData(0);
                }
                break;
            //销量
            case R.id.search_result_number_tv:
                //重复点击
                if (isOnclickAgainForNumber) {
                    if (isShopSearch) {
                        if (mState == 1) {
                            //人均降序
                            requestShopData(2);
                            mState = 2;
                        } else {
                            //人均升序
                            requestShopData(1);
                            mState = 1;
                        }
                    } else {
                        if (mState == 1) {
                            //销量升序
                            requestGoodsData(2);
                            mState = 2;
                        } else {
                            //销量降序
                            requestGoodsData(1);
                            mState = 1;
                        }
                    }
                    //第一次点击
                } else {
                    if (isShopSearch) {
                        requestShopData(1);
                    } else {
                        requestGoodsData(1);
                    }
                    mState = 1;
                }
                changeTitleColor(2);
                break;
            //价格
            case R.id.search_result_price_tv:
                //多次点击
                if (isOnclickAgainFor_Price) {
                    if (isShopSearch) {
                        if (mState == 3) {
                            //距离降序
                            requestShopData(4);
                            mState = 4;
                        } else {
                            //距离升序
                            requestShopData(3);
                            mState = 3;
                        }
                    } else {
                        if (mState == 3) {
                            //价格升序
                            requestGoodsData(4);
                            mState = 4;
                        } else {
                            //价格降序
                            requestGoodsData(3);
                            mState = 3;
                        }
                    }
                    //第一次点击
                } else {
                    if (isShopSearch) {
                        requestShopData(3);
                    } else {
                        requestGoodsData(3);
                    }
                    mState = 3;
                }
                changeTitleColor(3);
                break;
        }
    }

    /**
     * 改变筛选标题颜色
     *
     * @param type
     */
    private void changeTitleColor(int type) {
        if (type == 1) {
            isOnclickAgainForNumber = false;
            isOnclickAgainFor_Price = false;
            search_result_all_tv.setTextColor(getResources().getColor(R.color.nearby_blue));
            search_result_number_tv.setTextColor(getResources().getColor(R.color.black));
            search_result_price_tv.setTextColor(getResources().getColor(R.color.black));
            search_result_all_iv.setVisibility(View.VISIBLE);
            search_result_number_iv.setVisibility(View.GONE);
            search_result_price_iv.setVisibility(View.GONE);
        } else if (type == 2) {
            isOnclickAgainForNumber = true;
            isOnclickAgainFor_Price = false;
            search_result_all_tv.setTextColor(getResources().getColor(R.color.black));
            search_result_number_tv.setTextColor(getResources().getColor(R.color.nearby_blue));
            search_result_price_tv.setTextColor(getResources().getColor(R.color.black));
            search_result_all_iv.setVisibility(View.GONE);
            search_result_number_iv.setVisibility(View.VISIBLE);
            search_result_price_iv.setVisibility(View.GONE);
        } else if (type == 3) {
            isOnclickAgainForNumber = false;
            isOnclickAgainFor_Price = true;
            search_result_all_tv.setTextColor(getResources().getColor(R.color.black));
            search_result_number_tv.setTextColor(getResources().getColor(R.color.black));
            search_result_price_tv.setTextColor(getResources().getColor(R.color.nearby_blue));
            search_result_all_iv.setVisibility(View.GONE);
            search_result_number_iv.setVisibility(View.GONE);
            search_result_price_iv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLoadMoreRequested() {
        if (isLoadFail) {
            --mPage;
        } else {
            ++mPage;
        }
        if (isShopSearch) {
            requestShopDataAgain(mState);
        } else {
            requestGoodsDataAgain(mState);
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        if (isShopSearch) {
            intent.putExtra("store_id", mShopData.get(position).getM_maker_id());
            intent.putExtra("log", mLbsLocationBean.getLongitude());
            intent.putExtra("lat", mLbsLocationBean.getLatitude());
            intent.setClass(this, NearbyStoreHomeActivity.class);
        } else {
            intent.putExtra("store_id", mGoodsData.get(position).getM_item_m_maker_id());
            intent.putExtra("shop_id", mGoodsData.get(position).getId());
            intent.putExtra("sectionType", (Parcelable[]) null);
            intent.putExtra("log", mLbsLocationBean.getLongitude());
            intent.putExtra("lat", mLbsLocationBean.getLatitude());
            intent.setClass(this, ShopDetailActivity.class);
        }
        startActivity(intent);
    }
}
