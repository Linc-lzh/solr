package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.client.solrj.response.Suggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test06 {

    @Autowired
    private SolrClient solrClient;

    @Test
    public void createSolrCore() throws IOException, SolrServerException {
        CoreAdminRequest.createCore("collection2", "d:/solr_home/collection2", solrClient);
    }

    @Test
    public void reloadSolrCore() throws IOException, SolrServerException {
        CoreAdminRequest.reloadCore("collection2", solrClient);
    }

    @Test
    public void renameSolrCore() throws IOException, SolrServerException {
        CoreAdminRequest.renameCore("collection2", "newCore", solrClient);
    }

    @Test
    public void unloadSolrCore() throws IOException, SolrServerException {
        CoreAdminRequest.unloadCore("newCore", solrClient);
    }

    @Test
    public void swapSolrCore() throws IOException, SolrServerException {
        CoreAdminRequest.swapCore("collection1", "collection2", solrClient);
    }


}
