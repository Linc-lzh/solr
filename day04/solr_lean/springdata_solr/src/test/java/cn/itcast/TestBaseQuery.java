package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestBaseQuery {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testBaseQuery() {

        Query query = new SimpleQuery("item_title:iphone*");
        query.setOffset(0L);
        query.setRows(300);
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
