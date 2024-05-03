package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SpellcheckOptions;
import org.springframework.data.solr.core.query.result.SpellcheckQueryResult;
import org.springframework.data.solr.core.query.result.SpellcheckedPage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TesSpellChecking {
    @Autowired
    private SolrTemplate solrTemplate;
    @Test
    public void testSpellChecking() {
        //1.设置查询参数
        //item_title:iphoneX galaxz&spellcheck=true
        Query query = new SimpleQuery("item_title:iphoneX galaxz");
        query.setSpellcheckOptions(SpellcheckOptions.spellcheck().extendedResults());
        //2.执行查询

        SpellcheckedPage<Item> page = solrTemplate.query("collection1", query, Item.class);
        //3.解析结果

        long totalElements = page.getTotalElements();
        if(totalElements == 0) {
            Collection<SpellcheckQueryResult.Alternative> alternatives = page.getAlternatives();
            for (SpellcheckQueryResult.Alternative alternative : alternatives) {
                System.out.println("错误的词：" + alternative.getTerm());
                System.out.println("建议的词：" + alternative.getSuggestion());
            }
        }

    }
}
