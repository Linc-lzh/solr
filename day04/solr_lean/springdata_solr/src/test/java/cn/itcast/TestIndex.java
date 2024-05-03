package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestIndex {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd() {
        Item item = new Item();
        item.setId("9999");
        item.setBrand("锤子");
        item.setTitle("锤子（SHARP） 智能锤子");
        item.setCategory("手机");
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setPrice(9999.0);
        item.setImage("https://www.baidu.com/img/bd_logo1.png");
        solrTemplate.saveBean("collection1", item);
        solrTemplate.commit("collection1");
    }

    @Test
    public void testUpdate() {
        Item item = new Item();
        item.setId("9999");
        item.setBrand("三星");
        item.setTitle("三星（SHARP） 智能锤子");
        item.setCategory("手机");
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setPrice(9999.0);
        item.setImage("https://www.baidu.com/img/bd_logo1.png");
        solrTemplate.saveBean("collection1", item);
        solrTemplate.commit("collection1");
    }

    @Test
    public void testDelete() {
        solrTemplate.deleteByIds("collection1", "9999");
        solrTemplate.commit("collection1");
    }

    @Test
    public void testDeleteQuery() {
        SolrDataQuery query = new SimpleQuery("*:*");
        solrTemplate.delete("collection1", query);
        solrTemplate.commit("collection1");
    }

}
