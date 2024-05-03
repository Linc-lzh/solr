package cn.itcast;

import cn.itcast.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestFacetQuery {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testFacetQuery() {
        //1.设置查询参数
        //item_title:手机&facet=on&facet.field=item_brand&facet.mincount=1
        FacetQuery query = new SimpleFacetQuery(new Criteria("item_title").is("手机"));
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnField("item_brand");
        facetOptions.setFacetMinCount(1);
        facetOptions.setPageable(PageRequest.of(1, 10));
        query.setFacetOptions(facetOptions);
        //2.执行Facet查询
        //facetPage整个查询结果封装
        FacetPage<Item> facetPage = solrTemplate.queryForFacetPage("collection1", query, Item.class);
        //3.解析Facet查询结果
        Page<FacetFieldEntry> page = facetPage.getFacetResultPage("item_brand");
        List<FacetFieldEntry> pageContent = page.getContent();
        for (FacetFieldEntry facetFieldEntry : pageContent) {
            //获取组名称和组数量
            System.out.println(facetFieldEntry.getValue() + "---" + facetFieldEntry.getValueCount());
        }
    }

    @Test
    public void testFacetQuery2() {
        //1.设置查询参数
        /**
         * q=*:*&
         * facet=on&
         * facet.query=item_category:平板电视&
         * facet.query=item_brand:华为&
         * facet.query=item_brand:三星&
         * facet.query=item_price:%5B1000 TO 2000%5D
         */
        FacetQuery query = new SimpleFacetQuery(new SimpleStringCriteria("*:*"));
        FacetOptions facetOptions = new FacetOptions();
        SolrDataQuery solrDataQuery1 = new SimpleQuery("{!key=pb}item_category:平板电视");
        facetOptions.addFacetQuery(solrDataQuery1);

        SolrDataQuery solrDataQuery2 = new SimpleQuery("{!key=hw}item_brand:华为");
        facetOptions.addFacetQuery(solrDataQuery2);

        SolrDataQuery solrDataQuery3 = new SimpleQuery("item_brand:三星");
        facetOptions.addFacetQuery(solrDataQuery3);

        SolrDataQuery solrDataQuery4 = new SimpleQuery(new Criteria("item_price").between(1000, 2000));
        facetOptions.addFacetQuery(solrDataQuery4);
        query.setFacetOptions(facetOptions);
        //2.执行Facet查询

        FacetPage<Item> facetPage = solrTemplate.queryForFacetPage("collection1", query, Item.class);

        //3.解析Facet查询结果
        Page<FacetQueryEntry> page = facetPage.getFacetQueryResult();
        List<FacetQueryEntry> content = page.getContent();
        for (FacetQueryEntry facetQueryEntry : content) {
            String value = facetQueryEntry.getValue();
            long count = facetQueryEntry.getValueCount();
            System.out.println(value + "---" + count);
        }

    }

    @Test
    public void testFacetQuery3() {
       //1.设置查询参数
        /**
         * q=*:*&
         * facet=on&
         * facet.range=item_price&
         * facet.range.start=0&
         * facet.range.end=20000&
         * facet.range.gap=2000&
         * &facet.range.other=all
         */
        FacetQuery query = new SimpleFacetQuery(new SimpleStringCriteria("*:*"));
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetByRange(new FacetOptions.FieldWithNumericRangeParameters("item_price", 0, 20000, 2000));
        query.setFacetOptions(facetOptions);
        //2.执行Facet查询
        FacetPage<Item> facetPage = solrTemplate.queryForFacetPage("collection1", query, Item.class);
        //3.解析Facet查询结果
        Page<FacetFieldEntry> page = facetPage.getRangeFacetResultPage("item_price");

        List<FacetFieldEntry> list = page.getContent();
        for (FacetFieldEntry facetFieldEntry : list) {
            String value = facetFieldEntry.getValue();
            long count = facetFieldEntry.getValueCount();
            System.out.println(value + "--" + count);
        }
    }
    @Test
    public void testFacetQuery4() throws ParseException {
        //1.设置查询参数
        /**
         * q=*:*&
         facet=on&
         facet.range=item_createtime&
         facet.range.start=2015-01-01T00:00:00Z&
         facet.range.end=2016-01-01T00:00:00Z&
         facet.range.gap=%2B3MONTH
         */
        FacetQuery query = new SimpleFacetQuery(new SimpleStringCriteria("*:*"));
        FacetOptions facetOptions = new FacetOptions();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT +8"));
        facetOptions.setFacetMinCount(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = simpleDateFormat.parse("2015-01-01 00:00:00");
        Date end = simpleDateFormat.parse("2016-01-01 00:00:00");


        facetOptions.addFacetByRange(new FacetOptions.FieldWithDateRangeParameters("item_createtime", start, end, "+3MONTH"));
        query.setFacetOptions(facetOptions);
        //2.执行Facet查询
        FacetPage<Item> facetPage = solrTemplate.queryForFacetPage("collection1", query, Item.class);
        //3.解析Facet查询结果
        Page<FacetFieldEntry> page = facetPage.getRangeFacetResultPage("item_createtime");

        List<FacetFieldEntry> list = page.getContent();
        for (FacetFieldEntry facetFieldEntry : list) {
            String value = facetFieldEntry.getValue();
            long count = facetFieldEntry.getValueCount();
            System.out.println(value + "--" + count);
        }
    }

    @Test
    public void testFacetPivotQuery() {
        //1.设置查询参数

        /**
         *  q=*:*&
         *  &facet=on
         *  &facet.pivot=item_brand,item_category
         */
        FacetQuery query = new SimpleFacetQuery(new SimpleStringCriteria("*:*"));
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnPivot("item_brand","item_category");
        query.setFacetOptions(facetOptions);
        //2.执行Facet查询

        FacetPage<Item> facetPage = solrTemplate.queryForFacetPage("collection1", query, Item.class);
        //3.解析Facet查询结果

        List<FacetPivotFieldEntry> facetPagePivot = facetPage.getPivot("item_brand,item_category");
        for (FacetPivotFieldEntry facetPivotFieldEntry : facetPagePivot) {
            String value = facetPivotFieldEntry.getValue();
            long valueCount = facetPivotFieldEntry.getValueCount();
            System.out.println("品牌组名称:" + value + "---品牌组数量:" + valueCount);

            List<FacetPivotFieldEntry> pivot = facetPivotFieldEntry.getPivot();
            for (FacetPivotFieldEntry pivotFieldEntry : pivot) {
                String categoryValue = pivotFieldEntry.getValue();
                long categoryValueCount = pivotFieldEntry.getValueCount();
                System.out.println("   分类组名称:" + categoryValue + "---数量" +categoryValueCount);
            }
        }


    }
}
