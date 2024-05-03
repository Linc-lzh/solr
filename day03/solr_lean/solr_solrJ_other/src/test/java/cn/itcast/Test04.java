package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
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
public class Test04 {
    @Autowired
    private SolrClient solrClient;

    @Test
    public void testHighlighting() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询参数
        //q=item_title:iphonx&spellcheck=true
        solrQuery.setQuery("item_title:iphonx");
        solrQuery.set("spellcheck", true);
        QueryResponse response = solrClient.query(solrQuery);

        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
        Map<String, SpellCheckResponse.Suggestion> suggestionMap = spellCheckResponse.getSuggestionMap();
        for (String key : suggestionMap.keySet()) {
            System.out.println(key);//错误的词
            SpellCheckResponse.Suggestion suggestion = suggestionMap.get(key);
            List<String> alternatives = suggestion.getAlternatives();
            System.out.println(alternatives); //建议词的集合
        }
        //解析查询结果
        /**
         * suggestions: [
         * "iphonx",
         * {
         * numFound: 1,
         * startOffset: 11,
         * endOffset: 17,
         * suggestion: [
         * "iphone"
         * ]
         * }
         * ]
         */


    }





}
