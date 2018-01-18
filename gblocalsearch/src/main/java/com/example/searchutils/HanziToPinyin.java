package com.example.searchutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.guobi.common.wordpy.Wordpy;
/**
 * 汉字转拼音方法
 * @author lujibeat
 *
 */
public class HanziToPinyin {
	private static Wordpy wordpy = new Wordpy();
	private static Map<int[], String> allResults;
	/**
	 * 
	 * @param words要转的内容
	 * @return Map<int[], String> int数组为String中每个字拼音首字母对应字符串的位置，如“转换” ——>{0，5} “zhuanhuan”，用Map是因为有多音字
	 */
	public static Map<int[], String> getStringPinyin(String words){
		getStringPinyin(words, 0, new int[words.length()], "");
		return allResults;
	}
	/**
	 * 递归方法，每出现多音字就多一个分支
	 * @param str
	 * @param n
	 * @param count
	 * @param result
	 */
	public static void getStringPinyin(String str, int n, int[] count, String result){
		if(n >= str.length()){
			getPinyinResults(count, result);
			return;
		}
		char[] words = str.toCharArray();
		String word = String.valueOf(words[n]);
		if( Wordpy.IsHanZi(word)){
			String[] pinyins = wordpy.GetWordAllPy(word);
			for(String s : pinyins) {
				if(n < str.length() - 1){
					int c = count[n];
					count[n+1] = c + s.length();
				}
				result += s;
				getStringPinyin(str, n+1, count, result);
			}
		}
		else{
			result += word;
			if(n < str.length() - 1){
				int c = count[n];
				count[n+1] = c + 1;
			}
			getStringPinyin(str, n+1, count, result);
		}
		return;
	}
	
	private static Map<int[], String> getPinyinResults(int[] count, String result){
		allResults = new HashMap<int[], String>();
		allResults.put(count, result);
		return allResults;
	}
	/**
	 * 
	 * @param
	 * @return 简拼字符串
	 */
	public static List<String> getPinyinInitial(String words){
		Map<int[], String> allPinyin = HanziToPinyin.getStringPinyin(words);
		List<String> results = new ArrayList<String>();
		for(Map.Entry<int[], String> entry : allPinyin.entrySet()){
			int[] guide = entry.getKey();
			String raw = entry.getValue();
			char[] content = raw.toCharArray();
			char[] charResult = new char[guide.length];
			for(int i = 0; i < guide.length; i++){
				charResult[i] = content[guide[i]];
			}
			String result = String.valueOf(charResult);
			results.add(result);
		}
		return results;
	}
}
