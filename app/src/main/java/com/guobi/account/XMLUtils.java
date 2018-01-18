package com.guobi.account;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by chenq on 2017/1/3.
 */
public final class XMLUtils {

    /**
     * 问果常规XML解析方法
     * 内容如下:
     * <?xml version="1.0" encoding="utf-8"?>
     * <message>
     * #解析的内容，以Element的格式返回#
     * </message>
     *
     * @return
     */
    public static final Element getNodeElement(String xmlStr, String nodeTag) {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayInputStream is = new ByteArrayInputStream(xmlStr.getBytes());
            Document doc = db.parse(is);
            NodeList nl = doc.getElementsByTagName(nodeTag);
            Element elt = (Element) nl.item(0);
            return elt;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static final NodeList getNodeList(String xmlStr, String nodeTag) {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayInputStream is = new ByteArrayInputStream(xmlStr.getBytes());
            Document doc = db.parse(is);
            return doc.getElementsByTagName(nodeTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String getSubTagVal(Element elt, String subTagName) {
        Node tagVal = elt.getElementsByTagName(subTagName).item(0);
        if (tagVal != null) {
            Node childNode = tagVal.getFirstChild();
            if (childNode != null) {
                return childNode.getNodeValue();
            }
        }
        return null;
    }
}
