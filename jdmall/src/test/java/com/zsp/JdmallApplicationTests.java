package com.zsp;

import com.zsp.pojo.GoodList;
import com.zsp.utils.WebParse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
class JdmallApplicationTests {

    @Test
    void testParse() throws IOException {
        String url="https://search.jd.com/Search?keyword=java&enc=utf-8&pvid=ca87675d1fbf4d04b21483822317da95";
        Document document = Jsoup.parse(new URL(url), 30000);
        Element elemet = document.getElementById("J_goodsList");
        Elements elements = elemet.getElementsByTag("li");

        for (Element el : elements) {
            String img=el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price=el.getElementsByClass("p-price").eq(0).text();
            String title=el.getElementsByClass("p-name").eq(0).text();

            System.out.println("标题是："+title);
            System.out.println("价格为："+price);
            System.out.println("资源地址是："+img);
            System.out.println("=======================================");
        }
    }
    @Test
    void parseSex() throws Exception {
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
        for (GoodList goodList : result) {
            System.out.println("标题是："+goodList.getTitle());
            System.out.println("价格为："+goodList.getPrice());
            System.out.println("资源地址是："+goodList.getImg());
            System.out.println("=======================================");
        }



    }


}
