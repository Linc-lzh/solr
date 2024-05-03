package cn.itcast;

import cn.itcast.dao.DeptDao;
import cn.itcast.dao.EmpDao;
import cn.itcast.pojo.Dept;
import cn.itcast.pojo.Emp;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class Test01 {
    @Autowired
    private DeptDao deptDao;

    @Autowired
    private EmpDao empDao;

    @Autowired
    private SolrClient solrClient;

    @Test
    public void test01() throws IOException, SolrServerException {

        /**
         * 使用mybatis查询部门表的所有信息。
         * 迭代每一个部门-------->部门Doucment
         * 根据部门号查询该部门下所有的员工------->员工Document集合
         * 建立部门Doucment和其员工Document的父子关系
         */
        List<Dept> depts = deptDao.selectAll();
        for (Dept dept : depts) {
            SolrInputDocument deptDocument = new SolrInputDocument();
            deptDocument.setField("id", dept.getDeptno());
            deptDocument.setField("dept_dname", dept.getDname());
            deptDocument.setField("dept_loc", dept.getLoc());
            deptDocument.setField("docParent", "isParent");

            //调用下面方法
            List<SolrInputDocument> emps = findEmpsByDeptno(dept.getDeptno());
            deptDocument.addChildDocuments(emps);

            solrClient.add(deptDocument);
            solrClient.commit();
        }

    }

    /**
     * 根据部门号,获取部门下所有的员工,员工Document集合
     */
    private List<SolrInputDocument> findEmpsByDeptno(String deptno) {
        Example example = new Example(Emp.class) ;
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deptno", deptno);
        List<Emp> emps = empDao.selectByExample(example);
        //System.out.println(emps);
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();
        for (Emp emp : emps) {
            SolrInputDocument document = emp2SolrInputDocument(emp);
            solrInputDocuments.add(document);
        }
        return solrInputDocuments;
    }

    private SolrInputDocument emp2SolrInputDocument(Emp emp) {
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", emp.getEmpno());
        document.setField("emp_ename", emp.getEname());
        document.setField("emp_job", emp.getJob());
        document.setField("emp_mgr", emp.getMgr());
        document.setField("emp_hiredate", emp.getHiredate());
        document.setField("emp_sal", emp.getSal());
        document.setField("emp_comm", emp.getComm());
        document.setField("emp_cv", emp.getCv());
        return document;
    }

}
