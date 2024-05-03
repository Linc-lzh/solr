package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestHighLightingQuery {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testHighLightingQuery() {
        //1.设置查询参数
        HighlightQuery query = new SimpleHighlightQuery(new Criteria("item_title").is("三星手机"));
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置高亮的域
        highlightOptions.addField("item_title");
        //高亮的前后缀
        highlightOptions.setSimplePrefix("<font>");
        highlightOptions.setSimplePostfix("</font>");
        highlightOptions.addHighlightParameter("hl.method", "fastVector");
        query.setHighlightOptions(highlightOptions);
        //2.执行高亮查询

        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage("collection1", query, Item.class);
        //3.解析高亮结果
        //满足条件文档数量
        long totalElements = highlightPage.getTotalElements();
        System.out.println("满足条件文档数量：" + totalElements);

        int totalPages = highlightPage.getTotalPages();
        System.out.println("总页数：" + totalPages);

        //解析高亮数据
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            Item entity = itemHighlightEntry.getEntity();

            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if(highlights != null && highlights.size() > 0) {
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                String itemTitle = snipplets.get(0);
                entity.setTitle(itemTitle);
            }
        }

        List<Item> items = highlightPage.getContent();
        for (Item item : items) {
            System.out.println(item);
        }
    }
}
