package cn.itcast.pojo;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class Item {
    @Field("id")
    @Id
    private String id;
    @Field("item_title")
    private String title;
    @Field("item_price")
    private Double price;
    @Field("item_images")
    private String image;
    @Field("item_createtime")
    private Date createTime;
    @Field("item_updatetime")
    private Date updateTime;
    @Field("item_category")
    private String category;
    @Field("item_brand")
    private String brand;
}