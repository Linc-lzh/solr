package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestQuery {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testBaseQuery() {
        Query query = new SimpleQuery("item_title:手机");

        query.addFilterQuery(new SimpleFilterQuery(new Criteria("item_brand").is("华为")));
        query.addFilterQuery(new SimpleFilterQuery(new Criteria("item_price").greaterThanEqual(1000)));
        query.addFilterQuery(new SimpleFilterQuery(new Criteria("item_price").lessThanEqual(2000)));

        query.setOffset( (2 - 1) * 10L);
        query.setRows(10);

        query.addSort(new Sort(Sort.Direction.ASC,"item_price"));
        query.addSort(new Sort(Sort.Direction.DESC,"id"));

        ScoredPage<Item> scoredPage = solrTemplate.queryForPage("collection1", query, Item.class);

        long totalElements = scoredPage.getTotalElements(); //满足条件文档数量
        System.out.println("满足条件文档数量" + totalElements);

        int totalPages = scoredPage.getTotalPages(); //总页数
        System.out.println("总页数" + totalPages);

        List<Item> items = scoredPage.getContent();
        for (Item item : items) {
            System.out.println(item);
        }

    }
}
