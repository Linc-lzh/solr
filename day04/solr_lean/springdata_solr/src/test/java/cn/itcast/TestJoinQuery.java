package cn.itcast;

import cn.itcast.pojo.Emp;
import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestJoinQuery {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testJoinQuery() {
        //设置查询参数

        /**
         * 主查询条件q=*:*
         * 过滤条件fq={!join fromIndex=collection2 toIndex=collection1 from=id to=emp_deptno}dept_dname:SALES
         */
        Query query = new SimpleQuery("*:*");
        FilterQuery filterQuery = new SimpleFilterQuery(new Criteria("dept_dname").is("SALES"));
        Join join = new Join.Builder("id").fromIndex("collection2").to("emp_deptno");
        filterQuery.setJoin(join);
        query.addFilterQuery(filterQuery);
        //执行查询
        ScoredPage<Emp> scoredPage = solrTemplate.queryForPage("collection1", query, Emp.class);
        //解析结果
        //满足条件文档数量
        System.out.println(scoredPage.getTotalElements());
        System.out.println(scoredPage.getTotalPages());
        List<Emp> empList = scoredPage.getContent();
        for (Emp emp : empList) {
            System.out.println(emp);
        }

    }
}
