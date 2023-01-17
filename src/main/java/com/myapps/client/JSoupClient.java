package com.myapps.client;

import com.myapps.common.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JSoupClient {

    public static Document fetchDocument(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .userAgent(Constants.USER_AGENT)
                .get();
        //System.out.println(document.title());
        return document;
    }

}
