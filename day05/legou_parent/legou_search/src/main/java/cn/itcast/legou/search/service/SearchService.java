package cn.itcast.legou.search.service;

import java.util.Map;

public interface SearchService {
    /**
     *
     * @param searchMap 对搜索条件的封装
     * @return 对搜索结果的封装
     */
    public Map<String,Object> search(Map<String,String> searchMap);
}
