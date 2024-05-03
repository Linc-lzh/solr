package cn.itcast.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "dept")
public class Dept {
    @Id
    private String deptno;
    private String dname;
    private String loc;
}