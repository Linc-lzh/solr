package cn.itcast.controller;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    @Qualifier("cloudSolrClient")
    private SolrClient solrClient;

    @RequestMapping("/addDocument")
    public Map<String,Object> addDocument(String id,String bookName) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", id);
        doc.setField("book_name", bookName);
        solrClient.add(doc);
        solrClient.commit();

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("flag", true);
        resultMap.put("msg", "添加文档成功！");
        return resultMap;
    }
}
