package com.guobi.common.wordpy;

import java.io.UnsupportedEncodingException;
/**
 * 单个汉字转拼音方法
 * @author WINGUO
 *
 */
public class Wordpy {
	private static final byte pinyin[] = {
		0x77, 0x6f, 0x00, 0x6e, 0x69, 0x00, 0x6c, 0x65, 0x00, 0x6c, 0x69, 0x61, 0x6f, 0x00, 0x64, 0x65, 0x00, 0x62, 0x75, 0x00, 0x7a, 0x61, 0x69, 0x00, 0x71, 0x75, 0x00, 0x73, 0x68, 0x69, 0x00, 0x79, 
	    0x69, 0x00, 0x68, 0x61, 0x6f, 0x00, 0x61, 0x00, 0x74, 0x69, 0x61, 0x6e, 0x00, 0x6d, 0x65, 0x69, 0x00, 0x79, 0x6f, 0x75, 0x00, 0x73, 0x68, 0x61, 0x6e, 0x67, 0x00, 0x6c, 0x61, 0x69, 0x00, 0x62, 
	    0x61, 0x00, 0x79, 0x61, 0x6f, 0x00, 0x6a, 0x69, 0x75, 0x00, 0x64, 0x61, 0x6f, 0x00, 0x67, 0x65, 0x00, 0x78, 0x69, 0x61, 0x00, 0x68, 0x75, 0x69, 0x00, 0x68, 0x61, 0x69, 0x00, 0x68, 0x75, 0x61, 
	    0x6e, 0x00, 0x6e, 0x61, 0x00, 0x64, 0x69, 0x61, 0x6e, 0x00, 0x6b, 0x61, 0x6e, 0x00, 0x7a, 0x68, 0x65, 0x00, 0x63, 0x68, 0x69, 0x00, 0x6d, 0x61, 0x00, 0x67, 0x65, 0x69, 0x00, 0x73, 0x68, 0x75, 
	    0x6f, 0x00, 0x6a, 0x69, 0x61, 0x00, 0x67, 0x75, 0x6f, 0x00, 0x78, 0x69, 0x61, 0x6e, 0x67, 0x00, 0x6a, 0x69, 0x6e, 0x00, 0x79, 0x65, 0x00, 0x64, 0x6f, 0x75, 0x00, 0x64, 0x75, 0x00, 0x72, 0x65, 
	    0x6e, 0x00, 0x6d, 0x69, 0x6e, 0x67, 0x00, 0x6e, 0x65, 0x00, 0x77, 0x61, 0x6e, 0x00, 0x64, 0x75, 0x6f, 0x00, 0x68, 0x75, 0x61, 0x00, 0x64, 0x61, 0x00, 0x78, 0x75, 0x65, 0x00, 0x6e, 0x65, 0x6e, 
	    0x67, 0x00, 0x6b, 0x65, 0x00, 0x78, 0x69, 0x61, 0x6e, 0x00, 0x74, 0x61, 0x00, 0x66, 0x61, 0x00, 0x66, 0x61, 0x6e, 0x00, 0x64, 0x65, 0x6e, 0x67, 0x00, 0x6c, 0x61, 0x6f, 0x00, 0x6c, 0x69, 0x00, 
	    0x6b, 0x75, 0x61, 0x69, 0x00, 0x6c, 0x61, 0x00, 0x71, 0x69, 0x00, 0x73, 0x68, 0x75, 0x69, 0x00, 0x78, 0x69, 0x61, 0x6f, 0x00, 0x63, 0x68, 0x75, 0x00, 0x77, 0x65, 0x6e, 0x00, 0x7a, 0x68, 0x69, 
	    0x00, 0x6b, 0x61, 0x69, 0x00, 0x78, 0x69, 0x6e, 0x00, 0x63, 0x68, 0x65, 0x00, 0x68, 0x6f, 0x75, 0x00, 0x7a, 0x69, 0x00, 0x6d, 0x61, 0x69, 0x00, 0x68, 0x65, 0x6e, 0x00, 0x7a, 0x68, 0x61, 0x6f, 
	    0x00, 0x7a, 0x68, 0x75, 0x6f, 0x00, 0x79, 0x61, 0x6e, 0x67, 0x00, 0x77, 0x75, 0x00, 0x62, 0x61, 0x6e, 0x00, 0x6a, 0x69, 0x00, 0x79, 0x6f, 0x6e, 0x67, 0x00, 0x78, 0x69, 0x6e, 0x67, 0x00, 0x68, 
	    0x61, 0x6e, 0x67, 0x00, 0x68, 0x65, 0x00, 0x62, 0x69, 0x65, 0x00, 0x73, 0x68, 0x65, 0x6e, 0x67, 0x00, 0x7a, 0x61, 0x6f, 0x00, 0x7a, 0x68, 0x65, 0x6e, 0x00, 0x67, 0x61, 0x6e, 0x67, 0x00, 0x71, 
	    0x69, 0x6e, 0x67, 0x00, 0x7a, 0x75, 0x6f, 0x00, 0x6a, 0x75, 0x65, 0x00, 0x79, 0x61, 0x00, 0x6a, 0x69, 0x61, 0x6e, 0x00, 0x62, 0x61, 0x6e, 0x67, 0x00, 0x67, 0x61, 0x6e, 0x00, 0x78, 0x69, 0x65, 
	    0x00, 0x74, 0x61, 0x69, 0x00, 0x7a, 0x75, 0x69, 0x00, 0x7a, 0x68, 0x6f, 0x6e, 0x67, 0x00, 0x63, 0x61, 0x69, 0x00, 0x64, 0x69, 0x6e, 0x67, 0x00, 0x77, 0x65, 0x69, 0x00, 0x64, 0x61, 0x69, 0x00, 
	    0x64, 0x6f, 0x6e, 0x67, 0x00, 0x67, 0x65, 0x6e, 0x00, 0x6b, 0x61, 0x6f, 0x00, 0x73, 0x68, 0x6f, 0x75, 0x00, 0x6d, 0x69, 0x61, 0x6e, 0x00, 0x64, 0x69, 0x00, 0x67, 0x6f, 0x6e, 0x67, 0x00, 0x78, 
	    0x69, 0x00, 0x71, 0x69, 0x61, 0x6e, 0x00, 0x6e, 0x69, 0x61, 0x6e, 0x00, 0x6a, 0x69, 0x65, 0x00, 0x7a, 0x6f, 0x75, 0x00, 0x72, 0x61, 0x6e, 0x00, 0x6d, 0x61, 0x6e, 0x67, 0x00, 0x64, 0x75, 0x69, 
	    0x00, 0x74, 0x6f, 0x6e, 0x67, 0x00, 0x61, 0x69, 0x00, 0x6c, 0x69, 0x61, 0x6e, 0x67, 0x00, 0x64, 0x75, 0x61, 0x6e, 0x00, 0x63, 0x69, 0x00, 0x65, 0x72, 0x00, 0x7a, 0x68, 0x6f, 0x75, 0x00, 0x72, 
	    0x69, 0x00, 0x79, 0x75, 0x65, 0x00, 0x6b, 0x6f, 0x6e, 0x67, 0x00, 0x62, 0x69, 0x61, 0x6e, 0x00, 0x66, 0x61, 0x6e, 0x67, 0x00, 0x6a, 0x69, 0x6e, 0x67, 0x00, 0x73, 0x68, 0x61, 0x6f, 0x00, 0x73, 
	    0x69, 0x00, 0x72, 0x61, 0x6e, 0x67, 0x00, 0x77, 0x61, 0x6e, 0x67, 0x00, 0x67, 0x61, 0x69, 0x00, 0x74, 0x69, 0x6e, 0x67, 0x00, 0x72, 0x75, 0x00, 0x7a, 0x68, 0x65, 0x6e, 0x67, 0x00, 0x6a, 0x69, 
	    0x61, 0x6f, 0x00, 0x61, 0x6e, 0x00, 0x73, 0x75, 0x61, 0x6e, 0x00, 0x6c, 0x75, 0x00, 0x6c, 0x69, 0x61, 0x6e, 0x00, 0x79, 0x69, 0x6e, 0x67, 0x00, 0x6d, 0x65, 0x6e, 0x00, 0x73, 0x68, 0x75, 0x00, 
	    0x74, 0x6f, 0x75, 0x00, 0x66, 0x65, 0x6e, 0x00, 0x6e, 0x76, 0x00, 0x63, 0x75, 0x6f, 0x00, 0x73, 0x61, 0x6e, 0x00, 0x66, 0x75, 0x00, 0x7a, 0x68, 0x75, 0x6e, 0x00, 0x63, 0x68, 0x61, 0x6e, 0x67, 
	    0x00, 0x7a, 0x68, 0x61, 0x6e, 0x67, 0x00, 0x70, 0x69, 0x61, 0x6f, 0x00, 0x62, 0x61, 0x6f, 0x00, 0x62, 0x69, 0x00, 0x74, 0x69, 0x00, 0x73, 0x75, 0x6f, 0x00, 0x62, 0x65, 0x6e, 0x00, 0x73, 0x68, 
	    0x65, 0x6e, 0x00, 0x6e, 0x69, 0x6e, 0x00, 0x77, 0x61, 0x69, 0x00, 0x78, 0x69, 0x75, 0x00, 0x67, 0x61, 0x6f, 0x00, 0x63, 0x68, 0x65, 0x6e, 0x67, 0x00, 0x67, 0x75, 0x61, 0x6e, 0x00, 0x62, 0x65, 
	    0x69, 0x00, 0x73, 0x6f, 0x6e, 0x67, 0x00, 0x63, 0x68, 0x61, 0x00, 0x7a, 0x68, 0x75, 0x00, 0x68, 0x75, 0x6f, 0x00, 0x71, 0x69, 0x6e, 0x00, 0x63, 0x68, 0x75, 0x61, 0x6e, 0x67, 0x00, 0x6c, 0x65, 
	    0x69, 0x00, 0x6c, 0x6f, 0x75, 0x00, 0x6e, 0x6f, 0x6e, 0x67, 0x00, 0x6e, 0x61, 0x6e, 0x00, 0x64, 0x61, 0x6e, 0x00, 0x6b, 0x6f, 0x75, 0x00, 0x66, 0x65, 0x69, 0x00, 0x7a, 0x68, 0x61, 0x6e, 0x00, 
	    0x6e, 0x61, 0x6f, 0x00, 0x64, 0x61, 0x6e, 0x67, 0x00, 0x6c, 0x69, 0x75, 0x00, 0x73, 0x68, 0x65, 0x00, 0x63, 0x6f, 0x6e, 0x67, 0x00, 0x79, 0x69, 0x6e, 0x00, 0x79, 0x75, 0x00, 0x73, 0x75, 0x00, 
	    0x73, 0x68, 0x75, 0x6e, 0x00, 0x70, 0x65, 0x69, 0x00, 0x71, 0x75, 0x61, 0x6e, 0x00, 0x70, 0x61, 0x00, 0x62, 0x69, 0x61, 0x6f, 0x00, 0x6c, 0x65, 0x6e, 0x67, 0x00, 0x71, 0x69, 0x75, 0x00, 0x63, 
	    0x68, 0x6f, 0x6e, 0x67, 0x00, 0x6d, 0x61, 0x6e, 0x00, 0x6b, 0x61, 0x00, 0x79, 0x75, 0x61, 0x6e, 0x00, 0x65, 0x6e, 0x00, 0x72, 0x65, 0x00, 0x62, 0x61, 0x69, 0x00, 0x74, 0x75, 0x00, 0x6a, 0x75, 
	    0x00, 0x6d, 0x6f, 0x00, 0x63, 0x68, 0x75, 0x61, 0x6e, 0x00, 0x68, 0x75, 0x61, 0x69, 0x00, 0x73, 0x75, 0x69, 0x00, 0x74, 0x69, 0x61, 0x6f, 0x00, 0x6a, 0x69, 0x61, 0x6e, 0x67, 0x00, 0x78, 0x75, 
	    0x00, 0x70, 0x69, 0x6e, 0x67, 0x00, 0x7a, 0x68, 0x75, 0x61, 0x6e, 0x00, 0x73, 0x68, 0x61, 0x6e, 0x00, 0x66, 0x65, 0x6e, 0x67, 0x00, 0x7a, 0x6f, 0x6e, 0x67, 0x00, 0x67, 0x65, 0x6e, 0x67, 0x00, 
	    0x70, 0x61, 0x69, 0x00, 0x6d, 0x65, 0x6e, 0x67, 0x00, 0x71, 0x75, 0x65, 0x00, 0x70, 0x61, 0x6f, 0x00, 0x7a, 0x68, 0x75, 0x61, 0x6e, 0x67, 0x00, 0x6d, 0x75, 0x00, 0x64, 0x69, 0x61, 0x6f, 0x00, 
	    0x68, 0x75, 0x6e, 0x00, 0x63, 0x61, 0x6e, 0x00, 0x6b, 0x75, 0x6e, 0x00, 0x6c, 0x75, 0x6e, 0x00, 0x6b, 0x75, 0x00, 0x6d, 0x61, 0x6f, 0x00, 0x70, 0x69, 0x61, 0x6e, 0x00, 0x74, 0x65, 0x00, 0x63, 
	    0x68, 0x61, 0x6f, 0x00, 0x79, 0x75, 0x6e, 0x00, 0x78, 0x75, 0x61, 0x6e, 0x00, 0x67, 0x75, 0x61, 0x6e, 0x67, 0x00, 0x71, 0x69, 0x61, 0x6e, 0x67, 0x00, 0x6e, 0x65, 0x69, 0x00, 0x7a, 0x75, 0x00, 
	    0x62, 0x69, 0x6e, 0x67, 0x00, 0x79, 0x61, 0x6e, 0x00, 0x6d, 0x69, 0x00, 0x73, 0x68, 0x61, 0x00, 0x67, 0x75, 0x69, 0x00, 0x73, 0x65, 0x00, 0x68, 0x6f, 0x6e, 0x67, 0x00, 0x6c, 0x69, 0x6e, 0x67, 
	    0x00, 0x6e, 0x61, 0x69, 0x00, 0x70, 0x69, 0x6e, 0x00, 0x71, 0x69, 0x65, 0x00, 0x63, 0x68, 0x75, 0x6e, 0x00, 0x67, 0x75, 0x00, 0x72, 0x6f, 0x6e, 0x67, 0x00, 0x67, 0x75, 0x61, 0x69, 0x00, 0x74, 
	    0x75, 0x69, 0x00, 0x74, 0x75, 0x61, 0x6e, 0x00, 0x68, 0x75, 0x61, 0x6e, 0x67, 0x00, 0x63, 0x75, 0x6e, 0x00, 0x68, 0x75, 0x00, 0x68, 0x65, 0x69, 0x00, 0x70, 0x61, 0x6e, 0x00, 0x74, 0x61, 0x6e, 
	    0x67, 0x00, 0x63, 0x68, 0x6f, 0x75, 0x00, 0x6e, 0x69, 0x75, 0x00, 0x68, 0x61, 0x6e, 0x00, 0x6c, 0x75, 0x61, 0x6e, 0x00, 0x6c, 0x69, 0x6e, 0x00, 0x74, 0x61, 0x6e, 0x00, 0x6e, 0x69, 0x61, 0x6f, 
	    0x00, 0x6e, 0x69, 0x6e, 0x67, 0x00, 0x6b, 0x61, 0x6e, 0x67, 0x00, 0x67, 0x75, 0x61, 0x00, 0x6b, 0x75, 0x61, 0x6e, 0x00, 0x73, 0x61, 0x69, 0x00, 0x71, 0x75, 0x6e, 0x00, 0x65, 0x00, 0x6c, 0x6f, 
	    0x6e, 0x67, 0x00, 0x72, 0x6f, 0x75, 0x00, 0x74, 0x61, 0x6f, 0x00, 0x63, 0x68, 0x75, 0x69, 0x00, 0x62, 0x6f, 0x00, 0x73, 0x68, 0x75, 0x61, 0x6e, 0x67, 0x00, 0x63, 0x68, 0x65, 0x6e, 0x00, 0x63, 
	    0x68, 0x61, 0x6e, 0x00, 0x6e, 0x75, 0x61, 0x6e, 0x00, 0x6c, 0x75, 0x6f, 0x00, 0x6c, 0x61, 0x6e, 0x67, 0x00, 0x67, 0x6f, 0x75, 0x00, 0x63, 0x65, 0x00, 0x6e, 0x69, 0x61, 0x6e, 0x67, 0x00, 0x70, 
	    0x6f, 0x00, 0x63, 0x61, 0x6f, 0x00, 0x70, 0x75, 0x00, 0x70, 0x69, 0x00, 0x74, 0x75, 0x6f, 0x00, 0x74, 0x69, 0x65, 0x00, 0x7a, 0x61, 0x00, 0x73, 0x68, 0x75, 0x61, 0x00, 0x64, 0x75, 0x6e, 0x00, 
	    0x6d, 0x69, 0x6e, 0x00, 0x74, 0x65, 0x6e, 0x67, 0x00, 0x63, 0x65, 0x6e, 0x67, 0x00, 0x6a, 0x75, 0x61, 0x6e, 0x00, 0x7a, 0x65, 0x00, 0x72, 0x75, 0x61, 0x6e, 0x00, 0x71, 0x69, 0x61, 0x6f, 0x00, 
	    0x7a, 0x68, 0x75, 0x69, 0x00, 0x72, 0x75, 0x6f, 0x00, 0x73, 0x61, 0x6f, 0x00, 0x63, 0x61, 0x00, 0x6c, 0x61, 0x6e, 0x00, 0x6c, 0x69, 0x65, 0x00, 0x6b, 0x75, 0x61, 0x6e, 0x67, 0x00, 0x63, 0x61, 
	    0x6e, 0x67, 0x00, 0x62, 0x69, 0x6e, 0x00, 0x73, 0x75, 0x6e, 0x00, 0x6c, 0x76, 0x00, 0x72, 0x75, 0x6e, 0x00, 0x6f, 0x75, 0x00, 0x6d, 0x6f, 0x75, 0x00, 0x6d, 0x69, 0x61, 0x6f, 0x00, 0x66, 0x6f, 
	    0x00, 0x6a, 0x75, 0x6e, 0x00, 0x78, 0x75, 0x6e, 0x00, 0x7a, 0x65, 0x6e, 0x67, 0x00, 0x78, 0x69, 0x6f, 0x6e, 0x67, 0x00, 0x63, 0x75, 0x00, 0x6e, 0x65, 0x6e, 0x00, 0x63, 0x68, 0x61, 0x69, 0x00, 
	    0x6d, 0x69, 0x65, 0x00, 0x6e, 0x75, 0x00, 0x68, 0x65, 0x6e, 0x67, 0x00, 0x6c, 0x76, 0x65, 0x00, 0x7a, 0x75, 0x61, 0x6e, 0x00, 0x70, 0x65, 0x6e, 0x00, 0x64, 0x69, 0x65, 0x00, 0x73, 0x61, 0x6e, 
	    0x67, 0x00, 0x6f, 0x00, 0x77, 0x61, 0x00, 0x61, 0x6f, 0x00, 0x6e, 0x75, 0x6f, 0x00, 0x6b, 0x75, 0x69, 0x00, 0x73, 0x61, 0x00, 0x70, 0x65, 0x6e, 0x67, 0x00, 0x63, 0x68, 0x75, 0x6f, 0x00, 0x62, 
	    0x65, 0x6e, 0x67, 0x00, 0x67, 0x61, 0x00, 0x63, 0x75, 0x61, 0x6e, 0x00, 0x7a, 0x61, 0x6e, 0x00, 0x6e, 0x76, 0x65, 0x00, 0x63, 0x75, 0x69, 0x00, 0x70, 0x61, 0x6e, 0x67, 0x00, 0x7a, 0x68, 0x61, 
	    0x69, 0x00, 0x6a, 0x69, 0x6f, 0x6e, 0x67, 0x00, 0x7a, 0x68, 0x61, 0x00, 0x6e, 0x69, 0x65, 0x00, 0x71, 0x69, 0x6f, 0x6e, 0x67, 0x00, 0x74, 0x75, 0x6e, 0x00, 0x71, 0x69, 0x61, 0x00, 0x61, 0x6e, 
	    0x67, 0x00, 0x72, 0x75, 0x69, 0x00, 0x7a, 0x61, 0x6e, 0x67, 0x00, 0x63, 0x68, 0x75, 0x61, 0x69, 0x00, 0x73, 0x68, 0x75, 0x61, 0x6e, 0x00, 0x73, 0x65, 0x6e, 0x67, 0x00, 0x6d, 0x69, 0x75, 0x00, 
	    0x6b, 0x75, 0x6f, 0x00, 0x6b, 0x65, 0x6e, 0x67, 0x00, 0x7a, 0x68, 0x75, 0x61, 0x69, 0x00, 0x6b, 0x75, 0x61, 0x00, 0x70, 0x69, 0x65, 0x00, 0x77, 0x65, 0x6e, 0x67, 0x00, 0x64, 0x65, 0x69, 0x00, 
	    0x6d, 0x65, 0x00, 0x73, 0x68, 0x75, 0x61, 0x69, 0x00, 0x7a, 0x65, 0x6e, 0x00, 0x66, 0x6f, 0x75, 0x00, 0x68, 0x61, 0x00, 0x73, 0x68, 0x65, 0x69, 0x00, 0x72, 0x65, 0x6e, 0x67, 0x00, 0x6b, 0x65, 
	    0x6e, 0x00, 0x73, 0x6f, 0x75, 0x00, 0x7a, 0x75, 0x6e, 0x00, 0x7a, 0x68, 0x75, 0x61, 0x00, 0x73, 0x65, 0x6e, 0x00, 0x72, 0x61, 0x6f, 0x00, 0x67, 0x75, 0x6e, 0x00, 0x73, 0x68, 0x61, 0x69, 0x00, 
	    0x64, 0x69, 0x75, 0x00, 0x6c, 0x69, 0x61, 0x00, 0x6e, 0x61, 0x6e, 0x67, 0x00, 0x7a, 0x65, 0x69, 0x00, 0x70, 0x6f, 0x75, 0x00, 0x63, 0x6f, 0x75, 0x00, 0x79, 0x6f, 0x00, 0x64, 0x69, 0x61, 0x00, 
	    0x63, 0x65, 0x6e, 0x00, 0x65, 0x69, 0x00, 0x6e, 0x6f, 0x75, 0x00, 0x7a, 0x68, 0x65, 0x69, 0x00, 0x66, 0x69, 0x61, 0x6f, 0x00, 0x65, 0x6e, 0x67, 0x00, 0x64, 0x65, 0x6e, 0x00, 0x6b, 0x65, 0x69, 
	    0x00, 0x6c, 0x6f, 0x00, 0x63, 0x68, 0x75, 0x61, 0x00, 0x74, 0x65, 0x69, 0x00, 0x6c, 0x65, 0x6e, 0x00, 0x72, 0x75, 0x61, 0x00, 0x6e, 0x75, 0x6e, 0x00, 0x6e, 0x69, 0x61, 0x00, 0x6c, 0x75, 0x65, 
	    0x00, 0x6e, 0x75, 0x65, 0x00, 0x00, 0x00, 0x00, };

	
	
