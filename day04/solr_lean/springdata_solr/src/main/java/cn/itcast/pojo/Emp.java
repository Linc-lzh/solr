package cn.itcast.pojo;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

@Data
public class Emp {
    @Field("id")
    @Id
    private String empno;
    @Field("emp_ename")
    private String ename;
    @Field("emp_job")
    private String job;
    @Field("emp_mgr")
    private String mgr;
    @Field("emp_hiredate")
    private String hiredate;
    @Field("emp_sal")
    private String sal;
    @Field("emp_comm")
    private String comm;
    private String cv;
    @Field("emp_deptno")
    private String deptno;
}
