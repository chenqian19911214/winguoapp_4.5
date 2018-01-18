package com.guobi.gfc.VoiceFun.jcseg.util;

import java.io.InputStream;

public class Resource {

    public static String PROPERTY_FILE = "jcseg.properties";

    public static String DICTIONARY_FILE = "dic_clas.ser1";
    public static String DICTIONARY_FILE0 = "dic_clas.ser0";
    //public static String DICTIONARY_FILE = "dic_scws.ser1";

    // public static String DICTIONARY_FILE = "dic2.ser1";

    public static InputStream getPropertyStream() throws Exception {
        return Resource.class.getResource("/" + PROPERTY_FILE).openStream();

    }

    public static InputStream getDictionaryStream() throws Exception {
        return Resource.class.getResource("/" + DICTIONARY_FILE).openStream();
    }

    public static InputStream getDictionaryStream0() throws Exception {
        return Resource.class.getResource("/" + DICTIONARY_FILE0).openStream();
    }

    public static String getDictionaryFile() {
        return DICTIONARY_FILE;
    }

    public static String getDictionaryFile0() {
        return DICTIONARY_FILE0;
    }

    public static String getLexPath() {
        return "/";
    }

}