	private int wordPyOffset1;
	private int wordPyOffset2;
	private int wordPyOffset3;
	private int startchar2;
	private int startchar3;
	private int multPyOffset;

	public Wordpy(){
		wordPyOffset1 = Subword1.word1[0]/4;
		wordPyOffset2 = Subword2.word2[0]/4;
		wordPyOffset3 = Subword3.word3[0]/4;
		startchar2 = Subword2.word2[1];
		startchar3 = Subword3.word3[1];
		multPyOffset = Subword3.word3[2]/4;
	}
		
	public String[] GetWordAllPy(int uc){
			int pyCount = 0;
			int multPy = 0;
			int i=0;
			byte [][]pPY = new byte[9][];
			
			if (uc >= 0x4e00 && uc < 0x4e00 + startchar2){
				int ucpy = Subword1.word1[wordPyOffset1 + uc - 0x4e00];
				short pyPos = (short)(ucpy >> 20);
				pyPos &= 0xfff;
				for(i=0; pinyin[pyPos+i]!=0; i++){
				}
				byte[] pPYstr = new byte[i];
				for(i=0; pinyin[pyPos+i]!=0; i++){
					pPYstr[i] = pinyin[pyPos+i];
				}
				//pPYstr[i] = pinyin[pyPos+i];
				pPY[pyCount++] = pPYstr;
				multPy = ucpy & ((1 << 20) - 1);
			}
			else if(uc >= 0x4e00 + startchar2 && uc < 0x4e00 + startchar3){
				int ucpy = Subword2.word2[wordPyOffset2 + uc - 0x4e00 - startchar2];
				short pyPos = (short)(ucpy >> 20);
				pyPos &= 0xfff;
				
				for(i=0; pinyin[pyPos+i]!=0; i++){
				}
				byte[] pPYstr = new byte[i];
				for(i=0; pinyin[pyPos+i]!=0; i++){
					pPYstr[i] = pinyin[pyPos+i];
				}
				//pPYstr[i] = pinyin[pyPos+i];
				pPY[pyCount++] = pPYstr;
				
				multPy = ucpy & ((1 << 20) - 1);
			}
			else if(uc >= 0x4e00 + startchar3 && uc < 0x4e00 + 20902){
				int ucpy = Subword3.word3[wordPyOffset3 + uc - 0x4e00  - startchar3];
				short pyPos = (short)(ucpy >> 20);
				pyPos &= 0xfff;
				
				for(i=0; pinyin[pyPos+i]!=0; i++){
				}
				byte[] pPYstr = new byte[i];
				for(i=0; pinyin[pyPos+i]!=0; i++){
					pPYstr[i] = pinyin[pyPos+i];
				}
				//pPYstr[i] = pinyin[pyPos+i];
				pPY[pyCount++] = pPYstr;
				
				multPy = ucpy & ((1 << 20) - 1);
			}
			if (multPy > 0){
				int pyPos = 0;
				while (true)
				{
					int multpyint = Subword3.word3[multPyOffset+multPy/2];
					multpyint = multpyint >> (((multPy%2)==0)? 0 : 16);
					short multpy = (short)(multpyint & 0xffff);
					int isCountinue = multpy & 0x8000;
					pyPos = (short)(multpy & 0x7fff);
					
					for(i=0; pinyin[pyPos+i]!=0; i++){
					}
					byte[] pPYstr = new byte[i];
					for(i=0; pinyin[pyPos+i]!=0; i++){
						pPYstr[i] = pinyin[pyPos+i];
					}
					//pPYstr[i] = pinyin[pyPos+i];
					pPY[pyCount++] = pPYstr;
					
					multPy++;
					if (isCountinue == 0){
						break;
					}
				}
			}
			String[] str = new String[pyCount];
			for(i=0; i < pyCount; i++){
				str[i] = new String(pPY[i]);
			}
			return str;
		}
	
