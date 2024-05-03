package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test02 {
    @Autowired
    private SolrClient solrClient;

    @Test
    public void testFacetBaseField() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询参数

        /**
         * q=item_title:手机
         * &group=true
         * &group.field=item_brand
         */
        solrQuery.setQuery("item_title:手机");
        solrQuery.set(GroupParams.GROUP, true);
        solrQuery.set(GroupParams.GROUP_FIELD, "item_brand");

        //设置组的分页参数
        solrQuery.setStart(0);
        solrQuery.setRows(3);
        //设置组内文档分页参数

        solrQuery.set(GroupParams.GROUP_OFFSET, 0);
        solrQuery.set(GroupParams.GROUP_LIMIT, 5);
        solrQuery.set(GroupParams.GROUP_SORT, "item_price asc");

        QueryResponse response = solrClient.query(solrQuery);
        //解析Group结果

        //封装了整个group分组查询结果
        GroupResponse groupResponse = response.getGroupResponse();
        //获取grouped
        List<GroupCommand> grouped = groupResponse.getValues();
        //item_brand分组结果
        GroupCommand groupCommand = grouped.get(0);
        //获取满足条件文档
        int matches = groupCommand.getMatches();
        System.out.println(matches);

        //获取groups
        List<Group> groups = groupCommand.getValues();
        for (Group group : groups) {
            String groupValue = group.getGroupValue();
            System.out.println(groupValue);

            SolrDocumentList result = group.getResult();
            //获取组内文档数量
            System.out.println(result.getNumFound());
            //组内文档
            for (SolrDocument doc : result) {
                System.out.println(doc);
            }

            /**
             * {
             * groupValue: "TCL",
             * doclist: {
             * numFound: 19,
             * start: 0,
             * docs: [
             * {
             * item_images: "http://img14.360buyimg.com/n1/s450x450_jfs/t3532/159/131329856/208385/d2e05067/58004df9Ncaaf71cc.jpg",
             * item_updatetime: "2015-03-08T13:27:54Z",
             * item_createtime: "2015-03-08T13:27:54Z",
             * item_price: 199,
             * id: "1027857",
             * item_title: "TCL 老人手机 (i310) 纯净白 移动联通2G手机",
             * item_category: "手机",
             * item_brand: "TCL",
             * _version_: 1664292257854914600
             * }
             * ]
             * }
             * }
             */
        }

    }

}
