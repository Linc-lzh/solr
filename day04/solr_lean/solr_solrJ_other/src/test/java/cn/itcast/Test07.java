package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test07 {

    @Autowired
    @Qualifier("cloudSolrClient")
    private SolrClient solrClient;

    @Test
    /**
     * 完成添加文档
     */
    public void test01() throws IOException, SolrServerException {
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", 3);
        document.setField("name", "李四");
        solrClient.add( document);
        solrClient.commit();
    }

    @Test
    public void test02() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        QueryResponse queryResponse = solrClient.query(solrQuery);

        //获取满足条件的文档
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument doc : results) {
            System.out.println(doc);
        }

        long numFound = results.getNumFound();
        System.out.println("满足条件的文档数量" + numFound);


    }
}
