package com.winguo.search.presenter;


import com.winguo.search.ISearchCallBack;
import com.winguo.search.modle.SearchChars;
import com.winguo.search.view.ISearchView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class WordsListPresenter implements ISearchCallBack {
    private ISearchView searchView;
    private final SearchChars searchChars;

    public WordsListPresenter(ISearchView searchView){

        this.searchView = searchView;
        searchChars = new SearchChars();
    }
    public synchronized void getWordsList(String text){
        if (searchChars!=null){
            searchChars.getData(text,this);
        }
    }

    @Override
    public void onBackwordsList(List<String> items) {
        if (searchView!=null)
        searchView.showWordsList(items);
    }
}
