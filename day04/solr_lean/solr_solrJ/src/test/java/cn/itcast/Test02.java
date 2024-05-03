package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test02 {
    @Autowired
   private SolrClient solrClient;

    @Test
    public void testAdd() throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", "889900");
        doc.setField("item_title","华为 Meta30 高清手机");
        doc.setField("item_price", 20);
        doc.setField("item_images", "21312312.jpg");
        doc.setField("item_createtime", new Date());
        doc.setField("item_updatetime", new Date());
        doc.setField("item_category", "手机");
        doc.setField("item_brand","华为");
        solrClient.add(doc);
        solrClient.commit();
    }

    @Test
    public void testUpdate() throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", "889900");
        doc.setField("item_title","华为 Meta30 高清手机");
        doc.setField("item_price", 200);
        doc.setField("item_images", "21312312.jpg");
        doc.setField("item_createtime", new Date());
        doc.setField("item_updatetime", new Date());
        doc.setField("item_category", "高清手机");
        doc.setField("item_brand","华为");
        solrClient.add(doc);
        solrClient.commit();
    }
    @Test
    public void testDelete() throws IOException, SolrServerException {
       /* solrClient.deleteById("889900");
        solrClient.commit();*/

        solrClient.deleteByQuery("*:*");
        solrClient.commit();
    }


}
