package com.zsp.Controller;


import com.alibaba.fastjson.JSON;
import com.zsp.Service.GoodListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class GoodListController {
    @Autowired
    GoodListService goodListService;

    @GetMapping({"/", "/index"})

    public String inde() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/add/{keyword}")
    public Boolean addData(@PathVariable("keyword") String keyword) {
        try {
            return goodListService.parseGoodList(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @ResponseBody
    @GetMapping("/addSex/{keyword}")
    public Boolean addSexData(@PathVariable("keyword") String keyword) {
        try {
            return goodListService.parseSexList(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @ResponseBody
    @GetMapping("/parse/{keywords}")
    public String getParseJD(@PathVariable("keywords") String keywords) {
        try {
            return JSON.toJSONString(goodListService.parseJD(keywords));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @GetMapping("/search/{keywords}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> getParseJD(@PathVariable("keywords") String keywords,
                                                @PathVariable("pageNo") int pageNo,
                                                @PathVariable("pageSize") int pageSize) throws IOException {

        return goodListService.serchPage(keywords, pageNo, pageSize);

    }

    @ResponseBody
    @GetMapping("/searchh/{keywords}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> searchHighLightJD(@PathVariable("keywords") String keywords,
                                                @PathVariable("pageNo") int pageNo,
                                                @PathVariable("pageSize") int pageSize) throws IOException {

        return goodListService.searchHighLight(keywords, pageNo, pageSize);

    }

}