	/**
	 * 汉字转拼音方法
	 * @param word 汉字字符串
	 * @return 拼音字符串，多音字数组
	 */
	public String[] GetWordAllPy(String word){
		int code = wordDecode(word);
		return GetWordAllPy(code);
	}
	
	public static boolean IsHanZi(String word){
		int code = wordDecode(word);
		if(code >= 0x4e00 && code < 0x4e00 + 20902){
			return true;
		}
		return false;
	}
	/**
	 * 返回一个汉字的拼音
	 * @param word 一个汉字
	 * @param idx 拼音的索引(常规情况下值是 0 即可,多音字才会有多个)
	 * @return
	 */
	public final String getPinyinCode(String word, int idx) {
		int code = wordDecode(word);
		if(code != 0){
			String[] str = GetWordAllPy(code);
			if(str.length > idx){
				return str[idx];
			}
		}
		return null;
	}
	
	/**
	 * 解码函数,将一个字符转化为unicode码
	 * @param word 一个字符
	 * @return 字符对应的unicode码
	 */
	private static int wordDecode(String word) {
		int code = 0;
		if (word != null) {
			//ֻ����һ���ַ�
			if (word.length() == 1) {
				try {
					byte[] bytes = word.getBytes("utf-8");
					if (bytes != null) {
						int len = bytes.length;
						int char1 = bytes[0];
						if (len == 1) {
							code = char1;
						} else if (len == 2) {
							int char2 = bytes[1];
							code = (char2 & 0x3f) | ((char1 & 0x1f) << 6);
						} else if (len == 3) {
							int char2 = bytes[1];
							int char3 = bytes[2];
							code = (char3 & 0x3f) | ((char2 & 0x3f) << 6)
									| ((char1 & 0xf) << 12);
						} else if (len == 4) {
							int char2 = bytes[1];
							int char3 = bytes[2];
							int char4 = bytes[3];
							code = (char4 & 0x3f) | ((char3 & 0x3f) << 6)
									| ((char2 & 0x3f) << 12)
									| ((char1 & 0x7) << 18);

						} else if (len == 5) {
							int char2 = bytes[1];
							int char3 = bytes[2];
							int char4 = bytes[3];
							int char5 = bytes[4];
							code = (char5 & 0x3f) | ((char4 & 0x3f) << 6)
									| ((char3 & 0x3f) << 12)
									| ((char2 & 0x3f) << 18)
									| ((char1 & 0x3) << 24);

						} else if (len == 6) {
							int char2 = bytes[1];
							int char3 = bytes[2];
							int char4 = bytes[3];
							int char5 = bytes[4];
							int char6 = bytes[5];
							code = (char6 & 0x3f) | ((char5 & 0x3f) << 6)
									| ((char4 & 0x3f) << 12)
									| ((char3 & 0x3f) << 18)
									| ((char2 & 0x3f) << 24)
									| ((char1 & 0x1) << 30);
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return code;
	}
}


