package cn.itcast.legou.search.controller;

import cn.itcast.legou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController //@Controller + @ResponseBody
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping
    public Map<String,Object> search(@RequestBody Map<String,String> searchMap) {
        Map<String, Object> resultMap = searchService.search(searchMap);
        return resultMap;
    }
}
