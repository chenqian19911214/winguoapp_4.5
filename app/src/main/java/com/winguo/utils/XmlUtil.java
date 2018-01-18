package com.winguo.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/11/25.
 * xml转换成json的工具类
 */

public class XmlUtil {
    /**
     * xml转换成json，传入一个输入流
     *
     * @param inputStream 传入一个输入流
     * @return json字符串
     */
    public static String xmlToJson(InputStream inputStream) {
        String xmlContent;
        JSONObject jsonObj = null;
        try {
            xmlContent = StreamToString(inputStream);
            jsonObj = XML.toJSONObject(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * xml转换成json，传入String
     *
     * @param stringXml 要转换的xml，String类型
     * @return json字符串
     */
    public static String xmlToJson(String stringXml) {
        JSONObject jsonObj = null;
        try {
            jsonObj = XML.toJSONObject(stringXml);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String json = jsonObj.toString();

        return json;
    }

    public static String StreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line;
        String content = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        if (is != null && br != null) {
            is.close();
            br.close();
        }
        if (sb != null) {
            content = sb.toString();
        }
        return content;
    }
}
