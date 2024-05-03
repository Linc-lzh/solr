package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test01 {
    @Autowired
    private SolrClient solrClient;

    @Test
    public void testFacetBaseField() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询参数
        solrQuery.setQuery("item_title:手机");
        solrQuery.setFacet(true);
        solrQuery.addFacetField("item_brand");
        solrQuery.setFacetMinCount(1);
        QueryResponse response = solrClient.query(solrQuery);

        //解析Facet结果
        FacetField facetField = response.getFacetField("item_brand");
        List<FacetField.Count> values = facetField.getValues();
        for (FacetField.Count value : values) {
            System.out.println(value.getName() + "-" + value.getCount());
        }

    }
    @Test
    public void testFacetBaseQuery() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();

        //查询参数
        /**
         * q=*:*&
         * facet=on&
         * facet.query=item_category:平板电视&
         * facet.query=item_brand:华为&
         * facet.query=item_brand:三星&
         * facet.query=item_price:%5B1000 TO 2000%5D
         */
        solrQuery.setQuery("*:*");
        solrQuery.setFacet(true);
        solrQuery.addFacetQuery("{!key=平板电视}item_category:平板电视");
        solrQuery.addFacetQuery("{!key=华为品牌}item_brand:华为");
        solrQuery.addFacetQuery("{!key=三星品牌}item_brand:三星");
        solrQuery.addFacetQuery("{!key=价格1000到2000}item_price:[1000 TO 2000]");
        QueryResponse response = solrClient.query(solrQuery);
        //Facet结果解析
        /**
         * facet_queries: {
         * item_category:平板电视: 207,
         * item_brand:华为: 67,
         * item_brand:三星: 154,
         * item_price:[1000 TO 2000]: 217
         * },
         */
        Map<String, Integer> facetQuery = response.getFacetQuery();
        for (String key : facetQuery.keySet()) {
            System.out.println(key + "--" + facetQuery.get(key));
        }
    }

    @Test
    public void testFacetBaseRange() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //查询参数

        /**
         * q=*:*&
         * facet=on&
         * facet.range=item_price&
         * facet.range.start=0&
         * facet.range.end=20000&
         * facet.range.gap=2000
         */
        solrQuery.setQuery("*:*");
        solrQuery.setFacet(true);
        solrQuery.addNumericRangeFacet("item_price", 0, 20000, 2000);
        QueryResponse response = solrClient.query(solrQuery);
        //Facet结果解析

        List<RangeFacet> facetRanges = response.getFacetRanges();
        RangeFacet rangeFacet = facetRanges.get(0);
        List<RangeFacet.Count> counts = rangeFacet.getCounts();
        for (RangeFacet.Count count : counts) {
            System.out.println(count.getValue() + "-" + count.getCount());
        }

    }

    @Test
    public void testFacetBaseRangeDate() throws IOException, SolrServerException, ParseException {
        SolrQuery solrQuery = new SolrQuery();
        //查询参数

        /**
         q=*:*&
         facet=on&
         facet.range=item_createtime&
         facet.range.start=2015-01-01T00:00:00Z&
         facet.range.end=2016-01-01T00:00:00Z&
         facet.range.gap=%2B3MONTH&
         */
        solrQuery.setQuery("*:*");
        solrQuery.setFacet(true);
        TimeZone timeZone = TimeZone.getTimeZone("GTM +8");
        TimeZone.setDefault(timeZone);
        //solrQuery.addNumericRangeFacet("item_price", 0, 20000, 2000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = simpleDateFormat.parse("2015-01-01 00:00:00");
        Date end = simpleDateFormat.parse("2016-01-01 00:00:00");
        solrQuery.addDateRangeFacet("item_createtime", start, end, "+3MONTH");
        QueryResponse response = solrClient.query(solrQuery);
        //Facet结果解析

        List<RangeFacet> facetRanges = response.getFacetRanges();
        RangeFacet rangeFacet = facetRanges.get(0);
        List<RangeFacet.Count> counts = rangeFacet.getCounts();
        for (RangeFacet.Count count : counts) {
            System.out.println(count.getValue() + "-" + count.getCount());
        }

    }

    @Test
    public void testFacetBaseInterval() throws IOException, SolrServerException, ParseException {
        SolrQuery solrQuery = new SolrQuery();
        //查询参数

        /**
         * &facet=on
         * &facet.interval=item_price
         * &f.item_price.facet.interval.set=[0,1000]
         * &f.item_price.facet.interval.set=[0,100]
         * &facet.interval=item_createtime
         * &f.item_createtime.facet.interval.set=[2019-01-01T0:0:0Z,NOW]
         */
        solrQuery.setQuery("*:*");
        solrQuery.setFacet(true);
        solrQuery.addIntervalFacets("item_price", new String[]{"[0,1000]","[0,100]"});
        solrQuery.addIntervalFacets("item_createtime", new String[]{"[2019-01-01T0:0:0Z,NOW]"});
        //Facet结果解析
        QueryResponse response = solrClient.query(solrQuery);
        /**
         * facet_intervals: {
         * item_price: {
         * [0,1000]: 285,
         * [0,100]: 20
         * },
         * item_createtime: {
         * [2019-01-01T0:0:0Z,NOW]: 22
         * }
         * },
         */
        List<IntervalFacet> intervalFacets = response.getIntervalFacets();
        for (IntervalFacet intervalFacet : intervalFacets) {
            /**
             * item_price: {
             *          * [0,1000]: 285,
             *          * [0,100]: 20
             *          * }
             */
            System.out.println(intervalFacet.getField());
            List<IntervalFacet.Count> intervals = intervalFacet.getIntervals();
            for (IntervalFacet.Count count : intervals) {
                System.out.println(count.getKey() + "-" + count.getCount());
            }
        }
    }

    @Test
    public void testFacetFacetPivot() throws IOException, SolrServerException, ParseException {
        SolrQuery solrQuery = new SolrQuery();
        //查询参数
        solrQuery.setQuery("*:*");
        solrQuery.setFacet(true);
        solrQuery.addFacetPivotField("item_brand,item_category");
        QueryResponse response = solrClient.query(solrQuery);
        //解析结果
        NamedList<List<PivotField>> facetPivot = response.getFacetPivot(); //对整个pivot查询结果封装
        List<PivotField> pivotFields = facetPivot.get("item_brand,item_category"); //基于item_brand,item_category维度查询结果
        for (PivotField pivotField : pivotFields) {
            //pivotField------>
            /**
             * {
             * field: "item_brand",
             * value: "三星",
             * count: 154,
             * pivot: []
             * }
             */
            String field = pivotField.getField();
            Object value = pivotField.getValue();
            int count = pivotField.getCount();
            System.out.println(field + "-" + value + "-" + count);

            List<PivotField> fieldList = pivotField.getPivot();
            for (PivotField pivotField1 : fieldList) {
                String field1 = pivotField1.getField();
                Object value1 = pivotField1.getValue();
                int count1 = pivotField1.getCount();
                System.out.println(field1 + "-" + value1 + "-" + count1);

            }
        }


    }
}
