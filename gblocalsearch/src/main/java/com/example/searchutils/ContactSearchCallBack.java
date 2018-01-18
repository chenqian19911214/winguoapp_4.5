package com.example.searchutils;

import java.util.ArrayList;
import java.util.List;

import com.example.localsearch.WGContactResult;

public abstract class ContactSearchCallBack {
	public abstract void onAppSearchResult(List<WGContactResult> results);
}
