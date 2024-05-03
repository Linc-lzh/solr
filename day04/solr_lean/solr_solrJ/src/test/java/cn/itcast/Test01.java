package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test01 {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test01() {
        //使用restTemplate发送http请求http://localhost:8080/solr/collection1/select?q=item_title:手机
        String url = "http://localhost:8080/solr/collection1/select?q=item_title:手机";
        //响应结果
        Map result = restTemplate.getForObject(url, Map.class);

        Map<String,Object> response = (Map<String, Object>) result.get("response");

        //满足条件文档数
        Object numFound = response.get("numFound");
        System.out.println("满足条件文档:" + numFound);

        //满足条件的文档
        List<Map<String,Object>> docs = (List<Map<String, Object>>) response.get("docs");
        for (Map<String, Object> doc : docs) {
            System.out.println(doc);
        }
    }
}
