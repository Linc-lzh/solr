package cn.itcast.legou.search.service.impl;

import cn.itcast.legou.pojo.Item;
import cn.itcast.legou.search.service.SearchService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetAndHighlightPage;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //1.1 根据搜索的关键字进行搜索
        //1.1.1 获取搜索的关键字
        String keywords = searchMap.get("keywords"); //约定好前端传递搜索关键字的时候，参数名称keywords
        //1.1.2 根据关键字构建搜索条件
        FacetAndHighlightQuery query = new SimpleFacetAndHighlightQuery(new Criteria("item_title").is(keywords));
        //1.1.3 执行查询
        //queryForFacetAndHighlightPage 可以进行facet查询和高亮查询
        
        //2.1 基于品牌域进行Facet查询
        //2.1.1 设置Facet相关参数
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnField("item_brand");
        facetOptions.addFacetOnField("spec");
        facetOptions.addFacetByRange(new FacetOptions.FieldWithNumericRangeParameters("item_price", 0, 3000, 500));
        query.setFacetOptions(facetOptions);

        //3.1 品牌过滤
        //3.1.1 获取前端传递品牌条件
        String brand = searchMap.get("brand"); //约定，brand是前端传递品牌参数名称
        //3.1.2 判断前端是否传递品牌条件
        if(!StringUtils.isEmpty(brand)) {
            //3.1.3 如果前端传递品牌条件，基于品牌条件，构建过滤条件
            FilterQuery brandFilterQuery = new SimpleFilterQuery(new Criteria("item_brand").is(brand));
            query.addFilterQuery(brandFilterQuery);
        }

        //4.1 规格过滤
        //4.1.1 获取规格的过滤条件 特点：spec_开始
        for (String key : searchMap.keySet()) {
            String value = searchMap.get(key);
            if(key.startsWith("spec_")) {
                 //4.1.2 根据规格的过滤条件，构建FilterQuery
                FilterQuery specFilterQuery = new SimpleFilterQuery(new Criteria(key).is(value));
                query.addFilterQuery(specFilterQuery);
            }
        }


        //5.1 价格区间过滤
        //5.1.1 获取价格区间
        String priceBlock = searchMap.get("price"); //price 约定前端参数
        //5.1.2 判断前端是否传递价格区间
        if(!StringUtils.isEmpty(priceBlock)) {
            //5.1.3 如果存在，根据_进行切割，获取最小值，最大值，基于最小值和最大值构建过滤条件；
            //中文替换成""
            priceBlock = priceBlock.replaceAll("[\u4e00-\u9fa5]", "");
            String[] strings = priceBlock.split("_");
            if(strings.length >= 2) {
                //0_500
                FilterQuery priceFilterQuery = new SimpleFilterQuery(new Criteria("item_price").
                        between(strings[0],strings[1] ));
                query.addFilterQuery(priceFilterQuery);
            }else {
                //3000
                FilterQuery priceFilterQuery = new SimpleFilterQuery(new Criteria("item_price").greaterThanEqual(strings[0]));
                query.addFilterQuery(priceFilterQuery);
            }
        }

        //6.1 排序
        //6.1.1获取排序参数（排序的域，排序的方式）
        String sortField = searchMap.get("sortField");
        String sortType = searchMap.get("sortType");
        //6.1.2 非空判断
        if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortType)) {
            //6.1.3 基于排序域进行排序
            if(sortType.equals("asc")) {
                query.addSort(new Sort(Sort.Direction.ASC,sortField));
            }else {
                query.addSort(new Sort(Sort.Direction.DESC,sortField));
            }

        }

        //7.1 分页
        //7.1.1 获取当前页、每页显示条数
        //7.1.3 如果当前页、每页显示条数为null，需要设置默认 1 , 15
        String currentPage = searchMap.get("currentPage");
        String pageSize = searchMap.get("pageSize");
        //7.1.2 对当前页、每页显示条数进行非空判断
        if(StringUtils.isEmpty(currentPage)) {
            currentPage = "1";
        }

        if(StringUtils.isEmpty(pageSize)) {
            pageSize = "15";
        }

        //7.1.4 设置分页参数
        query.setPageRequest(PageRequest.of(Integer.parseInt(currentPage)-1,Integer.parseInt(pageSize) ));



        //8.1高亮查询
        //8.1.1 设置高亮参数
        HighlightOptions highlightOptions = new HighlightOptions();
        query.setHighlightOptions(highlightOptions);
        //8.1.2 设置高亮的域
        highlightOptions.addField("item_title");
        //8.1.3 设置高亮的前缀、后缀
        highlightOptions.setSimplePrefix("<span style='color:red'>");
        highlightOptions.setSimplePostfix("</span>");

        FacetAndHighlightPage<Item> page = solrTemplate.queryForFacetAndHighlightPage("collection1", query, Item.class);
        //1.1.4 解析查询结果
        List<Item> list = page.getContent(); //当前页数据
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", list);

        //解析facet查询结果
        Page<FacetFieldEntry> brandFacetPage = page.getFacetResultPage("item_brand");
        List<FacetFieldEntry> brandContent = brandFacetPage.getContent();
        List<String> brandList = new ArrayList<>();
        for (FacetFieldEntry facetFieldEntry : brandContent) {
            String brandName = facetFieldEntry.getValue();
            brandList.add(brandName);
        }

        Page<FacetFieldEntry> specFacetPage = page.getFacetResultPage("spec");
        List<FacetFieldEntry> specContent = specFacetPage.getContent();
        List<String> specList = new ArrayList<>();
        for (FacetFieldEntry facetFieldEntry : specContent) {
            String spec = facetFieldEntry.getValue();
            specList.add(spec);
        }

        Page<FacetFieldEntry> priceRangeFacet = page.getRangeFacetResultPage("item_price");
        List<FacetFieldEntry> priceContent = priceRangeFacet.getContent();
        List<String> priceList = new ArrayList<>();
        for (FacetFieldEntry facetFieldEntry : priceContent) {
            String start = facetFieldEntry.getValue();
            String end = String.valueOf(Double.parseDouble(start) + 500);
            String price = start + "_" + end + "元";
            priceList.add(price);
        }
        priceList.add("3000元以上");

        resultMap.put("brandList", brandList);
        resultMap.put("specList", specList2Map(specList));
        resultMap.put("priceList", priceList);

        //总记录数
        long totalElements = page.getTotalElements();
        resultMap.put("total", totalElements);

        //总页数
        long totalPages = page.getTotalPages();
        resultMap.put("totalPages", totalPages);


        //解析高亮结果
        List<HighlightEntry<Item>> highlighted = page.getHighlighted();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            Item item = itemHighlightEntry.getEntity(); //包含高亮文档
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights(); //高亮的数据
            if(highlights != null && highlights.size() >0) {
                HighlightEntry.Highlight highlight = highlights.get(0); //item_title域的高亮数据
                List<String> snipplets = highlight.getSnipplets();
                if(snipplets != null && snipplets.size() > 0) {
                    String title = snipplets.get(0);
                    item.setTitle(title);
                }
            }
        }

        return resultMap;
    }

    /**
     * 将specList转化为Map
     * @param specList
     * @return
     * [
     *         "{\"机身内存\":\"16G\",\"网络\":\"联通4G\"}",
     *         "{\"机身内存\":\"16G\",\"网络\":\"移动4G\"}",
     *         "{\"机身内存\":\"16G\",\"网络\":\"联通3G\"}",
     *         "{\"机身内存\":\"16G\",\"网络\":\"联通2G\"}",
     *         "{\"机身内存\":\"16G\",\"网络\":\"移动3G\"}",
     *         "{\"机身内存\":\"16G\",\"网络\":\"双卡\"}",
     *         "{\"机身内存\":\"16G\",\"网络\":\"电信4G\"}",
     *         "{\"机身内存\":\"64G\",\"网络\":\"电信4G\"}",
     *         "{\"机身内存\":\"64G\",\"网络\":\"移动4G\"}",
     *         "{\"机身内存\":\"32G\",\"网络\":\"联通4G\"}"
     *     ]
     */
   private Map<String,Set<String>> specList2Map(List<String> specList) {
       Map<String,Set<String>> allMap = new HashMap<>();
        //1.迭代specList
       for (String spec : specList) {
           Map<String,String> specMap = JSON.parseObject(spec, Map.class);
           for (String specName : specMap.keySet()) {
               String specOption = specMap.get(specName);
               if(!allMap.containsKey(specName)) {
                   //将specName作为key,将specOption存储到Set集合中作为value存储到allMap
                   Set<String> optionSet = new LinkedHashSet<>();
                   optionSet.add(specOption);
                   allMap.put(specName, optionSet);
               }else {
                   Set<String> optionSet = allMap.get(specName);
                   optionSet.add(specOption);
               }
           }
       }

       return allMap;
   }
}
