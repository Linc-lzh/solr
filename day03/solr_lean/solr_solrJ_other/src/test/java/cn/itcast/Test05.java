package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.client.solrj.response.Suggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test05 {

    @Autowired
    private SolrClient solrClient;

    @Test
    public void testAutoSugget() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置参数
        //q=三星&suggest=true&suggest.dictionary=mySuggester&suggest.count=5
        solrQuery.setQuery("三星");
        solrQuery.set("suggest", true);
        solrQuery.set("suggest.dictionary", "mySuggester");
        solrQuery.set("suggest.count", 5);
        QueryResponse response = solrClient.query(solrQuery);
        //解析结果
        SuggesterResponse suggesterResponse = response.getSuggesterResponse();
        //获取自动建议的数据
        Map<String, List<Suggestion>> stringListMap = suggesterResponse.getSuggestions();
        List<Suggestion> suggestions = stringListMap.get("mySuggester");
        for (Suggestion c : suggestions) {
            /**
             * {
             * term: "三星(SAMSUNG) UA65HU9800J 65英寸曲面UHD 4K超高清3D智能电视",
             * weight: 26999,
             * payload: ""
             * }
             */
            System.out.println(c.getTerm());
        }
    }





}
