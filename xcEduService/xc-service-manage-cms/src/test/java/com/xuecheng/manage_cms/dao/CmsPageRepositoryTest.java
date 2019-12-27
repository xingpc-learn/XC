package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.dao
 * @ClassName: CmsPageRepositoryTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/21 20:43
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
    * 测试dao层
    * */
    @Test
    public void testFindAll(){
        List<CmsPage> list = cmsPageRepository.findAll();
        System.out.println(list);
    }

    /**
    * 测试分页
    * */
    @Test
    public void testFindPage(){
        int page=1;
        int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> pages = cmsPageRepository.findAll(pageable);
        System.out.println(pages);
    }

    /*
    * 测试插入数据
    * */
    @Test
    public void testSave(){
        CmsPage cmsPage=new CmsPage();
        cmsPage.setPageName("测试");
        cmsPage.setPageAliase("test");
        cmsPageRepository.save(cmsPage);
    }

    /**
    * 测试mongodb修改方法,先查询后修改
    * */
    @Test
    public void testUpdate(){
//        查询
        Optional<CmsPage> cmsPage = cmsPageRepository.findById("5d3463f6c80c6411584122ec");
        if (cmsPage.isPresent()){
            CmsPage cmsPage1 = cmsPage.get();
            cmsPage1.setPageName("哈哈");
            cmsPageRepository.save(cmsPage1);
        }
    }

    /**
    * 测试查询,使用方法名约定查询
    * */
    @Test
    public void testFindById(){
        CmsPage cmsPage = cmsPageRepository.findByPageId("5d3463f6c80c6411584122ec");
        System.out.println(cmsPage);
    }

    /**
    * 测试删除
    * */
    @Test
    public void testDelete(){
        Optional<CmsPage> cmsPage = cmsPageRepository.findById("5d3463f6c80c6411584122ec");
        if (cmsPage.isPresent()){
            CmsPage cmsPage1 = cmsPage.get();
            cmsPage1.setPageName("哈哈");
            cmsPageRepository.delete(cmsPage1);
        }
    }

    /**
     * 测试条件查询（精准匹配和模糊查询）
     * */
    @Test
    public void testFindByCondition(){
        int page=1;
        int size=10;
        Pageable pageable = PageRequest.of(page, size);
//        定义条件匹配器，模糊查询需要定义条件查询器
        ExampleMatcher exampleMatcher=ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

//        定义查询条件的值
        CmsPage cmsPage=new CmsPage();
//        根据站点id查询
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
//        根据模板id查询
//        cmsPage.setTemplateId("5a925be7b00ffc4b3c1578b5");
//        根据别名匹配
        cmsPage.setPageAliase("轮播");

//        定义example，查询条件的值
        Example<CmsPage> example=Example.of(cmsPage, exampleMatcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getTotalElements());
    }
}
