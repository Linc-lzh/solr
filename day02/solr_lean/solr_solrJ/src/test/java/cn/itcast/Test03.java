package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test03 {
    @Autowired
    private SolrClient solrClient;

    @Test
    //添加过滤条件:品牌是华为。
    //价格在[1000-2000].
    public void testQuery01() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        //solrQuery.set("q","item_title:手机" );
        solrQuery.setQuery("item_title:手机");
        solrQuery.addFilterQuery("item_brand:华为");//指定过滤条件
        solrQuery.addFilterQuery("item_price:[1000 TO 2000]");//指定过滤条件

        solrQuery.setStart((1 - 1) * 20);
        solrQuery.setRows(20);

        solrQuery.addSort("item_price", SolrQuery.ORDER.desc );
        solrQuery.addSort("id", SolrQuery.ORDER.asc );


        solrQuery.setFields("id","title:item_title","price:item_price","brand:item_brand");


        QueryResponse response = solrClient.query(solrQuery);
        //获取满足条件文档
        SolrDocumentList solrDocuments = response.getResults();
        for (SolrDocument solrDocument : solrDocuments) {
            System.out.println(solrDocument);
        }
        //获取满足条件文档数量

        System.out.println(solrDocuments.getNumFound());
    }
}
