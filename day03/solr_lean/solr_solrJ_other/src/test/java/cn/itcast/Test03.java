package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
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
public class Test03 {
    @Autowired
    private SolrClient solrClient;

    @Test
    public void testHighlighting() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询参数

        /**
         *
         *     q=item_title:手机
         *     &hl=true
         *     &hl.fl=item_title
         *     &hl.simple.pre=<font>
         *     &h1.simple.post=</font>
         */
        solrQuery.setQuery("item_title:手机");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font>");
        solrQuery.setHighlightSimplePost("</font>");

        QueryResponse response = solrClient.query(solrQuery);

        //解析高亮结果
        //获取高亮结果
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //key------->文档的id value----->包含高亮的数据
        for (String id : highlighting.keySet()) {
            System.out.println("文档id:" + id);
            Map<String, List<String>> listMap = highlighting.get(id);
            /**
             * {
             * item_title: [
             * "飞利浦 老人<font> 手机</em> (X2560) 深情蓝 移动联通2G<font> 手机</em> 双卡双待"
             * ]
             * }
             */

            //key---->高亮的域名
            //value------>[
            //             利浦 老人<font> 手机</em> (X2560) 深情蓝 移动联通2G<font> 手机</em> 双卡双待
            //             ]
            if(listMap != null && listMap.size() > 0) {
                List<String> strings = listMap.get("item_title");
                if(strings != null && strings.size() > 0) {
                    String s = strings.get(0);
                    System.out.println(s);
                }
            }
        }

    }



    @Test
    public void testHighlightingSwitchHighLighter() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询参数


        QueryResponse response = solrClient.query(solrQuery);

        //解析高亮结果
        //获取高亮结果
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //key------->文档的id value----->包含高亮的数据
        for (String id : highlighting.keySet()) {
            System.out.println("文档id:" + id);
            Map<String, List<String>> listMap = highlighting.get(id);
            /**
             * {
             * item_title: [
             * "飞利浦 老人<font> 手机</em> (X2560) 深情蓝 移动联通2G<font> 手机</em> 双卡双待"
             * ]
             * }
             */

            //key---->高亮的域名
            //value------>[
            //             利浦 老人<font> 手机</em> (X2560) 深情蓝 移动联通2G<font> 手机</em> 双卡双待
            //             ]
            if(listMap != null && listMap.size() > 0) {
                List<String> strings = listMap.get("item_title");
                if(strings != null && strings.size() > 0) {
                    String s = strings.get(0);
                    System.out.println(s);
                }
            }
        }

    }

}
