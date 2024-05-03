package cn.itcast;

import cn.itcast.legou.dataimport.DataImportApplication;
import cn.itcast.legou.dataimport.dao.ItemDao;
import cn.itcast.legou.pojo.Item;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataImportApplication.class)
public class TestDataImport {
    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void dataImport() {
        //查询商品表中所有的数据
        List<Item> items = itemDao.selectAll();
        for (Item item : items) {
            System.out.println(item);
            //获取spec属性
            String spec = item.getSpec();
            if(!StringUtils.isEmpty(spec)) {
                Map<String,String> map = JSON.parseObject(spec, Map.class);
                item.setSpecMap(map);
            }
            solrTemplate.saveBean("collection1", item);
            solrTemplate.commit("collection1");
        }
    }
}
