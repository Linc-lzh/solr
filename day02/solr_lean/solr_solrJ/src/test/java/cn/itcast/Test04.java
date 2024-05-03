package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test04 {
    @Autowired
    private SolrClient solrClient;

    @Test
    public void testQuery01() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();


        //solrQuery.setQuery("item_title:手机 OR item_title:平板电视");
        //solrQuery.setQuery("item_title:手机 AND item_title:三星");
        //solrQuery.setQuery("+item_title:手机  -item_title:三星");
        solrQuery.setQuery("item_title:iphone*");
        solrQuery.setStart(0);
        solrQuery.setRows(500);
        QueryResponse response = solrClient.query(solrQuery);
        //获取满足条件文档
        SolrDocumentList solrDocuments = response.getResults();
        for (SolrDocument solrDocument : solrDocuments) {
            System.out.println(solrDocument);
        }
        //满足条件文档数量
        System.out.println(solrDocuments.getNumFound());
    }
}
