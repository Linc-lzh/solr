package cn.itcast;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Value("${url}")
    private String url;

    @Bean
    public SolrClient createHttpSolrClient() {
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(url);
        return builder.build();
    }

    @Value("${zk01}")
    private String zk01;

    @Value("${zk02}")
    private String zk02;

    @Value("${zk03}")
    private String zk03;

    @Bean("cloudSolrClient")
    public SolrClient createCloudSolrClient() {
        //1.构建Builder
        List<String> zkHosts = new ArrayList<>();
        zkHosts.add(zk01);
        zkHosts.add(zk02);
        zkHosts.add(zk03);
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, Optional.empty());
        CloudSolrClient solrClient = builder.build();
        //Zookeeper超时时间
        solrClient.setZkConnectTimeout(30000);
        solrClient.setZkClientTimeout(30000);

        //设置默认的collection
        solrClient.setDefaultCollection("myCollection");
        return solrClient;
    }
}
