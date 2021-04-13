package com.zsp.utils;

import com.zsp.pojo.GoodList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebParse {
    public static List<GoodList> parseJD(String keyword) throws Exception {
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element elemet = document.getElementById("J_goodsList");
        Elements elements = elemet.getElementsByTag("li");
        ArrayList<GoodList> list = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            list.add(new GoodList(title,img, price ));
        }
        return list;
    }
}