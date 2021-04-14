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
import java.util.stream.Collectors;

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
    public static List<GoodList> parseSex(String keyword) throws Exception {
        String url = "https://www.xvideos.com/?k=%E5%B0%91%E5%A6%87";
        Document document = Jsoup.parse(new URL(url), 30000);
        Element elemet = document.getElementById("content");
        Elements elements = elemet.getElementsByTag("div");

        ArrayList<GoodList> list = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-src");
            String price = "9.9";
            String title = el.getElementsByClass("title").eq(0).text() + "日本";
            if ( !img.equals("")&&!title.equals("日本")) {
                list.add(new GoodList(title, img, price));
            }

        }
        List<GoodList> result = list.stream().distinct().collect(Collectors.toList());

        return list;
    }
}