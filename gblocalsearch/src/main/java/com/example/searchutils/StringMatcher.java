package com.example.searchutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.localsearch.WGResultText;
/**
 * 关键字匹配方法
 * @author lujibeat
 *
 */
public class StringMatcher {
	public static final int  HANZI_TYPE = 0x0001, PINYIN_TYPE = 0x0002, JIANPIN_TYPE = 0x0004;
	public static final int ALL_TYPE = HANZI_TYPE | PINYIN_TYPE | JIANPIN_TYPE;
	public static final int SEARCH_TYPE = HANZI_TYPE | PINYIN_TYPE ;
	String key;
	String target;
	int type;
	WGResultText result = null;
	/**
	 * 
	 * @param key 关键字
	 * @param target 目标字符串
	 * @param type HANZI完全匹配，PINYIN全拼匹配，JIANPIN简拼匹配
	 */
	public StringMatcher(String key, String target, int type){
		this.key = key;
		this.target = target;
		this.type = type;
		matcher();
	}
	
	/**
	 * 匹配方法，按完全匹配，拼音匹配，简拼匹配依次进行，一旦匹配中，就跳出匹配
	 * 结果由result保存
	 */
	private void matcher() {
		// TODO Auto-generated method stub
		key.toLowerCase();
		target.toLowerCase();
		if((type & HANZI_TYPE) == HANZI_TYPE){
			if(target.contains(key)){
				int[] pos = new int[2];
				char[] a = target.toCharArray();
				char[] b = key.toCharArray();
				for(int i = 0; i < a.length; i++){
					for(int j = 0; j < b.length; j++){
						if(b[j] != a[i+j] || (i+b.length)>a.length){
							break;
						}
						if(j == b.length - 1){
							pos[0] = i;
							pos[1] = b.length;
							result = new WGResultText(target, pos);
							return;
						}
					}	
				}	
			}
		}
		if((type & PINYIN_TYPE) == PINYIN_TYPE){
			String convertKey = "";
			Map<int[], String> convertKeys = HanziToPinyin.getStringPinyin(key);
			for(Map.Entry<int[], String> entry : convertKeys.entrySet()){
				convertKey = entry.getValue();
				break;
			}
			Map<int[], String> allPinyin = HanziToPinyin.getStringPinyin(target);
			for(Map.Entry<int[], String> entry : allPinyin.entrySet()){
				int[] guide = entry.getKey();
				String content = entry.getValue();
				if(content.contains(convertKey)){
					int[] pos = new int[2];
					char[] a = target.toCharArray();
					char[] b = key.toCharArray();
					for(int i = 0; i < a.length; i++){
						if(!Arrays.asList(guide).contains(i)){
							continue;
						}
						for(int j = 0; j < b.length; j++){
							if(b[j] != a[i+j] || (i+j)>a.length){
								break;
							}
							if(j == b.length - 1){
								for(int k = 0; k < guide.length; k++){
									if(i == guide[k]){
										pos[0] = k;
										continue;
									}
									if((i+j) == guide[k] - 1){
										pos[1] = k - pos[0];
										result = new WGResultText(target, pos);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		if((type & JIANPIN_TYPE) == JIANPIN_TYPE){
            List<String> pinyinInitial = HanziToPinyin.getPinyinInitial(key);
            if (pinyinInitial.size() > 0) {
                String convertKey = pinyinInitial.get(0);

                List<String> allJianpin = HanziToPinyin.getPinyinInitial(target);
                for(String content : allJianpin){
                    if(content.contains(convertKey)){
                        int[] pos = new int[2];
                        char[] a = content.toCharArray();
                        char[] b = convertKey.toCharArray();
                        for(int i = 0; i < a.length; i++){
                            for(int j = 0; j < b.length; j++){
                                if(b[j] != a[i+j] || (i+b.length)>a.length){
                                    break;
                                }
                                if(j == b.length - 1){
                                    pos[0] = i;
                                    pos[1] = b.length;
                                    result = new WGResultText(target, pos);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
		}
		return;
	}
	/**
	 * 
	 * @return WGResultText 返回匹配后的结果和高亮位置
	 */
	public WGResultText getResult(){
		return result;
	}

}
