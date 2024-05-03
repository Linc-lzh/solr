package cn.itcast.legou.pojo;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Dynamic;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

@Data
@Table(name = "tb_item")
public class Item {
    @Id
    @Column(name="id")
    @Field("id")
    private Integer id;
    @Column(name="title")
    @Field("item_title")
    private String title;
    @Column(name="image")
    @Field("item_image")
    private String image;
    @Column(name="price")
    @Field("item_price")
    private Double price;
    @Column(name="create_time")
    @Field("item_createtime")
    private Date createTime;
    @Column(name="update_time")
    @Field("item_updatetime")
    private Date updateTime;
    @Column(name="category")
    @Field("item_category")
    private String category;
    @Column(name="brand")
    @Field("item_brand")
    private String brand;
    @Column(name="spec")
    @Field("spec")
    private String spec;

    @Field("spec_*")
    @Dynamic
    //将Map的key拼接到spec_作为动态域的域名，将Map的value作为动态域的域值；
    //{"机身内存":"16G","网络":"联通3G"}
    //spec_机身内存:16G
       //  spec_网络:联通3G;
    private Map<String,String> specMap;
}