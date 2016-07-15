package com.areastudio.nwtpdfanalyser;

import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by julien on 27.11.15.
 */
public class LanguageXml {

    public static String importFile(InputStream inputStream, int lang_id) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(inputStream);
        Element root = dom.getDocumentElement();

                    NodeList langs = root.getElementsByTagName("lang");


            ArrayList<Lang> langArray = new ArrayList<Lang>();
            for (int i = 0; i < langs.getLength(); i++) {
                Node langNode = langs.item(i);
                if (Integer.valueOf(langNode.getAttributes().getNamedItem("id").getNodeValue()) == lang_id);
                {

                    Lang lang = new Lang();
                    System.out.println("Lang Text :" + langNode.getAttributes().getNamedItem("text").getNodeValue());
                    lang.setId(Integer.valueOf(langNode.getAttributes().getNamedItem("id").getNodeValue()));
                    lang.setText(langNode.getAttributes().getNamedItem("text").getNodeValue());
                    lang.setCode(langNode.getAttributes().getNamedItem("code").getNodeValue());
                    lang.setIntro(Boolean.valueOf(langNode.getAttributes().getNamedItem("intro").getNodeValue()));
                    lang.setAppendix(Boolean.valueOf(langNode.getAttributes().getNamedItem("intro").getNodeValue()));
                    NodeList properties = langNode.getChildNodes();

                    for (int j = 0; j < properties.getLength(); j++) {
                        Node property = properties.item(j);
                        String name = property.getNodeName();
                        if (name.equalsIgnoreCase("books")) {
                            inputStream.close();
                            return importBook(property);
                        }
                    }
                    langArray.add(lang);
                }
            }
//            Gson gson = new Gson();
//            String json = gson.toJson(langArray);
            inputStream.close();
            return null;

    }

    private static String importBook(Node parent) {
        Map<String, Integer> books = new LinkedHashMap<String, Integer>();

        NodeList pNodes = parent.getChildNodes();
        for (int j = 0; j < pNodes.getLength(); j++) {
            Node p = pNodes.item(j);
            if (p instanceof Text) {
                continue;
            }
            String bookName = p.getAttributes().getNamedItem("text").getNodeValue();
            if (p.getAttributes().getNamedItem("epub") != null){
                bookName += "|" + p.getAttributes().getNamedItem("epub").getNodeValue();
            }
            int chapters = Integer.valueOf(p.getAttributes().getNamedItem("chapters").getNodeValue());
            books.put(bookName, chapters);
        }
        Gson gson = new Gson();
        String json = gson.toJson(books);
        return json;
    }


}
