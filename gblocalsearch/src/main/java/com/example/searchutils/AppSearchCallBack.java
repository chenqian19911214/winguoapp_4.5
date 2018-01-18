package com.example.searchutils;

import java.util.ArrayList;
import java.util.List;

import com.example.localsearch.LocalAppFinder.AppResult;

public abstract class AppSearchCallBack {
	public abstract void onAppSearchResult(List<AppResult> results);
}
