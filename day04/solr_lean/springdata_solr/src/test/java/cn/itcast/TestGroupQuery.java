package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestGroupQuery {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void basicGroupQuery() {
        //1.设置查询参数
        /**
         * q=item_title:手机
         * &group=true
         * &group.field=item_brand
         */
        Query query = new SimpleQuery("item_title:手机");
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_brand");
        groupOptions.setOffset(0);
        groupOptions.setLimit(5);
        groupOptions.addSort(new Sort(Sort.Direction.ASC,"item_price"));
        query.setGroupOptions(groupOptions);
        //group.field=item_brand&start=0&rows=3&group.limit=5&group.offset=0
        query.setPageRequest(PageRequest.of(0, 3));
        //2.执行group查询
        //groupPage对整个查询结果封装
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage("collection1", query, Item.class);
        //3.解析group结果
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_brand");

        int matches = groupResult.getMatches();
        System.out.println("匹配到的文档数量" + matches);

        Page<GroupEntry<Item>> page = groupResult.getGroupEntries();

        List<GroupEntry<Item>> content = page.getContent();
        for (GroupEntry<Item> itemGroupEntry : content) {
            System.out.println("组名称" + itemGroupEntry.getGroupValue());
            Page<Item> itemPage = itemGroupEntry.getResult();
            List<Item> items = itemPage.getContent();
            System.out.println(items);
        }

    }
}
